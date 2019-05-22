/**
 * ecoregenerator-maven-plugin - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.mojos.ecoregenerator.fwk;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

/**
 * A factory for repository system instances that employs Aether's built-in service locator
 * infrastructure to wire up the system's components.
 */
public class ManualRepositorySystemFactory
{

  public static RepositorySystem newRepositorySystem()
  {
    /*
     * Aether's components implement org.eclipse.aether.spi.locator.Service to ease manual wiring
     * and using the prepopulated DefaultServiceLocator, we only need to register the repository
     * connector and transporter factories.
     */
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
    locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

    locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler()
    {
      @Override
      public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception)
      {
        exception.printStackTrace();
      }
    });

    return locator.getService(RepositorySystem.class);
  }

}
