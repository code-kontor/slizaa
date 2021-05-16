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
