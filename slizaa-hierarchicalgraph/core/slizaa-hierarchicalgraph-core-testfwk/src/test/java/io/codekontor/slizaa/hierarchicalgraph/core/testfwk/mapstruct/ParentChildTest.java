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
package io.codekontor.slizaa.hierarchicalgraph.core.testfwk.mapstruct;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphPackage;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;

public class ParentChildTest {

  @ClassRule
  public static XmiBasedTestGraphProviderRule _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.MAP_STRUCT);

  /**
   * <p>
   * </p>
   */
  @Test
  public void testParentChild() {
    EcoreUtil.getAllContents(_graphProvider.rootNode(), false).forEachRemaining((c) -> {
      if (HierarchicalgraphPackage.eINSTANCE.getHGNode().isInstance(c)) {
        HGNode node = (HGNode) c;
        checkParentChild(node);
        if (node.getParent() != null) {
          checkBloodline(node, node.getParent());
        }
      }
    });
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   */
  private void checkParentChild(HGNode node) {

    // check parent
    if (node.getParent() != null) {
      assertThat(node.getParent().getChildren()).contains(node);
    }

    // check children
    for (HGNode child : node.getChildren()) {
      assertThat(child.getParent()).isEqualTo(node);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @param predecessor
   */
  private void checkBloodline(HGNode node, HGNode predecessor) {
    if (predecessor != null) {
      assertThat(predecessor.isPredecessorOf(node));
      assertThat(!predecessor.isSuccessorOf(node));
      assertThat(!node.isPredecessorOf(predecessor));
      assertThat(node.isSuccessorOf(node));
      checkBloodline(node, predecessor.getParent());
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] { {  } });
  }
}
