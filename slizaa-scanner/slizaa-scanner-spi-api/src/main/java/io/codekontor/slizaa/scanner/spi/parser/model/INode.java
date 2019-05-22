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

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Represents and encapsulates a node in the underlying graph database model.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface INode {

  /** the key for the attribute 'fqn' */
  public static final String FQN      = "fqn";

  /** the key for the attribute 'name' */
  public static final String NAME     = "name";

  /**
   * <p>
   * Returns the internal id of the underlying database node.
   * </p>
   * 
   * @return the internal id of the underlying database node.
   */
  long getId();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getFullyQualifiedName();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getName();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  Map<String, Object> getProperties();

  /**
   * <p>
   * </p>
   *
   * @param key
   * @return
   */
  Object getProperty(String key);

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<Label> getLabels();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  Map<RelationshipType, List<IRelationship>> getRelationships();

  /**
   * <p>
   * </p>
   *
   * @param key
   * @return
   */
  List<IRelationship> getRelationships(RelationshipType key);

  /**
   * <p>
   * </p>
   *
   * @return
   */
  boolean hasNodeId();
  
  /**
   * <p>
   * </p>
   * 
   * @param targetBean
   * @param relationshipType
   * @return
   */
  IRelationship addRelationship(INode targetBean, RelationshipType relationshipType);
  
  /**
   * <p>
   * </p>
   *
   */
  void clearRelationships();

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @param value
   */
  void putProperty(String key, Object value);

  /**
   * <p>
   * </p>
   * 
   * @param label
   */
  void addLabel(Label label);
  
  void setNodeId(long id);

  boolean containsRelationship(RelationshipType type, INode node);
}
