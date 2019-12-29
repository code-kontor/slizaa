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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphPackage;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver.IProxyDependencyResolverJob;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedHGProxyDependencyImpl extends HGProxyDependencyImpl {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProxyDependency() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGRootNode getRootNode() {
    return getFrom().getRootNode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> Optional<T> getDependencySource(Class<T> clazz) {

    //
    if (checkNotNull(clazz).isInstance(dependencySource)) {
      return Optional.of(clazz.cast(dependencySource));
    }

    return Optional.empty();
  }

  @Override
  public List<HGCoreDependency> getCoreDependencies(SourceOrTarget sourceOrTarget) {
    checkNotNull(sourceOrTarget);
    return getAccumulatedCoreDependencies().stream()
            .filter(d -> SourceOrTarget.SOURCE.equals(sourceOrTarget) ? d.getFrom().equals(getFrom()) : d.getTo().equals(getTo()))
            .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void resolve() {
    if (!resolved) {
      Utilities.resolveProxyDependency(this);
    }
  }

  @Override
  public synchronized boolean isResolved() {
    return super.isResolved();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IProxyDependencyResolverJob onResolveProxyDependency() {

    //
    if (!resolved) {

      //
      if (getRootNode().hasExtension(IProxyDependencyResolver.class)) {
        return getRootNode().getExtension(IProxyDependencyResolver.class).resolveProxyDependency(this);
      }
    }

    //
    return null;
  }

  /**
   * <p>
   * </p>
   *
   * @param newResolved
   */
  void setResolved(boolean newResolved) {
    boolean oldResolved = resolved;
    resolved = newResolved;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET,
          HierarchicalgraphPackage.HG_PROXY_DEPENDENCY__RESOLVED, oldResolved, resolved));
  }
}
