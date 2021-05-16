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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class MoveNodesAndDuplicatePredecessorsModification implements IHierarchicalGraphModification {

    private HGNode _newParent;

    private List<HGNode> _nodes;

    private Function<HGNode, Boolean> _shouldDuplicatePredecessorFunction;

    private Map<HGNode, HGNode> _node2FormerParentMap;

    private Map<HGNode, HGNode> _newlyDuplicatedPredecessorNodes;

    public MoveNodesAndDuplicatePredecessorsModification(HGNode newParent, List<HGNode> nodes, Function<HGNode, Boolean> shouldDuplicatePredecessorFunction) {
        this._newParent = checkNotNull(newParent);
        this._nodes = checkNotNull(nodes);
        this._shouldDuplicatePredecessorFunction = checkNotNull(shouldDuplicatePredecessorFunction);
    }

    @Override
    public void execute() {

        _node2FormerParentMap = this._nodes.stream().collect(Collectors.toMap(hgNode -> hgNode, HGNode::getParent));

        _nodes.forEach(node -> {

            var predecessorsToDuplicate = getReversedPredecessorsToDuplicate(node);
            
           // node.has
            

        });
    }

    @Override
    public void undo() {

    }

    private List<HGNode> getReversedPredecessorsToDuplicate(HGNode node) {
        List<HGNode> result = new ArrayList<>();
        for (HGNode predecessor : node.getPredecessors()) {
            if (_shouldDuplicatePredecessorFunction.apply(predecessor)) {
                result.add(predecessor);
            } else {
                break;
            }
        }
        Collections.reverse(result);
        return result;
    }
}
