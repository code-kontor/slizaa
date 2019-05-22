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

import java.io.File;

/**
 * <p>
 * Factory to create {@link IGraphDb} instances.
 * </p>
 * <p>
 * The creation of a graph database instance requires at least the specification of a port and and store directory. The
 * port parameter specifies the port clients should use to connect against this graph database via the BOLT protocol.
 * </p>
 * <p>
 * Example: <code><pre>
 * IGraphDbFactory graphDbFactory = ...;
 *
 * graphDbFactory.newGraphDb(5001, "/home/exampleUser/databaseDir").create();
 * </pre></code>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IGraphDbFactory {

  /**
   * <p>
   * Creates a new {@link IGraphDbBuilder} to configure a graph database instance.
   * </p>
   *
   * @param databaseDir
   *          the database directory
   * @return a newly created {@link IGraphDbBuilder}
   */
  IGraphDbBuilder newGraphDb(File databaseDir);

  /**
   * <p>
   * Creates a new {@link IGraphDbBuilder} to configure a graph database instance.
   * </p>
   *
   * @param port
   *          clients should use to connect against this graph database via the BOLT protocol.
   * @param databaseDir
   *          the database directory
   * @return a newly created {@link IGraphDbBuilder}
   */
  IGraphDbBuilder newGraphDb(int port, File databaseDir);

  /**
   * <p>
   * The {@link IGraphDbBuilder} is returned by the {@link IGraphDbFactory#newGraphDb(int, File)} method to allow the
   * configuration of the database to create.
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public interface IGraphDbBuilder {

    /**
     * <p>
     * Sets an arbitrary object (the user object) that is associated with the database instance. This user object can be
     * requested from the {@link IGraphDb} via {@link IGraphDb#getUserObject(Class)}.
     * </p>
     *
     * @param userObject
     *          an arbitrary object that is associated with the database instance.
     * @return this {@link IGraphDbBuilder}
     */
    <T> IGraphDbBuilder withUserObject(T userObject);

    /**
     * <p>
     * Sets a configuration value. Which values are allowed here depends on the underlying graph database
     * implementation.
     * </p>
     *
     * @param key
     *          the key of the value.
     * @param value
     *          the value of the configuration item.
     * @return this {@link IGraphDbBuilder}
     */
    IGraphDbBuilder withConfiguration(String key, Object value);

    /**
     * <p>
     * Finally creates the database.
     * </p>
     *
     * @return the created database.
     */
    IGraphDb create();
  }
}
