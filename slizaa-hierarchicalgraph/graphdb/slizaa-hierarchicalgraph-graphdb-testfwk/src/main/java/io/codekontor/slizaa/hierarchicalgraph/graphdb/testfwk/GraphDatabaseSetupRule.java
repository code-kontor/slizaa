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

import com.google.common.base.Preconditions;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.harness.junit.Neo4jRule;
import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class GraphDatabaseSetupRule implements TestRule {

  //
  private PredefinedDatabaseDirectoryRule _predefinedDatabaseDirectoryRule;

  //
  private Neo4jRule _neo4jRule;

  //
  private BoltClientConnectionRule _boltClientConnection;

  /**
   * @param databaseArchivePath
   */
  public GraphDatabaseSetupRule(String databaseArchivePath) {

    checkNotNull(databaseArchivePath);

    try (InputStream inputStream = GraphDatabaseSetupRule.class.getResourceAsStream(databaseArchivePath)) {

      //
      int freePort = findFreePort();

      _predefinedDatabaseDirectoryRule = new PredefinedDatabaseDirectoryRule(
          GraphDatabaseSetupRule.class.getResourceAsStream(databaseArchivePath));
      _predefinedDatabaseDirectoryRule.getParentDirectory().mkdirs();

      _neo4jRule = new Neo4jRule()
          .withConfig("dbms.directories.data", _predefinedDatabaseDirectoryRule.getParentDirectory().getAbsolutePath())
          .withConfig("dbms.connector.bolt.listen_address", ":" + freePort);

      _boltClientConnection = new BoltClientConnectionRule("localhost", freePort);

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
            _neo4jRule.apply(
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

  /**
   * @return
   */
  private static int findFreePort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
