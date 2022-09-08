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
package com.l2jserver.cli.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Server Names.
 * @author Zoey76
 * @version 1.0.0
 */
public class ServerNames {
	
	private static final Map<Integer, String> SERVERS = new HashMap<>();
	
	static {
		SERVERS.put(0, "Bartz");
		SERVERS.put(1, "Sieghardt");
		SERVERS.put(2, "Kain");
		SERVERS.put(3, "Lionna");
		SERVERS.put(4, "Erica");
		SERVERS.put(5, "Gustin");
		SERVERS.put(6, "Devianne");
		SERVERS.put(7, "Hindemith");
		SERVERS.put(8, "Teon (EURO)");
		SERVERS.put(9, "Franz (EURO)");
		SERVERS.put(10, "Luna (EURO)");
		SERVERS.put(11, "Sayha");
		SERVERS.put(12, "Aria");
		SERVERS.put(13, "Phoenix");
		SERVERS.put(14, "Chronos");
		SERVERS.put(15, "Naia (EURO)");
		SERVERS.put(16, "Elhwynna");
		SERVERS.put(17, "Ellikia");
		SERVERS.put(18, "Shikken");
		SERVERS.put(19, "Scryde");
		SERVERS.put(20, "Frikios");
		SERVERS.put(21, "Ophylia");
		SERVERS.put(22, "Shakdun");
		SERVERS.put(23, "Tarziph");
		SERVERS.put(24, "Aria");
		SERVERS.put(25, "Esenn");
		SERVERS.put(26, "Elcardia");
		SERVERS.put(27, "Yiana");
		SERVERS.put(28, "Seresin");
		SERVERS.put(29, "Tarkai");
		SERVERS.put(30, "Khadia");
		SERVERS.put(31, "Roien");
		SERVERS.put(32, "Kallint (Non-PvP)");
		SERVERS.put(33, "Baium");
		SERVERS.put(34, "Kamael");
		SERVERS.put(35, "Beleth");
		SERVERS.put(36, "Anakim");
		SERVERS.put(37, "Lilith");
		SERVERS.put(38, "Thifiel");
		SERVERS.put(39, "Lithra");
		SERVERS.put(40, "Lockirin");
		SERVERS.put(41, "Kakai");
		SERVERS.put(42, "Cadmus");
		SERVERS.put(43, "Athebaldt");
		SERVERS.put(44, "Blackbird");
		SERVERS.put(45, "Ramsheart");
		SERVERS.put(46, "Esthus");
		SERVERS.put(47, "Vasper");
		SERVERS.put(48, "Lancer");
		SERVERS.put(49, "Ashton");
		SERVERS.put(50, "Waytrel");
		SERVERS.put(51, "Waltner");
		SERVERS.put(52, "Tahnford");
		SERVERS.put(53, "Hunter");
		SERVERS.put(54, "Dewell");
		SERVERS.put(55, "Rodemaye");
		SERVERS.put(56, "Ken Rauhel");
		SERVERS.put(57, "Ken Abigail");
		SERVERS.put(58, "Ken Orwen");
		SERVERS.put(59, "Van Holter");
		SERVERS.put(60, "Desperion");
		SERVERS.put(61, "Einhovant");
		SERVERS.put(62, "Shunaiman");
		SERVERS.put(63, "Faris");
		SERVERS.put(64, "Tor");
		SERVERS.put(65, "Carneiar");
		SERVERS.put(66, "Dwyllios");
		SERVERS.put(67, "Baium");
		SERVERS.put(68, "Hallate");
		SERVERS.put(69, "Zaken");
		SERVERS.put(70, "Core");
	}
	
	public static String getServer(int id) {
		return SERVERS.getOrDefault(id, "Undefined");
	}
	
	public static Map<Integer, String> getServers() {
		return SERVERS;
	}
}
