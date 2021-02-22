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
package io.codekontor.slizaa.server.gql.hierarchicalgraph.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import io.codekontor.slizaa.server.gql.hierarchicalgraph.Node;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class NodeUtils {

    public static List<Node> toNodes(Collection<HGNode> nodes, boolean sorted) {
        Collection<HGNode> result = sorted ? sorted(checkNotNull(nodes)) : checkNotNull(nodes);
        return result.stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
    }

    public static List<String> toNodeIds(Collection<HGNode> nodes, boolean sorted) {
        Collection<HGNode> result = sorted ? sorted(checkNotNull(nodes)) : checkNotNull(nodes);
        return result.stream().map(hgNode -> hgNode.getIdentifier().toString()).collect(Collectors.toList());
    }

    private static List<HGNode> sorted(Collection<HGNode> nodes) {
        try {
            if (nodes != null && !nodes.isEmpty()) {
                HGRootNode rootNode = nodes.iterator().next().getRootNode();
                INodeComparator nodeComparator = rootNode.getExtension(INodeComparator.class);
                if (nodeComparator == null) {
                    return new LinkedList<>(nodes);
                }
                return nodes.stream().sorted(new Comparator<HGNode>() {
                    @Override
                    public int compare(HGNode node1, HGNode node2) {
                        int category1 = nodeComparator.category(node1);
                        int category2 = nodeComparator.category(node2);
                        if (category1 == category2) {
                            return nodeComparator.compare(node1, node2);
                        } else {
                            return category1 - category2;
                        }
                    }
                }).collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(nodes);
            throw e;
        }
    }
}
