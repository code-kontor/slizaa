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

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnfilteredNodesDefaultDependencySetTest extends AbstractDefaultDependencySetTest {

    @Test
    public void testUnfilteredSourceNodes() {

        assertThat(dependencySet().getUnfilteredSourceNodes(true, false))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.a1(), testGraph.a2(), testGraph.a22(), testGraph.a3());

        dependencySet().resolveAllProxyDependencies();

        assertThat(dependencySet().getUnfilteredSourceNodes(true, false))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.a1(), testGraph.a2(), testGraph.a22(), testGraph.a3());

        assertThat(dependencySet().getUnfilteredSourceNodes(true, true))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.a1(), testGraph.a2(), testGraph.a22(), testGraph.a3(), testGraph.a4());
    }

    @Test
    public void testUnfilteredTargetNodes() {

        assertThat(dependencySet().getUnfilteredTargetNodes(true, false))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b22(), testGraph.b3());

        dependencySet().resolveAllProxyDependencies();

        assertThat(dependencySet().getUnfilteredTargetNodes(true, false))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b22(), testGraph.b3());

        assertThat(dependencySet().getUnfilteredTargetNodes(true, true))
                .containsExactlyInAnyOrder(testGraph.root(), testGraph.b1(), testGraph.b2(), testGraph.b22(), testGraph.b3(), testGraph.b4());
    }
}
