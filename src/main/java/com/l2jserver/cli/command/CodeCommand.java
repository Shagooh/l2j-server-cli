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

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.cli.model.CloneType;
import com.l2jserver.cli.util.LoggerProgressMonitor;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Code command.
 * @author Zoey76
 * @author HorridoJoho
 * @version 1.0.0
 */
@Command(name = "code", aliases = "c")
public class CodeCommand extends AbstractCommand {

	private static final Logger LOG = LoggerFactory.getLogger(CodeCommand.class);

	private static final LoggerProgressMonitor LOGGER_PROGRESS_MONITOR = new LoggerProgressMonitor(LOG);

	@Option(names = {"--login-repository", "-lsrepo"}, defaultValue = DEFAULT_LOGIN_REPO, description = "Login repository")
	private String loginRepository = DEFAULT_LOGIN_REPO;
	@Option(names = {"--game-repository", "-gsrepo"}, defaultValue = DEFAULT_GAME_REPO, description = "Game repository")
	private String gameRepository = DEFAULT_GAME_REPO;
	@Option(names = {"--datapack-repository", "-dprepo"}, defaultValue = DEFAULT_DATAPACK_REPO, description = "Datapack repository")
	private String datapackRepository = DEFAULT_DATAPACK_REPO;

	@Option(names = {"--login-directory", "-lsdir"}, defaultValue = DEFAULT_LOGIN_SOURCE_DIR, description = "Login directory")
	private String loginDirectory = DEFAULT_LOGIN_SOURCE_DIR;
	@Option(names = {"--game-directory", "-gsdir"}, defaultValue = DEFAULT_GAME_SOURCE_DIR, description = "Game directory")
	private String gameDirectory = DEFAULT_GAME_SOURCE_DIR;
	@Option(names = {"--datapack-directory", "-dpdir"}, defaultValue = DEFAULT_DATAPACK_SOURCE_DIR, description = "DataPack directory")
	private String datapackDirectory = DEFAULT_DATAPACK_SOURCE_DIR;

	@Option(names = "--clone", defaultValue = "ALL", description = "Clone ALL|LOGIN|GAME|DATAPACK")
	private CloneType cloneType = CloneType.ALL;

	@Override
	public void run() {
		try {
			switch (cloneType) {
			case ALL: {
				LOG.info("Cloning L2J Loginserver");
				cloneRepository(loginRepository, loginDirectory);

				LOG.info("Cloning L2J Gameserver");
				cloneRepository(gameRepository, gameDirectory);

				LOG.info("Cloning L2J DataPack");
				cloneRepository(datapackRepository, datapackDirectory);
				break;
			}
			case LOGIN: {
				LOG.info("Cloning L2J Loginserver");
				cloneRepository(loginRepository, loginDirectory);
				break;
			}
			case GAME: {
				LOG.info("Cloning L2J Gameserver");
				cloneRepository(gameRepository, gameDirectory);
				break;
			}
			case DATAPACK: {
				LOG.info("Cloning L2J DataPack");
				cloneRepository(datapackRepository, datapackDirectory);
				break;
			}
			}
		} catch (Exception ex) {
			LOG.error("Unable to get the code!", ex);
		}
	}

	private void cloneRepository(String repository, String directoryStr) {
		try {
			File directory = new File(directoryStr);
			if (directory.exists()) {
				Git.open(directory).pull();
			} else {
				Git.cloneRepository() //
					.setURI(repository) //
					.setDirectory(directory) //
					.setProgressMonitor(LOGGER_PROGRESS_MONITOR) //
					.call();
			}
		} catch (Exception ex) {
			LOG.error("Unable to get the code!", ex);
		}
	}
}
