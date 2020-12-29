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
package io.codekontor.slizaa.server.service.slizaa.internal.hierarchicalgraph;

import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.IOrderedAdjacencyMatrix;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;
import io.codekontor.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Ignore("DEPENDENCY ON EXTENSION!")
public class HierarchicalGraphTest extends AbstractSlizaaServiceTest {

  @Autowired
  private SlizaaServiceImpl _slizaaService;

  private ISlizaaDatabase _structureDatabase;

  @Before
  public void setUp() throws Exception {

    String STRUCTURE_DATABASE_NAME = "HURZ";

    if (!_slizaaService.hasStructureDatabase(STRUCTURE_DATABASE_NAME)) {

      // create a new database
      _structureDatabase = _slizaaService.newGraphDatabase(STRUCTURE_DATABASE_NAME);

      // configure
      _structureDatabase.setContentDefinitionProvider(
          "io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
          "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");

      // and parse
      _structureDatabase.parse(true);
    }
    //
    else {
      _structureDatabase = _slizaaService.getGraphDatabase(STRUCTURE_DATABASE_NAME);
      _structureDatabase.start();
    }
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void hierarchicalGraphTest() {

    IHierarchicalGraph hierarchicalGraph = _structureDatabase.newHierarchicalGraph("HG");

    HGRootNode rootNode = hierarchicalGraph.getRootNode();

    ILabelDefinitionProvider labelDefinitionProvider = rootNode.getExtension(ILabelDefinitionProvider.class);

    List<HGNode> nodes = rootNode.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0)
        .getChildren();

    IOrderedAdjacencyMatrix dependencyStructureMatrix = GraphUtils.createOrderedAdjacencyMatrix(nodes);
    for (int i = 0; i < nodes.size(); i++) {
      for (int j = 0; j < nodes.size(); j++) {
        System.out.println(i + " -> " + j + " :" + dependencyStructureMatrix.getWeight(i, j));
      }
    }
  }
}
