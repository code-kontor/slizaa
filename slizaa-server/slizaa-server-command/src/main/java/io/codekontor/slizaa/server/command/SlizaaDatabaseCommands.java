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
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.Table;

import java.io.IOException;

@ShellComponent
@ShellCommandGroup("Slizaa Database Commands")
public class SlizaaDatabaseCommands extends AbstractGraphDatabaseCommandComponent {

    @ShellMethod(value = "List all configured databases.", key = "listDBs")
    public Object listDBs() {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        //
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Create a new database.", key = "createDB")
    public Object createDB(@ShellOption(value = {"-d", "--databaseId"}, help = "The identifier of the database to create.") String databaseIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        String checkDatabaseDoesNotExist = checkDatabaseDoesNotExist(databaseIdentifier);
        if (checkDatabaseDoesNotExist != null) {
            return checkDatabaseDoesNotExist;
        }

        //
        slizaaService().newGraphDatabase(databaseIdentifier);

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Delete an existing database.", key = "deleteDB")
    public Object deleteDB(@ShellOption(value={"-d", "--databaseId"}, help = "The identifier of the database to delete.") String databaseIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        String checkDatabaseExists = checkDatabaseExists(databaseIdentifier);
        if (checkDatabaseExists != null) {
            return checkDatabaseExists;
        }

        //
        IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);
        graphDatabase.terminate();

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Parse the definied content.", key = "parseDB")
    public Object parseDB(@ShellOption(value={"-d", "--databaseId"}, help = "The identifier of the database to parse.") String databaseIdentifier) {

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
    public Object startDB(@ShellOption(value={"-d", "--databaseId"}, help = "The identifier of the database to start.") String databaseIdentifier) {

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

        //
        graphDatabase.start();

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Stop the specified database.", key = "stopDB")
    public Object stopDB(@ShellOption(value={"-d", "--databaseId"}, help = "The identifier of the database to stop.") String databaseIdentifier) {

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

        //
        graphDatabase.stop();

        // return the result
        return dumpGraphDatabases();
    }
}