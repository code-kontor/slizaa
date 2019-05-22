/**
 * slizaa-hierarchicalgraph-core-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.INodeSource;

public class HGNodeUtils {

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  public static Map<String, String> getProperties(HGNode node) {

    //
    INodeSource nodeSource = (INodeSource) node.getNodeSource();

    //
    if (nodeSource instanceof DefaultNodeSource) {
      return ((DefaultNodeSource) nodeSource).getProperties();
    }

    //
    else {
      return Collections.emptyMap();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  public static List<String> getLabels(HGNode node) {

    //
    Map<String, String> props = getProperties(node);

    //
    if (props.containsKey("labels")) {
      String arrayString = props.get("labels");
      arrayString = arrayString.substring(1, arrayString.length() - 1);
      return Arrays.asList(arrayString.split("\\s*,\\s*"));
    }

    //
    return Collections.emptyList();
  }

  public static void dumpChildren(HGNode hgNode) {
    for (HGNode node : checkNotNull(hgNode).getChildren()) {
      System.out.println(node.getIdentifier() + " : " + HGNodeUtils.getProperties(node));
    }
  }

}
