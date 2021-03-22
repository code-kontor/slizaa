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

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import io.codekontor.slizaa.scanner.spi.contentdefinition.InvalidContentDefinitionException;
import io.codekontor.slizaa.scanner.spi.parser.IParserFactory;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabaseSPI;
import io.codekontor.slizaa.server.slizaadb.internal.HierarchicalGraph;
import io.codekontor.slizaa.server.slizaadb.internal.HierarchicalGraphDefinition;
import io.codekontor.slizaa.server.service.selection.IModifiableAggregatedDependencySelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class SlizaaDatabaseEnvironment implements ISlizaaDatabaseSPI {

    @Autowired
    private SlizaaServiceImpl _slizaaService;

    @Autowired
    private IModifiableAggregatedDependencySelectionService _selectionService;

    @Override
    public IGraphDb createGraphDb(int port, File databaseDirectory) {
        return _slizaaService.getInstanceProvider().getGraphDbFactory().newGraphDb(port, databaseDirectory).create();
    }

    @Override
    public IModelImporter createModelImporter(IContentDefinitionProvider<?> contentDefinitionProvider, File databaseDirectory) {


        try {

            IModelImporterFactory modelImporterFactory = _slizaaService.getInstanceProvider().getModelImporterFactory();
            List<IParserFactory> parserFactories = _slizaaService.getInstanceProvider().getParserFactories();
            ICypherStatementRegistry cypherStatementRegistry = _slizaaService.getInstanceProvider().getCypherStatementRegistry();

            IModelImporter result = modelImporterFactory
                    .createModelImporter(contentDefinitionProvider, databaseDirectory, parserFactories, cypherStatementRegistry.getAllStatements());

            return result;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    @Override
    public IBoltClient createBoltClient(String uri) {
        return _slizaaService.getBoltClientFactory().createBoltClient(uri);
    }

    @Override
    public IContentDefinitionProvider<?> createContentDefinitionProvider(String contentDefinitionFactoryId, String contentDefinition) {

        IContentDefinitionProviderFactory<?> factory = _slizaaService.getContentDefinitionProviderFactory(contentDefinitionFactoryId);

        if (factory == null) {
            throw new InvalidContentDefinitionException(
                    String.format("Unknown content definintion provider factory '%s'.", contentDefinitionFactoryId));
        }

        return factory.fromExternalRepresentation(contentDefinition);
    }

    @Override
    public void handleHierarchicalGraphRemoved(HierarchicalGraph hierarchicalGraph) {
        _selectionService.dropSelections(hierarchicalGraph.getRootNode());
    }

    @Override
    public void handleGraphDatabaseRemoved(String identifier) {
        _slizaaService.structureDatabases().remove(identifier);
    }


    @Override
    public HGRootNode mapHierarchicalGraph(HierarchicalGraphDefinition def, IBoltClient boltClient) {

        // TODO!!!
        IMappingProvider mappingProvider = _slizaaService.getInstanceProvider().getMappingProviderFactories().get(0).createNewMappingProvider();

        return _slizaaService.getMappingService().convert(mappingProvider, boltClient,
                new DefaultProgressMonitor("Mapping", 100, DefaultProgressMonitor.consoleLogger()));
    }

    @Override
    public ClassLoader getCurrentExtensionClassLoader() {
        return _slizaaService.getBackendService().getCurrentExtensionClassLoader();
    }
}
