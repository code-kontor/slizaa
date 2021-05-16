package io.codekontor.slizaa.hierarchicalgraph.core.transform.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.transform.IHierarchicalGraphModification;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateNodeModification implements IHierarchicalGraphModification {

    private HGNode _parent;

    private Supplier<HGNode> _nodeSupplier;

    private HGNode _newNode;

    public CreateNodeModification(HGNode parent, Supplier<HGNode> nodeSupplier) {
        this._parent = checkNotNull(parent);
        this._nodeSupplier = checkNotNull(nodeSupplier);
    }

    @Override
    public void execute() {
        _newNode = checkNotNull(_nodeSupplier.get());
        _parent.getChildren().add(_newNode);
    }

    @Override
    public void undo() {
        _parent.getChildren().remove(_newNode);
        _newNode = null;
    }
}
