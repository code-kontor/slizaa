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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import io.codekontor.slizaa.server.service.backend.impl.SlizaaServerBackendImpl;
import io.codekontor.slizaa.server.service.extensions.Version;
import io.codekontor.slizaa.server.service.extensions.mvn.MvnBasedExtension;
import io.codekontor.slizaa.server.service.extensions.mvn.MvnDependency;

public class ServerBackend_InstallExtensionsTest extends AbstractServerBackendTest {

  //
  private static MvnBasedExtension testExtension    = new MvnBasedExtension("Test", new Version(1, 0, 0))
      .withDependency(new MvnDependency("io.codekontor.slizaa:slizaa-core-progressmonitor:1.0.0-SNAPSHOT"));

  //
  private static MvnBasedExtension invalidExtension = new MvnBasedExtension("Missing", new Version(1, 0, 0))
      .withDependency(new MvnDependency("NOT_THERE:NOT_THERE:1.0.0-SNAPSHOT"));

  //
  private IBackendService  _backend;

  @Before
  public void before() {

    // prepare
    _backend = applicationContext.getBean(SlizaaServerBackendImpl.class);
    assertThat(_backend).isNotNull();
    assertThat(_backend.hasInstalledExtensions()).isFalse();
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void installExtensions_Success() {

    // try to install
    _backend.installExtensions(Collections.singletonList(testExtension));

    //
    assertThat(_backend.hasInstalledExtensions()).isTrue();
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void installExtensions_Failure() {

    // try to install
    _backend.installExtensions(Collections.singletonList(invalidExtension));

    //
    assertThat(_backend.hasInstalledExtensions()).isFalse();
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  public void reinstallExtensions_Success() {

    // try to install
    _backend.installExtensions(Collections.singletonList(testExtension));
    assertThat(_backend.hasInstalledExtensions()).isTrue();
    
    // try to install
    _backend.installExtensions(Collections.emptyList());
    assertThat(_backend.hasInstalledExtensions()).isFalse();

    // try to install
    _backend.installExtensions(Collections.singletonList(testExtension));
    assertThat(_backend.hasInstalledExtensions()).isTrue();
  }

}
