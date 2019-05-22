/**
 * slizaa-core-boltclient - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.boltclient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IBoltClient {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getName();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getDescription();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  String getUri();

  /**
   * <p>
   * </p>
   *
   */
  void connect();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  boolean isConnected();

  /**
   * <p>
   * </p>
   *
   */
  void disconnect();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<String> getNodeLabels();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<String> getPropertyKeys();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<String> getRelationshipTypes();

  /**
   * <p>
   * </p>
   *
   * @param nodeId
   * @return
   */
  Node getNode(long nodeId);

  /**
   * <p>
   * </p>
   *
   * @param nodeId
   * @return
   */
  Relationship getRelationship(long nodeId);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @return
   */
  StatementResult syncExecCypherQuery(String cypherQuery);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param params
   * @return
   */
  StatementResult syncExecCypherQuery(String cypherQuery, Map<String, Object> params);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @return
   */
  Future<StatementResult> asyncExecCypherQuery(String cypherQuery);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param params
   * @return
   */
  Future<StatementResult> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param consumer
   * @return
   */
  Future<Void> asyncExecCypherQuery(String cypherQuery, Consumer<StatementResult> consumer);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param params
   * @param consumer
   * @return
   */
  Future<Void> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params, Consumer<StatementResult> consumer);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param consumer
   * @return
   */
  Future<Void> asyncExecCypherQuery(String cypherQuery, IQueryResultConsumer consumer);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param params
   * @param consumer
   * @return
   */
  Future<Void> asyncExecCypherQuery(String cypherQuery, Map<String, Object> params, IQueryResultConsumer consumer);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param consumer
   * @return
   */
  <T> Future<T> asyncExecCypherQueryAndTransformResult(String cypherQuery, Function<StatementResult, T> consumer);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param params
   * @param consumer
   * @return
   */
  <T> Future<T> asyncExecCypherQueryAndTransformResult(String cypherQuery, Map<String, Object> params,
      Function<StatementResult, T> consumer);
}
