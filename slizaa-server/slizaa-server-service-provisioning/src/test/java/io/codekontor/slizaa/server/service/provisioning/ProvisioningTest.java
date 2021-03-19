package io.codekontor.slizaa.server.service.provisioning;

import org.awaitility.Awaitility;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ProvisioningTest extends AbstractProvisioningTest {

    @Test
    @DirtiesContext
    public void testCreateGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db01());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db01"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db01")).isNotNull();
    }

    @Test
    @DirtiesContext
    public void testDeleteAndCreateGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db01());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db01"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db01")).isNotNull();

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v1());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db01") && !provisioningService().hasPendingJobs("db02"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
    }

    @Test
    @DirtiesContext
    public void testDeleteGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db01());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db01"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db01")).isNotNull();

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v1());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db01") && !provisioningService().hasPendingJobs("db02"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
    }

    @Test
    @DirtiesContext
    public void testUpdateGraphDatabase() {

        assertThat(slizaaService().getGraphDatabases()).hasSize(0);

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v1());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db02"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
        assertThat(slizaaService().getGraphDatabase("db02").getHierarchicalGraph("identifier_1")).isNotNull();

        provisioningService().provision(ServerConfigurationRequests.getServerConfigurationRequest_db02_v2());
        Awaitility.await().until(() -> !provisioningService().hasPendingJobs("db02"));

        assertThat(slizaaService().getGraphDatabases()).hasSize(1);
        assertThat(slizaaService().getGraphDatabase("db02")).isNotNull();
        assertThat(slizaaService().getGraphDatabase("db02").getHierarchicalGraph("identifier_2")).isNotNull();
    }
}
