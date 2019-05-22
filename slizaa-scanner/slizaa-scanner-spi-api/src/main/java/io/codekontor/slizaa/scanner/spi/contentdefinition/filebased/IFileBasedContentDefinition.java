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

import java.io.File;
import java.util.Collection;
import java.util.Set;

import io.codekontor.slizaa.scanner.spi.contentdefinition.ContentType;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinition;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IFileBasedContentDefinition extends IContentDefinition {

  /**
   * <p>
   * Returns a {@link Set} of all resources of the specified type
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   *
   * @param type
   * @return a Set of resources, never null.
   */
  Collection<IFile> getFiles(ContentType type);

  /**
   * <p>
   * Returns a {@link Set} of all binary resources. This is a convenience method for {@link #getFiles(ContentType)
   * getResources(ContentType.BINARY)}
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   *
   * @return a Set of resources, never null.
   */
  Collection<IFile> getBinaryFiles();

  /**
   * <p>
   * Returns all source resources. This is a convenience method for {@link #getFiles(ContentType)
   * getResources(ContentType.SOURCE)}
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   *
   * @return a Set of resources, never null.
   */
  Collection<IFile> getSourceFiles();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  Collection<File> getBinaryRootPaths();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  Collection<File> getSourceRootPaths();
}
