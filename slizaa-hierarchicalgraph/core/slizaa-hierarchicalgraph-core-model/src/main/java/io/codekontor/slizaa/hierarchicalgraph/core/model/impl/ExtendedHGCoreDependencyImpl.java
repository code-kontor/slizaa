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

import java.util.Optional;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedHGCoreDependencyImpl extends HGCoreDependencyImpl {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProxyDependency() {
    return false;
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
    return dependencySource.getIdentifier();
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
}
