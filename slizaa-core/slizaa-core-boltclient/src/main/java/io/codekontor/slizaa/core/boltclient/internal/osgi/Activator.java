/**
 * slizaa-core-boltclient - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.boltclient.internal.osgi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class Activator implements BundleActivator {

  /** - */
  private static ExecutorService _executor;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static ExecutorService getExecutor() {

    //
    initializeExecutor();

    return _executor;
  }

  @Override
  public void start(BundleContext context) throws Exception {

    //
    initializeExecutor();
  }

  @Override
  public void stop(BundleContext context) throws Exception {

    //
    _executor.shutdown();

    //
    try {
      _executor.awaitTermination(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * <p>
   * </p>
   */
  private static void initializeExecutor() {
    if (_executor == null) {
      _executor = Executors.newFixedThreadPool(20);
    }
  }
}
