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
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ProxyDependencyResolve_NotificationTest extends AbstractResolverTest {

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
    _notifications = new ArrayList<>();

    //
    _adapter = new AdapterImpl() {
      public void notifyChanged(Notification notification) {
        if (notification.getEventType() == Notification.SET ) {
          System.out.println("Notification: " + notification.getFeature());
          _notifications.add(notification);
        }
      }
    };
  }

  @Test
  public void coreDependencyResolve() {

    //
    model().a3_b3_core1().eAdapters().add(_adapter);

    //
    resolve(() -> {
      model().a3_b3_core1().resolveProxyDependencies();
    });

    //
    model().a3_b3_core1().eAdapters().remove(_adapter);
  }
}
