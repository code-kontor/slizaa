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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.junit.Rule;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ExtensionNotification_Test {

  /** - */
  @Rule
  public SimpleTestModelRule     _model        = new SimpleTestModelRule();

  private List<Notification> _notifcations = new LinkedList<>();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testExtensionNotification() {

    //
    this._model.root().eAdapters().add(new AdapterImpl() {
      @Override
      public void notifyChanged(Notification msg) {
        ExtensionNotification_Test.this._notifcations.add(msg);
      }
    });

    //

    this._model.root().registerExtension(ExtensionNotification_Test.class, this);

    //
    assertThat(this._notifcations).hasSize(1);
  }
}
