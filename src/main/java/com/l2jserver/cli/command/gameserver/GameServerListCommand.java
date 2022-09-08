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
package com.l2jserver.cli.command.gameserver;

import java.sql.SQLException;

import com.l2jserver.cli.command.AbstractCommand;
import com.l2jserver.cli.dao.GameServerDAO;
import com.l2jserver.cli.model.ServerNames;

import de.vandermeer.asciitable.AsciiTable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Game Server list command.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "list", aliases = "l", description = "Lists all game servers from login server.")
public class GameServerListCommand extends AbstractCommand {
	
	private static final String IN_USE = "In Use";
	
	private static final String FREE = "Free";
	
	@Option(names = {
		"-u",
		"--used-only"
	}, description = "List servers in use only.", defaultValue = "false")
	private boolean usedOnly = false;
	
	@Override
	public void run() {
		try {
			listGameServers();
		} catch (Exception ex) {
			System.err.println("There has been an error listing the game servers!");
			ex.printStackTrace();
		}
	}
	
	private void listGameServers() throws SQLException {
		final var at = new AsciiTable();
		at.addRule();
		at.addRow("Id", "Name", "Status");
		at.addRule();
		final var gameservers = GameServerDAO.getInstance().gameServers();
		for (var gs : ServerNames.getServers().entrySet()) {
			final var inUse = gameservers.contains(gs.getKey());
			if (usedOnly && !inUse) {
				continue;
			}
			
			at.addRow(gs.getKey(), gs.getValue(), (inUse ? IN_USE : FREE));
			at.addRule();
		}
		System.out.println(at.render());
	}
}
