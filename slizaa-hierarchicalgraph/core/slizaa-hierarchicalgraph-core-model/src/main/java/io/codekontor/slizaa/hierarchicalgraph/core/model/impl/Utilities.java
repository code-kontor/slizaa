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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver.IProxyDependencyResolverJob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.removeDependency;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class Utilities {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static Optional<ExtendedHGNodeTrait> getTrait(HGNode hgNode) {

    // handle ExtendedHGNodeImpl
    if (hgNode instanceof ExtendedHGNodeImpl) {
      return Optional.of(((ExtendedHGNodeImpl) hgNode).getTrait());
    }
    // handle ExtendedHGRootNodeImpl
    else if (hgNode instanceof ExtendedHGRootNodeImpl) {
      return Optional.of(((ExtendedHGRootNodeImpl) hgNode).getTrait());
    }
    // should not happen
    return Optional.empty();
  }

  /**
   * <p>
   * </p>
   *
   * @param dependencies
   */
  public static void resolveProxyDependencies(IProgressMonitor progressMonitor, HGCoreDependency... dependencies) {
    resolveProxyDependencies(Arrays.asList(dependencies), progressMonitor);
  }

  public static void resolveProxyDependency(HGCoreDependency dependency) {

    //
    if (dependency == null) {
      return;
    }

    //
    if (!dependency.getRootNode().hasExtension(IProxyDependencyResolver.class)) {
      return;
    }

    if (dependency instanceof ExtendedHGProxyDependencyImpl
        && !((ExtendedHGProxyDependencyImpl) dependency).isResolved()) {

      //
      ExtendedHGProxyDependencyImpl extendedDependency = (ExtendedHGProxyDependencyImpl) dependency;
      DependencyResolution dependencyResolution = new DependencyResolution(
          extendedDependency.onResolveProxyDependency(), extendedDependency);
      dependencyResolution.waitForCompletion();
      dependencyResolution.getDependency().setResolved(true);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param dependencies
   */
  public static void resolveProxyDependencies(List<? extends HGCoreDependency> dependencies,
      IProgressMonitor progressMonitor) {

    //
    if (dependencies == null || dependencies.isEmpty()) {
      return;
    }

    //
    if (!dependencies.get(0).getRootNode().hasExtension(IProxyDependencyResolver.class)) {
      return;
    }

    //
    List<DependencyResolution> dependencyResolutions = new ArrayList<>();

    // copy the dependencies (concurrent modifications!)
    for (HGCoreDependency coreDependency : new ArrayList<>(dependencies)) {

      if (coreDependency instanceof ExtendedHGProxyDependencyImpl
          && !((ExtendedHGProxyDependencyImpl) coreDependency).isResolved()) {

        //
        ExtendedHGProxyDependencyImpl extendedDependency = (ExtendedHGProxyDependencyImpl) coreDependency;
        dependencyResolutions
            .add(new DependencyResolution(extendedDependency.onResolveProxyDependency(), extendedDependency));
      }
    }

    // wait for completion the result
    for (DependencyResolution dependencyResolution : dependencyResolutions) {
      dependencyResolution.waitForCompletion();
      dependencyResolution.getDependency().setResolved(true);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  private static class DependencyResolution {

    private IProxyDependencyResolverJob   _resolverJob;

    private ExtendedHGProxyDependencyImpl _dependency;

    public DependencyResolution(IProxyDependencyResolverJob resolverJob, ExtendedHGProxyDependencyImpl dependency) {
      this._dependency = checkNotNull(dependency);
      this._resolverJob = resolverJob;
    }

    public void waitForCompletion() {
      if (this._resolverJob != null) {
        this._resolverJob.waitForCompletion();
      }
    }

    public ExtendedHGProxyDependencyImpl getDependency() {
      return this._dependency;
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public static class NotificationBuffer {

    protected List<Notification> notifications = new ArrayList<Notification>();

    protected List<Adapter>      savedAdapters = new ArrayList<Adapter>();

    protected EObject            eObject;

    public NotificationBuffer(EObject eObject) {
      this.eObject = eObject;
    }

    public void startBuffering() {
      EList<Adapter> eAdapters = this.eObject.eAdapters();
      for (Adapter a : eAdapters) {
        this.savedAdapters.add(a);
      }
      for (Adapter a : this.savedAdapters) {
        eAdapters.remove(a);
      }
      System.out.println("NotificationBuffer: " + this.savedAdapters.size());
    }

    public void stopBuffering() {
      this.eObject.eAdapters().addAll(this.savedAdapters);
    }
  }
}
