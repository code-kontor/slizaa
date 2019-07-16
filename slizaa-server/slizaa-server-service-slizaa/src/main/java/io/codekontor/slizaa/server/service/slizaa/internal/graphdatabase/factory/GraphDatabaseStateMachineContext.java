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

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.IGraphDatabaseStateMachineContext;
import io.codekontor.slizaa.server.service.slizaa.internal.hierarchicalgraph.HierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.internal.hierarchicalgraph.HierarchicalGraphDefinition;

public class GraphDatabaseStateMachineContext extends AbstractGraphDatabaseStatemachineContext
    implements IGraphDatabaseStateMachineContext {

  private IGraphDb    _graphDb;

  private IBoltClient _boltClient;

  public GraphDatabaseStateMachineContext(String identifier, File databaseDirectory, int port,
      SlizaaServiceImpl slizaaService) {

    super(identifier, databaseDirectory, port, slizaaService);
  }

  public boolean isRunning() {
    return _graphDb != null;
  }

  public void start() {

    recomputePort();

    _graphDb = executeWithCtxClassLoader(() -> {
      return slizaaService().getInstanceProvider().getGraphDbFactory().newGraphDb(getPort(), getDatabaseDirectory())
          .create();
    });

    connectBoltClient();

    hierarchicalGraphs().values().forEach(hierarchicalGraph -> hierarchicalGraph.initialize(true));
  }

  public void stop() {

    hierarchicalGraphs().values().forEach(hierarchicalGraph -> hierarchicalGraph.dispose());

    if (this._boltClient != null) {
      this._boltClient.disconnect();
      this._boltClient = null;
    }

    if (this._graphDb != null) {
      this._graphDb.shutdown();
      this._graphDb = null;
    }
  }

  /**
   * @throws Exception
   */
  public boolean parse(boolean startDatabase) {

    // delete all contained files
    clearDatabaseDirectory();

    // create the graph database
    _graphDb = executeWithCtxClassLoader(() -> {

      IModelImporter modelImporter = slizaaService().getInstanceProvider().getModelImporterFactory()
          .createModelImporter(getContentDefinitionProvider(), getDatabaseDirectory(),
              slizaaService().getInstanceProvider().getParserFactories(),
              slizaaService().getInstanceProvider().getCypherStatementRegistry().getAllStatements());

      // parse the model
      if (startDatabase) {

        //
        modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()), () -> {

          recomputePort();

          return slizaaService().getInstanceProvider().getGraphDbFactory().newGraphDb(getPort(), getDatabaseDirectory())
              .create();
        });

        connectBoltClient();

        hierarchicalGraphs().values().forEach(hierarchicalGraph -> hierarchicalGraph.initialize(true));

        return modelImporter.getGraphDb();
      }
      else {
        modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()));
        return null;
      }
    });

    return isRunning();
  }


  /**
   * @param identifier
   * @return
   */
  public IHierarchicalGraph createHierarchicalGraph(String identifier) {

    // TODO: move to start/stop
    connectBoltClient();

    //
    HierarchicalGraph hierarchicalGraph = new HierarchicalGraph(new HierarchicalGraphDefinition(identifier),
        getGraphDatabase(), (def) -> {

          IMappingProvider mappingProvider = slizaaService().getInstanceProvider().getMappingProviders().get(0);

          return slizaaService().getMappingService().convert(mappingProvider, this._boltClient,
              new DefaultProgressMonitor("Mapping", 100, DefaultProgressMonitor.consoleLogger()));
        });

    hierarchicalGraph.initialize(true);

    hierarchicalGraphs().put(identifier, hierarchicalGraph);

    return hierarchicalGraph;
  }


  /**
   * @param action
   * @return
   * @throws Exception
   */
  private <T> T executeWithCtxClassLoader(Action<T> action) {

    //
    checkNotNull(action);

    // store the current ctx class loader
    ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();

    try {

      // set the extension class loader as the context class loader
      Thread.currentThread()
          .setContextClassLoader(slizaaService().getInstanceProvider().getCurrentExtensionClassLoader());

      // execute the action
      try {
        return action.execute();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    } finally {

      // reset the current ctx class loader
      Thread.currentThread().setContextClassLoader(currentContextClassLoader);
    }

  }

  private void connectBoltClient() {
    if (_boltClient == null) {
      // TODO!
      this._boltClient = slizaaService().getBoltClientFactory().createBoltClient("bolt://localhost:" + getPort());
      this._boltClient.connect();
    }
  }

  /**
   * @param <T>
   * @author Gerd W&uuml;therich (gw@code-kontor.io)
   */
  @FunctionalInterface
  private interface Action<T> {

    T execute() throws Exception;
  }
}
