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
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import io.codekontor.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

public class Tarjan<T extends HGNode> {

    private int _index = 0;
    private final ArrayList<Integer> _stack = new ArrayList<Integer>();
    private final List<List<T>> _stronglyConnectedComponents = new ArrayList<List<T>>();
    private int[] _vlowlink;
    private int[] _vindex;

    private HGNode[] _artifacts;

    public List<List<T>> detectStronglyConnectedComponents(Collection<? extends T> artifacts) {
        Objects.requireNonNull(artifacts);

        _artifacts = artifacts.toArray(new HGNode[0]);
        int[][] adjacencyList = GraphUtils.computeAdjacencyList(_artifacts);
        return executeTarjan(adjacencyList);
    }

    private List<List<T>> executeTarjan(int[][] graph) {
        Objects.requireNonNull(graph);

        _stronglyConnectedComponents.clear();
        _index = 0;
        _stack.clear();
        _vlowlink = new int[graph.length];
        _vindex = new int[graph.length];
        for (int i = 0; i < _vlowlink.length; i++) {
            _vlowlink[i] = -1;
            _vindex[i] = -1;
        }

        for (int i = 0; i < graph.length; i++) {
            if (_vindex[i] == -1) {
                tarjan(i, graph);
            }
        }

        return _stronglyConnectedComponents;
    }

    @SuppressWarnings("unchecked")
    private void tarjan(int v, int[][] graph) {
        Objects.requireNonNull(v);
        Objects.requireNonNull(graph);

        _vindex[v] = _index;
        _vlowlink[v] = _index;

        _index++;
        _stack.add(0, v);
        for (int n : graph[v]) {
            if (_vindex[n] == -1) {
                tarjan(n, graph);
                _vlowlink[v] = Math.min(_vlowlink[v], _vlowlink[n]);
            }
            else if (_stack.contains(n)) {
                _vlowlink[v] = Math.min(_vlowlink[v], _vindex[n]);
            }
        }
        if (_vlowlink[v] == _vindex[v]) {
            int n;
            ArrayList<T> component = new ArrayList<T>();
            do {
                n = _stack.remove(0);
                component.add((T) _artifacts[n]);
            }
            while (n != v);
            _stronglyConnectedComponents.add(component);
        }
    }
}
