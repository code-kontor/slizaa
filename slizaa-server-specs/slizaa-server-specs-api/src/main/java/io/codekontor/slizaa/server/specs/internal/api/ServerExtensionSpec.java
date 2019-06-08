/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs.internal.api;

import io.codekontor.slizaa.server.specs.api.IServerExtensionSpec;
import io.codekontor.slizaa.server.specs.internal.model.serverconfig.ServerExtension;

public class ServerExtensionSpec {

  private String symbolicName;

  private String version;
  
  @SuppressWarnings("unused")
  private ServerExtension() {
    //
  }

  public ServerExtension(String symbolicName, String version) {
    this.symbolicName = checkNotNull(symbolicName);
    this.version = checkNotNull(version);
  }

  public String getSymbolicName() {
    return symbolicName;
  }

  public String getVersion() {
    return version;
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " [symbolicName=" + symbolicName + ", version=" + version + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((symbolicName == null) ? 0 : symbolicName.hashCode());
    result = prime * result + ((version == null) ? 0 : version.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (! (obj instanceof ServerExtension )) {
      return false;
    }
    ServerExtension other = (ServerExtension) obj;
    if (symbolicName == null) {
      if (other.symbolicName != null) {
        return false;
      }
    } else if (!symbolicName.equals(other.symbolicName)) {
      return false;
    }
    if (version == null) {
      if (other.version != null) {
        return false;
      }
    } else if (!version.equals(other.version)) {
      return false;
    }
    return true;
  }

}
