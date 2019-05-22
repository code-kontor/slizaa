/**
 * slizaa-scanner-spi-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.spi.parser.model;

import static io.codekontor.slizaa.scanner.spi.internal.Preconditions.checkNotNull;
import static io.codekontor.slizaa.scanner.spi.internal.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class NodeFactory {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static INode createNode() {
    return new NodeBean();
  }

  /**
   * <p>
   * </p>
   *
   * @param id
   * @return
   */
  public static INode createNode(long id) {
    return new NodeBean(id);
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   *
   */
  private static final class NodeBean implements INode {

    //
    private static final Map<RelationshipType, List<IRelationship>> EMPTY_MAP = Collections.emptyMap();

    /** - */
    private long                                                    _nodeId   = -1;

    /** the properties */
    private Map<String, Object>                                     _properties;

    /** the labels */
    private List<Label>                                             _labels;

    /** the contained children */
    private Map<RelationshipType, List<IRelationship>>              _relationships;

    /**
     * <p>
     * Creates a new instance of type {@link NodeBean}.
     * </p>
     *
     * @param nodeId
     */
    public NodeBean(long nodeId) {
      this();

      //
      this._nodeId = nodeId;
    }

    /**
     * <p>
     * Creates a new instance of type {@link NodeBean}.
     * </p>
     *
     * @param _batchInserter
     */
    public NodeBean() {

      // we always use labels and properties, so there is no need to lazy create these fields
      this._labels = new ArrayList<Label>(0);
      this._properties = new HashMap<String, Object>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getId() {
      return this._nodeId;
    }

    @Override
    public String getFullyQualifiedName() {
      return (String) this._properties.get(INode.FQN);
    }

    @Override
    public String getName() {
      return (String) this._properties.get(INode.NAME);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    @Override
    public boolean hasNodeId() {
      return this._nodeId != -1;
    }

    /**
     * <p>
     * </p>
     *
     * @param nodeId
     */
    @Override
    public void setNodeId(long nodeId) {
      checkState(this._nodeId == -1, "Node id already has been set.");
      this._nodeId = nodeId;
    }

    /**
     * <p>
     * Adds the specified label.
     * </p>
     *
     * @param label
     *          the label to add. Must not be <code>null</code>.
     */
    @Override
    public void addLabel(Label label) {
      checkNotNull(label);

      this._labels.add(label);
    }

    // /**
    // * <p>
    // * </p>
    // *
    // * @param label
    // * @return
    // */
    // public boolean containsLabel(Label label) {
    // checkNotNull(label);
    //
    // return _labels.contains(label);
    // }

    /**
     * <p>
     * </p>
     *
     * @param key
     * @param value
     */
    @Override
    public void putProperty(String key, Object value) {
      checkNotNull(key);
      checkNotNull(value);

      this._properties.put(key, value);
    }

    /**
     * <p>
     * </p>
     *
     * @param key
     * @return
     */
    @Override
    public Object getProperty(String key) {
      checkNotNull(key);

      return this._properties.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRelationship addRelationship(INode targetBean, RelationshipType relationshipType) {
      checkNotNull(targetBean);
      checkNotNull(relationshipType);

      Relationship result = new Relationship(targetBean, relationshipType);
      synchronized (relationships()) {
        relationships().computeIfAbsent(relationshipType, rt -> new ArrayList<>()).add(result);
      }
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearRelationships() {
      synchronized (relationships()) {
        relationships().clear();
      }
    }

    @Override
    public boolean containsRelationship(RelationshipType type, INode node) {
      return this._relationships == null ? false
          : this._relationships.containsKey(type) && this._relationships.get(type).contains(node);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    @Override
    public Map<String, Object> getProperties() {
      return Collections.unmodifiableMap(this._properties);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    @Override
    public List<Label> getLabels() {
      return Collections.unmodifiableList(this._labels);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    @Override
    public Map<RelationshipType, List<IRelationship>> getRelationships() {
      return this._relationships == null ? EMPTY_MAP : this._relationships;
    }

    @Override
    public List<IRelationship> getRelationships(RelationshipType key) {
      return this._relationships == null || !this._relationships.containsKey(checkNotNull(key))
          ? Collections.emptyList()
          : this._relationships.get(key);
    }

    @Override
    public String toString() {
      return "NodeBean [_nodeId=" + this._nodeId + ", _properties=" + this._properties + ", _labels=" + this._labels
          + ", _relationships=" + (this._relationships != null ? this._relationships.toString() : "{}") + "]";
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    private Map<RelationshipType, List<IRelationship>> relationships() {

      //
      if (this._relationships == null) {

        //
        this._relationships = new HashMap<>();
      }
      //
      return this._relationships;
    }
  }

  private static class Relationship implements IRelationship {

    //
    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    /** - */
    private INode                            _targetBean;

    /** - */
    private RelationshipType                 _relationshipType;

    /** - */
    private Map<String, Object>              _properties;

    /**
     * <p>
     * Creates a new instance of type {@link Relationship}.
     * </p>
     *
     * @param targetBean
     * @param relationshipType
     */
    public Relationship(INode targetBean, RelationshipType relationshipType) {
      this._targetBean = checkNotNull(targetBean);
      this._relationshipType = checkNotNull(relationshipType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INode getTargetBean() {
      return this._targetBean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipType getRelationshipType() {
      return this._relationshipType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getRelationshipProperties() {
      return this._properties == null ? EMPTY_MAP : Collections.unmodifiableMap(this._properties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putRelationshipProperty(String key, Object value) {
      checkNotNull(key);
      checkNotNull(value);

      if (this._properties == null) {
        this._properties = new HashMap<>();
      }

      this._properties.put(key, value);
    }

    @Override
    public String toString() {
      return "Relationship [_targetBean=" + this._targetBean.getFullyQualifiedName() + "/" + this._targetBean.getName()
          + ", _relationshipType=" + this._relationshipType + ", _properties=" + this._properties + "]";
    }
  }
}
