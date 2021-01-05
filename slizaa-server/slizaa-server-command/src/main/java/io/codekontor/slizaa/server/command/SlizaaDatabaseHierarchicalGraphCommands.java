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

import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@ShellComponent
@ShellCommandGroup("Slizaa Database Commands - Hierarchical Graphs")
public class SlizaaDatabaseHierarchicalGraphCommands extends AbstractGraphDatabaseCommandComponent {

    @ShellMethod(value = "Create a new hierarchical graph.", key = "createHierarchicalGraph")
    public Object createHierarchicalGraph(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier, @ShellOption({"-h", "--hierarchicalGraphId"}) String hierarchicalGraphIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        ISlizaaDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // check that the hierarchical graph does not exist
        IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph("hierarchicalGraphIdentifier");
        if (hierarchicalGraph != null) {
            return cannotExecuteCommand(String.format("The specified hierarchical graph '%s' already exists.\n", hierarchicalGraphIdentifier));
        }

        // new hierarchical graph
        graphDatabase.newHierarchicalGraph(hierarchicalGraphIdentifier);

        //
        try {
            graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, TIMEOUT);
        } catch (TimeoutException e ) {
            //TODO
            e.printStackTrace();
        }

        // return the result
        return dumpGraphDatabases();
    }

    @ShellMethod(value = "Delete an existing hierarchical graph.", key = "deleteHierarchicalGraph")
    public Object deleteHierarchicalGraph(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier, @ShellOption({"-h", "--hierarchicalGraphId"}) String hierarchicalGraphIdentifier) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        ISlizaaDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // check that the hierarchical graph does exist
        IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph("hierarchicalGraphIdentifier");
        if (hierarchicalGraph == null) {
            return cannotExecuteCommand(String.format("The specified hierarchical graph '%s' does not exist.\n", hierarchicalGraphIdentifier));
        }

        // remove hierarchical graph
        graphDatabase.removeHierarchicalGraph(hierarchicalGraphIdentifier);

        //
        try {
            graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, TIMEOUT);
        } catch (TimeoutException e ) {
            //TODO
            e.printStackTrace();
        }

        // return the result
        return dumpGraphDatabases();
    }
}