/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql;

public class ErrorMessages {

  public static final String ERR_NON_EXISTING_DATABASE = "The specified database (id '%s') does not exist.";
  
  public final static RuntimeException newException(String msg, Object... args) {
    String message = args.length > 0 ? String.format(msg, args) : msg;
    return new RuntimeException(message);    
  }
  
  public final static RuntimeException newException(Exception cause, String msg, Object... args) {
    String message = args.length > 0 ? String.format(msg, args) : msg;
    return new RuntimeException(message, cause);    
  }
}
