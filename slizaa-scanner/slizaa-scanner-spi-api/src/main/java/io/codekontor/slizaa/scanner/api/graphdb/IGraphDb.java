/**
 * slizaa-scanner-spi-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.api.graphdb;

/**
 * <p>
 * Represents a (local) database that can be accessed via the BOLT protocol.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IGraphDb extends AutoCloseable {

  /**
   * <p>
   * Returns the port that clients should use to connect against this graph database via the BOLT protocol..
   * </p>
   *
   * @return the database port
   */
  int getPort();

  /**
   * <p>
   * Shuts the database down.
   * </p>
   */
  void shutdown();

  /**
   * <p>
   * Returns the user object with the specified type if one exists, or <code>null</code> otherwise.
   * </p>
   *
   * @param userObject
   *          the associated user object
   * @return the user object with the specified type.
   */
  <T> T getUserObject(Class<T> userObject);

  /**
   * <p>
   * Return <code>true</code> if the graph has an associated user object with the given type, <code>false</code>
   * otherwise.
   * </p>
   *
   * @param type
   *          the type of the user object
   * @return <code>true</code> if the graph has an associated user object with the given type, <code>false</code>
   *         otherwise.
   */
  <T> boolean hasUserObject(Class<T> type);
}
