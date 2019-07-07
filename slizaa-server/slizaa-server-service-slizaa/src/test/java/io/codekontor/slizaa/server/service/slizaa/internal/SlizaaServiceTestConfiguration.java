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
package io.codekontor.slizaa.server.service.slizaa.internal;

import io.codekontor.slizaa.server.service.selection.EnableSelectionServiceModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import io.codekontor.slizaa.server.service.backend.impl.DummyBackendService;
import io.codekontor.slizaa.server.service.configuration.EnableConfigurationModule;
import io.codekontor.slizaa.server.service.extensions.EnableExtensionsModule;
import io.codekontor.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.IGraphDatabaseStateMachineContextFactory;
import io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase.factory.DummyGraphDatabaseStateMachineContextFactory;
import io.codekontor.slizaa.server.service.svg.EnableSvgServiceModule;

@Configuration
// @EnableBackendServiceModule
@EnableExtensionsModule
@EnableConfigurationModule
@EnableSlizaaServiceModule
@EnableSvgServiceModule
@EnableSelectionServiceModule
@TestConfiguration
public class SlizaaServiceTestConfiguration { 
  
  @Bean(name="GraphDatabaseStateMachineContextFactory")
  @Primary
  public IGraphDatabaseStateMachineContextFactory graphDatabaseStateMachineContextFactory() {
    return new DummyGraphDatabaseStateMachineContextFactory();
  }
  
  @Bean
  @Primary
  public IBackendServiceInstanceProvider backendService() {
    return new DummyBackendService();
  }
}
