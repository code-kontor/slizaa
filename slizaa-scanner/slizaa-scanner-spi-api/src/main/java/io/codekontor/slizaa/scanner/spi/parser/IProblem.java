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
package io.codekontor.slizaa.scanner.spi.parser;

import static io.codekontor.slizaa.scanner.spi.internal.Preconditions.checkNotNull;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.filebased.IFile;

/**
 * <p>
 * Common interface for problems and errors that occur while parsing the content of a
 * {@link IContentDefinitionProvider}.
 * </p>
 * <p>
 * Clients may implement this interface.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IProblem {

  /**
   * <p>
   * Returns {@link IProjectContentResource} of the associated resource.
   * </p>
   * 
   * @return the {@link IProjectContentResource} of the associated resource.
   */
  IFile getResource();

  /**
   * <p>
   * Returns <code>true</code> if this problem is an error, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if this problem is an error, <code>false</code> otherwise.
   */
  boolean isError();

  /**
   * <p>
   * Returns the line number in source where the problem begins or -1 if unknown
   * </p>
   * 
   * @return the line number in source where the problem begins
   */
  int getSourceLineNumber();

  /**
   * <p>
   * Returns the message for this problem.
   * </p>
   * 
   * @return the message for this problem.
   */
  String getMessage();

  /**
   * <p>
   * Returns the start position of the problem (inclusive), or -1 if unknown.
   * </p>
   * 
   * @return the start position of the problem (inclusive), or -1 if unknown.
   */
  int getSourceStart();

  /**
   * <p>
   * Answer the end position of the problem (inclusive), or -1 if unknown.
   * </p>
   * 
   * @return the end position of the problem (inclusive), or -1 if unknown
   */
  int getSourceEnd();

  /**
   * <p>
   * </p>
   * 
   * @author Nils Hartmann (nils@nilshartmann.net)
   */
  public static class DefaultProblem implements IProblem {

    /** - */
    private final IFile _resource;

    /** - */
    private final String    _message;

    /**
     * @param resource
     * @param message
     */
    public DefaultProblem(IFile resource, String message) {

      checkNotNull(resource);
      checkNotNull(message);

      _resource = resource;
      _message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFile getResource() {
      return _resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
      return this._message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isError() {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceLineNumber() {
      return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceStart() {
      return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceEnd() {
      return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return "DefaultProblem [_resource=" + _resource + ", _message=" + _message + "]";
    }
  }
}
