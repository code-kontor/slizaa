/**
 * slizaa-server-service-svg - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.svg;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import io.codekontor.slizaa.server.service.svg.impl.fwk.SvgServiceTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SvgServiceTestConfiguration.class)
public class SvgServiceTest {

  @ClassRule
  public static TemporaryFolder folder = new TemporaryFolder();

  @BeforeClass
  public static void before() throws Exception {
    System.setProperty("configuration.rootDirectory", folder.newFolder().getAbsolutePath());
  }

  @Autowired
  private ISvgService _svgService;

  @Test
  public void test() throws Exception {

    String key = _svgService.createSvgAndReturnShortKey("icons/class_obj.svg", null, "icons/abstract_ovr.svg", null, "icons/private_ovr.svg");
    assertThat(key).isNotNull();
    
    System.out.println(_svgService.getSvg(key));
  }
}
