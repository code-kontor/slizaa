package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

import java.util.Collections;
import java.util.List;

public class DependencySelection {

    public NodeSet childrenFilteredByDependencySources(String node) {
        return new NodeSet(Collections.emptyList());
    }

    public NodeSet childrenFilteredByDependencyTargets(String node) {
        return new NodeSet(Collections.emptyList());
    }

    public NodeSet referencedSourceNodes(List<String> selectedTargetNodes) {
        return new NodeSet(Collections.emptyList());
    }

    public NodeSet referencedTargetNodes(List<String> selectedSourceNodes) {
        return new NodeSet(Collections.emptyList());
    }
}
