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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.ILabelAndPropertyProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.ILabelDefinitionProcessor;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.LabelMappingDsl;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractLabelDefinitionProvider extends LabelMappingDsl implements ILabelDefinitionProvider {

  /** - */
  private ILabelDefinitionProcessor _processor;

  /**
   * Creates a new instance of {@link AbstractLabelDefinitionProvider} with the given adapter function.
   *
   * @param labelAndPropertyProviderAdapterFunction
   */
  public AbstractLabelDefinitionProvider(
      Function<HGNode, ILabelAndPropertyProvider> labelAndPropertyProviderAdapterFunction) {
    super(labelAndPropertyProviderAdapterFunction);
  }

  /**
   *
   */
  public AbstractLabelDefinitionProvider() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final ILabelDefinition getLabelDefinition(HGNode node) {

    //
    DefaultLabelDefinition defaultLabelDefinition = new DefaultLabelDefinition();

    //
    processor().processLabelDefinition(node, defaultLabelDefinition);

    //
    return defaultLabelDefinition;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected abstract ILabelDefinitionProcessor createLabelDefinitionProcessor();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private ILabelDefinitionProcessor processor() {

    //
    if (this._processor == null) {
      this._processor = createLabelDefinitionProcessor();
      checkNotNull(this._processor);
    }

    //
    return this._processor;
  }
}
