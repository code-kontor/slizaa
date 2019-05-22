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
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class GetIncomingDependenciesFrom_Test {

  /** - */
  @Rule
  public SimpleTestModelRule _model = new SimpleTestModelRule();

  @Test
  public void testGetIncomingDependenciesFrom() {

    //
    HGAggregatedDependency aggregatedDependency = _model.b1().getIncomingDependenciesFrom(_model.a1());
    assertThat(aggregatedDependency).isNotNull();

    assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(4);
    assertThat(aggregatedDependency.getCoreDependencies()).containsExactly(_model.a1_b1_core1(), _model.a1_b1_core2(),
        _model.a2_b2_core1(), _model.a3_b3_core1());

    //
    aggregatedDependency = _model.b2().getIncomingDependenciesFrom(_model.a2());
    assertThat(aggregatedDependency).isNotNull();

    assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(2);
    assertThat(aggregatedDependency.getCoreDependencies()).containsExactly(_model.a2_b2_core1(),
        _model.a3_b3_core1());

    //
    aggregatedDependency = _model.b3().getIncomingDependenciesFrom(_model.a3());
    assertThat(aggregatedDependency).isNotNull();
    assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(1);
    assertThat(aggregatedDependency.getCoreDependencies()).containsExactly(_model.a3_b3_core1());
  }
}
