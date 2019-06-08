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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.codekontor.slizaa.server.spec.GraphDatabaseSpec;
import io.codekontor.slizaa.server.spec.ServerExtensionSpec;
import io.codekontor.slizaa.server.spec.SlizaaServerSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.service.spec.ISpecService;

/**
 * 
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

    // server extensions
    List<ServerExtensionSpec> serverExtensionSpecs =
            _slizaaService.getExtensionService().getExtensions().stream().map(toServerExtensionsSpec()).collect(Collectors.toList());

    List<GraphDatabaseSpec> graphDatabaseSpecs = new ArrayList<>();

    //
    return new SlizaaServerSpec(serverExtensionSpecs, graphDatabaseSpecs);


//    ServerExtensionSpec[]  _slizaaService.getExtensionService().getExtensions().stream().map(toServerExtensionsSpec())
//            .toArray(ServerExtensionSpec[]::new);
//
//    SlizaaServerSpec result = new SlizaaServerSpec();
//
//    // extensions
//    result.serverExtensions();
//
//    // graph databases
//    result.graphDatabases(_slizaaService.getGraphDatabases().stream().map(toGraphdatabaseSpec())
//        .toArray(GraphdatabaseSpec[]::new));
//
//    return result;
  }

  private Function<IExtension, ServerExtensionSpec> toServerExtensionsSpec() {
    return extension -> new ServerExtensionSpec(extension.getSymbolicName(),
        extension.getVersion().toString());
  }

//  private Function<IGraphDatabase, GraphDatabaseSpec> toGraphdatabaseSpec() {
//    return graphDatabase -> {
//
//      GraphdatabaseSpec result = GraphdatabaseSpec.graphDatabaseConfiguration(graphDatabase.getIdentifier());
//      result.forceRebuild(true);
//
//      String contentDefinitionFactoryId = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId();
//      String contentDefinition = graphDatabase.getContentDefinition().toExternalRepresentation();
//
//      result.contentDefinition(contentDefinitionFactoryId, contentDefinition);
//
//      return result;
//    };
//  }
}
