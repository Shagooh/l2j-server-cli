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

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.l2jserver.cli.config.Configuration;

/**
 * Game Server DAO.
 * @author Zoey76
 * @version 1.0.0
 */
public class GameServerDAO extends AbstractDAO {
	
	private static final String SELECT_SERVERS = "SELECT * FROM gameservers";
	
	private static final String INSERT_SERVER = "INSERT INTO gameservers (hexid, server_id, host) values (?,?,?)";
	
	private static final String DELETE_SERVER = "DELETE FROM gameservers WHERE server_id = ?";
	
	private static final String DELETE_ALL_SERVERS = "DELETE FROM gameservers";
	
	private GameServerDAO() {
		super(Configuration.loginServer());
	}
	
	public void unregister(int id) throws SQLException {
		try (var con = getConnection();
			var ps = con.prepareStatement(DELETE_SERVER)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
	
	public void unregisterAll() throws SQLException {
		try (var con = getConnection();
			var s = con.createStatement()) {
			s.executeUpdate(DELETE_ALL_SERVERS);
		}
	}
	
	public void register(byte[] hexId, int id, String externalHost) throws SQLException {
		try (var con = getConnection();
			var ps = con.prepareStatement(INSERT_SERVER)) {
			ps.setString(1, new BigInteger(hexId).toString(16));
			ps.setInt(2, id);
			ps.setString(3, externalHost);
			ps.executeUpdate();
		}
	}
	
	public Set<Integer> gameServers() throws SQLException {
		final var result = new HashSet<Integer>();
		try (var con = getConnection();
			var s = con.createStatement();
			var rs = s.executeQuery(SELECT_SERVERS)) {
			while (rs.next()) {
				result.add(rs.getInt("server_id"));
			}
		}
		return result;
	}
	
	public static GameServerDAO getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final GameServerDAO INSTANCE = new GameServerDAO();
	}
}
