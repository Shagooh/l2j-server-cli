/*
 * Copyright © 2019-2022 L2J Server
 *
 * This file is part of L2J Server.
 *
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.cli.command;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Deploy command.
 * @author Zoey76
 * @author HorridoJoho
 * @version 1.0.0
 */
@Command(name = "deploy", aliases = "d")
public class DeployCommand extends AbstractCommand {
	private static final Logger LOG = LoggerFactory.getLogger(DeployCommand.class);

	@Option(names = {"--login-source-directory", "-lssrc"}, defaultValue = DEFAULT_LOGIN_SOURCE_DIR, description = "Login directory")
	private String loginSourceDirectory = DEFAULT_LOGIN_SOURCE_DIR;
	@Option(names = {"--game-source-directory", "-gssrc"}, defaultValue = DEFAULT_GAME_SOURCE_DIR, description = "Game directory")
	private String gameSourceDirectory = DEFAULT_GAME_SOURCE_DIR;
	@Option(names = {"--datapack-source-directory", "-dpsrc"}, defaultValue = DEFAULT_DATAPACK_SOURCE_DIR, description = "DataPack directory")
	private String datapackSourceDirectory = DEFAULT_DATAPACK_SOURCE_DIR;

	@Option(names = {"--login-deploy-directory", "-lsdepl"}, defaultValue = DEFAULT_LOGIN_DEPLOY_DIR, description = "Login directory")
	private String loginDeployDirectory = DEFAULT_LOGIN_DEPLOY_DIR;
	@Option(names = {"--game-deploy-directory", "-gsdepl"}, defaultValue = DEFAULT_GAME_DEPLOY_DIR, description = "Game directory")
	private String gameDeployDirectory = DEFAULT_GAME_DEPLOY_DIR;
	@Option(names = {"--datapack-deploy-directory", "-dpdepl"}, defaultValue = DEFAULT_DATAPACK_DEPLOY_DIR, description = "DataPack directory")
	private String datapackDeployDirectory = DEFAULT_DATAPACK_DEPLOY_DIR;
	
	@Override
	public void run() {
		try {
			LOG.info("Deploying L2J Loginserver");
			processArtifact(loginSourceDirectory, loginDeployDirectory);
			
			LOG.info("Deploying L2J Gameserver");
			processArtifact(gameSourceDirectory, gameDeployDirectory);
			
			LOG.info("Deploying L2J DataPack");
			processArtifact(datapackSourceDirectory, datapackDeployDirectory);
		} catch (Exception e) {
			LOG.error("Unable to deploy components!", e);
		}
	}

	public void processArtifact(String srcDirPath, String dstDirPathStr) throws Exception {
		Files.list(Paths.get(srcDirPath, "target")).forEach(p->{
			if (!p.toString().endsWith(".zip")) {
				return;
			}
			
			LOG.info("Processing zip file {}", p.toString());

			try {
				processZip(p, Path.of(dstDirPathStr));
			} catch (Exception e) {
				LOG.error("Unable to process zip file!", e);
			}
		});
	}
	
	public void processZip(Path zipFilePath, Path dstDirPath) throws Exception {
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		URI uri = URI.create("jar:file:" + zipFilePath.toAbsolutePath().toUri().getPath());
		try (FileSystem zipFs = FileSystems.newFileSystem(uri, env)) {
			// mkdirs if not exists
			if (!Files.exists(dstDirPath)) {
				dstDirPath.toFile().mkdirs();
			}

			for (Path p : zipFs.getRootDirectories()) {
				processZipEntry(p, dstDirPath);
			}
		}
	}
	
	public void processZipEntry(Path zipFsPath, Path dstDirPath) throws Exception {
		if (Files.isDirectory(zipFsPath)) {
			LOG.info("Process directory {}", zipFsPath.toString());
			Files.list(zipFsPath).forEach(p->{
				try {
					processZipEntry(p, dstDirPath);
				} catch (Exception e) {
					LOG.info("Unable to unzip {}!", p, e);
				}
			});
		} else if (Files.isRegularFile(zipFsPath)) {
			checkZipSlip(dstDirPath, zipFsPath);

			Path outFilePath = Paths.get(dstDirPath.toString(), zipFsPath.toString());
			Path parentDirPath = outFilePath.getParent();
			if (!Files.exists(parentDirPath)) {
				parentDirPath.toFile().mkdirs();
			}

			Files.copy(zipFsPath, Paths.get(dstDirPath.toString(), zipFsPath.toString()), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	public static void checkZipSlip(Path dstDirPath, Path zipFsPath) throws Exception {
		Path dstFilePath = Paths.get(dstDirPath.toString(), zipFsPath.toString());

		if (!dstFilePath.toAbsolutePath().startsWith(dstDirPath.toAbsolutePath())) {
			throw new Exception("ZipSlipAttack detected: " + zipFsPath.toString());
		}
	}
}
