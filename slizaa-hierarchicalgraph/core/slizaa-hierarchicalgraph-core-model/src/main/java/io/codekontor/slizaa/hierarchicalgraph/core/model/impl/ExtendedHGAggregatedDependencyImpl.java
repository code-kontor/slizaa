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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphPackage;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedHGAggregatedDependencyImpl extends HGAggregatedDependencyImpl {

  /** - */
  private boolean                 initialized = false;

  /** - */
  private EList<HGCoreDependency> coreDependencies;

  /**
   * <p>
   * Invalidates the contained core dependencies.
   * </p>
   */
  public void invalidate() {

    // return immediately if this dependency already has been uninitialized
    if (!initialized) {
      return;
    }

    // set initialized to false
    initialized = false;
  }

  /**
   * <p>
   * </p>
   */
  public void initialize() {

    // return immediately if this dependency already has been initialized
    if (initialized) {
      return;
    }

    // lazy create the core dependencies list
    if (coreDependencies == null) {
      coreDependencies = new EObjectEListWithoutUniqueCheck<HGCoreDependency>(HGCoreDependency.class, this,
          HierarchicalgraphPackage.HG_AGGREGATED_DEPENDENCY__CORE_DEPENDENCIES);
    }

    //
    Utilities.getTrait(to).ifPresent((trait) -> {

      List<HGCoreDependency> prototypeList = trait.getAccumulatedIncomingCoreDependencies().stream()
          .filter((dep) -> from.equals(dep.getFrom()) || from.isPredecessorOf(dep.getFrom()))
          .collect(Collectors.toList());

      // add all incoming dependencies from successors of the specified node
      ECollections.setEList(coreDependencies, prototypeList);

      // compute the aggregated weight
      int weightOfSimpleDependencies = prototypeList.stream()
          .filter((dep) -> (dep.getProxyDependencyParent() == null)).mapToInt(coreDep -> coreDep.getWeight())
          .sum();

      // weightOfResovedCoreDependencies
      int weightOfResovedCoreDependencies = prototypeList.stream()
          .filter((dep) -> (dep.getProxyDependencyParent() != null))
          .map(coreDep -> coreDep.getProxyDependencyParent()).distinct()
          .mapToInt(coreDep -> coreDep.getWeight()).sum();

      setNewAggregatedWeight(weightOfSimpleDependencies + weightOfResovedCoreDependencies);
    });

    //
    initialized = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGRootNode getRootNode() {
    return getFrom().getRootNode();
  }

  @Override
  public Object getIdentifier() {
    // TODO!
    return super.getIdentifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EList<HGCoreDependency> getCoreDependencies() {

    initialize();

    return coreDependencies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAggregatedWeight() {

    //
    initialize();

    //
    return aggregatedWeight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolveProxyDependencies() {

    //
    initialize();

    //
    Utilities.resolveProxyDependencies(this.coreDependencies);
  }

  /**
   * <p>
   * </p>
   *
   * @param newAggregatedWeight
   */
  private void setNewAggregatedWeight(int newAggregatedWeight) {
    if (newAggregatedWeight != aggregatedWeight) {
      int oldAggregatedWeight = aggregatedWeight;
      aggregatedWeight = newAggregatedWeight;
      if (eNotificationRequired()) {
        eNotify(new ENotificationImpl(this, Notification.SET,
            HierarchicalgraphPackage.HG_AGGREGATED_DEPENDENCY__AGGREGATED_WEIGHT, oldAggregatedWeight,
            newAggregatedWeight));
      }
    }
  }

}
