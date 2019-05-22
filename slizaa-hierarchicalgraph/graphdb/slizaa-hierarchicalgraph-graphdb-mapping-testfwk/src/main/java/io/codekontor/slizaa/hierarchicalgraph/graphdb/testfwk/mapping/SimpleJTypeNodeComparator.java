/**
 * slizaa-hierarchicalgraph-graphdb-mapping-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.mapping;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;

public class SimpleJTypeNodeComparator implements INodeComparator {

  /**
   * {@inheritDoc}
   */
  @Override
  public int category(Object element) {

    //
    if (element instanceof HGNode) {
      HGNode hgNode = (HGNode) element;
      GraphDbNodeSource nodeSource = (GraphDbNodeSource) hgNode.getNodeSource();

      if (nodeSource.getLabels().contains("Field")) {
        return 1;
      } else if (nodeSource.getLabels().contains("Method")) {
        return 2;
      } else if (nodeSource.getLabels().contains("Package")) {
        return 2;
      } else if (nodeSource.getLabels().contains("Directory")) {
        return 1;
      } else if (nodeSource.getLabels().contains("Resource")) {
        return 3;
      } else if (nodeSource.getLabels().contains("Type")) {
        return 4;
      }
    }

    //
    return 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare(Object e1, Object e2) {

    //
    if (!(e1 instanceof HGNode && e2 instanceof HGNode)) {
      return 0;
    }

    //
    GraphDbNodeSource nodeSource1 = (GraphDbNodeSource) ((HGNode) e1).getNodeSource();
    GraphDbNodeSource nodeSource2 = (GraphDbNodeSource) ((HGNode) e2).getNodeSource();

    //
    if ((nodeSource1.getLabels().contains("Field") && nodeSource2.getLabels().contains("Field"))
        || (nodeSource1.getLabels().contains("Method") && nodeSource2.getLabels().contains("Method"))
        || (nodeSource1.getLabels().contains("Type") && nodeSource2.getLabels().contains("Type"))) {

      return nodeSource1.getProperties().get("name").compareTo(nodeSource2.getProperties().get("name"));
    }
    //
    else if (((nodeSource1.getLabels().contains("Directory") && nodeSource2.getLabels().contains("Directory"))
        || (nodeSource1.getLabels().contains("Resource") && nodeSource2.getLabels().contains("Resource")))) {

      return nodeSource1.getProperties().get("fqn").compareTo(nodeSource2.getProperties().get("fqn"));
    }
    //
    else if (((nodeSource1.getLabels().contains("Module") && nodeSource2.getLabels().contains("Module")))
        && nodeSource1.getProperties().containsKey("name") && nodeSource2.getProperties().containsKey("name")) {
      return nodeSource1.getProperties().get("name").compareTo(nodeSource2.getProperties().get("name"));
    }

    //
    return -1;
  }
}
