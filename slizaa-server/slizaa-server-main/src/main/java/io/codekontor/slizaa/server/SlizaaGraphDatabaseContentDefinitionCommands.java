/**
 * slizaa-server-main - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server;

import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@ShellCommandGroup("Slizaa Graph Databases Commands")
public class SlizaaGraphDatabaseContentDefinitionCommands extends AbstractGraphDatabaseCommandComponent {

    @ShellMethod(value = "List available content definition provider factories.", key = {"listContentDefinitionProviderFactories"}, group = "Slizaa Graph Databases Commands - Content Definition")
    public String listContentDefinitionProviderFactories() {
        return dumpContentDefinitionProviderFactories();
    }

    @ShellMethod(value = "Define the content that should be analyzed.", key = "setContentDefinitionProvider", group = "Slizaa Graph Databases Commands - Content Definition")
    public String setContentDefinitionProvider(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier, @ShellOption({"-f", "--factoryId"}) String contentDefinitionProviderFactoryId, @ShellOption({"-c", "--contentDefinition"}) String contentDefinition) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // TODO:
        // slizaaService().getContentDefinitionProviderFactories();

        //
        graphDatabase.setContentDefinitionProvider(contentDefinitionProviderFactoryId, contentDefinition);

        // return the result
        return dumpGraphDatabases();
    }
}