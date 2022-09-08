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
import java.util.Arrays;
import java.util.prefs.Preferences;

import com.l2jserver.cli.config.ServerConfiguration;
import com.l2jserver.cli.model.DatabaseInstallType;
import com.l2jserver.cli.util.SQLFilter;

/**
 * Database DAO.
 * 
 * @author Zoey76
 * @version 1.0.0
 */
public abstract class AbstractDatabaseDAO extends AbstractDAO {

	AbstractDatabaseDAO(ServerConfiguration server) {
		super(server);
	}

	public void mods(File sqlPath) {
		final var modsPath = new File(sqlPath, "mods");
		if (modsPath.exists()) {
			System.out.println("Installing mod tables...");
			runSQLFiles(modsPath.listFiles(new SQLFilter()));
		}
	}

	public void custom(File sqlPath) {
		final var customPath = new File(sqlPath, "custom");
		if (customPath.exists()) {
			System.out.println("Installing custom tables...");
			runSQLFiles(customPath.listFiles(new SQLFilter()));
		}
	}

	public void basic(File sqlPath) {
		System.out.println("Installing basic SQL scripts...");
		final var files = sqlPath.listFiles(new SQLFilter());
		runSQLFiles(files);
	}

	protected void updates(DatabaseInstallType mode, String cleanup, File sqlPath) {
		final var userPreferences = Preferences.userRoot();
		final var updatePath = new File(sqlPath, "updates");
		final var updatePreferences = getDatabase() + "_update";

		switch (mode) {
		case FULL: {
			System.out.println("Executing cleanup script...");

			runSQLFiles(new File(sqlPath, cleanup));

			if (updatePath.exists()) {
				final var sb = new StringBuilder();
				for (var sqlFile : updatePath.listFiles(new SQLFilter())) {
					sb.append(sqlFile.getName() + ';');
				}
				userPreferences.put(updatePreferences, sb.toString());
			}
			break;
		}
		case UPDATE: {
			System.out.println("Installing update SQL scripts...");
			final var updated = userPreferences.get(updatePreferences, "");
			if (updatePath.exists()) {
				for (var sqlFile : updatePath.listFiles(new SQLFilter())) {
					if (!updated.contains(sqlFile.getName())) {
						try {
							System.out.println("Installing " + sqlFile.getName() + "...");
							executeSQLScript(sqlFile);
						} catch (Exception ex) {
							System.err.println("There has been an error executing SQL update!");
							ex.printStackTrace();
							return;
						}
						userPreferences.put(updatePreferences, updated + sqlFile.getName() + ";");
					}
				}
			}
			break;
		}
		}
	}

	public void updates(DatabaseInstallType mode, File sqlPath) {
		updates(mode, "cleanup/cleanup.sql", sqlPath);
	}

	private void runSQLFiles(File... sqlFiles) {
		Arrays.sort(sqlFiles);
		
		for (var sqlFile : sqlFiles) {
			try {
				System.out.println("Running " + sqlFile.getName() + "...");
				executeSQLScript(sqlFile);
			} catch (Exception ex) {
				System.err.println("There has been an error executing SQL file " + sqlFile.getName() + "!");
				ex.printStackTrace();
			}
		}
	}
}
