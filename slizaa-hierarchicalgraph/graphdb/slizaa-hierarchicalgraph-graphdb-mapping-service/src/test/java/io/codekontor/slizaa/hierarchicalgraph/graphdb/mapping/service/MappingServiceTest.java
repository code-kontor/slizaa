/**
 * slizaa-hierarchicalgraph-graphdb-mapping-service - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import io.codekontor.slizaa.core.boltclient.testfwk.BoltClientConnectionRule;
import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.GraphDatabaseSetupRule;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.mapping.SimpleJTypeMappingProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class MappingServiceTest {

  @ClassRule
  public static GraphDatabaseSetupRule graphDatabaseSetup = new GraphDatabaseSetupRule("/mapstruct_1-2-0-Final-db.zip");

  /**
   * <p>
   * </p>
   *
   * @throws ClassNotFoundException
   */
  @Test
  public void testMappingService() throws ClassNotFoundException {

    IMappingService mappingService = MappingFactory.createMappingServiceForStandaloneSetup();

    HGRootNode rootNode = mappingService
        .convert(new SimpleJTypeMappingProvider(), graphDatabaseSetup.getBoltClient(), new DefaultProgressMonitor("Mapping...", 100, DefaultProgressMonitor.consoleLogger()));

    assertThat(rootNode.getChildren()).hasSize(2);

    assertThat(rootNode.getChildren().get(0).getChildren()).hasSize(5);
    assertThat(rootNode.getChildren().get(1).getChildren()).hasSize(36);

    int[][] matrix = GraphUtils.computeAdjacencyMatrix(rootNode.getChildren().get(1).getChildren());
  }
}
