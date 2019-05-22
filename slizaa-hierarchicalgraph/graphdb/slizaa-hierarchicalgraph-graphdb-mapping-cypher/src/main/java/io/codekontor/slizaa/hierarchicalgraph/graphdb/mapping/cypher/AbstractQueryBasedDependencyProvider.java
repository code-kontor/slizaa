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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.internal.BoltClientQueries;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.internal.ProxyDependencyQueriesHolder;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinitionProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractQueryBasedDependencyProvider implements IDependencyDefinitionProvider, IBoltClientAware {

  /** - */
  private List<String>                       _simpleDependenciesQueries;

  /** - */
  private List<ProxyDependencyQueriesHolder> _proxyDependenciesQueries;

  /** - */
  private List<IDependencyDefinition>        _dependencies;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractQueryBasedDependencyProvider}.
   * </p>
   */
  public AbstractQueryBasedDependencyProvider() {
    this._simpleDependenciesQueries = new LinkedList<>();
    this._proxyDependenciesQueries = new LinkedList<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(final IBoltClient boltClient, IProgressMonitor progressMonitor) throws Exception {

    checkNotNull(boltClient);

    initialize();

    this._dependencies = new ArrayList<>();

    // simple dependencies
    for (String query : this._simpleDependenciesQueries) {
      this._dependencies.addAll(BoltClientQueries.resolveDependencyQuery(boltClient, query, null));
    }

    // proxy dependencies
    for (ProxyDependencyQueriesHolder proxyDependenciesDefinition : this._proxyDependenciesQueries) {

      // create the resolver function
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolverFunction = (proxyDependency) -> {
        return BoltClientQueries.resolveProxyDependency(proxyDependency, proxyDependenciesDefinition, boltClient);
      };

      // resolve the 'top-level' queries
      for (String query : proxyDependenciesDefinition.proxyDependencyQueries()) {
        this._dependencies.addAll(BoltClientQueries.resolveDependencyQuery(boltClient, query, resolverFunction));
      }
    }
  }

  /**
   * <p>
   * </p>
   */
  protected abstract void initialize();

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IDependencyDefinition> getDependencies() throws Exception {
    return this._dependencies;
  }

  /**
   * <p>
   * </p>
   *
   * @param proxyDependencyQueries
   * @param detailDependencyQueries
   * @return
   */
  protected void addProxyDependencyDefinitions(String[] proxyDependencyQueries, String[] detailDependencyQueries) {

    //
    this._proxyDependenciesQueries.add(
        new ProxyDependencyQueriesHolder(checkNotNull(proxyDependencyQueries), checkNotNull(detailDependencyQueries)));
  }

  /**
   * <p>
   * </p>
   *
   * @param proxyDependencyQuery
   * @param detailDependencyQueries
   */
  protected void addProxyDependencyDefinitions(String proxyDependencyQuery, String[] detailDependencyQueries) {

    //
    this._proxyDependenciesQueries.add(new ProxyDependencyQueriesHolder(
        new String[] { checkNotNull(proxyDependencyQuery) }, checkNotNull(detailDependencyQueries)));
  }

  /**
   * <p>
   * </p>
   *
   * @param simpleDependencyQuery
   */
  protected void addSimpleDependencyDefinitions(String simpleDependencyQuery) {

    //
    this._simpleDependenciesQueries.add(simpleDependencyQuery);
  }
}
