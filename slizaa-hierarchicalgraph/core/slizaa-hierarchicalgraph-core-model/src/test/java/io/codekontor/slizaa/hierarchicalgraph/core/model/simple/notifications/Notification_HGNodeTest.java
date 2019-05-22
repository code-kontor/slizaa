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
package io.codekontor.slizaa.hierarchicalgraph.core.model.simple.notifications;

import static org.assertj.core.api.Assertions.assertThat;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewCoreDependency;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.core.model.simple.SimpleTestModelRule;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class Notification_HGNodeTest {

  /** - */
  @Rule
  public SimpleTestModelRule    _model = new SimpleTestModelRule();

  /** - */
  private List<Notification> _notifications;

  /** - */
  private Adapter            _adapter;

  /** - */
  private HGNode             _node;

  /**
   * <p>
   * </p>
   */
  @Before
  public void setup() {

    //
    this._notifications = new ArrayList<>();

    //
    this._node = _model.a2();
    assertThat(this._node).isNotNull();
    assertThat(this._node.getOutgoingCoreDependencies()).isNotNull();
    assertThat(this._node.getOutgoingCoreDependencies()).hasSize(1).containsOnly(_model.a2_b2_core1());

    //
    this._node.getAccumulatedOutgoingCoreDependencies();

    //
    this._adapter = new AdapterImpl() {
      @Override
      public void notifyChanged(Notification notification) {
        System.out.println("Notification: " + notification);
        Notification_HGNodeTest.this._notifications.add(notification);
      }
    };

    //
    this._node.eAdapters().add(this._adapter);
  }

  /**
   * <p>
   * </p>
   */
  @After
  public void teardown() {

    //
    this._node.eAdapters().remove(this._adapter);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testHGNodeOutgoingDependenciesNotification() {

    //
    HGCoreDependency newCoreDependency = createNewCoreDependency(_model.a2(), _model.b2(), "NEW_USAGE",
        () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), true);

    //
    assertThat(this._notifications).hasSize(2);

    //
    assertThat(this._node.getOutgoingCoreDependencies()).hasSize(2).containsOnly(_model.a2_b2_core1(),
        newCoreDependency);
  }

}
