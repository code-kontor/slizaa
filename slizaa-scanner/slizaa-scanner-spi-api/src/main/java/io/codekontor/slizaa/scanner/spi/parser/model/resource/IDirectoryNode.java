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
package io.codekontor.slizaa.scanner.spi.parser.model.resource;

public interface IDirectoryNode {

  /**
   * <p>
   * The full path of the directory, e.g. <code>'org/example'</code>. Note that resource paths are always
   * slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_PATH     = "path";

  /**
   * <p>
   * the root directory or archive file that contains the resource (e.g. <code>'c:/dev/classes.zip'</code> or
   * <code>'c:/dev/source'</code>). Note that resource paths are always slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_ROOT     = "root";

  /** - */
  public static final String PROPERTY_IS_EMPTY = "isEmpty";
}
