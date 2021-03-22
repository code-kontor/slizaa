/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.IBoltClientFactory;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.*;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.configuration.EnableConfigurationModule;
import io.codekontor.slizaa.server.service.selection.EnableSelectionServiceModule;
import io.codekontor.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import io.codekontor.slizaa.server.slizaadb.EnableSlizaaDatabaseModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@EnableConfigurationModule
@EnableSlizaaServiceModule
@EnableSelectionServiceModule
@EnableSlizaaDatabaseModule
@EnableProvisioningModule
@TestConfiguration
public class ProvisioningTestConfiguration {

  @Bean
  @Primary
  public IBoltClientFactory boltClientFactory() {

    IBoltClientFactory boltClientFactory = mock(IBoltClientFactory.class);
    IBoltClient boltClient = boltClient();

    when(boltClientFactory.createBoltClient(any())).thenReturn(boltClient);
    when(boltClientFactory.createBoltClient(any(),any())).thenReturn(boltClient);
    when(boltClientFactory.createBoltClient(any(),any(), any())).thenReturn(boltClient);

    return boltClientFactory;
  }

  @Bean
  public IBoltClient boltClient() {
    return mock(IBoltClient.class);
  }

  @Bean
  @Primary
  public IBackendServiceInstanceProvider backendService() {

    IBackendServiceInstanceProvider instanceProvider = mock(IBackendServiceInstanceProvider.class);

    IModelImporterFactory modelImporterFactory = modelImporterFactory();
    ICypherStatementRegistry cypherStatementRegistry = cypherStatementRegistry();
    IGraphDbFactory graphDbFactory = graphDbFactory();
    IMappingProviderFactory mappingProviderFactory = mappingProviderFactory();

    when(instanceProvider.hasInstalledExtensions()).thenReturn(true);
    when(instanceProvider.getModelImporterFactory()).thenReturn(modelImporterFactory);
    when(instanceProvider.getCypherStatementRegistry()).thenReturn(cypherStatementRegistry);
    when(instanceProvider.getGraphDbFactory()).thenReturn(graphDbFactory);
    when(instanceProvider.getMappingProviderFactories()).thenReturn(Collections.singletonList(mappingProviderFactory));

    return instanceProvider;
  }

  @Bean
  public IMappingProviderFactory mappingProviderFactory() {

    IMappingProviderFactory mappingProviderFactory = mock(IMappingProviderFactory.class);
    IMappingProvider mappingProvider = mock(IMappingProvider.class);

    when(mappingProviderFactory.createNewMappingProvider()).thenReturn(mappingProvider);

    IDependencyDefinitionProvider dependencyDefinitionProvider = mock(IDependencyDefinitionProvider.class);
    IMappingProvider.IMappingProviderMetadata mappingProviderMetadata = mock(IMappingProvider.IMappingProviderMetadata.class);
    IHierarchyDefinitionProvider hierarchyDefinitionProvider = mock(IHierarchyDefinitionProvider.class);
    ILabelDefinitionProvider labelDefinitionProvider = mock(ILabelDefinitionProvider.class);
    INodeComparator nodeComparator = mock(INodeComparator.class);

    when(mappingProvider.getDependencyDefinitionProvider()).thenReturn(dependencyDefinitionProvider);
    when(mappingProvider.getMappingProviderMetadata()).thenReturn(mappingProviderMetadata);
    when(mappingProvider.getHierarchyDefinitionProvider()).thenReturn(hierarchyDefinitionProvider);
    when(mappingProvider.getLabelDefinitionProvider()).thenReturn(labelDefinitionProvider);
    when(mappingProvider.getNodeComparator()).thenReturn(nodeComparator);

    return mappingProviderFactory;
  }

  @Bean
  public IGraphDbFactory graphDbFactory() {

    IGraphDbFactory graphDbFactory = mock(IGraphDbFactory.class);
    IGraphDbFactory.IGraphDbBuilder graphDbBuilder = mock(IGraphDbFactory.IGraphDbBuilder.class);

    IGraphDb graphDb = graphDb();

    when(graphDbFactory.newGraphDb(any())).thenReturn(graphDbBuilder);
    when(graphDbFactory.newGraphDb(anyInt(), any())).thenReturn(graphDbBuilder);

    when(graphDbBuilder.create()).thenReturn(graphDb);

    return graphDbFactory;
  }

  @Bean
  public IGraphDb graphDb() {
    return mock(IGraphDb.class);
  }

  @Bean
  public IModelImporter modelImporter() {

    IModelImporter modelImporter = mock(IModelImporter.class);

    IGraphDb graphDb = graphDb();

    // IModelImporter.getGraphDb
    when(modelImporter.getGraphDb()).thenReturn(graphDb);

    // IModelImporter.parse
    when(modelImporter.parse(any())).thenAnswer(inv -> {
      sleep();
      return Collections.emptyList();
    });

    // IModelImporter.parse
    when(modelImporter.parse(any(), any())).thenAnswer(inv -> {
      sleep();
      return Collections.emptyList();
    });

    return modelImporter;
  }

  @Bean
  public IModelImporterFactory modelImporterFactory() {

    IModelImporterFactory modelImporterFactory = mock(IModelImporterFactory.class);

    IModelImporter modelImporter = modelImporter();

    // IModelImporter.createModelImporter
    when(modelImporterFactory.createModelImporter(any(), any(), any(), any())).thenReturn(modelImporter);

    return modelImporterFactory;
  }

  @Bean
  public ICypherStatementRegistry cypherStatementRegistry() {

    ICypherStatementRegistry cypherStatementRegistryMock = mock(ICypherStatementRegistry.class);

    when(cypherStatementRegistryMock.getAllStatements()).thenReturn(Collections.emptyList());

    return cypherStatementRegistryMock;
  }

  private static void sleep() {
    try {
      Thread.sleep(2500);
    } catch (Exception e) {
      // ignore
    }
  }
}
