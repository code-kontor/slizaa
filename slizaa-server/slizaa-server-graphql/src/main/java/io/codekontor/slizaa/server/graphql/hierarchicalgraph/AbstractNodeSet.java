/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

public abstract class AbstractNodeSet {

  private List<HGNode> _hgNodeSet;

  public AbstractNodeSet(List<HGNode> hgNodeSet) {
    this._hgNodeSet = checkNotNull(hgNodeSet);
  }

  public List<Node> getNodes() {
    return _hgNodeSet.stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }

  public List<String> getNodeIds() {
    return _hgNodeSet.stream().map(hgNode -> hgNode.getIdentifier().toString()).collect(Collectors.toList());
  }
  
  protected List<HGNode> hgNodeSet() {
    return _hgNodeSet;
  }
}
