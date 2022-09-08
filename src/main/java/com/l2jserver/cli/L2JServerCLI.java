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
package com.l2jserver.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import com.l2jserver.cli.command.AccountCommand;
import com.l2jserver.cli.command.BuildCommand;
import com.l2jserver.cli.command.CodeCommand;
import com.l2jserver.cli.command.ConfigurationEditorGUICommand;
import com.l2jserver.cli.command.DatabaseCommand;
import com.l2jserver.cli.command.DeployCommand;
import com.l2jserver.cli.command.GameServerCommand;
import com.l2jserver.cli.command.HelpCommand;
import com.l2jserver.cli.command.QuitCommand;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * L2J Server CLI.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "l2jcli", version = "1.0", subcommands = {
	CodeCommand.class,
	BuildCommand.class,
	DeployCommand.class,
	DatabaseCommand.class,
	GameServerCommand.class,
	AccountCommand.class,
	HelpCommand.class,
	QuitCommand.class,
	ConfigurationEditorGUICommand.class
})
public class L2JServerCLI implements Callable<Void> {
	@Override
	public Void call() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~                                     ~");
		System.out.println("~ (>^_^)> Welcome to L2J CLI  <(^_^<) ~");
		System.out.println("~                                     ~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println();
		try (var isr = new InputStreamReader(System.in); //
			var br = new BufferedReader(isr)) {
			while (true) {
				System.out.print(">>> ");
				final var args = br.readLine().split(" ");
				new CommandLine(new L2JServerCLI()).execute(args);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		new CommandLine(new L2JServerCLI()).execute(args);
	}
}