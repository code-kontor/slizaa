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

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.boltclient.internal.BoltClientImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 *
 */
public class ServiceRegistrator {

  /** - */
  private static Map<IBoltClient, ServiceRegistration<IBoltClient>> serviceRegistrationMap = new HashMap<>();

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   */
  public static void registerAsOsgiService(IBoltClient boltClient) {

    try {

      // register adapter
      ServiceRegistration<IBoltClient> serviceRegistration = FrameworkUtil.getBundle(BoltClientImpl.class)
          .getBundleContext().registerService(IBoltClient.class, boltClient, null);

      //
      serviceRegistrationMap.put(boltClient, serviceRegistration);

    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param boltClient
   */
  public static void unregisterAsOsgiService(IBoltClient boltClient) {

    try {

      //
      if (serviceRegistrationMap.containsKey(boltClient)) {
        ServiceRegistration<IBoltClient> registration = serviceRegistrationMap.get(boltClient);
        registration.unregister();
      }

    } catch (Exception e) {
      // ignore
    }
  }
}
