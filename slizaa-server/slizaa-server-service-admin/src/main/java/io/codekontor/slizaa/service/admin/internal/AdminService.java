/**
 * slizaa-server-service-admin - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.service.admin.internal;

import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.service.admin.IAdminService;
import io.codekontor.slizaa.service.admin.descr.ContentDefinitionDescr;
import io.codekontor.slizaa.service.admin.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.service.admin.descr.HierarchicalGraphDescr;
import io.codekontor.slizaa.service.admin.descr.ServerExtensionDescr;
import io.codekontor.slizaa.service.admin.descr.SlizaaServerDescr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class AdminService implements IAdminService {

    @Autowired
    private ISlizaaService _slizaaService;

    @Override
    public SlizaaServerDescr fetchDescription() {

        // extensions
        List<ServerExtensionDescr> extensionDescriptions =
                _slizaaService.getBackendService().getInstalledExtensions()
                        .stream().map(toServerExtensionsDescr()).collect(Collectors.toList());

        // graph databases
        List<GraphDatabaseDescr> databaseDescriptions =
                _slizaaService.getGraphDatabases().stream().map(toGraphDatabaseDescr())
                        .collect(Collectors.toList());

        // return the server description
        return new SlizaaServerDescr(extensionDescriptions, databaseDescriptions);
    }

    private Function<IExtension, ServerExtensionDescr> toServerExtensionsDescr() {
        return extension -> new ServerExtensionDescr(extension.getSymbolicName(),
                extension.getVersion().toString());
    }

    private Function<ISlizaaDatabase, GraphDatabaseDescr> toGraphDatabaseDescr() {
        return graphDatabase -> {

            // map content definition
            String contentDefinitionFactoryId = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId();
            String contentDefinitionShortForm = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory().getShortForm();
            String contentDefinition = graphDatabase.getContentDefinition().toExternalRepresentation();
            ContentDefinitionDescr contentDefinitionDescr =
                    new ContentDefinitionDescr(contentDefinitionFactoryId, contentDefinitionShortForm, contentDefinition);

            // map hierarchicalGraphs
            List<HierarchicalGraphDescr> hierarchicalGraphDescrs = graphDatabase.getHierarchicalGraphs().stream()
                    .map(toHierarchicalGraphDescr()).collect(Collectors.toList());

            // map available actions
            List<String> availableActions = graphDatabase.getAvailableActions()
                    .stream().map(action -> action.getName()).collect(Collectors.toList());

            // map database specification
            GraphDatabaseDescr graphDatabaseSpec = new GraphDatabaseDescr(graphDatabase.getIdentifier(),
                    contentDefinitionDescr,
                    hierarchicalGraphDescrs,
                    graphDatabase.getState().name(),
                    graphDatabase.getPort(),
                    availableActions);

            // return the result
            return graphDatabaseSpec;
        };
    }

    private Function<IHierarchicalGraph, HierarchicalGraphDescr> toHierarchicalGraphDescr() {
        return hierarchicalGraph -> {

            // map database specification
            HierarchicalGraphDescr hierarchicalGraphDescr = new HierarchicalGraphDescr(hierarchicalGraph.getIdentifier());

            // return the result
            return hierarchicalGraphDescr;
        };
    }
}
