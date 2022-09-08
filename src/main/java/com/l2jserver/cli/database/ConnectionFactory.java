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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connection Factory.
 * @author Zoey76
 * @version 1.0.1
 */
public class ConnectionFactory {
	
	private final String url;
	
	private final String databaseName;
	
	private final Properties properties = new Properties();
	
	private ConnectionFactory(Builder builder) {
		this.url = builder.url;
		this.databaseName = builder.databaseName;
		this.properties.setProperty("user", builder.user);
		this.properties.setProperty("password", builder.password);
		this.properties.putAll(DatabaseType.getType(builder.url).getParameters());
	}
	
	public Connection getPlainConnection() {
		try {
			return DriverManager.getConnection(url, properties);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public Connection getConnection() {
		try {
			final var con = DriverManager.getConnection(url + "/" + databaseName, properties);
			con.setCatalog(databaseName);
			return con;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		private String url;
		private String user;
		private String password;
		private String databaseName;
		
		private Builder() {
		}
		
		public Builder withUrl(String url) {
			this.url = url;
			return this;
		}
		
		public Builder withUser(String user) {
			this.user = user;
			return this;
		}
		
		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}
		
		public Builder withDatabaseName(String databaseName) {
			this.databaseName = databaseName;
			return this;
		}
		
		public ConnectionFactory build() {
			return new ConnectionFactory(this);
		}
	}
}
