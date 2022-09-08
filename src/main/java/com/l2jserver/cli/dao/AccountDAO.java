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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.l2jserver.cli.config.Configuration;
import com.l2jserver.cli.model.AccountListType;

/**
 * Account DAO.
 * @author Zoey76
 * @version 1.0.0
 */
public class AccountDAO extends AbstractDAO {
	
	private static final String REPLACE_ACCOUNT = "REPLACE accounts(login, password, accessLevel) VALUES (?, ?, ?)";
	
	private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE login = ?";
	
	private static final String UPDATE_ACCOUNT_LEVEL = "UPDATE accounts SET accessLevel = ? WHERE login = ?";
	
	private static final String[] SELECT_ACCOUNTS = { //
		"SELECT login, accessLevel FROM accounts ORDER BY login ASC", //
		"SELECT login, accessLevel FROM accounts WHERE accessLevel < 0 ORDER BY login ASC", //
		"SELECT login, accessLevel FROM accounts WHERE accessLevel > 0 ORDER BY login ASC", //
		"SELECT login, accessLevel FROM accounts WHERE accessLevel = 0 ORDER BY login ASC" //
	};
	
	private static final String ALGORITHM = "SHA";
	
	private AccountDAO() {
		super(Configuration.loginServer());
	}
	
	public boolean upsert(String username, String password, int accessLevel) {
		try (var con = getConnection(); //
			var ps = con.prepareStatement(REPLACE_ACCOUNT)) {
			final var md = MessageDigest.getInstance(ALGORITHM);
			final var newPassword = md.digest(password.getBytes(UTF_8));
			
			ps.setString(1, username);
			ps.setString(2, Base64.getEncoder().encodeToString(newPassword));
			ps.setInt(3, accessLevel);
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("There was error while creating/updating account " + username + "!");
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean changeAccountLevel(String username, int accesslevel) {
		try (var con = getConnection(); //
			var ps = con.prepareStatement(UPDATE_ACCOUNT_LEVEL)) {
			ps.setInt(1, accesslevel);
			ps.setString(2, username);
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("There was error while updating account " + username + " level!");
		}
		return false;
	}
	
	public Map<String, Integer> listAccounts(AccountListType accountListType) {
		final var result = new HashMap<String, Integer>();
		try (var con = getConnection(); //
			var st = con.createStatement(); //
			var rs = st.executeQuery(SELECT_ACCOUNTS[accountListType.ordinal()])) {
			while (rs.next()) {
				result.put(rs.getString("login"), rs.getInt("accessLevel"));
			}
		} catch (Exception ex) {
			System.out.println("There was error while listing " + accountListType.name().toLowerCase() + " accounts!");
			ex.printStackTrace();
		}
		return result;
	}
	
	public boolean delete(String username) {
		try (var con = getConnection(); //
			var ps = con.prepareStatement(DELETE_ACCOUNT)) {
			ps.setString(1, username);
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("There was error while deleting account " + username + "!");
		}
		return false;
	}
	
	public static AccountDAO getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder {
		protected static final AccountDAO INSTANCE = new AccountDAO();
	}
}
