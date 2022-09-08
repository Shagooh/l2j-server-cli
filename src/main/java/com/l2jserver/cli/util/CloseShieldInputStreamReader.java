/*
 * Copyright © 2019-2020 L2J Server
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
package com.l2jserver.cli.util;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Close Shield InputStreamReader.
 * @author Zoey76
 * @version 1.0.6
 */
public class CloseShieldInputStreamReader extends InputStreamReader {
	
	public CloseShieldInputStreamReader(InputStream inputStream) {
		super(inputStream);
	}
	
	@Override
	public void close() {
		// Do nothing.
	}
}