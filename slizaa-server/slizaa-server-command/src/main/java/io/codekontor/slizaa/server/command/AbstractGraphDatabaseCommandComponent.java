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
import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.table.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public abstract class AbstractGraphDatabaseCommandComponent {

  private static final Map<ISlizaaDatabase.GraphDatabaseAction, String> GRAPH_DATABASE_ACTION_MAP = new HashMap<ISlizaaDatabase.GraphDatabaseAction, String>() {{
    put(ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION, "setContentDefinitionProvider");
    put(ISlizaaDatabase.GraphDatabaseAction.PARSE, "parseDB");
    put(ISlizaaDatabase.GraphDatabaseAction.START, "startDB");
    put(ISlizaaDatabase.GraphDatabaseAction.STOP, "stopDB");
    put(ISlizaaDatabase.GraphDatabaseAction.DELETE, "deleteDB");
    put(ISlizaaDatabase.GraphDatabaseAction.CREATE_HIERARCHICAL_GRAPH, "createHierarchicalGraph");
  }};

  protected static final long TIMEOUT = 120 * 1000;

  @Autowired
  private ISlizaaService            _slizaaService;

  @Autowired(required = false)
  private IModifiableBackendService _modifiableBackendService;

  @Autowired
  private IBackendService           _backendService;

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

  protected Object dumpGraphDatabases() {

    List<Object[]> rows = new ArrayList<>();
    rows.add(
        new String[] { "DatabaseId", "State", "Port", "Content Definition", "Hierarchical Graphs", "Available Actions" });

    if (_slizaaService.hasGraphDatabases()) {
      _slizaaService.getGraphDatabases().forEach(db -> {

        String contentDefinition = db.getContentDefinition() != null
            ? String.format("%s [%s]", db.getContentDefinition().getContentDefinitionProviderFactory().getShortForm(),
                db.getContentDefinition().toExternalRepresentation())
            : null;

        Object[] row = new Object[6];
        row[0] = db.getIdentifier();
        row[1] = db.getState().name();
        row[2] = db.getPort();
        row[3] = contentDefinition;
        row[4] = db.getHierarchicalGraphs().stream().map(hierarchicalGraph -> {
          return hierarchicalGraph.getIdentifier();
        }).collect(Collectors.toList());
        row[5] = db.getAvailableActions().stream().map(action -> GRAPH_DATABASE_ACTION_MAP.get(action)).collect(Collectors.toList());

        rows.add(row);
      });

      TableModel tableModel = new ArrayTableModel(rows.toArray(new Object[0][0]));
      TableBuilder tableBuilder = new TableBuilder(tableModel);
      tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.oldschool);
      // tableBuilder.on((row, column, model) -> row == 0 && row != 5).addSizer(new NoWrapSizeConstraints());
      return tableBuilder.build();
    } else {
      return "No database configured.\n";
    }
  }

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
      return cannotExecuteCommand("The Slizaa Server has not been configured properly: There are no installed backend extensions.\n");
    }
    return null;
  }

  protected String checkDatabaseExists(String identifier) {
    ISlizaaDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
    if (graphDatabase == null) {
      return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", identifier));
    }
    return null;
  }

  protected String checkDatabaseDoesNotExist(String identifier) {
    ISlizaaDatabase graphDatabase = _slizaaService.getGraphDatabase(identifier);
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

  protected String formatExtension(IExtension extension) {
    return String.format(" - %1$s_%2$s (Symbolic name: %1$s, version: %2$s)\n", extension.getSymbolicName(),
        extension.getVersion());
  }

  protected void logException(Exception exception) {
    // TODO
    exception.printStackTrace();
  }

  protected <T> T execute(Callable<T> callable) {
    try {
      return callable.call();
    } catch (Exception exception) {
      logException(exception);
      return null;
    }
  }
}