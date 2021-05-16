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
/**
 */
package io.codekontor.slizaa.hierarchicalgraph.core.model.impl;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultDependencySource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CustomHierarchicalgraphFactoryImpl extends HierarchicalgraphFactoryImpl
    implements HierarchicalgraphFactory {

  /**
   * <p>
   * Creates a new instance of type {@link CustomHierarchicalgraphFactoryImpl}.
   * </p>
   */
  public CustomHierarchicalgraphFactoryImpl() {
    super();
  }

  @Override
  public HGNode createHGNode() {
    HGNodeImpl hgNode = new ExtendedHGNodeImpl();
    return hgNode;
  }

  @Override
  public HGRootNode createHGRootNode() {
    HGRootNodeImpl hgRootNode = new ExtendedHGRootNodeImpl();
    return hgRootNode;
  }

  @Override
  public DefaultDependencySource createDefaultDependencySource() {
    DefaultDependencySourceImpl defaultDependencySource = new DefaultDependencySourceImpl();
    return defaultDependencySource;
  }

  @Override
  public HGAggregatedDependency createHGAggregatedDependency() {
    HGAggregatedDependencyImpl hgAggregatedDependency = new ExtendedHGAggregatedDependencyImpl();
    return hgAggregatedDependency;
  }

  @Override
  public HGCoreDependency createHGCoreDependency() {
    HGCoreDependencyImpl hgCoreDependency = new ExtendedHGCoreDependencyImpl();
    return hgCoreDependency;
  }

  @Override
  public HGProxyDependency createHGProxyDependency() {
    HGProxyDependencyImpl hgProxyDependency = new ExtendedHGProxyDependencyImpl();
    return hgProxyDependency;
  }
}
