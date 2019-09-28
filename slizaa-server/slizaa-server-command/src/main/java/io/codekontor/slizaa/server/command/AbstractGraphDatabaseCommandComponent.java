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

import io.codekontor.slizaa.server.descr.ContentDefinition;
import io.codekontor.slizaa.server.descr.GraphDatabase;
import io.codekontor.slizaa.server.descr.HierarchicalGraph;
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionService;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.spec.HierarchicalGraphSpec;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.table.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGraphDatabaseCommandComponent {

  @Autowired
  private ISlizaaService            _slizaaService;

  @Autowired(required = false)
  private IModifiableBackendService _modifiableBackendService;

  @Autowired
  private IBackendService           _backendService;

  @Autowired
  private IExtensionService         _extensionService;

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
  protected Object dumpGraphDatabases() {

    List<Object[]> rows = new ArrayList<>();
    rows.add(
        new String[] { "DatabaseId", "State", "Port", "ContentDefinition", "HierarchicalGraphs", "Available_Actions" });
    if (_slizaaService.hasGraphDatabases()) {
      _slizaaService.getGraphDatabases().forEach(db -> {

        ContentDefinition contentDefinition = db.getContentDefinition() != null
            ? new ContentDefinition(db.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId(),
                db.getContentDefinition().getContentDefinitionProviderFactory().getShortForm(),
                db.getContentDefinition().toExternalRepresentation())
            : null;

        Object[] row = new Object[6];
        row[0] = db.getIdentifier();
        row[1] = db.getState().name();
        row[2] = db.getPort();
        row[3] = contentDefinition;
        row[4] = db.getHierarchicalGraphs().stream().map(hierarchicalGraph -> {
          return new HierarchicalGraph(hierarchicalGraph.getIdentifier());
        }).collect(Collectors.toList());
        row[5] = db.getAvailableActions().stream().map(action -> action.getName()).collect(Collectors.toList());

        rows.add(row);
      });
    TableModel tableModel = new ArrayTableModel(rows.toArray(new Object[0][0]));
    TableBuilder tableBuilder = new TableBuilder(tableModel);
    tableBuilder.addInnerBorder(BorderStyle.fancy_light);
    tableBuilder.addHeaderBorder(BorderStyle.fancy_double);
    tableBuilder.on((row, column, model) -> row == 0).addSizer(new NoWrapSizeConstraints());
    return tableBuilder.build();

    }

    else {
      return "No database configured.\n";
    }
  }

//  @NotNull
//  protected String dumpGraphDatabases() {
//
//    StringBuffer stringBuffer = new StringBuffer("Graph Databases:\n");
//
//    if (_slizaaService.hasGraphDatabases()) {
//
//      _slizaaService.getGraphDatabases().forEach(db -> {
//
//        ContentDefinition contentDefinition = db.getContentDefinition() != null
//            ? new ContentDefinition(db.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId(),
//                db.getContentDefinition().getContentDefinitionProviderFactory().getShortForm(),
//                db.getContentDefinition().toExternalRepresentation())
//            : null;
//
//        GraphDatabase graphDatabase = new GraphDatabase(db.getIdentifier(), contentDefinition,
//            db.getHierarchicalGraphs().stream().map(hierarchicalGraph -> {
//              return new HierarchicalGraph(hierarchicalGraph.getIdentifier());
//            }).collect(Collectors.toList()), db.getState().name(), db.getPort(),
//            db.getAvailableActions().stream().map(action -> action.getName()).collect(Collectors.toList()));
//
//        stringBuffer.append(" - " + graphDatabase.toString() + "\n");
//      });
//    } else {
//      stringBuffer.append("No database configured.\n");
//    }
//
//    return stringBuffer.toString();
//  }

  @NotNull
  protected String dumpContentDefinitionProviderFactories() {

    StringBuffer stringBuffer = new StringBuffer("Content Definition Provider Factories:\n");

    if (!_slizaaService.getContentDefinitionProviderFactories().isEmpty()) {
      _slizaaService.getContentDefinitionProviderFactories().forEach(factory -> {
        stringBuffer.append(" - " + factory.getShortForm() + " (" + factory.getFactoryId() + ")\n");
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
      StringBuilder message = new StringBuilder();
      message
          .append("The Slizaa Server has not been configured properly: There are not installed backend extensions.\n");
      message.append(dumpAvailableExtensions());
      if (_modifiableBackendService != null) {
        message.append("You can install backend extensions using the command 'installExtensions'.");
      }
      return cannotExecuteCommand(message.toString());
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

  protected String dumpAvailableExtensions() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("Available Backend Extensions:\n");
    extensionService().getExtensions().forEach(extension -> {
      stringBuffer.append(formatExtension(extension));
    });
    return stringBuffer.toString();
  }

  protected String formatExtension(IExtension extension) {
    return String.format(" - %1$s_%2$s (Symbolic name: %1$s, version: %2$s)\n", extension.getSymbolicName(),
        extension.getVersion());
  }
}