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
package io.codekontor.slizaa.server.service.slizaa.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.swing.text.GapContent;

import io.codekontor.slizaa.scanner.spi.contentdefinition.InvalidContentDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.codekontor.slizaa.core.boltclient.IBoltClientFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceCallback;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.configuration.IConfigurationService;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionService;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.service.slizaa.internal.configuration.GraphDatabaseConfiguration;
import io.codekontor.slizaa.server.service.slizaa.internal.configuration.GraphDatabaseHierarchicalGraphConfiguration;
import io.codekontor.slizaa.server.service.slizaa.internal.configuration.SlizaaServiceConfiguration;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.GraphDatabaseFactory;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.GraphDatabaseImpl;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.SlizaaSocketUtils;
import io.codekontor.slizaa.server.service.svg.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class SlizaaServiceImpl implements ISlizaaService, IBackendServiceCallback {

  {
    io.codekontor.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport
        .registerCustomHierarchicalgraphFactory();

    io.codekontor.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport
        .registerCustomHierarchicalgraphFactory();
  }

  private static final String                       CONFIG_ID           = "io.codekontor.slizaa.server.service.slizaa";

  private static final Logger                       LOGGER              = LoggerFactory
      .getLogger(SlizaaServiceImpl.class);

  @Autowired
  private SlizaaServiceDatabaseProperties           _serviceProperties;

  @Autowired
  private IBackendServiceInstanceProvider           _backendService;

  @Autowired
  private IExtensionService                         _extensionService;

  @Autowired
  private IConfigurationService                     _configurationService;

  @Autowired
  private ISvgService                               _svgService;

  @Autowired
  private GraphDatabaseFactory                      _graphDatabaseFactory;

  @Autowired
  private ContentDefinitionProviderFactoryService   _contentDefinitionProviderFactoryService;

  @Autowired
  private IMappingService                           _mappingService;

  private ExecutorService                           _executorService;

  private ConcurrentHashMap<String, IGraphDatabase> _structureDatabases = new ConcurrentHashMap<>();

  private IBoltClientFactory                        _boltClientFactory;

  /**
   * <p>
   * </p>
   */
  @PostConstruct
  public void initialize() throws Exception {

    // TODO: config!
    this._executorService = Executors.newFixedThreadPool(20);
    _boltClientFactory = IBoltClientFactory.newInstance(this._executorService);

    SlizaaServiceConfiguration configuration = _configurationService.load(CONFIG_ID, SlizaaServiceConfiguration.class);

    if (configuration != null) {

      for (GraphDatabaseConfiguration dbConfig : configuration.getGraphDatabases()) {

        try {

          // create
          GraphDatabaseImpl graphDatabase = createStructureDatabaseIfAbsent(dbConfig.getIdentifier(),
              dbConfig.getPort());

          //
          if (dbConfig.getContentDefinition() != null) {

            // set the content definition...
            IContentDefinitionProvider<?> contentDefinitionProvider = graphDatabase.stateMachineContext().createContentDefinitionProvider(dbConfig.getContentDefinition().getFactoryId(),
                dbConfig.getContentDefinition().getContentDefinition());

            if (contentDefinitionProvider == null) {
              contentDefinitionProvider = graphDatabase.stateMachineContext().createContentDefinitionProvider(dbConfig.getContentDefinition().getFactoryId(),
                      dbConfig.getContentDefinition().getContentDefinition());
            }

            //
            if (contentDefinitionProvider == null) {
              throw new InvalidContentDefinitionException(String.format("Invalid content definition ('%s', '%s').", dbConfig.getContentDefinition().getFactoryId(),
                      dbConfig.getContentDefinition().getContentDefinition()));
            }

            graphDatabase.stateMachineContext().setContentDefinition(contentDefinitionProvider);

            // ...and start the database
            if (dbConfig.isRunning()) {
              graphDatabase.start();
            }
          }

          //
          for (GraphDatabaseHierarchicalGraphConfiguration hierarchicalGraphCfg : dbConfig.getHierarchicalGraphs()) {
            graphDatabase.newHierarchicalGraph(hierarchicalGraphCfg.getIdentifier());
          }

        } catch (Exception e) {

          // TODO Auto-generated catch block
          e.printStackTrace();
          _structureDatabases.remove(dbConfig.getIdentifier());
        }

      }
    }
  }

  @PreDestroy
  public void dispose() throws InterruptedException {

    this._executorService.shutdown();
    this._executorService.awaitTermination(5, TimeUnit.SECONDS);
  }

  @Override
  public Collection<IContentDefinitionProviderFactory<?>> getContentDefinitionProviderFactories() {
    return _contentDefinitionProviderFactoryService.getContentDefinitionProviderFactories();
  }

  @Override
  public IExtensionService getExtensionService() {
    return _extensionService;
  }

  @Override
  public IBackendService getBackendService() {
    return _backendService;
  }

  @Override
  public ISvgService getSvgService() {
    return _svgService;
  }

  @Override
  public boolean hasGraphDatabases() {
    checkConfigured();
    return _structureDatabases != null && !_structureDatabases.isEmpty();
  }

  @Override
  public List<? extends IGraphDatabase> getGraphDatabases() {
    checkConfigured();

    //
    List<? extends IGraphDatabase> result = new ArrayList<>(_structureDatabases.values());
    result.sort(new Comparator<IGraphDatabase>() {
      @Override
      public int compare(IGraphDatabase o1, IGraphDatabase o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
      }
    });
    return result;
  }

  @Override
  public IGraphDatabase getGraphDatabase(String identifier) {
    checkConfigured();
    return _structureDatabases.get(identifier);
  }

  @Override
  public IGraphDatabase newGraphDatabase(String identifier) {
    checkConfigured();
    //
    if (_structureDatabases.containsKey(identifier)) {
      // TODO
      throw new RuntimeException();
    }

    // create the result
    IGraphDatabase result = createStructureDatabaseIfAbsent(identifier, SlizaaSocketUtils.findAvailableTcpPort());

    // store the configuration
    storeConfig();

    // and return the result
    return result;
  }

  @Override
  public void beforeInstallExtensions(List<IExtension> extensionsToInstall) {

    if (_backendService.hasInstalledExtensions()) {
      getGraphDatabases().forEach(graphDatabase -> {
        if (graphDatabase.isRunning()) {
          graphDatabase.stop();
        }
      });
    }
  }

  public void storeConfig() {

    SlizaaServiceConfiguration configuration = new SlizaaServiceConfiguration();

    for (IGraphDatabase graphDatabase : _structureDatabases.values()) {
      configuration.getGraphDatabases().add(new GraphDatabaseConfiguration(graphDatabase));
    }

    // save the config
    try {
      _configurationService.store(CONFIG_ID, configuration);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public boolean hasStructureDatabase(String identifier) {
    checkConfigured();
    return _structureDatabases.containsKey(checkNotNull(identifier));
  }

  /**
   *
   * @return
   */
  public IBackendServiceInstanceProvider getInstanceProvider() {
    return _backendService;
  }

  /**
   *
   * @return
   */
  public IBoltClientFactory getBoltClientFactory() {
    return _boltClientFactory;
  }

  /**
   *
   * @return
   */
  public IMappingService getMappingService() {
    return _mappingService;
  }

  public ConcurrentHashMap<String, IGraphDatabase> structureDatabases() {
    return _structureDatabases;
  }

  public IContentDefinitionProviderFactory<?> getContentDefinitionProviderFactory(String contentDefinitionFactoryId) {

    IContentDefinitionProviderFactory<?> result = _contentDefinitionProviderFactoryService
            .getContentDefinitionProviderFactory(checkNotNull(contentDefinitionFactoryId));

    if (result == null) {
      result = _contentDefinitionProviderFactoryService
        .getContentDefinitionProviderFactoryByShortForm(contentDefinitionFactoryId);
    }

    return result;
  }

  private GraphDatabaseImpl createStructureDatabaseIfAbsent(String identifier, int port) {
    return (GraphDatabaseImpl) _structureDatabases.computeIfAbsent(identifier, id -> _graphDatabaseFactory
        .newInstance(id, new File(_serviceProperties.getDatabaseRootDirectoryAsFile(), identifier), port));
  }

  private void checkConfigured() {
    if (!this._backendService.hasInstalledExtensions()) {
      throw new RuntimeException("NOT CONFIGURED");
    }
  }
}
