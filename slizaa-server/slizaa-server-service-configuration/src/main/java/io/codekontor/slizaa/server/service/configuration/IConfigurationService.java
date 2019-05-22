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
package io.codekontor.slizaa.server.service.configuration;

import java.io.IOException;

public interface IConfigurationService {

  <T> void store(String configurationIdentifier, T configuration) throws IOException;

  <T> T load(String configurationIdentifier, Class<T> type) throws IOException;
  
  <T> void store(String configurationIdentifier, String fileName, T configuration) throws IOException;

  <T> T load(String configurationIdentifier, String fileName, Class<T> type) throws IOException;
}
