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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.junit.*;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.harness.junit.Neo4jRule;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.GraphDatabaseSetupRule;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.PredefinedDatabaseDirectoryRule;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;

import static org.neo4j.graphdb.factory.GraphDatabaseSettings.boltConnector;
import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewNode;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewRootNode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedNeo4JBackedNodeSource_Test {

  {
    CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  @ClassRule
  public static GraphDatabaseSetupRule graphDatabaseSetup = new GraphDatabaseSetupRule("/mapstruct_1-2-0-Final-db.zip");

  /** - */
  private HGRootNode                        _rootNode;

  /** - */
  private HGNode                            _node;

  @Before
  public void init() throws Exception {

    //
    _rootNode = createNewRootNode(() -> {

      // create the
      GraphDbRootNodeSource result = GraphDbHierarchicalgraphFactory.eINSTANCE.createGraphDbRootNodeSource();

      // set the repository
      result.setBoldClient(graphDatabaseSetup.getBoltClient());

      // return the result
      return result;
    });

    //
    _node = createNewNode(_rootNode, _rootNode, () -> {

      // create the
      GraphDbNodeSource nodeSource = GraphDbHierarchicalgraphFactory.eINSTANCE.createGraphDbNodeSource();

      try {

        // set the repository
        nodeSource.setIdentifier(NodeIdFinder.getDoGetMapperMethod(graphDatabaseSetup.getBoltClient()));

      } catch (Exception e) {
        e.printStackTrace();
        Assert.fail();
      }

      // return the result
      return nodeSource;
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @throws ExecutionException
   * @throws InterruptedException
   */
  @Test
  public void testGetProperties() throws InterruptedException, ExecutionException {

    //
    EMap<String, String> properties = ((GraphDbNodeSource) _node.getNodeSource()).getProperties();

    assertThat(properties).isNotNull();
    assertThat(properties).hasSize(5);
    assertThat(properties.get("fqn"))
        .isEqualTo("java.lang.Object org.mapstruct.factory.Mappers.doGetMapper(java.lang.Class,java.lang.ClassLoader)");
    assertThat(properties.get("visibility")).isEqualTo("private");
    assertThat(properties.get("name")).isEqualTo("doGetMapper");
    assertThat(properties.get("static")).isEqualTo("true");
    assertThat(properties.get("signature"))
        .isEqualTo("<T:Ljava/lang/Object;>(Ljava/lang/Class<TT;>;Ljava/lang/ClassLoader;)TT;");
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testGetLabels() {

    //
    EList<String> labels = ((GraphDbNodeSource) _node.getNodeSource()).getLabels();

    assertThat(labels).isNotNull();
    assertThat(labels).hasSize(1);
    assertThat(labels).contains("Method");
  }

  private static int findFreePort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
