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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Properties;

import com.l2jserver.cli.command.AbstractCommand;
import com.l2jserver.cli.dao.GameServerDAO;
import com.l2jserver.cli.model.ServerNames;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Game Server register command.
 * @author Zoey76
 * @version 1.0.0
 */
@Command(name = "register", aliases = "r", description = "Adds a game server to the login server.")
public class GameServerRegisterCommand extends AbstractCommand {
	
	@Option(names = {
		"-fb",
		"--fallback"
	}, description = "If during registration the specified game server Id is in use, an attempt with the first available Id will be made.")
	private boolean fallback;
	
	@Option(names = {
		"-f",
		"--force"
	}, description = "Forces a game server register operation to overwrite a previous registration on the specified Id, if necessary.")
	private boolean force = false;
	
	@Option(names = "-id", description = "Game Server Id.")
	private Integer id;
	
	@Option(names = {
		"-o",
		"--output"
	}, description = "Path where the file will be saved.", defaultValue = ".")
	private File outputPath = new File(".");
	
	@Override
	public void run() {
		try {
			register();
		} catch (Exception ex) {
			System.err.println("There has been an error registering a Game Server!");
			ex.printStackTrace();
		}
	}
	
	private void register() throws SQLException, IOException, NoSuchAlgorithmException {
		var gameservers = GameServerDAO.getInstance().gameServers();
		if (force) {
			if (id == null) {
				System.err.println("A Game Server Id must be specified when using --force!");
				return;
			}
			
			if (gameservers.contains(id)) {
				GameServerDAO.getInstance().unregister(id);
				gameservers.remove(id);
			}
		} else if (fallback) {
			if ((id == null) || gameservers.contains(id)) {
				for (var gs : ServerNames.getServers().entrySet()) {
					if (gameservers.contains(gs.getKey())) {
						continue;
					}
					id = gs.getKey();
					break;
				}
			}
		}
		
		// Validate Id not in use.
		if (gameservers.contains(id)) {
			System.err.println("The Id " + id + " is in use!");
			return;
		}
		
		registerGameServer(id, outputPath);
	}
	
	public static void registerGameServer(int id, File outputPath) throws IOException, SQLException, NoSuchAlgorithmException {
		final var hexId = generateHex();
		
		GameServerDAO.getInstance().register(hexId, id, "");
		
		final var hexSetting = new Properties();
		final var file = new File(outputPath, "hexid.txt");
		file.createNewFile();
		try (OutputStream out = new FileOutputStream(file)) {
			hexSetting.setProperty("ServerID", String.valueOf(id));
			hexSetting.setProperty("HexID", new BigInteger(hexId).toString(16));
			hexSetting.store(out, "The HexId to Auth into LoginServer");
		}
		
		System.out.println("Registered Game Server Id " + id + ".");
	}
	
	private static byte[] generateHex() throws NoSuchAlgorithmException {
		final var bytes = new byte[16];
		SecureRandom.getInstanceStrong().nextBytes(bytes);
		return bytes;
	}
}
