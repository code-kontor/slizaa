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
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;

public class ExtendedHGNodeImpl extends HGNodeImpl {

  /** - */
  protected ExtendedHGNodeTrait _trait;

  public ExtendedHGNodeImpl() {
    _trait = new ExtendedHGNodeTrait(this);
  }

  @Override
  public EList<HGNode> getPredecessors() {
    return _trait.getPredecessors();
  }

  @Override
  public HGRootNode getRootNode() {
    return _trait.getRootNode();
  }

  @Override
  public Object getIdentifier() {
    return _trait.getIdentifier();
  }

  @Override
  public EList<HGCoreDependency> getAccumulatedOutgoingCoreDependencies() {
    return _trait.getAccumulatedOutgoingCoreDependencies();
  }

  @Override
  public EList<HGCoreDependency> getAccumulatedIncomingCoreDependencies() {
    return _trait.getAccumulatedIncomingCoreDependencies();
  }

  @Override
  public HGAggregatedDependency getIncomingDependenciesFrom(HGNode node) {
    return _trait.getIncomingDependenciesFrom(node);
  }

  @Override
  public List<HGAggregatedDependency> getIncomingDependenciesFrom(List<HGNode> nodes) {
    return _trait.getIncomingDependenciesFrom(nodes);
  }

  @Override
  public HGAggregatedDependency getOutgoingDependenciesTo(HGNode node) {
    return _trait.getOutgoingDependenciesTo(node);
  }

  @Override
  public List<HGAggregatedDependency> getOutgoingDependenciesTo(List<HGNode> nodes) {
    return _trait.getOutgoingDependenciesTo(nodes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolveIncomingProxyDependencies() {
    _trait.resolveIncomingProxyDependencies();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resolveOutgoingProxyDependencies() {
    _trait.resolveOutgoingProxyDependencies();
  }

  @Override
  public boolean isPredecessorOf(HGNode node) {
    return _trait.isPredecessorOf(node);
  }

  @Override
  public boolean isSuccessorOf(HGNode node) {
    return _trait.isSuccessorOf(node);
  }

  public ExtendedHGNodeTrait getTrait() {
    return _trait;
  }

  public void onExpand() {
    _trait.onExpand();
  }

  public void onCollapse() {
    _trait.onCollapse();
  }

  public void onSelect() {
    _trait.onSelect();
  }
  
  public void invalidateLocalCaches() {
    _trait.invalidateLocalCaches();
  }

  public EList<HGCoreDependency> getIncomingCoreDependencies() {
    return _trait.getIncomingCoreDependencies();
  }

  public EList<HGCoreDependency> getOutgoingCoreDependencies() {
    return _trait.getOutgoingCoreDependencies();
  }

  @Override
  public <T> Optional<T> getNodeSource(Class<T> clazz) {
    return _trait.getNodeSource(clazz);
  }
}
