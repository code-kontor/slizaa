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

import io.codekontor.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport;
import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.DefaultLabelDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.ILabelAndPropertyProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.ILabelDefinitionProcessor;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.LabelMappingDsl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract base class for {@link LabelMappingDsl} tests.
 */
public abstract class AbstractLabelMappingDslTest extends LabelMappingDsl {

  {
    CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  /**
   * Creates a new instance of type {@link AbstractLabelMappingDslTest}.
   */
  public AbstractLabelMappingDslTest() {
    super(hgNode -> new DefaultNodeSourceAdapter(hgNode.getNodeSource(DefaultNodeSource.class).get()));
  }

  /**
   * Executes the given processor on the given hgNode.
   *
   * @param processor the processor
   * @param hgNode    the node
   * @return the created label definition
   */
  protected DefaultLabelDefinition process(ILabelDefinitionProcessor processor, HGNode hgNode) {

    checkNotNull(processor);
    checkNotNull(hgNode);

    DefaultLabelDefinition labelDefinition = new DefaultLabelDefinition();
    processor.processLabelDefinition(hgNode, labelDefinition);
    return labelDefinition;
  }

  /**
   * Creates a new {@link HGNode} with no properties set.
   *
   * @return the new {@link HGNode} with no properties set.
   */
  protected HGNode createHGNode() {
    return createHGNode(null);
  }

  /**
   * <p>
   * Creates a new HGNode with the given properties
   * </p>
   *
   * @param properties
   * @return
   */
  protected HGNode createHGNode(Map<String, String> properties) {
    HGNode hgNode = HierarchicalgraphFactory.eINSTANCE.createHGNode();
    DefaultNodeSource nodeSource = HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
    nodeSource.setIdentifier(Long.valueOf(666l));
    if (properties != null) {
      nodeSource.getProperties().putAll(properties);
    }
    hgNode.setNodeSource(nodeSource);
    return hgNode;
  }

  /**
   * Simple adapter for the default node source.
   */
  private static class DefaultNodeSourceAdapter implements ILabelAndPropertyProvider {

    private DefaultNodeSource _defaultNodeSource;

    public DefaultNodeSourceAdapter(DefaultNodeSource _defaultNodeSource) {
      this._defaultNodeSource = _defaultNodeSource;
    }

    @Override
    public Map<String, String> getProperties() {
      return _defaultNodeSource.getProperties();
    }

    @Override
    public Collection<String> getLabels() {
      return Collections.emptySet();
    }
  }
}
