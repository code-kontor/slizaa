/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs;

import static io.codekontor.slizaa.server.specs.api.ISlizaaServerSpec.Factory.*;

import io.codekontor.slizaa.server.specs.api.ISlizaaServerSpec;

public class Example {

  public static void main(String[] args) throws Exception {

    ISlizaaServerSpec slizaaServerSpec = slizaaServerSpec()

        // the server extensions
        .serverExtensions(serverExtensionSpec("io.codekontor.slizaa.extensions.jtype", "1.0.0"))

        // the graph database 'test'
        .graphDatabase(graphDatabaseConfiguration("test2")
            
            // add the content definition
            .contentDefinition(
                "io.codekontor.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
                "org.hibernate:hibernate-entitymanager:5.1.17.Final")
            
            // always rebuild from scratch
            .forceRebuild(true)
            
            // also create a hierarchical graph named "01"
            .hierarchicalGraphs(hierarchicalGraphSpec("01")));

    // execute the specification
    new SlizaaAdminClient("http://localhost:8085/graphql").sync(slizaaServerSpec);
  }
}
