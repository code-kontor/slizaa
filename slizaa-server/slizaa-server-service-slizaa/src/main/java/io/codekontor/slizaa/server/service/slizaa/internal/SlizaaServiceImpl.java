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

import io.codekontor.slizaa.core.boltclient.IBoltClientFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabaseFactory;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import io.codekontor.slizaa.server.slizaadb.SlizaaSocketUtils;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceCallback;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.configuration.IConfigurationService;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.service.slizaa.internal.json.SlizaaDatabaseConfiguration;
import io.codekontor.slizaa.server.service.slizaa.internal.json.GraphDatabaseHierarchicalGraphConfiguration;
import io.codekontor.slizaa.server.service.slizaa.internal.json.SlizaaServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class SlizaaServiceImpl implements ISlizaaService, IBackendServiceCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlizaaServiceImpl.class);

    private static final long TIMEOUT = 300 * 1000;

    {
        io.codekontor.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport
                .registerCustomHierarchicalgraphFactory();

        io.codekontor.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport
                .registerCustomHierarchicalgraphFactory();
    }

    private static final String CONFIG_ID = "io.codekontor.slizaa.server.service.slizaa";

    @Autowired
    private SlizaaServiceDatabaseProperties _slizaaServiceProperties;

    @Autowired
    private IBackendServiceInstanceProvider _backendService;

    @Autowired
    private IConfigurationService _configurationService;

    @Autowired
    private ISlizaaDatabaseFactory _graphDatabaseFactory;

    @Autowired
    private ContentDefinitionProviderFactoryService _contentDefinitionProviderFactoryService;

    @Autowired
    private IMappingService _mappingService;

    @Autowired
    private IBoltClientFactory _boltClientFactory;

    private ConcurrentHashMap<String, ISlizaaDatabase> _structureDatabases = new ConcurrentHashMap<>();


    /**
     * <p>
     * </p>
     */
    @PostConstruct
    public void initialize() throws Exception {

        SlizaaServiceConfiguration configuration = _configurationService.load(CONFIG_ID, SlizaaServiceConfiguration.class);

        if (configuration != null) {

            for (SlizaaDatabaseConfiguration dbConfig : configuration.getGraphDatabases()) {

                try {

                    ISlizaaDatabase graphDatabase = _structureDatabases.computeIfAbsent(dbConfig.getIdentifier(), id -> _graphDatabaseFactory
                            .newInstanceFromConfiguration(dbConfig, _slizaaServiceProperties.getDatabaseRootDirectoryAsFile()));

                    if (SlizaaDatabaseState.STARTING.equals(graphDatabase.getState())) {
                        graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, TIMEOUT);
                    }

                    LOGGER.info("Creating hierarchical graphs...");

                    //
                    for (GraphDatabaseHierarchicalGraphConfiguration hierarchicalGraphCfg : dbConfig.getHierarchicalGraphs()) {
                        graphDatabase.newHierarchicalGraph(hierarchicalGraphCfg.getIdentifier());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    _structureDatabases.remove(dbConfig.getIdentifier());
                }
            }
        }
    }

    @Override
    public Collection<IContentDefinitionProviderFactory<?>> getContentDefinitionProviderFactories() {
        return _contentDefinitionProviderFactoryService.getContentDefinitionProviderFactories();
    }

    @Override
    public IBackendService getBackendService() {
        return _backendService;
    }

    @Override
    public boolean hasGraphDatabases() {
        checkConfigured();
        return _structureDatabases != null && !_structureDatabases.isEmpty();
    }

    @Override
    public List<? extends ISlizaaDatabase> getGraphDatabases() {
        checkConfigured();

        //
        List<? extends ISlizaaDatabase> result = new ArrayList<>(_structureDatabases.values());
        result.sort(Comparator.comparing(o -> o.getIdentifier()));
        return result;
    }

    @Override
    public ISlizaaDatabase getGraphDatabase(String identifier) {
        checkConfigured();
        return _structureDatabases.get(identifier);
    }

    @Override
    public ISlizaaDatabase newGraphDatabase(String identifier) {
        checkConfigured();
        //
        if (_structureDatabases.containsKey(identifier)) {
            // TODO
            throw new RuntimeException();
        }

        // create the result
        ISlizaaDatabase result = _structureDatabases.computeIfAbsent(identifier, id -> _graphDatabaseFactory
                .newInstanceFromConfiguration(identifier, SlizaaSocketUtils.findAvailableTcpPort(), _slizaaServiceProperties.getDatabaseRootDirectoryAsFile()));

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

    public void storeConfiguration() {

        SlizaaServiceConfiguration configuration = new SlizaaServiceConfiguration();

        for (ISlizaaDatabase graphDatabase : _structureDatabases.values()) {
            configuration.getGraphDatabases().add(new SlizaaDatabaseConfiguration(graphDatabase));
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
     * @return
     */
    public IBackendServiceInstanceProvider getInstanceProvider() {
        return _backendService;
    }

    /**
     * @return
     */
    public IBoltClientFactory getBoltClientFactory() {
        return _boltClientFactory;
    }

    /**
     * @return
     */
    public IMappingService getMappingService() {
        return _mappingService;
    }

    public ConcurrentHashMap<String, ISlizaaDatabase> structureDatabases() {
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

    private void checkConfigured() {
        if (!this._backendService.hasInstalledExtensions()) {
            throw new RuntimeException("The slizaa service implementation has no configured extensions.");
        }
    }
}
