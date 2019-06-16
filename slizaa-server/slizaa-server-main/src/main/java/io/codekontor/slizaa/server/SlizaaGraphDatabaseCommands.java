/**
 * slizaa-server-main - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.server;

import io.codekontor.slizaa.server.descr.ContentDefinition;
import io.codekontor.slizaa.server.descr.GraphDatabase;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.util.Collections;

@ShellComponent
@ShellCommandGroup("Slizaa Graph Databases Commands")
public class SlizaaGraphDatabaseCommands {

    @Autowired(required = false)
    private IModifiableBackendService _modifiableBackendService;

    @Autowired
    private ISlizaaService _slizaaService;

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
        _slizaaService.newGraphDatabase(identifier);

        // return the result
        return dumpGraphDatabases();
    }

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
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // TODO:
        // _slizaaService.getContentDefinitionProviderFactories();

        //
        graphDatabase.setContentDefinitionProvider(contentDefinitionProviderFactoryId, contentDefinition);

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Parse the definied content.", key = "parseDB", group = "Slizaa Graph Databases Commands - Lifecycle")
    public String parseDB(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // TODO:
        // _slizaaService.getContentDefinitionProviderFactories();

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

    @ShellMethod(value = "Start the specified database.", key = "startDB", group = "Slizaa Graph Databases Commands - Lifecycle")
    public String startDB(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // TODO:
        // _slizaaService.getContentDefinitionProviderFactories();

        //
        graphDatabase.start();

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Stop the specified database.", key = "stopDB", group = "Slizaa Graph Databases Commands - Lifecycle")
    public String stopDB(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // TODO:
        // _slizaaService.getContentDefinitionProviderFactories();

        //
        graphDatabase.stop();

        // return the result
        return dumpGraphDatabases();
    }


    @ShellMethod(value = "Create a new hierarchical graph.", key = "createHierarchicalGraph", group = "Slizaa Graph Databases Commands - Hierarchical Graphs")
    public String createHierarchicalGraph(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier, @ShellOption({"-h", "--hierarchicalGraphId"}) String hierarchicalGraphIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // check that the hierarchical graph does not exist
        IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph("hierarchicalGraphIdentifier");
        if (hierarchicalGraph != null) {
            return cannotExecuteCommand(String.format("The specified hierarchical graph '%s' already exists.\n", hierarchicalGraphIdentifier));
        }

        //
        graphDatabase.newHierarchicalGraph(hierarchicalGraphIdentifier);

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
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
        graphDatabase.terminate();

        // return the result
        StringBuffer result = new StringBuffer();
        result.append(String.format("Successfully deleted graph database '%s'.\n", identifier));
        result.append(dumpGraphDatabases());
        return result.toString();

    }

    @NotNull
    private String dumpGraphDatabases() {

        StringBuffer stringBuffer = new StringBuffer("Graph Databases:\n");

        if (_slizaaService.hasGraphDatabases()) {

            _slizaaService.getGraphDatabases().forEach(db -> {

                ContentDefinition contentDefinition =
                        db.getContentDefinition() != null ?
                                new ContentDefinition(
                                        db.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId(),
                                        db.getContentDefinition().toExternalRepresentation()) :
                                null;

                GraphDatabase graphDatabase = new GraphDatabase(
                        db.getIdentifier(),
                        contentDefinition,
                        Collections.emptyList(),
                        db.getState().name(),
                        db.getPort(),
                        Collections.emptyList());

                stringBuffer.append(" - " + graphDatabase.toString() + "\n");
            });
        } else {
            stringBuffer.append("No database configured.\n");
        }

        return stringBuffer.toString();
    }

    @NotNull
    private String dumpContentDefinitionProviderFactories() {

        StringBuffer stringBuffer = new StringBuffer("Content Definition Provider Factories:\n");

        if (!_slizaaService.getContentDefinitionProviderFactories().isEmpty()) {
            _slizaaService.getContentDefinitionProviderFactories().forEach(factory -> {
                stringBuffer.append(" - " + factory.getFactoryId() + "\n");
            });
        } else {
            stringBuffer.append("No Content Definition Provider Factories available.\n");
        }

        return stringBuffer.toString();
    }

    /**
     * Checks if the backend is configured properly.
     *
     * @return <code>true</code> if the backend is configured properly.
     */
    private String checkBackendConfigured() {
        if (!_slizaaService.getBackendService().hasInstalledExtensions()) {
            return cannotExecuteCommand("The Slizaa Server has not been configured properly: There are not installed backend extensions.\n");
        }
        return null;
    }

    private String checkDatabaseExists(String identifier) {
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", identifier));
        }
        return null;
    }

    private String checkDatabaseDoesNotExist(String identifier) {
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
        if (graphDatabase != null) {
            return cannotExecuteCommand(String.format("The specified database '%s' already exists.\n", identifier));
        }
        return null;
    }

    private String cannotExecuteCommand(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can not execute command.\n");
        stringBuffer.append(msg + "\n");
        return stringBuffer.toString();
    }
}