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

import java.io.FilterInputStream;
import java.io.IOException;

/**
 * Abstract command.
 * @author Zoey76
 * @version 1.0.0
 */
public abstract class AbstractCommand implements Runnable {
	protected static final String YES = "y";

	protected static final String DEFAULT_LOGIN_REPO = "https://bitbucket.org/l2jserver/l2j-server-login.git";
	protected static final String DEFAULT_GAME_REPO = "https://bitbucket.org/l2jserver/l2j-server-game.git";
	protected static final String DEFAULT_DATAPACK_REPO = "https://bitbucket.org/l2jserver/l2j-server-datapack.git";

	protected static final String DEFAULT_LOGIN_SOURCE_DIR = "./l2j/git/l2j-server-login";
	protected static final String DEFAULT_GAME_SOURCE_DIR = "./l2j/git/l2j-server-game";
	protected static final String DEFAULT_DATAPACK_SOURCE_DIR = "./l2j/git/l2j-server-datapack";

	protected static final String DEFAULT_LOGIN_DEPLOY_DIR = "./l2j/deploy/login";
	protected static final String DEFAULT_GAME_DEPLOY_DIR = "./l2j/deploy/game";
	protected static final String DEFAULT_DATAPACK_DEPLOY_DIR = DEFAULT_GAME_DEPLOY_DIR;

	protected static final FilterInputStream FILTER_INPUT_STREAM = new FilterInputStream(System.in) {
		@Override
		public void close() throws IOException {
			// Do not close it.
		}
	};
}
