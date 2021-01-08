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
/**
 *
 */
package io.codekontor.slizaa.core.boltclient;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.Executors;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public class AbstractBoltClientTest {

  /** - */
  @ClassRule
  public static Neo4jContainer neo4jContainer = new Neo4jContainer(DockerImageName.parse("neo4j:4.2.1"))
          .withoutAuthentication();

  /** - */
  private IBoltClient     _boltClient;

  /**
   * <p>
   * </p>
   */
  @Before
  public void init() {
    IBoltClientFactory boltClientFactory = IBoltClientFactory.newInstance(Executors.newFixedThreadPool(20));
    this._boltClient = boltClientFactory.createBoltClient(neo4jContainer.getBoltUrl());
    this._boltClient.connect();

    boolean isInitialized = this._boltClient.syncExecCypherQuery("MATCH (p:Person) RETURN count(p)", result -> result.single().get(0).asLong() > 0);
    if (!isInitialized) {
      this._boltClient.syncExecCypherQuery("CREATE (n:Person { name: 'Andres', title: 'Developer' })");
      this._boltClient.syncExecCypherQuery("CREATE (n:Person { name: 'George', title: 'Project Owner' })");
    }
  }

  /**
   * <p>
   * </p>
   */
  @After
  public void dispose() {
    this._boltClient.disconnect();
  }

  protected IBoltClient boltClient() {
    return _boltClient;
  }
}
