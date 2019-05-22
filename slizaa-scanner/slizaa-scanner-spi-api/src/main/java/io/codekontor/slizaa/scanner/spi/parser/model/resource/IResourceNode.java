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

import io.codekontor.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * Defines the interface for a resource node bean.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IResourceNode extends INode {
  
  /**
   * <p>
   * The full path of the resource, e.g. <code>'org/example/Test.java'</code>. Note that resource paths are always
   * slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_PATH               = "path";

  /**
   * <p>
   * the root directory or archive file that contains the resource (e.g. <code>'c:/dev/classes.zip'</code> or
   * <code>'c:/dev/source'</code>). Note that resource paths are always slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_ROOT               = "root";


  /**
   * <p>
   * Return <code>true</code> if references of the underlying resource should be parsed.
   * </p>
   */
  public static final String PROPERTY_ANALYSE_REFERENCES = "analyseReferences";

  /**
   * <p>
   * Return <code>true</code> if the last attempt to parse the resource returned one or more errors.
   * </p>
   */
  public static final String PROPERTY_ERRONEOUS          = "erroneous";

  /**
   * <p>
   * Returns the type of the resource (either {@link ResourceType#SOURCE} or {@link ResourceType#BINARY}.
   * </p>
   * 
   * @return the type of the resource (either {@link ResourceType#SOURCE} or {@link ResourceType#BINARY}.
   */
  ResourceType getResourceType();
}
