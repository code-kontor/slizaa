/**
 * slizaa-server-service-slizaa - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.slizaa.internal.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.Ignore;
import org.junit.Test;
import io.codekontor.slizaa.server.service.configuration.IConfigurationService;
import io.codekontor.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.google.common.io.Files;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class SlizaaServiceConfigurationTest extends AbstractSlizaaServiceTest {

  private static final String   TEST_DIRECTORY_NAME = "test";

  private static final String   TEST_FILE_NAME      = "test.json";

  @Autowired
  private IConfigurationService _configurationService;

  @Test
  public void testConfiguration() throws IOException {

    setupConfigurationFile("src/test/resources/slizaaservicecfg/cfg_1.json");

    SlizaaServiceConfiguration slizaaServiceConfiguration = _configurationService.load(TEST_DIRECTORY_NAME,
        TEST_FILE_NAME, SlizaaServiceConfiguration.class);

    assertThat(slizaaServiceConfiguration.getGraphDatabases()).hasSize(1);
    assertThat(slizaaServiceConfiguration.getGraphDatabases().get(0).getContentDefinition()).isNotNull();
  }

  @Test(expected = UnrecognizedPropertyException.class)
  public void testIncorrectConfiguration() throws IOException {

    setupConfigurationFile("src/test/resources/slizaaservicecfg/incorrect_cfg.json");

    SlizaaServiceConfiguration slizaaServiceConfiguration = _configurationService.load(TEST_DIRECTORY_NAME,
        TEST_FILE_NAME, SlizaaServiceConfiguration.class);
  }
  
  @Test
  public void testInvalidPayloadConfiguration() throws IOException {

    setupConfigurationFile("src/test/resources/slizaaservicecfg/invaldPayload_cfg.json");

    SlizaaServiceConfiguration slizaaServiceConfiguration = _configurationService.load(TEST_DIRECTORY_NAME,
        TEST_FILE_NAME, SlizaaServiceConfiguration.class);

    assertThat(slizaaServiceConfiguration.getGraphDatabases()).hasSize(1);
    assertThat(slizaaServiceConfiguration.getGraphDatabases().get(0).getContentDefinition()).isNotNull();
  }

  private void setupConfigurationFile(String filePath) throws IOException {

    File targetFile = new File(getConfigurationRootDirectory(),
        TEST_DIRECTORY_NAME + File.separatorChar + TEST_FILE_NAME);

    if (!targetFile.getParentFile().exists()) {
      targetFile.getParentFile().mkdirs();
    }

    Files.copy(new File(filePath),
        new File(getConfigurationRootDirectory(), TEST_DIRECTORY_NAME + File.separatorChar + TEST_FILE_NAME));
  }
}
