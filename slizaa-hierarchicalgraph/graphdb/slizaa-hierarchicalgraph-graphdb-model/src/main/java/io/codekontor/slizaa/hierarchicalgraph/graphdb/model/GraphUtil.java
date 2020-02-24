/**
 * slizaa-hierarchicalgraph-graphdb-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.model;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.impl.InternalQueryUtils;
import org.eclipse.emf.common.util.EMap;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GraphUtil {

  public static void loadLabelsAndProperties(List<HGNode> hgNodes){
    InternalQueryUtils.loadLabelsAndProperties(checkNotNull(hgNodes));
  }

  public static List<HGCoreDependency> sortCoreDependencies(Collection<HGCoreDependency> hgCoreDependencies){
    return InternalQueryUtils.sortCoreDependencies(checkNotNull(hgCoreDependencies));
  }

  @Deprecated
  public static void dump(HGRootNode rootNode) {

    System.out.println(rootNode);
    rootNode.getChildren().forEach(c1 -> {
      System.out.println(" - " + fqn(c1));
      c1.getChildren().forEach(c2 -> {
        System.out.println("   -- " + fqn(c2));
        c2.getChildren().forEach(c3 -> {
          System.out.println("     --- " + fqn(c3));
          c3.getChildren().forEach(c4 -> {
            System.out.println("       ----- " + fqn(c4));
            // c4.getChildren().forEach(c5 -> {
            // System.out.println(" -" + fqn(c5));
            // });
          });
        });
      });
    });
  }

  private static String fqn(HGNode hgNode) {
    EMap<String, String> properties = ((GraphDbNodeSource) hgNode.getNodeSource()).getProperties();
    return properties.get("fqn");
  }
}
