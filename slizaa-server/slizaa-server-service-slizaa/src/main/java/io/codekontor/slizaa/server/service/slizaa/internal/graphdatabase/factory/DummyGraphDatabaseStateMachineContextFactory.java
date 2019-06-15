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

import org.springframework.beans.factory.annotation.Autowired;

import io.codekontor.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.IGraphDatabaseStateMachineContext;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.IGraphDatabaseStateMachineContextFactory;

public class DummyGraphDatabaseStateMachineContextFactory implements IGraphDatabaseStateMachineContextFactory {

  @Autowired
  private SlizaaServiceImpl _slizaaService;

  @Override
  public IGraphDatabaseStateMachineContext createGraphDatabaseStateMachineContext(String id, File databaseDirectory,
      int port) {
    return new DummyGraphDatabaseStateMachineContext(id, databaseDirectory, port, _slizaaService);
  }
}

