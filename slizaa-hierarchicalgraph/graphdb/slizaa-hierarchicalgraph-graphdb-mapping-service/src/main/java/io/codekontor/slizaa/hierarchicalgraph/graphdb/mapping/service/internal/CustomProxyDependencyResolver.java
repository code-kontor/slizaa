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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.internal.GraphFactoryFunctions.createDependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbDependencySource;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CustomProxyDependencyResolver implements IProxyDependencyResolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public IProxyDependencyResolverJob resolveProxyDependency(final HGProxyDependency dependency) {
    return new ProxyDependencyResolverJob(checkNotNull(dependency));
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  private class ProxyDependencyResolverJob implements IProxyDependencyResolverJob {

    /** - */
    private List<Future<List<IDependencyDefinition>>> _futures;

    /** - */
    private HGProxyDependency                         _proxyDependency;

    /**
     * <p>
     * Creates a new instance of type {@link ProxyDependencyResolverJob}.
     * </p>
     */
    public ProxyDependencyResolverJob(HGProxyDependency proxyDependency) {

      //
      this._proxyDependency = checkNotNull(proxyDependency);

      //
      GraphDbDependencySource dependencySource = (GraphDbDependencySource) proxyDependency.getDependencySource();

      // TODO List<Future<List<IDependencyDefinition>>>>
      @SuppressWarnings("unchecked")
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolveFunction = (Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>>) dependencySource
          .getUserObject();

      //
      this._futures = resolveFunction.apply(proxyDependency);
    }

    @Override
    public void waitForCompletion() {

      //
      List<IDependencyDefinition> resolvedDependencyDefinitions = new ArrayList<>();

      //
      for (Future<List<IDependencyDefinition>> future : this._futures) {
        try {
          resolvedDependencyDefinitions.addAll(future.get());
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }

      //
      List<HGCoreDependency> coreDependencies = createDependencies(resolvedDependencyDefinitions,
          this._proxyDependency.getRootNode(),
          (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, null);

      //
      this._proxyDependency.getAccumulatedCoreDependencies().addAll(coreDependencies);
    }
  }
}
