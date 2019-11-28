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
package io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.factory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.InvalidContentDefinitionException;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.IGraphDatabaseStateMachineContext;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.SlizaaSocketUtils;
import io.codekontor.slizaa.server.service.slizaa.internal.hierarchicalgraph.HierarchicalGraph;

public abstract class AbstractGraphDatabaseStatemachineContext implements IGraphDatabaseStateMachineContext {

  private static Logger                  LOGGER = LoggerFactory
      .getLogger(AbstractGraphDatabaseStatemachineContext.class);

  private String                         _identifier;

  private File                           _databaseDirectory;

  private int                            _port  = -1;

  private IGraphDatabase                 _graphDatabase;

  private IContentDefinitionProvider<?>  _contentDefinitionProvider;

  private SlizaaServiceImpl              _slizaaService;

  private Map<String, HierarchicalGraph> _hierarchicalGraphs;

  AbstractGraphDatabaseStatemachineContext(String identifier, File databaseDirectory, int port,
      SlizaaServiceImpl slizaaService) {

    this._identifier = checkNotNull(identifier);
    this._databaseDirectory = checkNotNull(databaseDirectory);
    this._slizaaService = checkNotNull(slizaaService);

    _hierarchicalGraphs = new HashMap<>();
    _port = SlizaaSocketUtils.available(port) ? port : SlizaaSocketUtils.findAvailableTcpPort();
  }

  public String getIdentifier() {
    return _identifier;
  }

  public int getPort() {
    return _port;
  }

  public File getDatabaseDirectory() {
    return _databaseDirectory;
  }

  public boolean hasPopulatedDatabaseDirectory() {
    return _databaseDirectory.isDirectory() && _databaseDirectory.list().length > 0;
  }

  public void setSlizaaService(SlizaaServiceImpl slizaaService) {
    _slizaaService = slizaaService;
  }

  public void setGraphDatabase(IGraphDatabase structureDatabase) {
    _graphDatabase = structureDatabase;
  }

  public IGraphDatabase getGraphDatabase() {
    return _graphDatabase;
  }

  @Override
  public IContentDefinitionProvider<?> createContentDefinitionProvider(String contentDefinitionFactoryId,
      String contentDefinition) {
    
    IContentDefinitionProviderFactory<?> factory = slizaaService()
        .getContentDefinitionProviderFactory(contentDefinitionFactoryId);

    if (factory == null) {
      throw new InvalidContentDefinitionException(
          String.format("Unknown content definintion provider factory '%s'.", contentDefinitionFactoryId));
    }

    return factory.fromExternalRepresentation(contentDefinition);
  }

  @Override
  public void setContentDefinition(IContentDefinitionProvider<?> contentDefinitionProvider) {

    LOGGER.debug("setContentDefinitionProvider({})", contentDefinitionProvider);

    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  public boolean hasContentDefinitionProvider() {
    return _contentDefinitionProvider != null;
  }

  public IContentDefinitionProvider<?> getContentDefinitionProvider() {
    return _contentDefinitionProvider;
  }

  public void terminate() {
    stop();
    slizaaService().structureDatabases().remove(getIdentifier());
    clearDatabaseDirectory();
  }

  public void disposeHierarchicalGraph(String identifier) {
    HierarchicalGraph hierarchicalGraph = hierarchicalGraphs().remove(identifier);
    this.slizaaService().getSelectionService().dropSelections(hierarchicalGraph.getRootNode());
  }

  public List<IHierarchicalGraph> getHierarchicalGraphs() {
    return new ArrayList<>(hierarchicalGraphs().values());
  }

  public IHierarchicalGraph getHierarchicalGraph(String identifier) {
    return hierarchicalGraphs().get(identifier);
  }

  public void storeConfiguration() {
    slizaaService().storeConfig();
  }

  protected void recomputePort() {
    if (_port == -1 || !SlizaaSocketUtils.available(_port)) {
      _port = SlizaaSocketUtils.findAvailableTcpPort();
    }
  }

  protected Map<String, HierarchicalGraph> hierarchicalGraphs() {
    return _hierarchicalGraphs;
  }

  protected SlizaaServiceImpl slizaaService() {
    return _slizaaService;
  }

  protected void clearDatabaseDirectory() {
      if (_databaseDirectory.exists()) {
          try {
              Files.walk(_databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
                      .map(Path::toFile).forEach(File::delete);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
}
