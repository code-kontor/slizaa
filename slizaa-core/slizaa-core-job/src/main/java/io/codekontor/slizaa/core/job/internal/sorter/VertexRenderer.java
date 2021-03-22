/**
 * slizaa-core-job - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.job.internal.sorter;

/**
 * <p>
 * Interface for a vertex renderer. A vertex renderer is used to create a custom string representation of a vertex for
 * further usage in an exception message.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <T>
 *          the type of the vertices
 */
public interface VertexRenderer<T> {

  /**
   * <p>
   * Must return an not-null string that represents the given vertex.
   * </p>
   * 
   * @param vertex
   *          the vertex to render.
   * @return must return an not-null string that represents the given vertex.
   */
  String renderVertex(T vertex);

} /* ENDINTERFACE */