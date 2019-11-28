/**
 * slizaa-hierarchicalgraph-core-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;

public class HierarchicalGraphUtils {

  public static List<HGNode> sorted(Collection<HGNode> nodes) {

    if (nodes == null || nodes.isEmpty()) {
      return Collections.emptyList();
    }

    HGRootNode rootNode = nodes.iterator().next().getRootNode();
    INodeComparator nodeComparator = rootNode.getExtension(INodeComparator.class);

    if (nodeComparator == null) {
        return Collections.emptyList();
    }

    return nodes.stream().sorted((node1, node2) -> {
      int category1 = nodeComparator.category(node1);
      int category2 = nodeComparator.category(node2);
      if (category1 == category2) {
        return nodeComparator.compare(node1, node2);
      } else {
        return category1 - category2;
      }
    }).collect(Collectors.toList());
  }
}
