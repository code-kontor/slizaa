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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.codekontor.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class ParseNewDatabaseTest extends AbstractSlizaaServiceTest {

  public static final String STRUCTURE_DATABASE_NAME = "TEST_STRUCTURE_DATABASE";

  private ISlizaaDatabase structureDatabase;

  @Before
  public void before() {
    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
    assertThat(getDatabaseRootDirectory().listFiles()).hasSize(0);
  }

  @After
  public void after() {
    structureDatabase.terminate();
    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
  }

  @Test
  public void parseWithStart() throws IOException, TimeoutException {

    // create a new database and parse with start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinitionProvider(
        "io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");

    structureDatabase.parse(true);
    structureDatabase.awaitState(SlizaaDatabaseState.RUNNING, 5000);
  }

  @Test
  public void parseWithoutStart() throws IOException, TimeoutException {

    // create a new database and parse without start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinitionProvider(
        "io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");

    structureDatabase.parse(false);
    structureDatabase.awaitState(SlizaaDatabaseState.NOT_RUNNING, 5000);
  }

  @Test
  public void parseWithStartAndStop() throws IOException, TimeoutException {

    // create a new database and parse without start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinitionProvider(
        "io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");

    structureDatabase.parse(true);
    structureDatabase.awaitState(SlizaaDatabaseState.RUNNING, 5000);

    //
    structureDatabase.stop();
    structureDatabase.awaitState(SlizaaDatabaseState.NOT_RUNNING, 5000);
  }

  @Test
  public void parseWithStartAndStopAndStart() throws IOException, TimeoutException {

    // create a new database and parse without start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinitionProvider(
        "io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
    structureDatabase.parse(true);
    structureDatabase.awaitState(SlizaaDatabaseState.RUNNING, 5000);
    assertThat(structureDatabase.isRunning()).isTrue();

    //
    structureDatabase.stop();
    assertThat(structureDatabase.isRunning()).isFalse();

    //
    structureDatabase.start();
    assertThat(structureDatabase.isRunning()).isTrue();
  }
}
