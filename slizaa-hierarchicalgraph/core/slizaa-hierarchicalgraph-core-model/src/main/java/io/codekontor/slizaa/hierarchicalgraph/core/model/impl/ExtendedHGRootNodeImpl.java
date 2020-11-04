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
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphPackage;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public class ExtendedHGRootNodeImpl extends HGRootNodeImpl {

  /** - */
  protected ExtendedHGNodeTrait           _trait;

  /** - */
  protected Map<Object, HGNode>           _idToNodeMap;

  /** - */
  protected Map<Object, HGCoreDependency> _idToCoreDependencyMap;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedHGRootNodeImpl}.
   * </p>
   */
  public ExtendedHGRootNodeImpl() {
    this._trait = new ExtendedHGNodeTrait(this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getExtension(Class<T> clazz) {
    return (T) getExtensionRegistry().get(checkNotNull(clazz).getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void registerExtension(Class<T> clazz, T extension) {

    //
    if (getExtensionRegistry().containsKey(checkNotNull(clazz).getName())) {
      eSetDeliver(false);
      getExtensionRegistry().remove(checkNotNull(clazz).getName());
      eSetDeliver(true);
    }

    //
    getExtensionRegistry().put(checkNotNull(clazz).getName(), extension);
  }

  @Override
  public <T> boolean hasExtension(Class<T> key) {
    return getExtensionRegistry().containsKey(checkNotNull(key).getName());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getExtension(String key, Class<T> type) {
    Object result = getExtensionRegistry().get(checkNotNull(key));

    if (result != null) {
      checkState(checkNotNull(type).isAssignableFrom(result.getClass()));
    }

    return (T) result;
  }

  @Override
  public void registerExtension(String key, Object extension) {
    getExtensionRegistry().put(checkNotNull(key), extension);
  }

  @Override
  public <T> boolean hasExtension(String key, Class<T> type) {
    Object result = getExtensionRegistry().get(checkNotNull(key));

    if (result != null) {
      return checkNotNull(type).isAssignableFrom(result.getClass());
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EList<HGNode> getPredecessors() {
    return ECollections.emptyEList();
  }

  @Override
  public void invalidateNodeIdCache() {
    this._idToNodeMap.clear();
    this._idToNodeMap = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidateAllCaches() {
    this._trait.invalidateLocalCaches();
    EcoreUtil.getAllContents(this, false).forEachRemaining((c) -> {
      ExtendedHGNodeTrait.getTrait(c).ifPresent((trait) -> trait.invalidateLocalCaches());
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidateCaches(List<HGNode> modifiedNodes) {
    for (HGNode hgNode : getSelfAndParentNodes(checkNotNull(modifiedNodes))) {
      ExtendedHGNodeTrait.getTrait(hgNode).ifPresent((trait) -> trait.invalidateLocalCaches());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initializeCaches(List<HGNode> modifiedNodes) {
    for (HGNode hgNode : getSelfAndParentNodes(checkNotNull(modifiedNodes))) {
      ExtendedHGNodeTrait.getTrait(hgNode).ifPresent((trait) -> trait.initializeLocalCaches());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGNode lookupNode(Object identifier) {
    if (this._idToNodeMap == null) {
      EcoreUtil.getAllContents(this, false).forEachRemaining((c) -> {
        if (HierarchicalgraphPackage.eINSTANCE.getHGNode().isInstance(c)) {
          HGNode node = (HGNode) c;
          idToNodeMap().put(node.getIdentifier(), node);
        }
      });
    }

    HGNode result = this._idToNodeMap.get(identifier);
    if (result == null && this.getIdentifier().equals(identifier)) {
      return this;
    }
    return result;
  }

  @Override
  public HGCoreDependency lookupCoreDependency(Object identifier) {

    if (this._idToCoreDependencyMap == null) {
      Map<Object, HGCoreDependency> hgCoreDependencyMap = idToCoreDependencyMap();
      this.getAccumulatedOutgoingCoreDependencies()
          .forEach(coreDependency -> hgCoreDependencyMap.put(coreDependency.getIdentifier(), coreDependency));
    }

    return this._idToCoreDependencyMap.get(identifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getIdentifier() {
    return this._trait.getIdentifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGRootNode getRootNode() {
    return this._trait.getRootNode();
  }

  public void resolveProxyDependencies() {
    this._trait.resolveProxyDependencies();
  }

  @Override
  public void resolveIncomingProxyDependencies() {
    this._trait.resolveIncomingProxyDependencies();
  }

  @Override
  public void resolveOutgoingProxyDependencies() {
    this._trait.resolveOutgoingProxyDependencies();
  }

  public void invalidateLocalCaches() {
    this._trait.invalidateLocalCaches();
  }

  public void initializeLocalCaches() {
    this._trait.initializeLocalCaches();
  }

  @Override
  public <T> Optional<T> getNodeSource(Class<T> clazz) {
    return this._trait.getNodeSource(clazz);
  }

  @Override
  public EList<HGCoreDependency> getAccumulatedOutgoingCoreDependencies() {
    return this._trait.getAccumulatedOutgoingCoreDependencies();
  }

  @Override
  public EList<HGCoreDependency> getAccumulatedIncomingCoreDependencies() {
    return this._trait.getAccumulatedIncomingCoreDependencies();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGAggregatedDependency getIncomingDependenciesFrom(HGNode node) {
    return this._trait.getIncomingDependenciesFrom(node);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<HGAggregatedDependency> getIncomingDependenciesFrom(List<HGNode> nodes) {
    return this._trait.getIncomingDependenciesFrom(nodes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGAggregatedDependency getOutgoingDependenciesTo(HGNode node) {
    return this._trait.getOutgoingDependenciesTo(node);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<HGAggregatedDependency> getOutgoingDependenciesTo(List<HGNode> nodes) {
    return this._trait.getOutgoingDependenciesTo(nodes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPredecessorOf(HGNode node) {
    return this._trait.isPredecessorOf(node);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSuccessorOf(HGNode node) {
    return this._trait.isSuccessorOf(node);
  }

  @Override
  public String toString() {
    return _trait.toString(this.getClass());
  }

  public void onExpand() {
    this._trait.onExpand();
  }

  public void onCollapse() {
    this._trait.onCollapse();
  }

  public Map<Object, HGNode> getIdToNodeMap() {
    return idToNodeMap();
  }

  public ExtendedHGNodeTrait getTrait() {
    return this._trait;
  }

  private List<HGNode> getSelfAndParentNodes(List<HGNode> modifiedNodes) {
    //
    List<HGNode> selfAndParentNodes = new ArrayList<HGNode>();
    for (HGNode hgNode : modifiedNodes) {
      if (hgNode instanceof ExtendedHGNodeImpl) {
        ExtendedHGNodeImpl extendedHGNode = (ExtendedHGNodeImpl) hgNode;
        selfAndParentNodes.add(extendedHGNode);
        selfAndParentNodes.addAll(extendedHGNode.getPredecessors());
      }
    }
    return selfAndParentNodes;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private Map<Object, HGNode> idToNodeMap() {
    if (this._idToNodeMap == null) {
      this._idToNodeMap = new HashMap<>();
    }
    return this._idToNodeMap;
  }

  private Map<Object, HGCoreDependency> idToCoreDependencyMap() {
    if (this._idToCoreDependencyMap == null) {
      this._idToCoreDependencyMap = new HashMap<>();
    }
    return this._idToCoreDependencyMap;
  }

  @Override
  public EList<HGCoreDependency> getIncomingCoreDependencies() {
    return this._trait.getIncomingCoreDependencies();
  }

  @Override
  public EList<HGCoreDependency> getOutgoingCoreDependencies() {
    return this._trait.getOutgoingCoreDependencies();
  }
}
