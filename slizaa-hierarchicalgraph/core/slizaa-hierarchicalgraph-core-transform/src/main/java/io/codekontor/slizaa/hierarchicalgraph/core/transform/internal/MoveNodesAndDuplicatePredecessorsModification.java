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
