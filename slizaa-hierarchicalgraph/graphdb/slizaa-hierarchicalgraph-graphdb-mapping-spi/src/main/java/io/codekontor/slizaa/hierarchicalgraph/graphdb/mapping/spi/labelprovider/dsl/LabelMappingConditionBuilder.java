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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class LabelMappingConditionBuilder {

  /**
   * -
   */
  private Function<HGNode, Boolean> _condition;

  /**
   * <p>
   * Creates a new instance of type {@link LabelMappingConditionBuilder}.
   * </p>
   *
   * @param condition
   */
  public LabelMappingConditionBuilder(Function<HGNode, Boolean> condition) {
    this._condition = checkNotNull(condition);
  }

  /**
   * <p>
   * </p>
   *
   * @param processor
   * @return
   */
  public ILabelDefinitionProcessor then(ILabelDefinitionProcessor processor) {

    //
    checkNotNull(processor);

    //
    return (hgNode, labelDefinition) -> {
      if (this._condition.apply(hgNode)) {
        processor.processLabelDefinition(hgNode, labelDefinition);
      }
    };
  }
}
