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
package io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase;

import java.util.List;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public interface IGraphDatabaseStateMachineContext {
  
  void setGraphDatabase(IGraphDatabase graphDatabase);

  String getIdentifier();

  int getPort();

  boolean hasPopulatedDatabaseDirectory();

  boolean hasContentDefinitionProvider();

  void setContentDefinition(String factoryId, String contentDefinition);

  IContentDefinitionProvider<?> getContentDefinitionProvider();
  
  boolean isRunning();
  
  boolean parse(boolean startDatabase);
  
  void start();
  
  void stop();
  
  void terminate();

  IHierarchicalGraph createHierarchicalGraph(String identifier);

  IHierarchicalGraph getHierarchicalGraph(String identifier);

  List<IHierarchicalGraph> getHierarchicalGraphs();

  void disposeHierarchicalGraph(String identifier);

  void storeConfiguration();
}
