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
package io.codekontor.slizaa.hierarchicalgraph.core.selection;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DependencySetTest extends AbstractSelectionsTest {

    private HGAggregatedDependency aggregatedDependency;
    private DependencySet dependencySet;

    @Before
    public void initialize() {
        aggregatedDependency = dependency(20483, 7676);
        dependencySet = new DependencySet(aggregatedDependency.getCoreDependencies());
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
        assertThat(dependencySet.getUnfilteredSourceNodes(true)).containsAll(sourceNodesWithPredecessors);
    }

    @Test
    public void testUnfilteredTargetNodes() {

        Set<HGNode> targetNodesWithPredecessors = aggregatedDependency.getCoreDependencies().stream()
                .flatMap(dep -> NodeSelections.getPredecessors(dep.getTo(), true).stream())
                .collect(Collectors.toSet());
        assertThat(dependencySet.getUnfilteredTargetNodes(true)).containsAll(targetNodesWithPredecessors);
    }

    @Test
    public void testFilteredDependencies() {

        DependencySet.ReferencedNodes referencedNodes = dependencySet.computeReferencedNodes(node(7193), SourceOrTarget.SOURCE);
        assertThat(referencedNodes.getFilteredCoreDependencies()).containsOnlyElementsOf(dependencySet.getUnfilteredCoreDependencies().stream().filter(dep -> dep.getFrom().getIdentifier().equals(Long.valueOf(7193))).collect(Collectors.toList()));
        assertThat(referencedNodes.getSelectedNodesWithSuccessorsAndPredecessors()).containsExactlyInAnyOrder(node(7193),
                node(20483),node(577), rootNode());

        assertThat(referencedNodes.getFilteredNodes(false)).containsExactlyInAnyOrder(node(6518),node(7544),node(8075));
    }
}
