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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.eclipse.emf.ecore.util.EcoreUtil;
import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.DefaultDependencyDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinition;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class BoltClientQueries {

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @param query
   * @IDependencyDefinitionDependencyDefinitionInterruptedException
   * @throws ExecutionException
   */
  public static List<IDependencyDefinition> resolveDependencyQuery(IBoltClient boltClient, String query,
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolverFunction)
      throws InterruptedException, ExecutionException {

    //
    return checkNotNull(boltClient).asyncExecCypherQuery(checkNotNull(query)).get().list(r -> {

      //
      if (resolverFunction != null) {
        return new ProxyDependencyDefinitionImpl(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(),
            r.get(3).asString(), resolverFunction);
      }
      //
      else {
        return new DefaultDependencyDefinition(r.get(0).asLong(), r.get(1).asLong(), r.get(2).asLong(),
            r.get(3).asString());
      }

    });
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @pIDependencyDefinitions
   * @return IDependencyDefinitionInterruptedException
   * @throws ExecutionException
   */
  public static List<IDependencyDefinition> resolveDependencyQueries(IBoltClient boltClient, String[] queries,
      Function<HGProxyDependency, List<Future<List<IDependencyDefinition>>>> resolverFunction)
      throws InterruptedException, ExecutionException {

    //
    checkNotNull(boltClient);

    //
    if (queries != null) {

      // create the result list
      List<IDependencyDefinition> result = new ArrayList<>();

      // process all queries
      for (String query : queries) {
        result.addAll(resolveDependencyQuery(boltClient, query, resolverFunction));
      }

      // return the result
      return result;
    }

    //
    return Collections.emptyList();
  }

  /**
   * <p>
   * </p>
   */
  public static List<Future<List<IDependencyDefinition>>> resolveProxyDependency(HGProxyDependency proxyDependency,
      ProxyDependencyQueriesHolder proxyDependenciesDefinition, IBoltClient boltClient) {

    //
    checkNotNull(proxyDependency);
    checkNotNull(proxyDependenciesDefinition);
    checkNotNull(boltClient);

    //
    Set<Object> fromNodeIds = new HashSet<>();
    Set<Object> toNodeIds = new HashSet<>();

    //
    for (Iterator<?> iter = EcoreUtil.getAllContents(Collections.singleton(proxyDependency.getFrom())); iter
        .hasNext();) {
      Object containedElement = iter.next();
      if (containedElement instanceof HGNode) {
        fromNodeIds.add(((HGNode) containedElement).getIdentifier());
      }
    }

    for (Iterator<?> iter = EcoreUtil.getAllContents(Collections.singleton(proxyDependency.getTo())); iter.hasNext();) {
      Object containedElement = iter.next();
      if (containedElement instanceof HGNode) {
        toNodeIds.add(((HGNode) containedElement).getIdentifier());
      }
    }

    //
    Map<String, Object> params = new HashMap<>();
    params.put("from", fromNodeIds);
    params.put("to", toNodeIds);

    //
    String[] detailDependencyQueries = proxyDependenciesDefinition.detailDependencyQueries();

    //
    if (detailDependencyQueries != null && detailDependencyQueries.length > 0) {

      // create the result list
      List<Future<List<IDependencyDefinition>>> result = new ArrayList<>();

      // process all queries
      for (String cypherQuery : detailDependencyQueries) {

        //
        Future<List<IDependencyDefinition>> dependencyDefinitions = boltClient
            .asyncExecCypherQueryAndTransformResult(cypherQuery, params, statementResult -> {
              return statementResult.list(r -> new DefaultDependencyDefinition(r.get(0).asLong(), r.get(1).asLong(),
                  r.get(2).asLong(), r.get(3).asString()));
            });

        //
        result.add(dependencyDefinitions);
      }

      // return the result
      return result;
    }

    //
    return Collections.emptyList();
  }
}
