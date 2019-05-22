/**
 * slizaa-server-service-backend - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.backend;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.codekontor.slizaa.server.service.configuration.IConfigurationService;
import io.codekontor.slizaa.server.service.configuration.impl.NullConfigurationService;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionIdentifier;
import io.codekontor.slizaa.server.service.extensions.IExtensionService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AbstractServerBackendTest.class)
@TestConfiguration
@ComponentScan(basePackageClasses = AbstractServerBackendTest.class)
public abstract class AbstractServerBackendTest {

  @Bean
  public IConfigurationService configurationService() {
    return new NullConfigurationService();
  }

  @Autowired
  protected ApplicationContext applicationContext;

  @Autowired
  protected DummyExtensionService extensionService;

  /**
   *
   */
  @Component
  public static class DummyExtensionService implements IExtensionService {

    //
    private List<IExtension> _extensions = Collections.emptyList();

    /**
     *
     * @return
     */
    @Override
    public List<IExtension> getExtensions() {
      return _extensions;
    }

    /**
     *
     * @param extensionIdentifiers
     * @return
     */
    @Override
    public List<IExtension> getExtensions(List<? extends IExtensionIdentifier> extensionIdentifiers) {
      return getExtensions().stream().filter(extensionIdentifiers::contains).collect(Collectors.toList());
    }

    /**
     *
     * @param _extensions
     */
    public void setExtensions(List<IExtension> _extensions) {
      this._extensions = _extensions;
    }
  }
}

