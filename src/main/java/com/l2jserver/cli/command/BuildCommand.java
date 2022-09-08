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

import java.io.File;
import java.util.Collections;
import java.util.Properties;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Build command.
 * @author Zoey76
 * @author HorridoJoho
 * @version 1.0.0
 */
@Command(name = "build", aliases = "b")
public class BuildCommand extends AbstractCommand {

	private static final Logger LOG = LoggerFactory.getLogger(BuildCommand.class);

	@Option(names = {"--login-directory", "-lsdir"}, defaultValue = DEFAULT_LOGIN_SOURCE_DIR, description = "Login directory")
	private String loginDirectory = DEFAULT_LOGIN_SOURCE_DIR;
	@Option(names = {"--game-directory", "-gsdir"}, defaultValue = DEFAULT_GAME_SOURCE_DIR, description = "Game directory")
	private String gameDirectory = DEFAULT_GAME_SOURCE_DIR;
	@Option(names = {"--datapack-directory", "-dpdir"}, defaultValue = DEFAULT_DATAPACK_SOURCE_DIR, description = "DataPack directory")
	private String datapackDirectory = DEFAULT_DATAPACK_SOURCE_DIR;
	
	@Option(names = {"--skip-tests", "-st"}, defaultValue = "true", description = "Skip Tests")
	private boolean skipTests = true;

	@Override
	public void run() {
		try {
			boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

			Properties properties = new Properties();
			if (skipTests) {
				properties.setProperty("skipTests", "true");
			}

			System.out.println("Building L2J Loginserver");
			executeMavenWrapper("install", loginDirectory, properties, isWindows);

			System.out.println("Building L2J Gameserver");
			executeMavenWrapper("install", gameDirectory, properties, isWindows);

			System.out.println("Building L2J DataPack");
			executeMavenWrapper("install", datapackDirectory, properties, isWindows);
		} catch (Exception e) {
			LOG.error("Unable to build the code!", e);
		}
	}

	private final void executeMavenWrapper(String goal, String sourceDir, Properties properties, boolean isWindows)
	        throws Exception {
		String mavenExecutableName = "mvnw" + (isWindows ? ".cmd" : "");
		File mavenExecutableFile = new File(sourceDir, mavenExecutableName).getAbsoluteFile();
		mavenExecutableFile.setExecutable(true);
		
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(new File(sourceDir, "pom.xml"));
		request.setGoals(Collections.singletonList(goal));
		request.setProperties(properties);
		Invoker invoker = new DefaultInvoker();
		invoker.setMavenHome(new File(sourceDir));
		invoker.setMavenExecutable(mavenExecutableFile);
		invoker.execute(request);
	}
}
