/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@ConfigurationProperties(prefix = "database")
public class SlizaaServiceDatabaseProperties {

  /** the default slizaa working directory name */
  private static final String DEFAULT_SLIZAA_WOKRING_DIR  = "slizaa-work";

  private static final String DEFAULT_SLIZAA_DATABASE_DIR = "databases";

  private String              _rootDirectory;

  public String getRootDirectory() {
    return getDatabaseRootDirectoryAsFile().getAbsolutePath();
  }

  public void setRootDirectory(String rootDirectory) {
    this._rootDirectory = rootDirectory;
  }

  public File getDatabaseRootDirectoryAsFile() {

    // set the default working directory
    if (this._rootDirectory == null || this._rootDirectory.isEmpty()) {
            this._rootDirectory = new File(DEFAULT_SLIZAA_WOKRING_DIR + File.separatorChar + DEFAULT_SLIZAA_DATABASE_DIR)
          .getAbsolutePath();
    }

    File result = new File(_rootDirectory);
    if (!result.exists()) {
      result.mkdirs();
    }

    if (!result.isDirectory()) {
      throw new RuntimeException(String.format("File '%s' is not a directory.", result.getAbsolutePath()));
    }

    return result;
  }
}
