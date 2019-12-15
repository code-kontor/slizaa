/**
 * slizaa-hierarchicalgraph-core-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.model.simple;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class GetIncomingDependencies_Test {

  /** - */
  @Rule
  public SimpleTestModelRule _model = new SimpleTestModelRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testGetIncomingDependencies() {

    //
    assertThat(_model.b1().getIncomingCoreDependencies()).hasSize(2).containsOnly(_model.a1_b1_core1(),
        _model.a1_b1_core2());

    assertThat(_model.b1().getAccumulatedIncomingCoreDependencies()).hasSize(4).containsOnly(_model.a1_b1_core1(),
        _model.a1_b1_core2(), _model.a2_b2_core1(), _model.dep_a3_b3_proxy1());

    //
    assertThat(_model.b2().getIncomingCoreDependencies()).hasSize(1).containsOnly(_model.a2_b2_core1());

    assertThat(_model.b2().getAccumulatedIncomingCoreDependencies()).hasSize(2).containsOnly(_model.a2_b2_core1(),
        _model.dep_a3_b3_proxy1());

    //
    assertThat(_model.b3().getIncomingCoreDependencies()).hasSize(1).containsOnly(_model.dep_a3_b3_proxy1());

    assertThat(_model.b3().getAccumulatedIncomingCoreDependencies()).hasSize(1).containsOnly(_model.dep_a3_b3_proxy1());
  }
}
