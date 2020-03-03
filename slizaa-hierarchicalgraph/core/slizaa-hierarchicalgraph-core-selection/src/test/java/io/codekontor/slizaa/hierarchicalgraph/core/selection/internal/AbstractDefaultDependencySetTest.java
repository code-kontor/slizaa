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

import io.codekontor.slizaa.hierarchicalgraph.core.model.*;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.SimpleTestModelRule;
import org.junit.Before;
import org.junit.Rule;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractDefaultDependencySetTest {

    @Rule
    public SimpleTestModelRule testGraph = new SimpleTestModelRule();

    private HGAggregatedDependency aggregatedDependency;

    private HGProxyDependency proxyDependency;

    private DefaultDependencySet dependencySet;

    @Before
    public void initialize() {
        aggregatedDependency = testGraph.a1().getOutgoingDependenciesTo(testGraph.b1());
        proxyDependency = testGraph.a3_b3_proxy1();
        dependencySet = new DefaultDependencySet(aggregatedDependency.getCoreDependencies());
    }

    protected SimpleTestModelRule testGraph() {
        return testGraph;
    }

    protected HGAggregatedDependency aggregatedDependency() {
        return aggregatedDependency;
    }

    protected HGProxyDependency proxyDependency() {
        return proxyDependency;
    }

    protected DefaultDependencySet dependencySet() {
        return dependencySet;
    }

    /**
     *
     * @param filteredDependencies
     * @param selectedNodesWithSuccessorsAndPredecessors
     * @param selectedNodesType
     * @param filteredCoreDependencies
     * @param filteredNodesWithPredecessors
     * @param filteredNodes
     */
    protected void assertFilteredDependencies(IFilteredDependencies filteredDependencies,
                                              HGNode[] selectedNodesWithSuccessorsAndPredecessors,
                                              SourceOrTarget selectedNodesType,
                                              HGCoreDependency[] filteredCoreDependencies,
                                              HGNode[] filteredNodesWithPredecessors,
                                              HGNode[] filteredNodes) {

        SourceOrTarget referencedNodesType = selectedNodesType.equals(SourceOrTarget.SOURCE) ? SourceOrTarget.TARGET : SourceOrTarget.SOURCE;

        assertThat(filteredDependencies.getEffectiveNodes(selectedNodesType, true).containsAll(Arrays.asList(selectedNodesWithSuccessorsAndPredecessors)));

        assertThat(filteredDependencies.getEffectiveCoreDependencies()).containsExactlyInAnyOrder(filteredCoreDependencies);

        assertThat(filteredDependencies.getEffectiveNodes(referencedNodesType, true))
                .containsExactlyInAnyOrder(filteredNodesWithPredecessors);

        assertThat(filteredDependencies.getEffectiveNodes(referencedNodesType, false))
                .containsExactlyInAnyOrder(filteredNodes);
    }
}
