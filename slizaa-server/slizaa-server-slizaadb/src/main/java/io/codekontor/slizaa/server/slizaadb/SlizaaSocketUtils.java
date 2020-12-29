/**
 * slizaa-server-slizaadb - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.server.slizaadb;

import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.ServerSocket;

public class SlizaaSocketUtils {

	private static final int MIN_PORT_NUMBER = 1024;
	
	private static final int MAX_PORT_NUMBER = 65535;

	/**
	 * 
	 */
	public static int findAvailableTcpPort() {
		return SocketUtils.findAvailableTcpPort();
	}
	
	/**
	 * Checks to see if a specific port is available.
	 *
	 * @param port the port to check for availability
	 */
	public static boolean available(int port) {
		
	    if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
	        return false;
	    }

	    ServerSocket ss = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
}
