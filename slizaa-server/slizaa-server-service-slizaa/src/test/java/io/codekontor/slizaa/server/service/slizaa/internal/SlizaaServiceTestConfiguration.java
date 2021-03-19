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
import io.codekontor.slizaa.core.boltclient.IBoltClientFactory;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDbFactory;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.api.importer.IModelImporterFactory;
import io.codekontor.slizaa.server.slizaadb.EnableSlizaaDatabaseModule;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.configuration.EnableConfigurationModule;
import io.codekontor.slizaa.server.service.selection.EnableSelectionServiceModule;
import io.codekontor.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@EnableConfigurationModule
@EnableSlizaaServiceModule
@EnableSelectionServiceModule
@EnableSlizaaDatabaseModule
@TestConfiguration
public class SlizaaServiceTestConfiguration {

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

    when(instanceProvider.hasInstalledExtensions()).thenReturn(true);
    when(instanceProvider.getModelImporterFactory()).thenReturn(modelImporterFactory);
    when(instanceProvider.getCypherStatementRegistry()).thenReturn(cypherStatementRegistry);
    when(instanceProvider.getGraphDbFactory()).thenReturn(graphDbFactory);

    return instanceProvider;
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
