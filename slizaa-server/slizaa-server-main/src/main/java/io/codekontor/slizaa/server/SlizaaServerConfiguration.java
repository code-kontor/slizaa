/**
 * slizaa-server-main - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server;

import io.codekontor.slizaa.server.command.EnableSlizaaServerCommandModule;
import io.codekontor.slizaa.server.graphql.EnableGraphqlModule;
import io.codekontor.slizaa.server.rest.EnableAdminRestModule;
import io.codekontor.slizaa.server.service.backend.EnableBackendServiceModule;
import io.codekontor.slizaa.server.service.configuration.EnableConfigurationModule;
import io.codekontor.slizaa.server.service.selection.EnableSelectionServiceModule;
import io.codekontor.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import io.codekontor.slizaa.server.service.svg.EnableSvgServiceModule;
import io.codekontor.slizaa.service.spec.EnableSpecServiceModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@SpringBootApplication
@EnableBackendServiceModule
@EnableSlizaaServiceModule
@EnableGraphqlModule
@EnableConfigurationModule
@EnableSvgServiceModule
@EnableSlizaaServerCommandModule
@EnableSelectionServiceModule
@EnableAdminRestModule
@EnableSpecServiceModule
public class SlizaaServerConfiguration {

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(SlizaaServerConfiguration.class, args);
  }
}
