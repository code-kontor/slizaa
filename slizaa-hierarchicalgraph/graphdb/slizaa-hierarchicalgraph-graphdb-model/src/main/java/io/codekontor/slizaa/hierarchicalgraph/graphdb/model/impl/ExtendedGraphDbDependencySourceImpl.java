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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphPackage;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.StringToStringMapImpl;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbHierarchicalgraphPackage;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbRootNodeSource;
import org.neo4j.driver.types.Relationship;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtendedGraphDbDependencySourceImpl extends GraphDbDependencySourceImpl {

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<T> getUserObject(Class<T> type) {
    return checkNotNull(type).isInstance(getUserObject()) ? Optional.of((T) getUserObject()) : Optional.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EMap<String, String> getProperties() {

    // lazy load...
    if (this.properties == null) {
      reloadProperties();
    }

    // return the result
    return this.properties;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public EMap<String, String> reloadProperties() {

    // lazy init
    if (this.properties == null) {
      this.properties = new EcoreEMap<String, String>(HierarchicalgraphPackage.Literals.STRING_TO_STRING_MAP,
          StringToStringMapImpl.class, this,
          GraphDbHierarchicalgraphPackage.GRAPH_DB_NODE_SOURCE__PROPERTIES);
    }

    // clear properties first
    this.properties.clear();

    getBoltClient().getRelationshipAndConsume((long) getIdentifier(), relationship -> {
      // re-populate
      relationship.asMap().entrySet().forEach((e) -> {
        this.properties.put(e.getKey(), e.getValue().toString());
      });
    });



    // return the result
    return this.properties;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IBoltClient getBoltClient() {
    return ((GraphDbRootNodeSource) getDependency().getFrom().getRootNode().getNodeSource()).getBoldClient();
  }
}
