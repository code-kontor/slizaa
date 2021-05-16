package io.codekontor.slizaa.hierarchicalgraph.core.transform;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.transform.internal.CreateNodeModification;

import java.util.function.Supplier;

public class TransformFactory {

    public static IHierarchicalGraphModification createNodeModification(HGNode parent, Supplier<HGNode> nodeSupplier) {
        return new CreateNodeModification(parent, nodeSupplier);
    }
}
