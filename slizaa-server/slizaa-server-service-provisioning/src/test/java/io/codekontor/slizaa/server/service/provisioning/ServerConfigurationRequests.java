package io.codekontor.slizaa.server.service.provisioning;

import com.google.common.io.Files;
import io.codekontor.slizaa.server.service.provisioning.model.request.ContentDefinitionRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.GraphDatabaseRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.HierarchicalGraphRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;

import java.io.File;
import java.util.Collections;

public class ServerConfigurationRequests {

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_nodb() {

        return new SlizaaServerConfigurationRequest(Collections.emptyList());
    }

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_db01() {

        File tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier");

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db01",
                new ContentDefinitionRequest("directory", tempDirectory.getAbsolutePath()),
                Collections.singletonList(hierarchicalGraphRequest));

        return new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));
    }

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_db02_v1() {

        File tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier_1");

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db02",
                new ContentDefinitionRequest("directory", tempDirectory.getAbsolutePath()),
                Collections.singletonList(hierarchicalGraphRequest));

        return new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));
    }

    public static SlizaaServerConfigurationRequest getServerConfigurationRequest_db02_v2() {

        File tempDirectory = Files.createTempDir();
        tempDirectory.deleteOnExit();

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier_2");

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db02",
                new ContentDefinitionRequest("directory", tempDirectory.getAbsolutePath()),
                Collections.singletonList(hierarchicalGraphRequest));

        return new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));
    }
}
