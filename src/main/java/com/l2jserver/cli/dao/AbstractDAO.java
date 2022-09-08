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
package com.l2jserver.cli.dao;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.l2jserver.cli.config.ServerConfiguration;
import com.l2jserver.cli.database.ConnectionFactory;
import com.l2jserver.cli.util.FileWriterStdout;
import com.l2jserver.cli.util.SQLFilter;

/**
 * Abstract DAO.
 * @author Zoey76
 * @version 1.0.0
 */
class AbstractDAO {
	
	private static final String CREATE_DATABASE = "CREATE DATABASE ";
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
	
	private final ConnectionFactory connectionFactory;
	
	private final String database;
	
	AbstractDAO(ServerConfiguration server) {
		this.database = server.getDatabaseName();
		
		this.connectionFactory = ConnectionFactory.builder() //
			.withDatabaseName(server.getDatabaseName()) //
			.withUrl(server.getDatabaseURL()) //
			.withUser(server.getDatabaseUser()) //
			.withPassword(server.getDatabasePassword()) //
			.build();
	}
	
	public String getDatabase() {
		return database;
	}
	
	public Connection getConnection() {
		return connectionFactory.getConnection();
	}
	
	public void createDatabase() throws SQLException {
		try (var con = connectionFactory.getPlainConnection(); //
			var st = con.createStatement()) {
			st.executeUpdate(CREATE_DATABASE + database);
		}
	}
	
	public void executeSQLScript(File file) {
		String line = "";
		try (var con = getConnection(); //
			var stmt = con.createStatement(); //
			var scn = new Scanner(file)) {
			var sb = new StringBuilder();
			while (scn.hasNextLine()) {
				line = scn.nextLine();
				if (line.startsWith("--")) {
					continue;
				}
				
				if (line.contains("--")) {
					line = line.split("--")[0];
				}
				
				line = line.trim();
				if (!line.isEmpty()) {
					sb.append(line).append(System.getProperty("line.separator"));
				}
				
				if (line.endsWith(";")) {
					stmt.execute(sb.toString());
					sb = new StringBuilder();
				}
			}
		} catch (Exception ex) {
			System.err.println("There has been an error executing file " + file.getName() + "!");
			ex.printStackTrace();
		}
	}
	
	public void executeDirectoryOfSQLScripts(File dir, boolean skipErrors) {
		final var files = dir.listFiles(new SQLFilter());
		if (files != null) {
			Arrays.sort(files);
			for (var file : files) {
				if (skipErrors) {
					try {
						executeSQLScript(file);
					} catch (Throwable t) {
					}
				} else {
					executeSQLScript(file);
				}
			}
		}
	}
	
	public void createDump() {
		try (var con = getConnection(); //
			var s = con.createStatement(); //
			var rset = s.executeQuery("SHOW TABLES")) {
			final var fileName = database + "_dump_" + DATE_TIME_FORMATTER.format(LocalDateTime.now()) + ".sql";
			var dump = new File("dumps", fileName);
			new File("dumps").mkdir();
			dump.createNewFile();
			
			if (rset.last()) {
				rset.beforeFirst();
			}
			
			try (var fileWriter = new FileWriter(dump);
				var fws = new FileWriterStdout(fileWriter)) {
				while (rset.next()) {
					fws.println("CREATE TABLE `" + rset.getString(1) + "`");
					fws.println("(");
					try (var desc = con.createStatement();
						var dset = desc.executeQuery("DESC " + rset.getString(1))) {
						final var keys = new HashMap<String, List<String>>();
						var isFirst = true;
						while (dset.next()) {
							if (!isFirst) {
								fws.println(",");
							}
							fws.print("\t`" + dset.getString(1) + "`");
							fws.print(" " + dset.getString(2));
							if (dset.getString(3).equals("NO")) {
								fws.print(" NOT NULL");
							}
							if (!dset.getString(4).isEmpty()) {
								if (!keys.containsKey(dset.getString(4))) {
									keys.put(dset.getString(4), new ArrayList<String>());
								}
								keys.get(dset.getString(4)).add(dset.getString(1));
							}
							if (dset.getString(5) != null) {
								fws.print(" DEFAULT '" + dset.getString(5) + "'");
							}
							if (!dset.getString(6).isEmpty()) {
								fws.print(" " + dset.getString(6));
							}
							isFirst = false;
						}
						if (keys.containsKey("PRI")) {
							fws.println(",");
							fws.print("\tPRIMARY KEY (");
							isFirst = true;
							for (String key : keys.get("PRI")) {
								if (!isFirst) {
									fws.print(", ");
								}
								fws.print("`" + key + "`");
								isFirst = false;
							}
							fws.print(")");
						}
						if (keys.containsKey("MUL")) {
							fws.println(",");
							isFirst = true;
							for (String key : keys.get("MUL")) {
								if (!isFirst) {
									fws.println(", ");
								}
								fws.print("\tKEY `key_" + key + "` (`" + key + "`)");
								isFirst = false;
							}
						}
						fws.println();
						fws.println(");");
						fws.flush();
					}
					
					try (var desc = con.createStatement();
						var dset = desc.executeQuery("SELECT * FROM " + rset.getString(1))) {
						boolean isFirst = true;
						int cnt = 0;
						while (dset.next()) {
							if ((cnt % 100) == 0) {
								fws.println("INSERT INTO `" + rset.getString(1) + "` VALUES ");
							} else {
								fws.println(",");
							}
							
							fws.print("\t(");
							boolean isInFirst = true;
							for (int i = 1; i <= dset.getMetaData().getColumnCount(); i++) {
								if (!isInFirst) {
									fws.print(", ");
								}
								
								if (dset.getString(i) == null) {
									fws.print("NULL");
								} else {
									fws.print("'" + dset.getString(i).replace("\'", "\\\'") + "'");
								}
								isInFirst = false;
							}
							fws.print(")");
							isFirst = false;
							
							if ((cnt % 100) == 99) {
								fws.println(";");
							}
							cnt++;
						}
						if (!isFirst && ((cnt % 100) != 0)) {
							fws.println(";");
						}
						fws.println();
						fws.flush();
					}
				}
				fws.flush();
			}
		} catch (Exception ex) {
			System.err.println("There has been an error creating a database backup for " + database + "!");
			ex.printStackTrace();
		}
	}
}
