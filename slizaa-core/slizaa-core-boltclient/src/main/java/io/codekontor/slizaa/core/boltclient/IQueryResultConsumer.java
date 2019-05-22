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

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.exceptions.Neo4jException;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IQueryResultConsumer {

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @return
   */
  boolean canConsume(String cypherQuery);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   */
  void handleQueryStarted(String cypherQuery);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param result
   */
  void handleQueryResultReceived(String cypherQuery, StatementResult result);

  /**
   * <p>
   * </p>
   *
   * @param cypherQuery
   * @param result
   * @param exception
   */
  void handleError(String cypherQuery, StatementResult result, Neo4jException exception);
}
