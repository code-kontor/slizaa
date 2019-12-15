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
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewCoreDependency;

import java.util.List;

import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGProxyDependencyImpl;
import org.junit.Before;
import org.junit.Rule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.core.model.simple.SimpleTestModelRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractResolverTest implements IProxyDependencyResolver {

  /** - */
  @Rule
  public SimpleTestModelRule   _model = new SimpleTestModelRule();

  /** - */
  private HGCoreDependency _newDependency_1;

  /** - */
  private HGCoreDependency _newDependency_2;

  @Before
  public void before() {

    //
    this._model.root().registerExtension(IProxyDependencyResolver.class, this);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public SimpleTestModelRule model() {
    return this._model;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public HGCoreDependency getNewDependency_1() {
    return this._newDependency_1;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public HGCoreDependency getNewDependency_2() {
    return this._newDependency_2;
  }

  
  
  /**
   * {@inheritDoc}
   */
  @Override
  public IProxyDependencyResolverJob resolveProxyDependency(HGProxyDependency dependencyToResolve) {

    this._newDependency_1 = createNewCoreDependency(dependencyToResolve.getFrom(), dependencyToResolve.getTo(),
        "NEW_USAGE_1", () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), false);

    this._newDependency_2 = createNewCoreDependency(dependencyToResolve.getFrom(), dependencyToResolve.getTo(),
        "NEW_USAGE_2", () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), false);

    dependencyToResolve.getAccumulatedCoreDependencies().add(this._newDependency_1);
    dependencyToResolve.getAccumulatedCoreDependencies().add(this._newDependency_2);

    // return null as we resolved the dependencies immediately
    return null;
  }

  /**
   * <p>
   * </p>
   *
   * @param runnable
   */
  protected void resolve(Runnable runnable) {

    //
    assertDependenciesBeforeResolve();

    //
    runnable.run();

    //
    assertDependenciesAfterResolve();
  }

  /**
   * <p>
   * </p>
   */
  private void assertDependenciesBeforeResolve() {

    //
    HGAggregatedDependency aggregatedDependency = this._model.a1().getOutgoingDependenciesTo(this._model.b1());
    assertThat(aggregatedDependency).isNotNull();
    assertThat(aggregatedDependency.getAggregatedWeight()).isEqualTo(4);
    assertThat(aggregatedDependency.getCoreDependencies()).isNotNull();
    assertThat(aggregatedDependency.getCoreDependencies()).hasSize(4).contains(this._model.a1_b1_core1(),
        this._model.a1_b1_core2(), this._model.a2_b2_core1(), this._model.dep_a3_b3_proxy1());

    //
    List<HGCoreDependency> outgoingDependencies = this._model.a1().getAccumulatedOutgoingCoreDependencies();
    assertThat(outgoingDependencies).hasSize(4).contains(this._model.a1_b1_core1(), this._model.a1_b1_core2(),
        this._model.a2_b2_core1(), this._model.dep_a3_b3_proxy1());

    //
    List<HGCoreDependency> incomingDependencies = this._model.b1().getAccumulatedIncomingCoreDependencies();
    assertThat(incomingDependencies).hasSize(4).contains(this._model.a1_b1_core1(), this._model.a1_b1_core2(),
        this._model.a2_b2_core1(), this._model.dep_a3_b3_proxy1());
  }

  /**
   * <p>
   * </p>
   */
  private void assertDependenciesAfterResolve() {

    // should be same as before
    assertDependenciesBeforeResolve();

    HGProxyDependency proxyDependency = _model.dep_a3_b3_proxy1();

    assertThat(proxyDependency.isResolved());
    assertThat(proxyDependency.getAccumulatedCoreDependencies()).hasSize(2);

    // proxyDependency.getAccumulatedCoreDependencies().forEach(dep -> System.out.println(dep.getType() + " -> " + dep.getProxyDependencyParent().getType()));
  }
}
