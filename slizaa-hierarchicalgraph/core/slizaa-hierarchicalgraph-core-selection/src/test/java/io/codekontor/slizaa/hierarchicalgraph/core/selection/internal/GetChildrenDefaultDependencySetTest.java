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

import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.IReferencedNodes;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class GetChildrenDefaultDependencySetTest extends AbstractDefaultDependencySetTest {

    @Test
    public void testGetSourceNodeChildrenWithoutPreResolve() {

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.SOURCE, false))
                .containsExactlyInAnyOrder(testGraph.a1());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a1(), SourceOrTarget.SOURCE, false))
                .containsExactlyInAnyOrder(testGraph.a2(), testGraph.a22());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a2(), SourceOrTarget.SOURCE, false))
                .containsExactlyInAnyOrder(testGraph.a3());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a3(), SourceOrTarget.SOURCE, false))
                .isEmpty();

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a1());
        assertThat(this.proxyDependency().isResolved()).isFalse();
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a1(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a2(), testGraph.a22());
        assertThat(this.proxyDependency().isResolved()).isFalse();
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a2(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a3());
        assertThat(this.proxyDependency().isResolved()).isFalse();
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a3(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a4());
        assertThat(this.proxyDependency().isResolved()).isTrue();
    }

    @Test
    public void testGetTargetNodeChildrenWithoutPreResolve() {

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b1());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b1(), SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b2(), testGraph.b22());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b2(), SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b3());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b3(), SourceOrTarget.TARGET, false))
                .isEmpty();

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b1());
        assertThat(this.proxyDependency().isResolved()).isFalse();
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b1(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b2(), testGraph.b22());
        assertThat(this.proxyDependency().isResolved()).isFalse();
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b2(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b3());
        assertThat(this.proxyDependency().isResolved()).isFalse();
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b3(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b4());
        assertThat(this.proxyDependency().isResolved()).isTrue();
    }

    @Test
    public void testGetChildrenWithPreResolve() {

        dependencySet().resolveAllProxyDependencies();

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.SOURCE, false))
                .containsExactlyInAnyOrder(testGraph.a1());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a1(), SourceOrTarget.SOURCE, false))
                .containsExactlyInAnyOrder(testGraph.a2(), testGraph.a22());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a2(), SourceOrTarget.SOURCE, false))
                .containsExactlyInAnyOrder(testGraph.a3());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a3(), SourceOrTarget.SOURCE, false))
                .isEmpty();

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a1());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a1(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a2(), testGraph.a22());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a2(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a3());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.a3(), SourceOrTarget.SOURCE, true))
                .containsExactlyInAnyOrder(testGraph.a4());

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b1());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b1(), SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b2(), testGraph.b22());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b2(), SourceOrTarget.TARGET, false))
                .containsExactlyInAnyOrder(testGraph.b3());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b3(), SourceOrTarget.TARGET, false))
                .isEmpty();

        assertThat(dependencySet().getFilteredNodeChildren(testGraph.root(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b1());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b1(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b2(), testGraph.b22());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b2(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b3());
        assertThat(dependencySet().getFilteredNodeChildren(testGraph.b3(), SourceOrTarget.TARGET, true))
                .containsExactlyInAnyOrder(testGraph.b4());
    }
}
