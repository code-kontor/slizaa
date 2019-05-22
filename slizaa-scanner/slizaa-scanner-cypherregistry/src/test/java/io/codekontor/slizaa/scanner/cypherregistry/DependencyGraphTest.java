/**
 * slizaa-scanner-cypherregistry - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.cypherregistry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatement;
import io.codekontor.slizaa.scanner.cypherregistry.DefaultCypherStatement;
import io.codekontor.slizaa.scanner.cypherregistry.impl.DependencyGraph;

public class DependencyGraphTest {

  @Test
  public void test() {

    //
    DefaultCypherStatement a_1 = new DefaultCypherStatement("group", "a_1", "Match Return");
    DefaultCypherStatement a_2 = new DefaultCypherStatement("group", "a_2", "Match Return");
    DefaultCypherStatement b_1 = new DefaultCypherStatement("group", "b_1", "Match Return");
    DefaultCypherStatement b_2 = new DefaultCypherStatement("group", "b_2", "Match Return");
    DefaultCypherStatement c_1 = new DefaultCypherStatement("group", "c_1", "Match Return");
    DefaultCypherStatement c_2 = new DefaultCypherStatement("group", "c_2", "Match Return");
    DefaultCypherStatement d = new DefaultCypherStatement("group", "d", "Match Return");

    DependencyGraph<ICypherStatement> dependencyGraph = new DependencyGraph<ICypherStatement>();
    dependencyGraph.addVertex(a_1);
    dependencyGraph.addVertex(a_2);
    dependencyGraph.addVertex(b_1);
    dependencyGraph.addVertex(b_2);
    dependencyGraph.addVertex(c_1);
    dependencyGraph.addVertex(c_2);
    dependencyGraph.addVertex(d);

    dependencyGraph.addEdge(a_2, a_1);
    dependencyGraph.addEdge(b_2, b_1);
    dependencyGraph.addEdge(c_2, c_1);

    assertThat(dependencyGraph.calculateOrder()).containsSubsequence(a_1, a_2);
    assertThat(dependencyGraph.calculateOrder()).containsSubsequence(b_1, b_2);
    assertThat(dependencyGraph.calculateOrder()).containsSubsequence(c_1, c_2);
  }
}
