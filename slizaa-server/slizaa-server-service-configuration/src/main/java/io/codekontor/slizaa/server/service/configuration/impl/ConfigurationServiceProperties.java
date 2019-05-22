/**
 * slizaa-server-service-configuration - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.configuration.impl;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "configuration")
public class ConfigurationServiceProperties {

  /** the default slizaa working directory name */
  private static final String DEFAULT_SLIZAA_WOKRING_DIR  = "slizaa-work";

  private static final String DEFAULT_SLIZAA_CONFIGURATION_DIR = "configuration";

  private String              _rootDirectory;

  public void setRootDirectory(String rootDirectory) {
    this._rootDirectory = rootDirectory;
  }

  public String getRootDirectory() {
    return getRootDirectoryAsFile().getAbsolutePath();
  }

  public File getRootDirectoryAsFile() {
    
    // set the default working directory
    if (this._rootDirectory == null || this._rootDirectory.isEmpty()) {
            this._rootDirectory = new File(DEFAULT_SLIZAA_WOKRING_DIR + File.separatorChar + DEFAULT_SLIZAA_CONFIGURATION_DIR)
          .getAbsolutePath();
    }

    File result = new File(_rootDirectory);
    if (!result.exists()) {
      result.mkdirs();
    }
    // TODO: check dir!
    return result;
  }
}
