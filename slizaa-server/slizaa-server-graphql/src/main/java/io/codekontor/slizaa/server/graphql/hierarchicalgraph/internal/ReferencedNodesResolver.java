package io.codekontor.slizaa.server.graphql.hierarchicalgraph.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.server.graphql.hierarchicalgraph.NodesToConsider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReferencedNodesResolver {

    /**
     * @param node
     * @param includePredecessors
     * @return
     */
    public static List<HGNode> getReferencedNodes(HGNode node, boolean includePredecessors) {
        return getReferencedNodes(Collections.singleton(node), includePredecessors);
    }

    /**
     * @param node
     * @param includePredecessors
     * @return
     */
    public static List<HGNode> getReferencingNodes(HGNode node, boolean includePredecessors) {
        return getReferencingNodes(Collections.singleton(node), includePredecessors);
    }

    /**
     * @param node
     * @param includePredecessors
     * @return
     */
    public static List<HGNode> getReferencedNodes(Collection<HGNode> node, boolean includePredecessors) {

        Stream<HGNode> nodeStream = node.stream()
                .flatMap(hgNode -> hgNode.getAccumulatedOutgoingCoreDependencies().stream()).map(dep -> dep.getTo());

        if (includePredecessors) {
            nodeStream = nodeStream.flatMap(n -> Stream.concat(n.getPredecessors().stream(), Stream.of(n)));
        }

        return nodeStream.distinct().collect(Collectors.toList());
    }

    /**
     * @param node
     * @param includePredecessors
     * @return
     */
    public static List<HGNode> getReferencingNodes(Collection<HGNode> node, boolean includePredecessors) {

        Stream<HGNode> nodeStream = node.stream()
                .flatMap(hgNode -> hgNode.getAccumulatedIncomingCoreDependencies().stream()).map(dep -> dep.getFrom());

        if (includePredecessors) {
            nodeStream = nodeStream.flatMap(n -> Stream.concat(n.getPredecessors().stream(), Stream.of(n)));
        }

        return nodeStream.distinct().collect(Collectors.toList());
    }

    public static List<HGNode> filterReferencedNodes(HGNode node, Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {
        return filterReferencedNodes(Collections.singleton(node), nodeIds, nodesToConsider, includePredecessorsInResult);
    }

    public static List<HGNode> filterReferencingNodes(HGNode node, Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {
        return filterReferencingNodes(Collections.singleton(node), nodeIds, nodesToConsider, includePredecessorsInResult);
    }

    public static List<HGNode> filterReferencedNodes(Collection<HGNode> nodes, Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {

        Set<Long> expandedNodeIds = checkNotNull(nodeIds).stream().map(id -> Long.parseLong(id)).collect(Collectors.toSet());

        List<HGNode> result = checkNotNull(nodes).stream()
                .flatMap(hgNode -> hgNode.getAccumulatedOutgoingCoreDependencies().stream())
                .map(dep -> dep.getTo())
                .flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)))
                .filter(hgNode -> {
                    switch (nodesToConsider) {
                        case SELF: {
                            return expandedNodeIds.contains(hgNode.getIdentifier());
                        }
                        case SELF_AND_CHILDREN: {
                            return expandedNodeIds.contains(hgNode.getIdentifier()) || (hgNode.getParent() != null && expandedNodeIds.contains(hgNode.getParent().getIdentifier()));
                        }
                        case SELF_AND_SUCCESSORS: {
                            return expandedNodeIds.contains(hgNode.getIdentifier()) || hgNode.getPredecessors().stream().anyMatch(node ->  expandedNodeIds.contains(node.getIdentifier()));
                        }
                        default: {
                            return false;
                        }
                    }
                })
                .flatMap(node -> includePredecessorsInResult ? Stream.concat(node.getPredecessors().stream(), Stream.of(node)) : Stream.of(node))
                .distinct().collect(Collectors.toList());

        return result;
    }

    public static List<HGNode> filterReferencingNodes(Collection<HGNode> nodes, Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {

        Set<Long> expandedNodeIds = checkNotNull(nodeIds).stream().map(id -> Long.parseLong(id)).collect(Collectors.toSet());

        List<HGNode> result = checkNotNull(nodes).stream()
                .flatMap(hgNode -> hgNode.getAccumulatedIncomingCoreDependencies().stream())
                .map(dep -> dep.getFrom())
                .flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)))
                .filter(hgNode -> {
                    switch (nodesToConsider) {
                        case SELF: {
                            return expandedNodeIds.contains(hgNode.getIdentifier());
                        }
                        case SELF_AND_CHILDREN: {
                            return expandedNodeIds.contains(hgNode.getIdentifier()) || (hgNode.getParent() != null && expandedNodeIds.contains(hgNode.getParent().getIdentifier()));
                        }
                        case SELF_AND_SUCCESSORS: {
                            return expandedNodeIds.contains(hgNode.getIdentifier()) || hgNode.getPredecessors().stream().anyMatch(node ->  expandedNodeIds.contains(node.getIdentifier()));
                        }
                        default: {
                            return false;
                        }
                    }
                })
                .flatMap(node -> includePredecessorsInResult ? Stream.concat(node.getPredecessors().stream(), Stream.of(node)) : Stream.of(node))
                .distinct().collect(Collectors.toList());

        return result;
    }
}


