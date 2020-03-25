/**
 * slizaa-server-service-spec - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.service.spec.internal;

import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.spec.ContentDefinitionSpec;
import io.codekontor.slizaa.server.spec.GraphDatabaseSpec;
import io.codekontor.slizaa.server.spec.ServerExtensionSpec;
import io.codekontor.slizaa.server.spec.SlizaaServerSpec;
import io.codekontor.slizaa.service.spec.ISpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class SpecService implements ISpecService {

    @Autowired
    private ISlizaaService _slizaaService;

    @Override
    public void reconfigure(SlizaaServerSpec slizaaServerSpec) {

    }

    @Override
    public SlizaaServerSpec fetchSpec() {

        //
        List<ServerExtensionSpec> extensionSpecs =
                _slizaaService.getBackendService().getInstalledExtensions()
                        .stream().map(toServerExtensionsSpec()).collect(Collectors.toList());

        // graph databases
        List<GraphDatabaseSpec> databaseSpecs =
                _slizaaService.getGraphDatabases().stream().map(toGraphdatabaseSpec())
                        .collect(Collectors.toList());

        return new SlizaaServerSpec(extensionSpecs, databaseSpecs);
    }

    private Function<IExtension, ServerExtensionSpec> toServerExtensionsSpec() {
        return extension -> new ServerExtensionSpec(extension.getSymbolicName(),
                extension.getVersion().toString());
    }

    private Function<IGraphDatabase, GraphDatabaseSpec> toGraphdatabaseSpec() {
        return graphDatabase -> {

            // map content definition
            String contentDefinitionFactoryId = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId();
            String contentDefinition = graphDatabase.getContentDefinition().toExternalRepresentation();
            ContentDefinitionSpec contentDefinitionSpec = new ContentDefinitionSpec(contentDefinitionFactoryId, contentDefinition);

            // map database specification
            GraphDatabaseSpec graphDatabaseSpec = new GraphDatabaseSpec(graphDatabase.getIdentifier(), contentDefinitionSpec);

            // return the result
            return graphDatabaseSpec;
        };
    }
}
