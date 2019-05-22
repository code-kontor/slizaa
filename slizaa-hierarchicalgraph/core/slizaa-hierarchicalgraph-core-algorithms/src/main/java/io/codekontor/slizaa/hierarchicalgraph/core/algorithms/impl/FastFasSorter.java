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
package io.codekontor.slizaa.hierarchicalgraph.core.algorithms.impl;

import java.util.ArrayList;
import java.util.List;

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.INodeSorter;
import io.codekontor.slizaa.hierarchicalgraph.core.model.AbstractHGDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

public class FastFasSorter implements INodeSorter {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public INodeSorter.SortResult sort(List<HGNode> artifacts) {

        // we have to compute the adjacency matrix first
        int[][] adjacencyMatrix = GraphUtils.computeAdjacencyMatrix(artifacts);

        // the ordered sequence (highest first!)
        FastFAS fastFAS = new FastFAS(adjacencyMatrix);
        int[] ordered = fastFAS.getOrderedSequence();

        // Bubbles
        for (int outerIndex = 1; outerIndex < ordered.length; outerIndex++) {
            for (int index = outerIndex; index >= 1; index--) {

                //
                if (adjacencyMatrix[ordered[index]][ordered[index
                        - 1]] > adjacencyMatrix[ordered[index - 1]][ordered[index]]) {

                    // swap...
                    int temp = ordered[index];
                    ordered[index] = ordered[index - 1];
                    ordered[index - 1] = temp;

                }
                else {

                    // stop bubbling...
                    break;
                }
            }
        }

        // reverse it
        ordered = FastFAS.reverse(ordered);

        // create the result nodes list
        List<HGNode> resultNodes = new ArrayList<>(artifacts.size());
        for (int index : ordered) {
            resultNodes.add(artifacts.get(index));
        }

        // create the list of upwards dependencies
        List<AbstractHGDependency> upwardsDependencies = new ArrayList<>();
        for (Integer[] values : fastFAS.getSkippedEdge()) {
            HGNode source = artifacts.get(values[0]);
            HGNode target = artifacts.get(values[1]);
            upwardsDependencies.add(source.getOutgoingDependenciesTo(target));
        }

        // return the result
        return new SortResult() {

            @Override
            public List getOrderedNodes() {
                return resultNodes;
            }

            @Override
            public List getUpwardDependencies() {
                return upwardsDependencies;
            }
        };
    }
}
