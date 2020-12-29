/**
 * slizaa-server-slizaadb - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.slizaadb;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@EnableSlizaaDatabaseModule
@TestConfiguration
public class SlizaaDatabaseTestConfiguration {

    @Bean
    public ISlizaaDatabaseEnvironment graphDatabaseEnvironment() {

        ISlizaaDatabaseEnvironment graphDatabaseEnvironment = mock(ISlizaaDatabaseEnvironment.class);
        IGraphDb graphDb = graphDb();
        IModelImporter modelImporter = modelImporter();
        IBoltClient boltClient = boltClient();

        // IGraphDatabaseEnvironment.createContentDefinitionProvider
        when(graphDatabaseEnvironment.createContentDefinitionProvider(eq("mvn"), anyString())).
                thenAnswer(inv -> new MvnBasedContentDefinitionProviderFactory().fromExternalRepresentation((String) inv.getArguments()[1]));

        // IGraphDatabaseEnvironment.createModelImporter
        when(graphDatabaseEnvironment.createModelImporter(any(), any())).thenReturn(modelImporter);

        // IGraphDatabaseEnvironment.createBoltClient
        when(graphDatabaseEnvironment.createGraphDb(anyInt(), any())).thenReturn(graphDb);

        // IGraphDatabaseEnvironment.createBoltClient
        when(graphDatabaseEnvironment.createBoltClient(any())).thenReturn(boltClient);

        // IGraphDatabaseEnvironment.getCurrentExtensionClassLoader
        when(graphDatabaseEnvironment.getCurrentExtensionClassLoader()).thenReturn(AbstractSlizaaDatabaseTest.class.getClassLoader());

        // IGraphDatabaseEnvironment.getCurrentExtensionClassLoader
        when(graphDatabaseEnvironment.mapHierarchicalGraph(any(), any())).thenReturn(HierarchicalgraphFactory.eINSTANCE.createHGRootNode());

        return graphDatabaseEnvironment;
    }

    @Bean
    public IGraphDb graphDb() {
       return mock(IGraphDb.class);
    }

    @Bean
    public IBoltClient boltClient() {
        return mock(IBoltClient.class);
    }

    @Bean
    public IModelImporter modelImporter() {

        IModelImporter modelImporter = mock(IModelImporter.class);
        IGraphDb graphDb = graphDb();


        // IModelImporter.getGraphDb
        when(modelImporter.getGraphDb()).thenReturn(graphDb);

        // IModelImporter.parse
        when(modelImporter.parse(any())).thenAnswer(inv -> {
            sleep(5000);
            return Collections.emptyList();
        });

        // IModelImporter.parse
        when(modelImporter.parse(any(), any())).thenAnswer(inv -> {
            sleep(5000);
            return Collections.emptyList();
        });

        return modelImporter;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            // ignore
        }
    }
}
