/**
 * slizaa-hierarchicalgraph-graphdb-mapping-cypher - Slizaa Static Software Analysis Tools
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
/**
 *
 */
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.DefaultDependencyDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IProxyDependencyDefinition;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ProxyDependencyDefinitionImpl extends DefaultDependencyDefinition implements IProxyDependencyDefinition {

  /** - */
  private Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> _function;

  /**
   * <p>
   * Creates a new instance of type {@link ProxyDependencyDefinitionImpl}.
   * </p>
   *
   * @param idStart
   * @param idTarget
   * @param idRel
   * @param type
   */
  public ProxyDependencyDefinitionImpl(long idStart, long idTarget, long idRel, String type,
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> function) {
    super(idStart, idTarget, idRel, type);

    //
    this._function = checkNotNull(function);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> getResolveFunction() {
    return this._function;
  }
}
