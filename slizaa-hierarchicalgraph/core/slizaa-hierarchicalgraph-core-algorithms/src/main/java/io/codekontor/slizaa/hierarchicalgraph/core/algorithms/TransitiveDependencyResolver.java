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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

public class TransitiveDependencyResolver {

  public static Set<HGCoreDependency> getTransitiveAccumulatedOutgoingCoreDependencies(HGNode node) {
    return getTransitiveAccumulatedOutgoingCoreDependencies(Collections.singleton(node));
  }
  
  public static Set<HGCoreDependency> getTransitiveAccumulatedOutgoingCoreDependencies(Collection<HGNode> nodes) {
   
    // create the (yet empty) result stream
    Set<HGCoreDependency> result = new HashSet<>();
    
    Set<HGNode> nodesAlreadyConsidered = new HashSet<>();
    Collection<HGNode> nodesToConsideredNext = nodes;

    while (!nodesToConsideredNext.isEmpty()) {

      Set<HGCoreDependency> tmpDep = nodesToConsideredNext.stream().flatMap(hgNode -> hgNode.getAccumulatedOutgoingCoreDependencies().stream()).collect(Collectors.toSet());

      result.addAll(tmpDep);
      
      nodesAlreadyConsidered.addAll(nodesToConsideredNext);

      nodesToConsideredNext = tmpDep.stream().map(dep -> dep.getTo())
          .distinct().filter(node -> !nodesAlreadyConsidered.contains(node)).collect(Collectors.toSet());
    }
    
    // 
    return result;
  }
  
  public static Set<HGCoreDependency> getTransitiveAccumulatedIncomingCoreDependencies(HGNode node) {
    return getTransitiveAccumulatedIncomingCoreDependencies(Collections.singleton(node));
  }
  
  public static Set<HGCoreDependency> getTransitiveAccumulatedIncomingCoreDependencies(Collection<HGNode> nodes) {
    
    // create the (yet empty) result stream
    Set<HGCoreDependency> result = new HashSet<>();
    
    Set<HGNode> nodesAlreadyConsidered = new HashSet<>();
    Collection<HGNode> nodesToConsideredNext = nodes;

    while (!nodesToConsideredNext.isEmpty()) {

      Set<HGCoreDependency> tmpDep = nodesToConsideredNext.stream().flatMap(hgNode -> hgNode.getAccumulatedIncomingCoreDependencies().stream()).collect(Collectors.toSet());

      result.addAll(tmpDep);
      
      nodesAlreadyConsidered.addAll(nodesToConsideredNext);

      nodesToConsideredNext = tmpDep.stream().map(dep -> dep.getFrom())
          .distinct().filter(node -> !nodesAlreadyConsidered.contains(node)).collect(Collectors.toSet());
    }
    
    // 
    return result;
  }
}
