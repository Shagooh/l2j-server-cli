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
package com.l2jserver.cli.util;

import org.eclipse.jgit.lib.BatchingProgressMonitor;
import org.slf4j.Logger;

/**
 * Logger progress monitor.
 * @author Zoey76
 * @version 1.0.0
 */
public class LoggerProgressMonitor extends BatchingProgressMonitor {
	private final Logger log;
	
	public LoggerProgressMonitor(Logger log) {
		this.log = log;
	}
	
	@Override
	protected void onUpdate(String taskName, int workCurr) {
		final var s = new StringBuilder();
		format(s, taskName, workCurr);
		send(s);
	}
	
	@Override
	protected void onEndTask(String taskName, int workCurr) {
		final var s = new StringBuilder();
		format(s, taskName, workCurr);
		s.append("\n");
		send(s);
	}
	
	private void format(StringBuilder s, String taskName, int workCurr) {
		s.append("\r");
		s.append(taskName);
		s.append(": ");
		while (s.length() < 25) {
			s.append(' ');
		}
		s.append(workCurr);
	}
	
	@Override
	protected void onUpdate(String taskName, int cmp, int totalWork, int pcnt) {
		final var s = new StringBuilder();
		format(s, taskName, cmp, totalWork, pcnt);
		send(s);
	}
	
	@Override
	protected void onEndTask(String taskName, int cmp, int totalWork, int pcnt) {
		final var s = new StringBuilder();
		format(s, taskName, cmp, totalWork, pcnt);
		s.append("\n");
		send(s);
	}
	
	private void format(StringBuilder s, String taskName, int cmp, int totalWork, int pcnt) {
		s.append("\r");
		s.append(taskName);
		s.append(": ");
		while (s.length() < 25)
			s.append(' ');
		
		String endStr = String.valueOf(totalWork);
		StringBuilder curStr = new StringBuilder(String.valueOf(cmp));
		while (curStr.length() < endStr.length()) {
			curStr.insert(0, " ");
		}
		if (pcnt < 100)
			s.append(' ');
		if (pcnt < 10)
			s.append(' ');
		s.append(pcnt);
		s.append("% (");
		s.append(curStr);
		s.append("/");
		s.append(endStr);
		s.append(")");
	}
	
	private void send(StringBuilder s) {
		log.info(s.toString());
	}
}
