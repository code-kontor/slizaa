/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.factory;

import java.io.File;
import java.util.List;

import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import io.codekontor.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.IGraphDatabaseStateMachineContext;

public class DummyGraphDatabaseStateMachineContext extends AbstractGraphDatabaseStatemachineContext
    implements IGraphDatabaseStateMachineContext {

  private boolean _running;

  public DummyGraphDatabaseStateMachineContext(String identifier, File databaseDirectory, int port,
      SlizaaServiceImpl slizaaService) {
    super(identifier, databaseDirectory, port, slizaaService);
  }

  @Override
  public boolean isRunning() {
    return _running;
  }

  @Override
  public boolean parse(boolean startDatabase) {
    _running = startDatabase;
    return _running;
  }

  @Override
  public void start() {
    _running = true;
  }

  @Override
  public void stop() {
    _running = false;
  }

  @Override
  public void terminate() {
    super.terminate();
    _running = false;
  }

  @Override
  public IHierarchicalGraph createHierarchicalGraph(String identifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IHierarchicalGraph getHierarchicalGraph(String identifier) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IHierarchicalGraph> getHierarchicalGraphs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void disposeHierarchicalGraph(String identifier) {
    // TODO Auto-generated method stub

  }

  @Override
  public void storeConfiguration() {
    // TODO Auto-generated method stub

  }
}
