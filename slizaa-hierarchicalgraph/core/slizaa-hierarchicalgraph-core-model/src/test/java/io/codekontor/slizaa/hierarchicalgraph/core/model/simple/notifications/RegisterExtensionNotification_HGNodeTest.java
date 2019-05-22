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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.core.model.simple.SimpleTestModelRule;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class RegisterExtensionNotification_HGNodeTest {

  /** - */
  @Rule
  public SimpleTestModelRule _model = new SimpleTestModelRule();

  /** - */
  private List<Notification> _notifications;

  /** - */
  private Adapter            _adapter;

  /**
   * <p>
   * </p>
   */
  @Before
  public void setup() {

    //
    this._notifications = new ArrayList<>();

    //
    this._adapter = new AdapterImpl() {
      @Override
      public void notifyChanged(Notification notification) {
        System.out.println("Notification: " + notification);
        RegisterExtensionNotification_HGNodeTest.this._notifications.add(notification);
      }
    };

    //
    this._model.root().eAdapters().add(this._adapter);
  }

  /**
   * <p>
   * </p>
   */
  @After
  public void teardown() {

    //
    this._model.root().eAdapters().remove(this._adapter);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testHGNodeOutgoingDependenciesNotification() {

    //
    assertThat(this._notifications).isEmpty();

    //
    this._model.root().registerExtension(INodeComparator.class, new DummyNodeComparator());
    assertThat(this._notifications).hasSize(1);

    this._model.root().registerExtension(INodeComparator.class, new DummyNodeComparator());
    assertThat(this._notifications).hasSize(2);

    //
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  private static class DummyNodeComparator implements INodeComparator {

    @Override
    public int compare(Object e1, Object e2) {
      return 0;
    }

    @Override
    public int category(Object element) {
      return 0;
    }
  }
}
