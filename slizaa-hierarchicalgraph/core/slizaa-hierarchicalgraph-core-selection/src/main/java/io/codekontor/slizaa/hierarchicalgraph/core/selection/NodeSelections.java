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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.util.EcoreUtil;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * Utility methods for working with node selections.
 */
public class NodeSelections {

    /**
     *
     * @param nodes
     * @param includeSelf
     * @return
     */
    public static Set<HGNode> getSuccessors(Collection<HGNode> nodes, boolean includeSelf) {

        checkNotNull(nodes);

        final Set<HGNode> result = new HashSet<>();

        nodes.forEach((node) -> {
            if (includeSelf) {
                result.add(node);
            }
            EcoreUtil.getAllContents(node, false).forEachRemaining((child) -> {
                if (child instanceof HGNode) {
                    result.add((HGNode) child);
                }
            });
        });

        return result;
    }

    /**
     *
     * @param node
     * @param includeSelf
     * @return
     */
    public static Set<HGNode> getSuccessors(HGNode node, boolean includeSelf) {
        return getSuccessors(Collections.singleton(checkNotNull(node)), includeSelf);
    }

    /**
     *
     * @param nodes
     * @param includeSelf
     * @return
     */
    public static Set<HGNode> getPredecessors(Collection<HGNode> nodes, boolean includeSelf) {
        return checkNotNull(nodes).stream()
                .flatMap(node -> includeSelf ? Stream.concat(node.getPredecessors().stream(), Stream.of(node)) : node.getPredecessors().stream())
                .collect(Collectors.toSet());
    }

    /**
     *
     * @param node
     * @param includeSelf
     * @return
     */
    public static Set<HGNode> getPredecessors(HGNode node, boolean includeSelf) {
        return getPredecessors(Collections.singleton(checkNotNull(node)), includeSelf);
    }

    /**
     * <p>
     * </p>
     *
     * @param nodes
     * @return
     */
    public static Set<HGCoreDependency> getAccumulatedOutgoingCoreDependencies(Collection<HGNode> nodes) {
        return checkNotNull(nodes).stream().flatMap(node -> node.getAccumulatedOutgoingCoreDependencies().stream())
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * </p>
     *
     * @param nodes
     * @return
     */
    public static Set<HGCoreDependency> getAccumulatedIncomingCoreDependencies(Collection<HGNode> nodes) {
        return checkNotNull(nodes).stream().flatMap(node -> node.getAccumulatedIncomingCoreDependencies().stream())
                .collect(Collectors.toSet());
    }
}
