/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.gql.hierarchicalgraph;

import io.codekontor.slizaa.core.progressmonitor.DefaultProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.GraphDatabaseSetupRule;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk.mapping.SimpleJTypeMappingProvider;
import io.codekontor.slizaa.server.gql.hierarchicalgraph.internal.Utils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    @ClassRule
    public static GraphDatabaseSetupRule graphDatabaseSetup = new GraphDatabaseSetupRule("/mapstruct_1-2-0-Final-db.zip");

    private List<HGCoreDependency> coreDependencies;

    @Before
    public void before() {

        IMappingService mappingService = MappingFactory.createMappingServiceForStandaloneSetup();

        HGRootNode rootNode = mappingService
                .convert(new SimpleJTypeMappingProvider(),
                        graphDatabaseSetup.getBoltClient(),
                        new DefaultProgressMonitor("Mapping...", 100, DefaultProgressMonitor.consoleLogger()));

        coreDependencies = rootNode.getAccumulatedOutgoingCoreDependencies();
    }

    @Test
    public void testGetDependencyPage_1() {

        // prepare
        List<HGCoreDependency> dependencies = coreDependencies.subList(0, 10);
        assertThat(dependencies).hasSize(10);

        // test
        DependencyPage dependencyPage = Utils.getDependencyPage(dependencies, 1, 10, dep -> new Dependency(dep));
        assertThat(dependencyPage).isNotNull();
        assertThat(dependencyPage.getDependencies()).hasSize(10);
        assertPageInfo(dependencyPage.getPageInfo(), 1, 10, 1, 10);
    }

    @Test
    public void testGetDependencyPage_2() {

        // prepare
        List<HGCoreDependency> dependencies = coreDependencies.subList(0, 1);
        assertThat(dependencies).hasSize(1);

        // test
        DependencyPage dependencyPage = Utils.getDependencyPage(dependencies, 1, 10,  dep -> new Dependency(dep));
        assertThat(dependencyPage).isNotNull();
        assertThat(dependencyPage.getDependencies()).hasSize(1);
        assertPageInfo(dependencyPage.getPageInfo(), 1, 10, 1, 1);
    }

    @Test
    public void testGetDependencyPage_3() {

        // prepare
        List<HGCoreDependency> dependencies = coreDependencies.subList(0, 1);
        assertThat(dependencies).hasSize(1);

        // test
        DependencyPage dependencyPage = Utils.getDependencyPage(dependencies, 10, 10,  dep -> new Dependency(dep));
        assertThat(dependencyPage).isNotNull();
        assertThat(dependencyPage.getDependencies()).hasSize(1);
        assertPageInfo(dependencyPage.getPageInfo(), 1, 10, 1, 1);
    }

    private void assertPageInfo(PageInfo pageInfo, int pageNumber, int pageSize, int maxPages, int totalCount) {

        assertThat(pageInfo).isNotNull();
        assertThat(pageInfo.getMaxPages()).withFailMessage(String.format("MaxPages (expected: %s, actual: %s)", maxPages, pageInfo.getMaxPages())).isEqualTo(maxPages);
        assertThat(pageInfo.getPageSize()).withFailMessage(String.format("PageSize (expected: %s, actual: %s)", pageSize, pageInfo.getPageSize())).isEqualTo(pageSize);
        assertThat(pageInfo.getPageNumber()).withFailMessage(String.format("PageNumber (expected: %s, actual: %s)", pageNumber, pageInfo.getPageNumber())).isEqualTo(pageNumber);
        assertThat(pageInfo.getTotalCount()).withFailMessage(String.format("TotalCount (expected: %s, actual: %s)", totalCount, pageInfo.getTotalCount())).isEqualTo(totalCount);
    }
}
