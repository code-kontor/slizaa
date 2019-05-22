/**
 * slizaa-hierarchicalgraph-graphdb-mapping-service - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@SuppressWarnings("serial")
public class MappingException extends RuntimeException {

  /**
   * <p>
   * Creates a new instance of type {@link MappingException}.
   * </p>
   *
   */
  public MappingException() {
    super();
  }

  /**
   * <p>
   * Creates a new instance of type {@link MappingException}.
   * </p>
   *
   * @param message
   * @param cause
   */
  public MappingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * <p>
   * Creates a new instance of type {@link MappingException}.
   * </p>
   *
   * @param message
   */
  public MappingException(String message) {
    super(message);
  }

  /**
   * <p>
   * Creates a new instance of type {@link MappingException}.
   * </p>
   *
   * @param cause
   */
  public MappingException(Throwable cause) {
    super(cause);
  }
}
