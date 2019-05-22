/**
 * slizaa-hierarchicalgraph-core-algorithms - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class StronglyConnectedComponentsTest extends AbstractAlgorithmTest {

    @Test
    public void detectSCCs() {

		//
		List<HGNode> nodes = _graphProvider.node(577L).getChildren();

        //
        List<List<HGNode>> stronglyConnectedComponents = GraphUtils.detectStronglyConnectedComponents(nodes);

        //
        assertThat(stronglyConnectedComponents).hasSize(25);

		// @formatter:off
        for (List<HGNode> scc : stronglyConnectedComponents) {
            if (scc.size() == 2) {
            	assertThat(scc).hasSize(10);
                assertThat(scc)
                	.containsExactlyInAnyOrder(
                		node(19564),
                		node(7155),
                		node(19647),
                		node(19397),
                		node(20483),
                		node(16667),
                		node(14485),
                		node(7676),
                		node(14576),
                		node(14903)
                	);
            }
        }
		// @formatter:on
    }
}
