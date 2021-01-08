/**
 * slizaa-hierarchicalgraph-graphdb-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class GraphDatabaseSetupRule implements TestRule {

  public static final String NEO4J_DOCKER_IMAGE = "neo4j:4.2.1";
  //
  private PredefinedDatabaseDirectoryRule _predefinedDatabaseDirectoryRule;

  //
  private Neo4jContainer _neo4jContainer;

  //
  private BoltClientConnectionRule _boltClientConnection;

  /**
   * @param databaseArchivePath
   */
  public GraphDatabaseSetupRule(String databaseArchivePath) {

    checkNotNull(databaseArchivePath);

    try (InputStream inputStream = GraphDatabaseSetupRule.class.getResourceAsStream(databaseArchivePath)) {

      _predefinedDatabaseDirectoryRule = new PredefinedDatabaseDirectoryRule(
          GraphDatabaseSetupRule.class.getResourceAsStream(databaseArchivePath));
      _predefinedDatabaseDirectoryRule.getDatabaseDirectory().mkdirs();
      
      _neo4jContainer = new Neo4jContainer(DockerImageName.parse(NEO4J_DOCKER_IMAGE))
          .withoutAuthentication()
          .withNeo4jConfig("dbms.recovery.fail_on_missing_files", "false")
          .withDatabase(MountableFile.forHostPath(_predefinedDatabaseDirectoryRule.getDatabaseDirectory().getAbsolutePath()));

      _boltClientConnection = new BoltClientConnectionRule(() -> _neo4jContainer.getBoltUrl());

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * @param base
   * @param description
   * @return
   */
  @Override
  public Statement apply(final Statement base, final Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {

        _predefinedDatabaseDirectoryRule.apply(
            _neo4jContainer.apply(
                _boltClientConnection.apply(base, description),
                description),
            description).evaluate();
      }
    };
  }

  /**
   * @return
   */
  public IBoltClient getBoltClient() {
    return _boltClientConnection.getBoltClient();
  }
}
