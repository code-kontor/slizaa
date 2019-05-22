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
package io.codekontor.slizaa.hierarchicalgraph.core.model.simple.resolvedeps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.simple.SimpleTestModelRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CallResolver_Test {

  /** - */
  @Rule
  public SimpleTestModelRule           _model = new SimpleTestModelRule();

  /** - */
  private IProxyDependencyResolver _resolver;

  /** - */
  private HGAggregatedDependency   _aggregatedDependency;

  /**
   * {@inheritDoc}
   */
  @Before
  public void before() {

    //
    this._resolver = mock(IProxyDependencyResolver.class);
    this._model.root().registerExtension(IProxyDependencyResolver.class, this._resolver);

    // get the aggregated dependency
    this._aggregatedDependency = this._model.a1().getOutgoingDependenciesTo(this._model.b1());
    assertThat(this._aggregatedDependency).isNotNull();
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testResolveProxyDependencies() {

    //
    this._aggregatedDependency.resolveProxyDependencies();

    //
    verify(this._resolver).resolveProxyDependency(this._model.a3_b3_core1());
    verifyNoMoreInteractions(this._resolver);

    //
    reset(this._resolver);

    // don't call 'createNewAggregatedDependencyResolver' again
    this._aggregatedDependency.resolveProxyDependencies();

    //
    verify(this._resolver, never()).resolveProxyDependency(any());
  }
}
