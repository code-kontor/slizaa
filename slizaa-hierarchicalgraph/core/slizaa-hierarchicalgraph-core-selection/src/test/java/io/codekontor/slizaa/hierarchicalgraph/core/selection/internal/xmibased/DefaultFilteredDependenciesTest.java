/**
 * slizaa-hierarchicalgraph-core-selection - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.hierarchicalgraph.core.selection.internal.xmibased;

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.INodeSelection;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.NodeSelections;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.internal.DefaultDependencySet;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultFilteredDependenciesTest {

    @ClassRule
    public static XmiBasedTestGraphProviderRule _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.EUREKA);

    private HGAggregatedDependency aggregatedDependency;

    private DefaultDependencySet dependencySet;
    private HGNode selectedSourceNode;
    private HGNode selectedTargetNode;

    private INodeSelection sourceSelection;
    private INodeSelection targetSelection;

    @Before
    public void initialize() {

        // dump the dependency matrix
        // dumpDependencyMatrix(_graphProvider.rootNode().getChildren());

        aggregatedDependency = _graphProvider.node(109767).getOutgoingDependenciesTo(_graphProvider.node(149054));
        dependencySet = new DefaultDependencySet(aggregatedDependency.getCoreDependencies());

        // dump the dependencies
        // dumpDependencies(dependencySet.getUnfilteredCoreDependencies());

        selectedSourceNode = _graphProvider.node(109795);
        selectedTargetNode = _graphProvider.node(149851);

        sourceSelection = INodeSelection.create(selectedSourceNode, SourceOrTarget.SOURCE);
        targetSelection = INodeSelection.create(selectedTargetNode, SourceOrTarget.TARGET);
    }

    @Test
    public void testUnfilteredSourceNodes() {

        HGNode[] expectedUnfilteredSourceNodes = dependencySet.getUnfilteredCoreDependencies().stream().
                flatMap(dep -> Stream.concat(Stream.of(dep.getFrom()), dep.getFrom().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);

        Set<HGNode> unfilteredSourceNodes = dependencySet.getUnfilteredSourceNodes(true, false);
        assertThat(unfilteredSourceNodes).containsExactlyInAnyOrder(expectedUnfilteredSourceNodes);

        // fetch the selected nodes with successors
        Set<HGNode> selectedNodesWithSuccessors = Stream.of(selectedSourceNode)
                .flatMap(node -> NodeSelections.getSuccessors(node, true).stream())
                .filter(n -> unfilteredSourceNodes.contains(n))
                .collect(Collectors.toSet());
    }

    @Test
    public void testFilteredDependencies_1() {

        //
        HGCoreDependency[] expectedEffectiveDependencies = dependencySet.getUnfilteredCoreDependencies().stream().
                filter(dep -> dep.getFrom().equals(selectedSourceNode) || dep.getFrom().isSuccessorOf(selectedSourceNode)).
                distinct().
                toArray(HGCoreDependency[]::new);

        HGNode[] expectedEffectiveSourceNodes = Arrays.stream(expectedEffectiveDependencies).
                flatMap(dep -> Stream.concat(Stream.of(dep.getFrom()), dep.getFrom().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);


        HGNode[] expectedEffectiveTargetNodes = Arrays.stream(expectedEffectiveDependencies).
                flatMap(dep -> Stream.concat(Stream.of(dep.getTo()), dep.getTo().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);

        // get the filtered dependencies
        IFilteredDependencies filteredDependencies = dependencySet.getFilteredDependencies(sourceSelection);

        // assertions
        assertThat(filteredDependencies.hasNodeSelections(SourceOrTarget.SOURCE)).isTrue();
        assertThat(filteredDependencies.hasNodeSelections(SourceOrTarget.TARGET)).isFalse();

        assertThat(filteredDependencies.getNodeSelections(SourceOrTarget.SOURCE)).containsExactlyInAnyOrder(sourceSelection);
        assertThat(filteredDependencies.getNodeSelections(SourceOrTarget.TARGET).isEmpty()).isTrue();

        assertThat(filteredDependencies.getEffectiveCoreDependencies()).containsExactlyInAnyOrder(expectedEffectiveDependencies);

        assertThat(filteredDependencies.getEffectiveNodes(SourceOrTarget.SOURCE, true)).containsExactlyInAnyOrder(expectedEffectiveSourceNodes);
        assertThat(filteredDependencies.getEffectiveNodes(SourceOrTarget.TARGET, true)).containsExactlyInAnyOrder(expectedEffectiveTargetNodes);

        assertThat(filteredDependencies.getReferencedNodes(SourceOrTarget.SOURCE, true).isEmpty()).isTrue();
        assertThat(filteredDependencies.getReferencedNodes(SourceOrTarget.TARGET, true)).containsExactlyInAnyOrder(expectedEffectiveTargetNodes);
    }

    @Test
    public void testFilteredDependencies_2() {

        //
        HGNode[] expectedSourceFilteredTargetNodes = dependencySet.getUnfilteredCoreDependencies().stream().
                filter(dep -> dep.getFrom().equals(selectedSourceNode) || dep.getFrom().isSuccessorOf(selectedSourceNode)).
                distinct().
                flatMap(dep -> Stream.concat(Stream.of(dep.getTo()), dep.getTo().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);

        HGNode[] expectedTargetFilteredSourceNodes = dependencySet.getUnfilteredCoreDependencies().stream().
                filter(dep -> dep.getTo().equals(selectedTargetNode) || dep.getTo().isSuccessorOf(selectedTargetNode)).
                distinct().
                flatMap(dep -> Stream.concat(Stream.of(dep.getFrom()), dep.getFrom().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);

        HGCoreDependency[] expectedEffectiveDependencies = dependencySet.getUnfilteredCoreDependencies().stream().
                filter(dep -> dep.getFrom().equals(selectedSourceNode) || dep.getFrom().isSuccessorOf(selectedSourceNode)).
                filter(dep -> dep.getTo().equals(selectedTargetNode) || dep.getTo().isSuccessorOf(selectedTargetNode)).
                distinct().
                toArray(HGCoreDependency[]::new);

        HGNode[] expectedEffectiveSourceNodes = Arrays.stream(expectedEffectiveDependencies).
                flatMap(dep -> Stream.concat(Stream.of(dep.getFrom()), dep.getFrom().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);


        HGNode[] expectedEffectiveTargetNodes = Arrays.stream(expectedEffectiveDependencies).
                flatMap(dep -> Stream.concat(Stream.of(dep.getTo()), dep.getTo().getPredecessors().stream())).
                distinct().
                toArray(HGNode[]::new);


        // get the filtered dependencies
        IFilteredDependencies filteredDependencies = dependencySet.getFilteredDependencies(sourceSelection, targetSelection);

        // assertions
        assertThat(filteredDependencies.hasNodeSelections(SourceOrTarget.SOURCE)).isTrue();
        assertThat(filteredDependencies.hasNodeSelections(SourceOrTarget.TARGET)).isTrue();

        assertThat(filteredDependencies.getNodeSelections(SourceOrTarget.SOURCE)).containsExactlyInAnyOrder(sourceSelection);
        assertThat(filteredDependencies.getNodeSelections(SourceOrTarget.TARGET)).containsExactlyInAnyOrder(targetSelection);

        assertThat(filteredDependencies.getEffectiveCoreDependencies()).containsExactlyInAnyOrder(expectedEffectiveDependencies);

        assertThat(filteredDependencies.getEffectiveNodes(SourceOrTarget.SOURCE, true)).containsExactlyInAnyOrder(expectedEffectiveSourceNodes);
        assertThat(filteredDependencies.getEffectiveNodes(SourceOrTarget.TARGET, true)).containsExactlyInAnyOrder(expectedEffectiveTargetNodes);

        assertThat(filteredDependencies.getReferencedNodes(SourceOrTarget.SOURCE, true)).containsExactlyInAnyOrder(expectedTargetFilteredSourceNodes);
        assertThat(filteredDependencies.getReferencedNodes(SourceOrTarget.TARGET, true)).containsExactlyInAnyOrder(expectedSourceFilteredTargetNodes);
    }

    /**
     * @param id
     * @return
     */
    private HGNode node(long id) {
        return _graphProvider.node(id);
    }

    private void dumpDependencyMatrix(Collection<HGNode> nodes) {
        IDependencyStructureMatrix matrix = GraphUtils.createDependencyStructureMatrix(nodes);
        List<HGNode> orderedNodes = matrix.getOrderedNodes();
        for (int i = 0; i < orderedNodes.size(); i++) {
            for (int j = 0; j < orderedNodes.size(); j++) {
                System.out.println(orderedNodes.get(i).getIdentifier() + " -> " + orderedNodes.get(j).getIdentifier() + " :" + matrix.getWeight(i, j));
            }
        }
    }

    private void dumpDependencies(Collection<HGCoreDependency> dependencies) {
        dependencies.forEach(dep -> System.out.printf("%s [%s] - %s [%s]\n",
                dep.getFrom().getIdentifier(),
                dep.getFrom().getPredecessors().stream().map(n -> n.getIdentifier()).collect(Collectors.toList()),
                dep.getTo().getIdentifier(),
                dep.getTo().getPredecessors().stream().map(n -> n.getIdentifier()).collect(Collectors.toList())));
    }
}
