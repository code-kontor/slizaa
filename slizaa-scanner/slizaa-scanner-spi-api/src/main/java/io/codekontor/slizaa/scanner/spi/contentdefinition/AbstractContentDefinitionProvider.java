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
package io.codekontor.slizaa.scanner.spi.contentdefinition;

import static io.codekontor.slizaa.scanner.spi.internal.Preconditions.checkNotNull;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.codekontor.slizaa.scanner.spi.internal.contentdefinition.filebased.FileBasedContentDefinition;

/**
 * <p>
 * Superclass for all implementations of {@link ITempDefinitionProvider}
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractContentDefinitionProvider<T extends IContentDefinitionProvider<T>> implements IContentDefinitionProvider<T> {

  /** the list of content definitions */
  private List<IContentDefinition> _contentDefinitions;

  /** indicates whether or not this provider has been initialized */
  private boolean                  _isInitialized;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractContentDefinitionProvider}.
   * </p>
   */
  public AbstractContentDefinitionProvider() {

    //
    _contentDefinitions = new LinkedList<IContentDefinition>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<IContentDefinition> getContentDefinitions() {

    //
    initialize();

    //
    return Collections.unmodifiableList(_contentDefinitions);
  }

  /**
   * <p>
   * </p>
   */
  protected abstract void onInitializeProjectContent();

  /**
   * <p>
   * </p>
   */
  protected abstract void onDisposeProjectContent();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected List<IContentDefinition> contentDefinitions() {
    return _contentDefinitions;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected final void initialize() {

    if (!_isInitialized) {

      onInitializeProjectContent();

      _isInitialized = true;
    }
  }

  /**
   * <p>
   * </p>
   */
  protected void dispose() {

    if (_isInitialized) {

      onDisposeProjectContent();

      _isInitialized = false;

      _contentDefinitions.clear();
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param contentName
   * @param contentVersion
   * @param binaryPaths
   * @param sourcePaths
   * @param analyzeMode
   * @return
   */
  protected IContentDefinition createFileBasedContentDefinition(String contentName, String contentVersion,
      File[] binaryPaths, File[] sourcePaths, AnalyzeMode analyzeMode) {

    // asserts
    checkNotNull(contentName);
    checkNotNull(contentVersion);
    checkNotNull(binaryPaths);
    checkNotNull(analyzeMode);

    FileBasedContentDefinition result = new FileBasedContentDefinition();

    result.setAnalyzeMode(analyzeMode);
    result.setName(contentName);
    result.setVersion(contentVersion);

    for (File binaryPath : binaryPaths) {
      result.addRootPath(binaryPath, ContentType.BINARY);
    }

    if (sourcePaths != null) {
      for (File sourcePath : sourcePaths) {
        result.addRootPath(sourcePath, ContentType.SOURCE);
      }
    }

    // initialize the result
    result.initialize();

    //
    _contentDefinitions.add(result);

    //
    return result;
  }
}
