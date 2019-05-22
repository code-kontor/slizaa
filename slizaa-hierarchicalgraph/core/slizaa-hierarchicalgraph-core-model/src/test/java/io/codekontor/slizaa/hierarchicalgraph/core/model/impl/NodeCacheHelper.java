/**
 * slizaa-hierarchicalgraph-core-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.model.impl;

import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGNodeImpl;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGNodeTrait;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGRootNodeImpl;

public class NodeCacheHelper {

  public static Optional<EList<HGNode>> cachedParents(Object c) {
    return getTrait(c).map(trait -> trait._cachedParents);
  }

  public static Optional<EMap<HGNode, HGAggregatedDependency>> cachedAggregatedIncomingDependenciesMap(Object c) {
    return getTrait(c).map(trait -> trait._cachedAggregatedIncomingDependenciesMap);
  }

  public static Optional<EMap<HGNode, HGAggregatedDependency>> cachedAggregatedOutgoingDependenciesMap(Object c) {
    return getTrait(c).map(trait -> trait._cachedAggregatedOutgoingDependenciesMap);
  }

  public static Optional<EList<HGCoreDependency>> cachedIncomingSelfAndSubTreeCoreDependencies(Object c) {
    return getTrait(c).map(trait -> trait._accumulatedIncomingCoreDependencies);
  }

  public static Optional<EList<HGCoreDependency>> cachedOutgoingSelfAndSubTreeCoreDependencies(Object c) {
    return getTrait(c).map(trait -> trait._accumulatedOutgoingCoreDependencies);
  }

  public static void populateCaches(Object c) {
    getTrait(c).ifPresent((trait) -> {
      trait.getPredecessors();
      trait.getAccumulatedOutgoingCoreDependencies();
      trait.getAccumulatedIncomingCoreDependencies();
      trait.getIncomingDependenciesFrom(trait.getRootNode());
      trait.getOutgoingDependenciesTo(trait.getRootNode());
    });
  }

  /**
   * <p>
   * </p>
   *
   * @param o
   * @return
   */
  public static Optional<ExtendedHGNodeTrait> getTrait(Object o) {

    //
    if (o instanceof ExtendedHGNodeImpl) {
      return Optional.of(((ExtendedHGNodeImpl) o).getTrait());
    }

    //
    if (o instanceof ExtendedHGRootNodeImpl) {
      return Optional.of(((ExtendedHGRootNodeImpl) o).getTrait());
    }

    //
    return Optional.empty();
  }
}
