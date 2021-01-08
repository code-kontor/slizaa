/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal.structuredatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.codekontor.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class AllowedTransitionsOnDatabaseTest extends AbstractSlizaaServiceTest {

  public static final String STRUCTURE_DATABASE_NAME = "TEST_STRUCTURE_DATABASE";

  private ISlizaaDatabase database;

  @Before
  public void before() {
    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
    assertThat(getDatabaseRootDirectory().listFiles()).hasSize(0);
  }

  @After
  public void after() {

    if (!SlizaaDatabaseState.TERMINATED.equals(database.getState())) {
      database.terminate();
    }

    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
  }

  @Test
  public void test_INITIAL() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    assertThat(database.getAvailableActions()).containsExactlyInAnyOrder(
        ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION, ISlizaaDatabase.GraphDatabaseAction.DELETE);
  }

  @Test
  public void test_SET_CONTENT_DEFINITION() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    database.setContentDefinitionProvider("io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "ant4eclipse:ant4eclipse:0.5.0.rc1");

    assertThat(database.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.PARSE, ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION,
        ISlizaaDatabase.GraphDatabaseAction.DELETE);
  }

  @Test
  public void test_RUNNING() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    database.setContentDefinitionProvider("io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "ant4eclipse:ant4eclipse:0.5.0.rc1");

    database.parse(true);

    await().atMost(60, TimeUnit.SECONDS).until(() -> database.isRunning());

    assertThat(database.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.STOP,
        ISlizaaDatabase.GraphDatabaseAction.CREATE_HIERARCHICAL_GRAPH,
        ISlizaaDatabase.GraphDatabaseAction.DELETE);
  }

  @Test
  public void test_NOT_RUNNING() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    database.setContentDefinitionProvider("io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "ant4eclipse:ant4eclipse:0.5.0.rc1");

    database.parse(false);
    await().atMost(60, TimeUnit.SECONDS).until(() -> SlizaaDatabaseState.NOT_RUNNING.equals(database.getState()));

    assertThat(database.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.START,
        ISlizaaDatabase.GraphDatabaseAction.PARSE, ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION,
        ISlizaaDatabase.GraphDatabaseAction.DELETE);
  }

  @Test
  public void test_TERMINATED() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    database.terminate();

    assertThat(database.getAvailableActions()).containsExactlyInAnyOrder();
  }
}
