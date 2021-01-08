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

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphPackage;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.StringToStringMapImpl;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbHierarchicalgraphPackage;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbRootNodeSource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.neo4j.driver.types.Node;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedGraphDbNodeSourceTrait {

  /** - */
  private GraphDbNodeSourceImpl _nodeSource;

  /**
   * <p>
   * Creates a new instance of type {@link ExtendedGraphDbNodeSourceTrait}.
   * </p>
   *
   * @param nodeSource
   */
  public ExtendedGraphDbNodeSourceTrait(GraphDbNodeSourceImpl nodeSource) {
    this._nodeSource = nodeSource;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public EMap<String, String> getProperties() {

    // lazy load...
    if (this._nodeSource.properties == null) {
      reloadNodeAndProperties();
    }

    // return the result
    return this._nodeSource.properties;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public EList<String> getLabels() {

    // lazy load...
    if (this._nodeSource.labels == null) {
      reloadNodeAndProperties();
    }

    // return the result
    return this._nodeSource.labels;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public void reloadNodeAndProperties() {
    getBoltClient().getNodeAndConsume((long) this._nodeSource.getIdentifier(), node -> {
      setLabels(node);
      setProperties(node);
    });
  }

  /**
   * <p>
   * </p>
   */
  public void loadPropertiesAndLabelsForChildren() {
    InternalQueryUtils.loadLabelsAndProperties(this._nodeSource.getNode().getChildren());
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  EList<String> setLabels(Node node) {
    checkNotNull(node);

    // lazy init
    if (this._nodeSource.labels == null) {
      this._nodeSource.labels = new EDataTypeUniqueEList<String>(String.class, this._nodeSource,
          GraphDbHierarchicalgraphPackage.GRAPH_DB_NODE_SOURCE__LABELS);
    } else {
      this._nodeSource.labels.clear();
    }

    node.labels().forEach((e) -> this._nodeSource.labels.add(e));

    // return the result
    return this._nodeSource.labels;
  }

  public void onExpand() {
    loadPropertiesAndLabelsForChildren();
  }

  public void onCollapse() {
  }

  public void onSelect() {

  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  EMap<String, String> setProperties(Node node) {
    checkNotNull(node);

    // lazy init
    if (this._nodeSource.properties == null) {
      this._nodeSource.properties = new EcoreEMap<String, String>(
          HierarchicalgraphPackage.Literals.STRING_TO_STRING_MAP,
         StringToStringMapImpl.class, this._nodeSource,
          GraphDbHierarchicalgraphPackage.GRAPH_DB_NODE_SOURCE__PROPERTIES);
    } else {
      // clear the properties first
      this._nodeSource.properties.clear();
    }

    // re-populate
    node.asMap().entrySet().forEach((e) -> {
      this._nodeSource.properties.put(e.getKey(), e.getValue().toString());
    });

    // return the result
    return this._nodeSource.properties;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IBoltClient getBoltClient() {

    //
    GraphDbRootNodeSource rootNodeSource = (GraphDbRootNodeSource) this._nodeSource.getNode().getRootNode()
        .getNodeSource();

    //
    IBoltClient boltClient = rootNodeSource.getBoldClient();
    checkNotNull(boltClient, "No bolt client set.");
    return boltClient;
  }

  public boolean isAutoExpand() {
    // TODO
    return true;
  }
}
