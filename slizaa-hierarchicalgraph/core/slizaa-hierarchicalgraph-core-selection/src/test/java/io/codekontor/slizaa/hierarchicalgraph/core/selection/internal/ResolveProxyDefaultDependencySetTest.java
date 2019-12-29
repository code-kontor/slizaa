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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResolveProxyDefaultDependencySetTest extends AbstractDefaultDependencySetTest {

    @Test
    public void testResolveProxyDependencyOnAggregatedDependency() {

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isFalse();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(0);

        aggregatedDependency().resolveProxyDependencies();

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isTrue();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(2);
    }

    @Test
    public void testResolveProxyDependencyOnProxyDependency() {

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isFalse();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(0);

        proxyDependency().resolve();

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isTrue();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(2);
    }

    @Test
    public void testResolveProxyDependencyOnDependencySet() {

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isFalse();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(0);

        dependencySet().resolveAllProxyDependencies();

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isTrue();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(2);
    }

    @Test
    public void testResolveProxyDependencyForSourceNode() {

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isFalse();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(0);

        dependencySet().resolveProxyDependencies(testGraph.a4(), SourceOrTarget.SOURCE);

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isTrue();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(2);
    }

    @Test
    public void testResolveProxyDependencyForTargetNode() {

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isFalse();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(0);

        dependencySet().resolveProxyDependencies(testGraph.b4(), SourceOrTarget.TARGET);

        assertThat(dependencySet().getUnfilteredCoreDependencies()).hasSize(6);
        assertThat(dependencySet().getUnfilteredCoreDependencies()).containsExactlyElementsOf(aggregatedDependency().getCoreDependencies());
        assertThat(proxyDependency().isResolved()).isTrue();
        assertThat(proxyDependency().getAccumulatedCoreDependencies()).hasSize(2);
    }
}
