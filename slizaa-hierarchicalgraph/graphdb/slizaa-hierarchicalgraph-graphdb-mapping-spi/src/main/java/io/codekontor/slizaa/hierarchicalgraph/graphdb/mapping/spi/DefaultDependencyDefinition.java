/**
 * slizaa-hierarchicalgraph-graphdb-mapping-spi - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class DefaultDependencyDefinition implements IDependencyDefinition {

  /** - */
  public long   _idStart;

  /** - */
  public long   _idTarget;

  /** - */
  public long   _idRel;

  /** - */
  public String _type;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractDependency}.
   * </p>
   *
   * @param idStart
   * @param idTarget
   * @param idRel
   * @param type
   */
  public DefaultDependencyDefinition(long idStart, long idTarget, long idRel, String type) {
    this._idStart = idStart;
    this._idTarget = idTarget;
    this._idRel = idRel;
    this._type = type;
  }

  @Override
  public long getIdStart() {
    return this._idStart;
  }

  @Override
  public long getIdTarget() {
    return this._idTarget;
  }

  @Override
  public long getIdRel() {
    return this._idRel;
  }

  @Override
  public String getType() {
    return this._type;
  }

  @Override
  public String toString() {
    return "DefaultDependencyDefinition [_idStart=" + this._idStart + ", _idTarget=" + this._idTarget + ", _idRel="
        + this._idRel + ", _type=" + this._type + "]";
  }
}
