/*
 * Copyright © 2019 L2J Server
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

import com.l2jserver.cli.command.database.DatabaseInstallCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Database command.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "database", aliases = "db", subcommands = {
	DatabaseInstallCommand.class
})
public class DatabaseCommand extends AbstractCommand {
	
	@Override
	public void run() {
		System.err.println("Please invoke a subcommand");
		new CommandLine(new DatabaseCommand()).usage(System.out);
	}
}
