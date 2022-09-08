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
package com.l2jserver.cli.database;

import java.util.Properties;

/**
 * DatabaseType.
 * @author Zoey76
 * @version 1.0.1
 */
public enum DatabaseType {
	MARIADB("MariaDB", "mariadb", true),
	MYSQL("MySQL", "mysql", true) {
		@Override
		public Properties getParameters() {
			properties.setProperty("serverTimezone", "UTC");
			// For MySQL versions previous to 8.0.17:
			properties.setProperty("useSSL", "false");
			properties.setProperty("allowPublicKeyRetrieval", "true");
			return properties;
		}
	},
	SQLSERVER("Microsoft SQL Server", "sqlserver", false),
	HSQLDB("HyperSQL DataBase", "hsqldb", false),
	H2("H2 Database Engine", "h2", false),
	POSTGRESQL("PostgreSQL", "postgresql", false),
	ORACLE("Oracle Database", "oracle", false),
	DERBY("Apache Derby", "derby", false);
	
	String name;
	
	String jdbcPrefix;
	
	boolean supported;
	
	Properties properties;
	
	private DatabaseType(String name, String jdbcPrefix, boolean supported) {
		this.name = name;
		this.jdbcPrefix = jdbcPrefix;
		this.supported = supported;
		this.properties = new Properties();
	}
	
	public Properties getParameters() {
		return properties;
	}
	
	public static DatabaseType getType(String url) {
		for (DatabaseType databaseType : DatabaseType.values()) {
			if (url != null && url.startsWith("jdbc:" + databaseType.jdbcPrefix)) {
				return databaseType;
			}
		}
		return null;
	}
}
