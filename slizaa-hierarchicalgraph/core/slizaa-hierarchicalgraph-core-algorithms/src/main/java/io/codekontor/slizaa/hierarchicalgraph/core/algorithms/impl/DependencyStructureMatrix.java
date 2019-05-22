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

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.INodeSorter;
import io.codekontor.slizaa.hierarchicalgraph.core.model.AbstractHGDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

import java.util.*;
import java.util.stream.Collectors;


public class DependencyStructureMatrix implements IDependencyStructureMatrix {

    private List<List<HGNode>> cycles;

    private List<HGNode> nodes;

    private List<AbstractHGDependency> upwardDependencies;

    public DependencyStructureMatrix(Collection<HGNode> nodes) {
        initialize(nodes);
    }

    @Override
    public List<AbstractHGDependency> getUpwardDependencies() {
        return upwardDependencies;
    }

    @Override
    public int getWeight(int i, int j) {

        if (i < 0 || i >= nodes.size() || j < 0 || j >= nodes.size()) {
            return -1;
        }

        HGAggregatedDependency dependency = nodes.get(i).getOutgoingDependenciesTo(nodes.get(j));

        return dependency != null ? dependency.getAggregatedWeight() : 0;
    }

    @Override
    public List<HGNode> getOrderedNodes() {
        return nodes;
    }

    @Override
    public boolean isRowInCycle(int i) {
        return isCellInCycle(i, i);
    }

    @Override
    public boolean isCellInCycle(int i, int j) {

        if (i < 0 || i >= nodes.size() || j < 0 || j >= nodes.size()) {
            return false;
        }

        for (List<HGNode> cycle : cycles) {
            if (cycle.size() > 1 && cycle.contains(nodes.get(i)) && cycle.contains(nodes.get(j))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<List<HGNode>> getCycles() {
        return cycles;
    }
    
    @Override
	public int[][] getMatrix() {
    	
    	//
    	int[][] result = new int[nodes.size()][nodes.size()];
    	
    	//
    	for (int i = 0; i < nodes.size(); i++) {
        	for (int j = 0; j < nodes.size(); j++) {
        		HGAggregatedDependency dependency = nodes.get(i).getOutgoingDependenciesTo(nodes.get(j));
        		result[i][j] = dependency != null ? dependency.getAggregatedWeight() : 0;
    		}
		}
    	
    	//
    	return result;
	}

	private void initialize(Collection<HGNode> unorderedArtifacts) {
        Objects.requireNonNull(unorderedArtifacts);

        upwardDependencies = new ArrayList<>();

        List<List<HGNode>> c = GraphUtils.detectStronglyConnectedComponents(unorderedArtifacts);
        INodeSorter artifactSorter = new FastFasSorter();
        for (List<HGNode> cycle : c) {
            INodeSorter.SortResult sortResult = artifactSorter.sort(cycle);
            cycle.clear();
            cycle.addAll(sortResult.getOrderedNodes());
            upwardDependencies.addAll(sortResult.getUpwardDependencies());
        }

        List<HGNode> orderedArtifacts = new ArrayList<>();

        // optimize: un-cycled artifacts without dependencies first
        for (List<HGNode> artifactList : c) {
            if (artifactList.size() == 1 && artifactList.get(0).getOutgoingCoreDependencies().isEmpty()) {
                orderedArtifacts.add(artifactList.get(0));
            }
        }

        for (List<HGNode> cycle : c) {
            for (HGNode node : cycle) {
                if (!orderedArtifacts.contains(node)) {
                    orderedArtifacts.add(node);
                }
            }
        }
        Collections.reverse(orderedArtifacts);
        nodes = orderedArtifacts;

        //
        cycles = c.stream().filter(nodeList -> nodeList.size() > 1).collect(Collectors.toList());
    }
}
