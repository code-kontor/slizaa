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

public class FasNodeSorterTest extends AbstractAlgorithmTest {

    @Test
    public void sortNodes() {

		//
		List<HGNode> nodes = _graphProvider.node(577L).getChildren();

        //
        INodeSorter nodeSorter = GraphUtils.createFasNodeSorter();

        //
        INodeSorter.SortResult sortResult = nodeSorter.sort(nodes);

		// assert upward dependencies
		// @formatter:off
        assertThat(sortResult.getUpwardDependencies()).hasSize(14)
            .containsExactlyInAnyOrder( 
        		dependency(16667, 7676),
        		dependency(7155, 7676),
        		dependency(19564, 7676),
        		dependency(14903, 7676),
        		dependency(20483, 7676),
        		dependency(14576, 7676),
        		dependency(19647, 7676),
        		dependency(20483, 14903),
        		dependency(19647, 14903),
        		dependency(19647, 14576),
        		dependency(19647, 16667),
        		dependency(7155, 20483),
        		dependency(19647, 20483),
        		dependency(7155, 19647)
        		);
        // @formatter:on
    }
}
