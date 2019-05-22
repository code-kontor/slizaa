/**
 * slizaa-hierarchicalgraph-graphdb-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.model.impl;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedGraphDbRootNodeSourceImpl extends GraphDbRootNodeSourceImpl {

  /** - */
  private ExtendedGraphDbNodeSourceTrait _trait;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedGraphDbRootNodeSourceImpl}.
   * </p>
   */
  public ExtendedGraphDbRootNodeSourceImpl() {
    this._trait = new ExtendedGraphDbNodeSourceTrait(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EMap<String, String> getProperties() {
    return ECollections.emptyEMap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EList<String> getLabels() {
    return ECollections.emptyEList();
  }

  /**
   * {@inheritDoc}
   */
  public EMap<String, String> reloadProperties() {
    return ECollections.emptyEMap();
  }

  /**
   * {@inheritDoc}
   */
  public EList<String> reloadLabels() {
    return ECollections.emptyEList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onExpand() {
    this._trait.onExpand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onCollapse() {
    this._trait.onCollapse();
  }

  @Override
  public boolean isAutoExpand() {
    return this._trait.isAutoExpand();
  }
}
