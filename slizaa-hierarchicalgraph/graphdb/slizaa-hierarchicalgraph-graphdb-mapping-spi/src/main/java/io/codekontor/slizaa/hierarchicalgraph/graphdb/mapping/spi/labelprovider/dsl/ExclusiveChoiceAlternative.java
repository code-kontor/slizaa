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
 *
 */
public class ExclusiveChoiceAlternative {

  /**
   * -
   */
  private ExclusiveChoiceBuilder _parent;

  /**
   * -
   */
  private Function<HGNode, Boolean> _condition;

  /**
   * -
   */
  private ILabelDefinitionProcessor _processor;

  /**
   * <p>
   * Creates a new instance of type {@link ExclusiveChoiceAlternative}.
   * </p>
   *
   * @param parent
   * @param condition
   */
  public ExclusiveChoiceAlternative(ExclusiveChoiceBuilder parent, Function<HGNode, Boolean> condition) {

    //
    this._parent = checkNotNull(parent);
    this._condition = checkNotNull(condition);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Function<HGNode, Boolean> getCondition() {
    return this._condition;
  }

  public ILabelDefinitionProcessor getProcessor() {
    return this._processor;
  }

  /**
   * <p>
   * </p>
   *
   * @param processor
   * @return
   */
  public ExclusiveChoiceBuilder then(ILabelDefinitionProcessor processor) {
    this._processor = checkNotNull(processor);
    return this._parent;
  }
}
