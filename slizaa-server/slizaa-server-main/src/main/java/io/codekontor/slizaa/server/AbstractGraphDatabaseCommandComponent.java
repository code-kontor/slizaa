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

import io.codekontor.slizaa.server.descr.ContentDefinition;
import io.codekontor.slizaa.server.descr.GraphDatabase;
import io.codekontor.slizaa.server.descr.HierarchicalGraph;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import io.codekontor.slizaa.server.service.extensions.IExtensionService;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.spec.HierarchicalGraphSpec;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.stream.Collectors;

public abstract class AbstractGraphDatabaseCommandComponent {

    @Autowired
    private ISlizaaService _slizaaService;

    @Autowired(required = false)
    private IModifiableBackendService _modifiableBackendService;

    @Autowired
    private IBackendService _backendService;

    @Autowired
    private IExtensionService _extensionService;

    protected ISlizaaService slizaaService() {
        return _slizaaService;
    }

    protected boolean hasModifiableBackendService() {
        return _modifiableBackendService != null;
    }

    protected IModifiableBackendService modifiableBackendService() {
        return _modifiableBackendService;
    }

    protected IBackendService backendService() {
        return _backendService;
    }

    protected IExtensionService extensionService() {
        return _extensionService;
    }

    @NotNull
    protected String dumpGraphDatabases() {

        StringBuffer stringBuffer = new StringBuffer("Graph Databases:\n");

        if (_slizaaService.hasGraphDatabases()) {

            _slizaaService.getGraphDatabases().forEach(db -> {

                ContentDefinition contentDefinition =
                        db.getContentDefinition() != null ?
                                new ContentDefinition(
                                        db.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId(),
                                        db.getContentDefinition().getContentDefinitionProviderFactory().getShortForm(),
                                        db.getContentDefinition().toExternalRepresentation()) :
                                null;



                GraphDatabase graphDatabase = new GraphDatabase(
                        db.getIdentifier(),
                        contentDefinition,
                        db.getHierarchicalGraphs().stream().map(hierarchicalGraph -> {
                            return new HierarchicalGraph(hierarchicalGraph.getIdentifier());
                        }).collect(Collectors.toList()),
                        db.getState().name(),
                        db.getPort(),
                        db.getAvailableActions().stream().map(action -> action.getName()).collect(Collectors.toList()));

                stringBuffer.append(" - " + graphDatabase.toString() + "\n");
            });
        } else {
            stringBuffer.append("No database configured.\n");
        }

        return stringBuffer.toString();
    }

    @NotNull
    protected String dumpContentDefinitionProviderFactories() {

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
    protected String checkBackendConfigured() {
        if (!_slizaaService.getBackendService().hasInstalledExtensions()) {
            return cannotExecuteCommand("The Slizaa Server has not been configured properly: There are not installed backend extensions.\n");
        }
        return null;
    }

    protected String checkDatabaseExists(String identifier) {
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", identifier));
        }
        return null;
    }

    protected String checkDatabaseDoesNotExist(String identifier) {
        IGraphDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
        if (graphDatabase != null) {
            return cannotExecuteCommand(String.format("The specified database '%s' already exists.\n", identifier));
        }
        return null;
    }

    protected String cannotExecuteCommand(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can not execute command.\n");
        stringBuffer.append(msg + "\n");
        return stringBuffer.toString();
    }
}