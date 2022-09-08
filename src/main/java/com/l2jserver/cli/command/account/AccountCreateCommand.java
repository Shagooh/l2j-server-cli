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

import com.l2jserver.cli.command.AbstractCommand;
import com.l2jserver.cli.dao.AccountDAO;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Account create command.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "create")
public class AccountCreateCommand extends AbstractCommand {
	
	@Option(names = {
		"-u",
		"--username"
	}, required = true, description = "Username")
	private String username;
	
	@Option(names = {
		"-p",
		"--password"
	}, required = true, interactive = true, description = "Password")
	private String password;
	
	@Option(names = {
		"-a",
		"--access-level"
	}, defaultValue = "0", description = "Access Level")
	private int accessLevel = 0;
	
	@Override
	public void run() {
		System.out.println("Creating account " + username + "...");
		
		AccountDAO.getInstance().upsert(username, password, accessLevel);
	}
}
