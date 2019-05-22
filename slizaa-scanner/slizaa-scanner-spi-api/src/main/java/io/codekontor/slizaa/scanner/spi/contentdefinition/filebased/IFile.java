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
package io.codekontor.slizaa.scanner.spi.contentdefinition.filebased;

/**
 * <p>
 * Defines the common interface for a resource. Normally a resource is either a file or a entry in an archive file. A
 * resource has a path and a <code>timestamp</code>. The {@link IFile} interface also provides convenience methods
 * to access the name of the resource and the path of the containing directory.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IFile {

  /**
   * <p>
   * Returns the root directory or archive file that contains the resource (e.g. <code>'c:/dev/classes.zip'</code> or
   * <code>'c:/dev/source'</code>). Note that resource paths are always slash-delimited ('/').
   * </p>
   * 
   * @return the root directory or archive file that contains the resource.
   */
  public String getRoot();

  /**
   * <p>
   * Returns the full path of the resource, e.g. <code>'org/example/Test.java'</code>. Note that resource paths are
   * always slash-delimited ('/').
   * </p>
   * <p>
   * The result of this method is equivalent to <code>'getDirectory() + "/" + getName()'</code>.
   * </p>
   * 
   * @return the full path of the resource.
   */
  public String getPath();

  /**
   * <p>
   * Returns the directory of the resource, e.g. <code>'org/example'</code>. Note that resource paths are always
   * slash-delimited ('/').
   * </p>
   * 
   * @return the directory of the resource
   */
  String getDirectory();

  /**
   * <p>
   * Returns the name of the resource, e.g. <code>'Test.java'</code>.
   * </p>
   * 
   * @return the name of the resource
   */
  String getName();

  /**
   * <p>
   * Returns the content of this resource.
   * </p>
   * 
   * @return the content of this resource.
   */
  byte[] getContent();
}
