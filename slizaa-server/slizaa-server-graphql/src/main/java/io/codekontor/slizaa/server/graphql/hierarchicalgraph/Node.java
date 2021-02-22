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
package io.codekontor.slizaa.server.gql.hierarchicalgraph;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import io.codekontor.slizaa.server.gql.hierarchicalgraph.internal.ReferencedNodesResolver;
import io.codekontor.slizaa.server.gql.hierarchicalgraph.internal.Utils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Node {

    // the text of the node
    private String text;

    //
    private final transient HGNode hgNode;

    //
    private final transient ILabelDefinitionProvider labelDefinitionProvider;

    /**
     *
     */
    public Node(HGNode hgNode) {
        this.hgNode = checkNotNull(hgNode);

        //
        labelDefinitionProvider = Utils.getLabelDefinitionProvider(hgNode);
    }

    /**
     * @return
     */
    public String getId() {
        return hgNode.getIdentifier().toString();
    }

    /**
     * @return
     */
    public String getText() {
        if (text == null) {
            text = labelDefinitionProvider.getLabelDefinition(hgNode).getText();
        }
        return text;
    }

    /**
     * @return
     */
    public Optional<Node> getParent() {
        return getHgNode().getParent() != null
                ? Optional.of(new Node(getHgNode().getParent()))
                : Optional.empty();
    }

    /**
     * @return
     */
    public NodeSet getChildren() {
        return new NodeSet(getHgNode().getChildren());
    }

    public boolean hasChildren() {
        return !getHgNode().getChildren().isEmpty();
    }

    public NodeSet getChildrenFilteredByReferencedNodes(List<String> referencedNodeIds) {

        Set<HGNode> referencedNodes = checkNotNull(referencedNodeIds).stream()
                .map(id -> getHgNode().getRootNode().lookupNode(Long.parseLong(id)))
                .filter(n -> n != null)
                .collect(Collectors.toSet());

        List<HGNode> filteredChildren = getHgNode().getChildren().stream()
                .filter(child -> child.getAccumulatedOutgoingCoreDependencies().stream().parallel()
                // .filter(child -> TransitiveDependencyResolver.getTransitiveAccumulatedOutgoingCoreDependencies(child).stream().parallel()
                        .map(dep -> dep.getTo())
                        .filter(node -> referencedNodes.contains(node) || node.getPredecessors().stream().anyMatch(referencedNodes::contains))
                        .findAny().isPresent())
                .collect(Collectors.toList());

        return new NodeSet(filteredChildren);
    }

    public NodeSet getChildrenFilteredByReferencingNodes(List<String> referencingNodeIds) {

        Set<HGNode> referencingNodes = checkNotNull(referencingNodeIds).stream()
                .map(id -> getHgNode().getRootNode().lookupNode(Long.parseLong(id)))
                .filter(n -> n != null)
                .collect(Collectors.toSet());

        List<HGNode> filteredChildren = getHgNode().getChildren().stream()
                  .filter(child -> child.getAccumulatedIncomingCoreDependencies().stream().parallel()
                  // .filter(child -> TransitiveDependencyResolver.getTransitiveAccumulatedIncomingCoreDependencies(child).stream().parallel()
                        .map(dep -> dep.getFrom())
                        .filter(node -> referencingNodes.contains(node) || node.getPredecessors().stream().anyMatch(referencingNodes::contains))
                        .findAny().isPresent())
                .collect(Collectors.toList());

        return new NodeSet(filteredChildren);
    }

    /**
     * @return
     */
    public List<Node> getPredecessors() {
        return getHgNode().getPredecessors().stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
    }

    /**
     * @return
     */
    public List<MapEntry> getProperties() {
        GraphDbNodeSource nodeSource = getHgNode().getNodeSource(GraphDbNodeSource.class).get();
        return nodeSource.getProperties().stream().map(entry -> new MapEntry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<Dependency> getDependenciesTo(List<String> targetNodeIds) {

        //
        HGRootNode rootNode = getHgNode().getRootNode();
        List<HGNode> targetNodes = targetNodeIds.stream().map(id -> rootNode.lookupNode(Long.parseLong(id))).filter(n -> n != null).collect(Collectors.toList());
        List<HGAggregatedDependency> dependenciesTo = getHgNode().getOutgoingDependenciesTo(targetNodes);

        //
        return dependenciesTo.stream().map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }

    public List<Dependency> getDependenciesFrom(List<String> sourceNodeIds) {

        //
        HGRootNode rootNode = getHgNode().getRootNode();
        List<HGNode> sourceNodes = sourceNodeIds.stream().map(id -> rootNode.lookupNode(Long.parseLong(id))).filter(n -> n != null).collect(Collectors.toList());
        List<HGAggregatedDependency> dependenciesFrom = getHgNode().getIncomingDependenciesFrom(sourceNodes);

        //
        return dependenciesFrom
                .stream().map(dep -> new Dependency(dep)).collect(Collectors.toList());
    }

    public NodeSet referencedNodes(boolean includePredecessors) {
        return new NodeSet(ReferencedNodesResolver.getReferencedNodes(getHgNode(), includePredecessors));
    }

    public NodeSet referencingNodes(boolean includePredecessors) {
        return new NodeSet(ReferencedNodesResolver.getReferencingNodes(getHgNode(), includePredecessors));
    }

    public NodeSet filterReferencedNodes(Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {
        return new NodeSet(ReferencedNodesResolver.filterReferencedNodes(getHgNode(), nodeIds, nodesToConsider, includePredecessorsInResult));
    }

    public NodeSet filterReferencingNodes(Collection<String> nodeIds, NodesToConsider nodesToConsider, boolean includePredecessorsInResult) {
        return new NodeSet(ReferencedNodesResolver.filterReferencingNodes(getHgNode(), nodeIds, nodesToConsider, includePredecessorsInResult));
    }

    /**
     * @return
     */
    ILabelDefinitionProvider labelDefinitionProvider() {
        return labelDefinitionProvider;
    }

    /**
     * @return
     */
    HGNode getHgNode() {
        return hgNode;
    }
}
