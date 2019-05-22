/**
 * slizaa-scanner-cypherregistry - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.cypherregistry.impl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * Internal representation of an edge.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 * 
 * @param <T>
 *          the type of the vertices
 */
public class Edge<T> {

  /** parent */
  private T parent;

  /** child */
  private T child;

  /**
   * <p>
   * Creates a new instance of type Edge.
   * </p>
   * 
   * @param aParent
   *          the parent object
   * @param aChild
   *          the child object
   */
  public Edge(T aParent, T aChild) {
    checkNotNull(aParent);
    checkNotNull(aChild);
    this.parent = aParent;
    this.child = aChild;
  }

  /**
   * <p>
   * Returns the child object.
   * </p>
   * 
   * @return the child object.
   */
  public T getChild() {
    return this.child;
  }

  /**
   * <p>
   * Returns the parent object.
   * </p>
   * 
   * @return the parent object.
   */
  public T getParent() {
    return this.parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int PRIME = 31;
    int result = 1;
    result = PRIME * result + this.parent.hashCode();
    result = PRIME * result + this.child.hashCode();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Edge<T> other = (Edge<T>) obj;
    if (!this.parent.equals(other.parent)) {
      return false;
    }
    if (!this.child.equals(other.child)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("[Edge");
    result.append(" parent:");
    result.append(this.parent);
    result.append(" child:");
    result.append(this.child);
    result.append("]");
    return result.toString();
  }

} /* ENDCLASS */
