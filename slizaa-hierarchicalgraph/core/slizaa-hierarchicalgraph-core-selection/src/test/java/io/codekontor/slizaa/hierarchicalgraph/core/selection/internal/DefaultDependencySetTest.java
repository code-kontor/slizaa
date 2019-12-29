/**
 * slizaa-hierarchicalgraph-core-selection - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.selection.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.NodeSelections;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IReferencedNodes;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultDependencySetTest {

    @ClassRule
    public static XmiBasedTestGraphProviderRule _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.MAP_STRUCT);

    private HGAggregatedDependency aggregatedDependency;

    private DefaultDependencySet dependencySet;

    @Before
    public void initialize() {
        aggregatedDependency = _graphProvider.node(20483).getOutgoingDependenciesTo(_graphProvider.node(7676));
        dependencySet = new DefaultDependencySet(aggregatedDependency.getCoreDependencies());
    }

    @Test
    public void testUnfilteredDependencies() {
        assertThat(dependencySet.getUnfilteredCoreDependencies()).hasSize(9);
        assertThat(dependencySet.getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency.getCoreDependencies());
    }

    @Test
    public void testUnfilteredSourceNodes() {

        Set<HGNode> sourceNodesWithPredecessors = aggregatedDependency.getCoreDependencies().stream()
                .flatMap(dep -> NodeSelections.getPredecessors(dep.getFrom(), true).stream())
                .collect(Collectors.toSet());
        assertThat(dependencySet.getUnfilteredSourceNodes(true, false)).containsAll(sourceNodesWithPredecessors);
    }

    @Test
    public void testUnfilteredTargetNodes() {

        Set<HGNode> targetNodesWithPredecessors = aggregatedDependency.getCoreDependencies().stream()
                .flatMap(dep -> NodeSelections.getPredecessors(dep.getTo(), true).stream())
                .collect(Collectors.toSet());
        assertThat(dependencySet.getUnfilteredTargetNodes(true, false)).containsAll(targetNodesWithPredecessors);
    }

    @Test
    public void testFilteredDependencies() {

        IReferencedNodes referencedNodes = dependencySet.computeReferencedNodes(Collections.singleton(node(7193)), SourceOrTarget.SOURCE, false);
        assertThat(referencedNodes.getSelectedCoreDependencies()).containsOnlyElementsOf(dependencySet.getUnfilteredCoreDependencies().stream().filter(dep -> dep.getFrom().getIdentifier().equals(Long.valueOf(7193))).collect(Collectors.toList()));
        assertThat(referencedNodes.getReferencedNodes()).containsExactlyInAnyOrder(node(7193));

        assertThat(referencedNodes.getReferencedNodes(false)).containsExactlyInAnyOrder(node(6518), node(7544), node(8075));
    }

    /**
     *
     * @param id
     * @return
     */
    private HGNode node(long id) {
        return _graphProvider.node(id);
    }
}