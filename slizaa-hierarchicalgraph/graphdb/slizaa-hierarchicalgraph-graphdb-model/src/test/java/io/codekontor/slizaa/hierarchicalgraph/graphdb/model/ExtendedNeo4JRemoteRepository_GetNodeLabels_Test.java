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
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neo4j.harness.junit.Neo4jRule;
import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;

import com.google.common.collect.Lists;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.GraphDatabaseSetupRule;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.PredefinedDatabaseDirectoryRule;

/**
 * <p>
 * https://github.com/lukas-krecan/JsonUnit
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@RunWith(value = Parameterized.class)
public class ExtendedNeo4JRemoteRepository_GetNodeLabels_Test {

  {
    CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  @ClassRule
  public static GraphDatabaseSetupRule graphDatabaseSetup = new GraphDatabaseSetupRule("/mapstruct_1-2-0-Final-db.zip");

  /** - */
  private Function<IBoltClient, Long>       _nodeIdProvider;

  /** - */
  private List<String>                      _expectedLabels;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedNeo4JRemoteRepository_GetNodeLabels_Test}.
   * </p>
   *
   * @param nodeIdProvider
   * @param expectedLabels
   */
  public ExtendedNeo4JRemoteRepository_GetNodeLabels_Test(Function<IBoltClient, Long> nodeIdProvider,
      List<String> expectedLabels) {
    this._nodeIdProvider = checkNotNull(nodeIdProvider);
    this._expectedLabels = checkNotNull(expectedLabels);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void getNodeProperties() {

    IBoltClient boltClient = graphDatabaseSetup.getBoltClient();

    List<String> labels = Lists.newArrayList(boltClient.getNode(_nodeIdProvider.apply(boltClient)).labels());
    assertThat(labels).containsExactlyElementsOf(_expectedLabels);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Parameters(name = "{index}: getNodeLabels({0}) = {1}")
  public static Collection<Object[]> data() {

    Function<IBoltClient, Long> f1 = c -> NodeIdFinder.getAssignmentClassFile(c);
    Function<IBoltClient, Long> f2 = c -> NodeIdFinder.getDoGetMapperMethod(c);
    Function<IBoltClient, Long> f3 = c -> NodeIdFinder.getSetterWrapperForCollectionsAndMapsWithNullCheckType(c);

    return Arrays.asList(new Object[][] { { f1, Arrays.asList("Resource", "Binary", "ClassFile") },
        { f2, Arrays.asList("Method") }, { f3, Arrays.asList("Type", "Class") } });
  }
}
