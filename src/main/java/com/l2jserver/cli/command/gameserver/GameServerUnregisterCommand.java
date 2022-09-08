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

import com.l2jserver.cli.command.AbstractCommand;
import com.l2jserver.cli.dao.GameServerDAO;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Game Server unregister command.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "unregister", aliases = "u", description = "Removes a game server from the login server.")
public class GameServerUnregisterCommand extends AbstractCommand {
	
	@Option(names = {
		"-a",
		"--all"
	}, description = "If true, all Game Servers will be unregistered.", defaultValue = "false")
	private boolean all = false;
	
	@Option(names = "-id", description = "Game Server Id.")
	private Integer id;
	
	@Override
	public void run() {
		try {
			if (all) {
				GameServerDAO.getInstance().unregisterAll();
				
				System.out.println("Unregistered all Game Servers.");
			} else {
				if (id == null) {
					System.err.println("The Game Server Id is required!");
					return;
				}
				
				GameServerDAO.getInstance().unregister(id);
				
				System.out.println("Unregistered Game Server Id " + id + ".");
			}
		} catch (Exception ex) {
			System.err.println("There has been an error unregistering " + (all ? "all servers!" : id + " game server!"));
			ex.printStackTrace();
		}
	}
}
