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
package com.l2jserver.cli.command.account;

import java.util.Scanner;

import com.l2jserver.cli.command.AbstractCommand;
import com.l2jserver.cli.dao.AccountDAO;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Account delete command.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "delete")
public class AccountDeleteCommand extends AbstractCommand {
	
	@Option(names = {
		"-u",
		"--username"
	}, required = true, description = "Username")
	private String username;
	
	@Override
	public void run() {
		System.out.print("WARNING: This will not delete the gameserver data (characters, items, etc...)");
		System.out.print(" it will only delete the account login server data.");
		System.out.println();
		System.out.print("Do you really want to delete this account? y/N: ");
		try (var s = new Scanner(FILTER_INPUT_STREAM)) {
			if (YES.equalsIgnoreCase(s.next())) {
				final var deleted = AccountDAO.getInstance().delete(username);
				if (deleted) {
					System.out.println("Account " + username + " has been deleted.");
				} else {
					System.out.println("Account " + username + " does not exist!");
				}
			} else {
				System.out.println("Account " + username + " has not been deleted.");
			}
		}
	}
}
