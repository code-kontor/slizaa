/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning;

import io.codekontor.slizaa.core.job.IProvisioningPlanExecution;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ProvisioningTest extends AbstractProvisioningTest {

    @Test
    @DirtiesContext
    public void testCreateGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

        IProvisioningPlanExecution provisioningJob = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db01());
        Awaitility.await().until(() -> !provisioningJob.hasPendingTasks());

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db01")).isNotNull();
    }

   @Test
    @DirtiesContext
    public void testDeleteAndCreateGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

       IProvisioningPlanExecution provisioningJob_1 = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db01());
        Awaitility.await().until(() -> !provisioningJob_1.hasPendingTasks());

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db01")).isNotNull();

       IProvisioningPlanExecution provisioningJob_2 = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v1());
        Awaitility.await().until(() -> !provisioningJob_2.hasPendingTasks());

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
    }

   @Test
    @DirtiesContext
    public void testDeleteGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

       IProvisioningPlanExecution provisioningJob_1 = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db01());
        Awaitility.await().until(() -> !provisioningJob_1.hasPendingTasks());

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db01")).isNotNull();

       IProvisioningPlanExecution provisioningJob_2 = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v1());
       Awaitility.await().until(() -> !provisioningJob_2.hasPendingTasks());

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
    }

    @Test
    @DirtiesContext
    public void testUpdateGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

        IProvisioningPlanExecution provisioningJob_1 = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v1());
        Awaitility.await().until(() -> !provisioningJob_1.hasPendingTasks());

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
        assertThat(slizaaService().getGraphDatabase("db02").getHierarchicalGraph("identifier_1")).isNotNull();

        IProvisioningPlanExecution provisioningJob_2 = provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v2());
        Awaitility.await().until(() -> !provisioningJob_2.hasPendingTasks());;

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
        assertThat(slizaaService().getGraphDatabase("db02").getHierarchicalGraph("identifier_2")).isNotNull();
    }
}
