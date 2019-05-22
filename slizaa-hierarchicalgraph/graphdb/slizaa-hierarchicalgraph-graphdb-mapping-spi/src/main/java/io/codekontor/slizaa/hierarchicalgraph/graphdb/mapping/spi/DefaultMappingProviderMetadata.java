/**
 * slizaa-hierarchicalgraph-graphdb-mapping-spi - Slizaa Static Software Analysis Tools
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
 *
 */
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider.IMappingProviderMetadata;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class DefaultMappingProviderMetadata implements IMappingProviderMetadata {

  /** - */
  private String              _identifier;

  /** - */
  private String              _name;

  /** - */
  private String              _description;

  /** - */
  private Map<String, String> _categories;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderMetadata}.
   * </p>
   *
   * @param identifier
   * @param name
   */
  public DefaultMappingProviderMetadata(String identifier, String name) {
    this(identifier, name, null);
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderMetadata}.
   * </p>
   *
   * @param identifier
   * @param name
   * @param description
   */
  public DefaultMappingProviderMetadata(String identifier, String name, String description) {
    this._identifier = checkNotNull(identifier);
    this._name = checkNotNull(name);
    this._description = description;
    this._categories = new HashMap<>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultMappingProviderMetadata}.
   * </p>
   *
   * @param identifier
   * @param name
   * @param description
   * @param categories
   */
  public DefaultMappingProviderMetadata(String identifier, String name, String description,
      Map<String, String> categories) {
    this._identifier = checkNotNull(identifier);
    this._name = checkNotNull(name);
    this._description = description;
    this._categories = categories != null ? new HashMap<>(categories) : new HashMap<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param categoryValues
   */
  public void setCategoryValues(Map<String, String> categoryValues) {
    this._categories.clear();
    this._categories.putAll(categoryValues);
  }

  /**
   * <p>
   * </p>
   *
   * @param description
   */
  public void setDescription(String description) {
    this._description = description;
  }

  @Override
  public String getIdentifier() {
    return this._identifier;
  }

  @Override
  public String getName() {
    return this._name;
  }

  @Override
  public String getDescription() {
    return this._description;
  }

  @Override
  public String[] getCategories() {
    return this._categories.keySet().toArray(new String[0]);
  }

  @Override
  public String getCategoryValue(String category) {
    return this._categories.get(checkNotNull(category));
  }
}
