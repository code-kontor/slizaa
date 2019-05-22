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
package io.codekontor.slizaa.hierarchicalgraph.core.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.Utilities;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ProxyDependencyFunctions {

  /**
   * <p>
   * </p>
   *
   * @param dependencies
   * @return
   */
  public static boolean containsUnresolvedProxyDependencies(List<? extends AbstractHGDependency> dependencies) {

    //
    if (dependencies == null) {
      return false;
    }

    //
    for (AbstractHGDependency dependency : dependencies) {

      // HGAggregatedDependency
      if (dependency instanceof HGAggregatedDependency) {
        HGAggregatedDependency aggregatedDependency = (HGAggregatedDependency) dependency;
        for (HGCoreDependency coreDependency : aggregatedDependency.getCoreDependencies()) {
          if (coreDependency instanceof HGProxyDependency && !((HGProxyDependency) coreDependency).isResolved()) {
            return true;
          }
        }
      }

      // HGProxyDependency
      if (dependency instanceof HGProxyDependency) {
        return true;
      }
    }

    //
    return false;
  }

  /**
   * <p>
   * </p>
   *
   * @param dependencies
   */
  public static void resolveProxyDependencies(List<AbstractHGDependency> dependencies,
      IProgressMonitor progressMonitor) {

    checkNotNull(dependencies);

    //
    List<HGProxyDependency> proxyDependencies = new ArrayList<>();

    //
    for (AbstractHGDependency dependency : dependencies) {
      if (dependency instanceof HGAggregatedDependency) {
        HGAggregatedDependency aggregatedDependency = (HGAggregatedDependency) dependency;
        for (HGCoreDependency coreDependency : aggregatedDependency.getCoreDependencies()) {
          if (coreDependency instanceof HGProxyDependency && !((HGProxyDependency) coreDependency).isResolved()) {
            proxyDependencies.add(((HGProxyDependency) coreDependency));
          }
        }
      }
      if (dependency instanceof HGProxyDependency) {
        proxyDependencies.add(((HGProxyDependency) dependency));
      }
    }

    //
    Utilities.resolveProxyDependencies(proxyDependencies, progressMonitor);
  }

  /**
   * <p>
   * </p>
   *
   * @param nodes
   * @param incoming
   * @param outgoing
   * @param progressMonitor
   */
  public static void resolveProxyDependencies(Collection<HGNode> nodes, boolean incoming, boolean outgoing,
      IProgressMonitor progressMonitor) {

    //
    checkNotNull(nodes);

    //
    List<HGProxyDependency> proxyDependencies = new ArrayList<>();

    //
    for (HGNode hgNode : nodes) {
      addProxyDependencies(hgNode, incoming, outgoing, proxyDependencies);
    }

    //
    Utilities.resolveProxyDependencies(proxyDependencies, progressMonitor);
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @param incoming
   * @param outgoing
   * @param proxyDependencies
   * @return
   */
  private static List<HGProxyDependency> addProxyDependencies(HGNode node, boolean incoming, boolean outgoing, List<HGProxyDependency> proxyDependencies) {

    //
    checkNotNull(node);
    checkNotNull(proxyDependencies);

    //
    if (incoming) {
      proxyDependencies.addAll(filterProxyDependencies(node.getIncomingCoreDependencies()));
      for (HGNode predecessor : node.getPredecessors()) {
        proxyDependencies.addAll(filterProxyDependencies(predecessor.getIncomingCoreDependencies()));
      }
    }

    //
    if (outgoing) {
      proxyDependencies.addAll(filterProxyDependencies(node.getOutgoingCoreDependencies()));
      for (HGNode predecessor : node.getPredecessors()) {
        proxyDependencies.addAll(filterProxyDependencies(predecessor.getOutgoingCoreDependencies()));
      }
    }

    return proxyDependencies;
  }

  /**
   * <p>
   * </p>
   *
   * @param dependencies
   * @return
   */
  private static List<HGProxyDependency> filterProxyDependencies(List<HGCoreDependency> dependencies) {
    return checkNotNull(dependencies).stream().filter(dep -> dep instanceof HGProxyDependency)
        .map(dep -> (HGProxyDependency) dep).collect(Collectors.toList());
  }
}
