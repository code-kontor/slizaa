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
package io.codekontor.slizaa.server.graphql.serverconfig;

public class ServerExtension {

  private String symbolicName;

  private String version;

  public ServerExtension(String symbolicName, String version) {
    this.symbolicName = symbolicName;
    this.version = version;
  }
  
  public ServerExtension() {
    super();
  }

  public String getSymbolicName() {
    return symbolicName;
  }

  public String getVersion() {
    return version;
  }

  void setSymbolicName(String symbolicName) {
    this.symbolicName = symbolicName;
  }

  void setVersion(String version) {
    this.version = version;
  }
  
  
}
