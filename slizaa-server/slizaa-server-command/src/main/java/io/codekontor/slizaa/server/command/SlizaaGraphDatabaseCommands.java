/**
 * slizaa-server-command - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.command;

import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;

@ShellComponent
@ShellCommandGroup("Slizaa Graph Databases Commands")
public class SlizaaGraphDatabaseCommands extends AbstractGraphDatabaseCommandComponent {

    @ShellMethod(value = "List all configured graph databases.", key = "listDBs")
    public String listDBs() {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        //
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Create a new graph databases.", key = "createDB")
    public String createDB(String identifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        String checkDatabaseDoesNotExist = checkDatabaseDoesNotExist(identifier);
        if (checkDatabaseDoesNotExist != null) {
            return checkDatabaseDoesNotExist;
        }

        //
        slizaaService().newGraphDatabase(identifier);

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Delete an existing graph database.", key = "deleteDB")
    public String deleteDB(String identifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        String checkDatabaseExists = checkDatabaseExists(identifier);
        if (checkDatabaseExists != null) {
            return checkDatabaseExists;
        }

        //
        IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(identifier);
        graphDatabase.terminate();

        // return the result
        StringBuffer result = new StringBuffer();
        result.append(String.format("Successfully deleted graph database '%s'.\n", identifier));
        result.append(dumpGraphDatabases());
        return result.toString();
    }

    @ShellMethod(value = "Parse the definied content.", key = "parseDB")
    public String parseDB(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier) {

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
        try {
            graphDatabase.parse(true);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Start the specified database.", key = "startDB")
    public String startDB(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier) {

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
        graphDatabase.start();

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Stop the specified database.", key = "stopDB")
    public String stopDB(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier) {

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
        graphDatabase.stop();

        // return the result
        return dumpGraphDatabases();
    }
}