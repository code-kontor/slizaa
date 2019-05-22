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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExclusiveChoiceBuilder {

  /**
   * -
   */
  private List<ExclusiveChoiceAlternative> _alternatives = new ArrayList<>();

  /**
   * <p>
   * </p>
   *
   * @param condition
   * @return
   */
  public ExclusiveChoiceAlternative when(Function<HGNode, Boolean> condition) {

    ExclusiveChoiceAlternative builder = new ExclusiveChoiceAlternative(this, condition);
    this._alternatives.add(builder);
    return builder;
  }

  /**
   * <p>
   * </p>
   *
   * @param processor
   * @return
   */
  public ILabelDefinitionProcessor otherwise(ILabelDefinitionProcessor processor) {

    //
    return (n, l) -> {

      //
      for (ExclusiveChoiceAlternative alternative : this._alternatives) {
        if (alternative.getCondition().apply(n)) {
          alternative.getProcessor().processLabelDefinition(n, l);
          return;
        }
      }

      //
      processor.processLabelDefinition(n, l);
    };
  }
}
