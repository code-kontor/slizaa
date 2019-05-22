/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs.internal.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.codekontor.slizaa.server.specs.api.IGraphdatabaseSpec;
import io.codekontor.slizaa.server.specs.api.IServerExtensionSpec;
import io.codekontor.slizaa.server.specs.api.ISlizaaServerSpec;

public class SlizaaServerSpec implements ISlizaaServerSpec {

  private static final Logger       logger              = LoggerFactory.getLogger(SlizaaServerSpec.class);

  private List<ServerExtensionSpec> _serverExtensionSpecs;

  private List<GraphdatabaseSpec>   _graphdatabaseSpecs = new ArrayList<>();

  public ISlizaaServerSpec serverExtensions(IServerExtensionSpec... serverExtensionConfiguration) {

    //
    if (_serverExtensionSpecs != null) {
      // TODO
      throw new RuntimeException("INVALID");
    }

    //
    _serverExtensionSpecs = new ArrayList<ServerExtensionSpec>();
    for (IServerExtensionSpec spec : serverExtensionConfiguration) {
      _serverExtensionSpecs.add((ServerExtensionSpec) spec);
    }

    //
    return this;
  }

  public ISlizaaServerSpec graphDatabase(IGraphdatabaseSpec... graphdatabaseSpecs) {

    for (IGraphdatabaseSpec spec : graphdatabaseSpecs) {
      _graphdatabaseSpecs.add((GraphdatabaseSpec) spec);
    }

    return this;
  }

  public List<ServerExtensionSpec> getServerExtensionSpecs() {
    return _serverExtensionSpecs;
  }

  public List<GraphdatabaseSpec> getGraphdatabaseSpecs() {
    return _graphdatabaseSpecs;
  }
}
