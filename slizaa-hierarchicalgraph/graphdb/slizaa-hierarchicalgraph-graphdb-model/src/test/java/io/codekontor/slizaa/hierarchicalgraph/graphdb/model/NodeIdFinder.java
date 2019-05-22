/**
 * slizaa-hierarchicalgraph-graphdb-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;

import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import io.codekontor.slizaa.core.boltclient.IBoltClient;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class NodeIdFinder {

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getDoGetMapperMethod(IBoltClient boltClient) {
    return requestId(boltClient,
        "Match (m:Method {fqn: 'java.lang.Object org.mapstruct.factory.Mappers.doGetMapper(java.lang.Class,java.lang.ClassLoader)'}) Return id(m)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getAssignmentClassFile(IBoltClient boltClient) {
    return requestId(boltClient,
        "Match (r:Resource {fqn: 'org/mapstruct/ap/internal/model/common/Assignment.class'}) Return id(r)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @return
   * @throws NoSuchRecordException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public static long getSetterWrapperForCollectionsAndMapsWithNullCheckType(IBoltClient boltClient) {
    return requestId(boltClient,
        "Match (t:Type {fqn: 'org.mapstruct.ap.internal.model.assignment.SetterWrapperForCollectionsAndMapsWithNullCheck'}) Return id(t)");
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   * @param cypherQuery
   * @return
   */
  private static long requestId(IBoltClient boltClient, String cypherQuery) {

    try {
      return checkNotNull(boltClient).syncExecCypherQuery(checkNotNull(cypherQuery)).single().get(0).asLong();
    } catch (NoSuchRecordException e) {
      throw new RuntimeException(e);
    }
  }
}
