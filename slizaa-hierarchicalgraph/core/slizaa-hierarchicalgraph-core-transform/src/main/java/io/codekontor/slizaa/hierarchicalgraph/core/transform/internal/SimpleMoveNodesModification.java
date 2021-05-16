/**
 * slizaa-hierarchicalgraph-core-transform - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.transform.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.transform.IHierarchicalGraphModification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleMoveNodesModification implements IHierarchicalGraphModification {

    private HGNode _newParent;

    private List<HGNode> _nodes;

    private Map<HGNode, HGNode> _node2FormerParentMap;

    public SimpleMoveNodesModification(HGNode newParent, List<HGNode> nodes) {
        this._newParent = checkNotNull(newParent);
        this._nodes = checkNotNull(nodes);
    }

    @Override
    public void execute() {

        _node2FormerParentMap = this._nodes.stream().collect(Collectors.toMap(hgNode -> hgNode, HGNode::getParent));

        _nodes.forEach(node -> {
            node.getParent().getChildren().remove(node);
        });

        _newParent.getChildren().addAll(_nodes);
    }

    @Override
    public void undo() {

        _newParent.getChildren().removeAll(_nodes);

        _nodes.forEach(node -> {
            _node2FormerParentMap.get(node).getChildren().add(node);
        });

        _node2FormerParentMap = null;
    }
}
