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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IMappingProvider {

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public class DefaultMappingProvider implements IMappingProvider {

    /** - */
    private IMappingProviderMetadata _metaData;

    /** - */
    private IHierarchyDefinitionProvider       _hierarchyProvider;

    /** - */
    private IDependencyDefinitionProvider      _dependencyProvider;

    /** - */
    private ILabelDefinitionProvider _labelProvider;

    /** - */
    private INodeComparator          _nodeComparator;

    /**
     * <p>
     * Creates a new instance of type {@link DelegatingMappingProvider}.
     * </p>
     *
     * @param metaInformation
     * @param hierarchyProvider
     * @param dependencyProvider
     * @param labelProvider
     * @param nodeComparator
     */
    public DefaultMappingProvider(IMappingProviderMetadata metaInformation, IHierarchyDefinitionProvider hierarchyProvider,
        IDependencyDefinitionProvider dependencyProvider, ILabelDefinitionProvider labelProvider,
        INodeComparator nodeComparator) {

      this._metaData = checkNotNull(metaInformation);
      this._hierarchyProvider = checkNotNull(hierarchyProvider);
      this._dependencyProvider = checkNotNull(dependencyProvider);
      this._labelProvider = checkNotNull(labelProvider);
      this._nodeComparator = checkNotNull(nodeComparator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMappingProviderMetadata getMappingProviderMetadata() {
      return this._metaData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHierarchyDefinitionProvider getHierarchyDefinitionProvider() {
      return this._hierarchyProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDependencyDefinitionProvider getDependencyDefinitionProvider() {
      return this._dependencyProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILabelDefinitionProvider getLabelDefinitionProvider() {
      return this._labelProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INodeComparator getNodeComparator() {
      return this._nodeComparator;
    }
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public interface IMappingProviderMetadata {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getIdentifier();

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
    String getDescription();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String[] getCategories();

    /**
     * <p>
     * </p>
     *
     * @param category
     * @return
     */
    String getCategoryValue(String category);

    /**
     * <p>
     * </p>
     *
     * @param identifier
     *          has to be set
     * @param name
     *          has to be set
     * @param description
     *          may null
     * @param categories
     *          may null
     * @return
     */
    public static IMappingProviderMetadata createMetadata(String identifier, String name, String description,
        Map<String, String> categories) {

      return new DefaultMappingProviderMetadata(checkNotNull(identifier), checkNotNull(name), description, categories);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  IMappingProviderMetadata getMappingProviderMetadata();

  /**
   * <p>
   * </p>
   */
  IHierarchyDefinitionProvider getHierarchyDefinitionProvider();

  /**
   * <p>
   * </p>
   */
  IDependencyDefinitionProvider getDependencyDefinitionProvider();

  /**
   * <p>
   * </p>
   */
  ILabelDefinitionProvider getLabelDefinitionProvider();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  INodeComparator getNodeComparator();
}
