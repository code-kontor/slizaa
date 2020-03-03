/**
 * slizaa-hierarchicalgraph-core-selection - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.selection;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import io.codekontor.slizaa.hierarchicalgraph.core.model.AbstractHGDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;

/**
 * <p>
 * Helper class that provides several static methods useful when working with dependency collections.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DependencySelections {

  /**
   * <p>
   * </p>
   *
   * @param dependencies
   * @return
   */
  public static Set<HGCoreDependency> getCoreDependencies(AbstractHGDependency... dependencies) {

    //
    if (dependencies == null) {
      return Collections.emptySet();
    }

    //
    final Set<HGCoreDependency> result = new HashSet<>();

    for (AbstractHGDependency dependency : dependencies) {
      if (dependency instanceof HGCoreDependency) {
        result.add((HGCoreDependency) dependency);
      }
      //
      else if (dependency instanceof HGAggregatedDependency) {
        result.addAll(((HGAggregatedDependency) dependency).getCoreDependencies());
      }
    }

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public static Set<HGCoreDependency> getCoreDependencies(Collection<? extends AbstractHGDependency> dependencies) {

    //
    if (dependencies == null) {
      return Collections.emptySet();
    }

    //
    return getCoreDependencies((AbstractHGDependency[]) dependencies.toArray(new AbstractHGDependency[0]));
  }
}
