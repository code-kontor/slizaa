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
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.scanner.api.graphdb.IGraphDb;
import io.codekontor.slizaa.scanner.api.importer.IModelImporter;
import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import io.codekontor.slizaa.server.slizaadb.internal.HierarchicalGraph;
import io.codekontor.slizaa.server.slizaadb.internal.HierarchicalGraphDefinition;

import java.io.File;

public interface ISlizaaDatabaseEnvironment {

    void storeConfig();

   IGraphDb createGraphDb(int port, File databaseDirectory);

    void handleHierarchicalGraphRemoved(HierarchicalGraph hierarchicalGraph);

    void handleGraphDatabaseRemoved(String identifier);

    IModelImporter createModelImporter(IContentDefinitionProvider<?> contentDefinitionProvider, File databaseDirectory);

    IBoltClient createBoltClient(String s);

    HGRootNode mapHierarchicalGraph(HierarchicalGraphDefinition def, IBoltClient boltClient);

    ClassLoader getCurrentExtensionClassLoader();

    IContentDefinitionProvider<?> createContentDefinitionProvider(String contentDefinitionFactoryId, String contentDefinition);
}
