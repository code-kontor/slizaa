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

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IFilteredDependencies;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.INodeSelection;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferencedNodesDefaultDependencySetTest extends AbstractDefaultDependencySetTest {

    @Test
    public void referencedTargetNodes_a1() {

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a1(), SourceOrTarget.SOURCE), false);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.a1()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{testGraph.a1_b1_core1(), testGraph.a1_b1_core2(), testGraph.a2_b2_core1(), testGraph.a2_b22_core1(), testGraph.a22_b22_core1(), testGraph.a3_b3_proxy1()},
                new HGNode[]{testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b22(), testGraph.b3()},
                new HGNode[]{testGraph.b1(), testGraph.b2(), testGraph.b22(), testGraph.b3()}
        );
    }

    @Test
    public void referencedTargetNodes_a2() {

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a2(), SourceOrTarget.SOURCE), false);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.a2()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{testGraph.a2_b2_core1(), testGraph.a2_b22_core1(), testGraph.a3_b3_proxy1()},
                new HGNode[]{testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b22(), testGraph.b3()},
                new HGNode[]{testGraph.b2(), testGraph.b22(), testGraph.b3()}
        );
    }

    @Test
    public void referencedTargetNodes_a22() {

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a22(), SourceOrTarget.SOURCE), false);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.a22()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{testGraph.a22_b22_core1()},
                new HGNode[]{testGraph.root(), testGraph.b1(), testGraph.b22()},
                new HGNode[]{testGraph.b22()}
        );
    }

    @Test
    public void referencedTargetNodes_a3() {

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a3(), SourceOrTarget.SOURCE), false);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.a3()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{testGraph.a3_b3_proxy1()},
                new HGNode[]{testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b3()},
                new HGNode[]{testGraph.b3()}
        );
    }

    @Test
    @Ignore
    public void referencedTargetNodes_a4() {

        this.proxyDependency().resolve();

        // compute the referenced nodes
        IFilteredDependencies filteredDependencies = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a4(), SourceOrTarget.SOURCE), true);

        // assertions
        assertFilteredDependencies(
                filteredDependencies,
                new HGNode[]{testGraph.a4()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{},
                new HGNode[]{testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b3(), testGraph.b4()},
                new HGNode[]{testGraph.b4()}
        );
    }

    @Test
    public void referencedTargetNodes_a4_unresolved() {

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a4(), SourceOrTarget.SOURCE), true);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.a4()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{},
                new HGNode[]{},
                new HGNode[]{}
        );
    }

    @Test
    public void referencedTargetNodes_a4_resolvedWithoutResolvedProxyDependencies() {

        this.proxyDependency().resolve();

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a4(), SourceOrTarget.SOURCE), false);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.a4()},
                SourceOrTarget.SOURCE,
                new HGCoreDependency[]{},
                new HGNode[]{},
                new HGNode[]{}
        );
    }

    @Test
    public void testComputeReferencedTargetNodes2() {

        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.a3(), SourceOrTarget.SOURCE), false);

        assertThat(referencedNodes.getNodes(SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b3());
        assertThat(referencedNodes.getNodes(SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b3());
    }

    @Test
    public void referencedSourceNodes_b1() {

        // compute the referenced nodes
        IFilteredDependencies filteredDependencies = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.b1(), SourceOrTarget.TARGET), false);

        // assertions
        assertFilteredDependencies(
                filteredDependencies,
                new HGNode[]{testGraph.b1()},
                SourceOrTarget.TARGET,
                new HGCoreDependency[]{testGraph.a1_b1_core1(), testGraph.a1_b1_core2(), testGraph.a2_b2_core1(), testGraph.a2_b22_core1(), testGraph.a22_b22_core1(), testGraph.a3_b3_proxy1()},
                new HGNode[]{testGraph.root(), testGraph.a1(), testGraph.a2(), testGraph.a22(), testGraph.a3()},
                new HGNode[]{testGraph.a1(), testGraph.a2(), testGraph.a22(), testGraph.a3()}
        );
    }

    @Test
    public void referencedSourceNodes_b3() {

        // compute the referenced nodes
        IFilteredDependencies referencedNodes = dependencySet().getFilteredDependencies(INodeSelection.create(testGraph.b3(), SourceOrTarget.TARGET), false);

        // assertions
        assertFilteredDependencies(
                referencedNodes,
                new HGNode[]{testGraph.b3()},
                SourceOrTarget.TARGET,
                new HGCoreDependency[]{testGraph.a3_b3_proxy1()},
                new HGNode[]{testGraph.root(), testGraph.a1(), testGraph.a2(), testGraph.a3()},
                new HGNode[]{testGraph.a3()}
        );
    }
}
