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

import java.util.ArrayList;

import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CoreDependencyResolve_Test extends AbstractResolverTest {

  /**
   * <p>
   * Precondition: Node 'a1' has four (accumulated) outgoing core dependencies. The dependency from 'a3 -> b3' is a
   * aggregated core dependency.
   * </p>
   * <p>
   * Test: Resolve the aggregated core dependency from 'a3 -> b3'
   * </p>
   * <p>
   * Postcondition:
   * <ul>
   * <li>Node 'a1' has five (accumulated) outgoing core dependencies.</li>
   * <li>The aggregated core dependency from 'a3 -> b3' is a not longer part of the accumulated dependencies.</li>
   * <li>The aggregated core dependency contains the resolved dependencies</li>
   * </ul>
   * </p>
   */
  @Test
  public void coreDependencyResolve() {
    
    //
    resolve(() -> {
      for (HGCoreDependency outgoingDependency : new ArrayList<>(model().a1().getAccumulatedOutgoingCoreDependencies())) {
        if (outgoingDependency instanceof HGProxyDependency) {
          ((HGProxyDependency) outgoingDependency).resolveProxyDependencies();
        }
      }
    });
  }
}
