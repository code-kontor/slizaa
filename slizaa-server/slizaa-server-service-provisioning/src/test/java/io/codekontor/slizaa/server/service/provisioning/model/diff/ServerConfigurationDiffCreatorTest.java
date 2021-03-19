package io.codekontor.slizaa.server.service.provisioning.model.diff;

import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.descr.ContentDefinitionDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.HierarchicalGraphDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.SlizaaServerConfigurationDescr;
import io.codekontor.slizaa.server.service.provisioning.model.request.ContentDefinitionRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.GraphDatabaseRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;

public class ServerConfigurationDiffCreatorTest {

    @Test
    public void testIt() {

        GraphDatabaseRequest graphDatabaseRequest = new GraphDatabaseRequest(
                "db01",
                new ContentDefinitionRequest("factoryIdShortForm", "definition"),
                Collections.emptyList());

        SlizaaServerConfigurationRequest serverConfigurationRequest = new SlizaaServerConfigurationRequest(Collections.singletonList(graphDatabaseRequest));

        GraphDatabaseDescr graphDatabaseDescription = new GraphDatabaseDescr(
                "db01",
                new ContentDefinitionDescr("factoryId", "factoryIdShortForm2", "definition"),
                Collections.emptyList(),
                "Running",
                1234,
                Collections.emptyList());

        SlizaaServerConfigurationDescr serverConfigurationDescr = new SlizaaServerConfigurationDescr(Collections.emptyList(), Collections.singletonList(graphDatabaseDescription));

        IServerConfigurationDiff<IGraphDatabaseDTO>  serverConfigurationDiff =
                ServerConfigurationDiffCreator.createGraphDatabaseDiff(serverConfigurationDescr, serverConfigurationRequest);

        System.out.println(serverConfigurationDiff.getComponentsToCreate());
        System.out.println(serverConfigurationDiff.getComponentsToRemove());

        serverConfigurationDiff.getComponentsToModify().forEach(
                vd -> {
                    System.out.println("old: " + vd.leftValue());
                    System.out.println("new: " + vd.rightValue());
                }
        );

    }
}
