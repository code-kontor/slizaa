package io.codekontor.slizaa.server.service.provisioning.model;

import io.codekontor.slizaa.server.service.provisioning.model.descr.ContentDefinitionDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.HierarchicalGraphDescr;
import io.codekontor.slizaa.server.service.provisioning.model.request.ContentDefinitionRequest;
import io.codekontor.slizaa.server.service.provisioning.model.request.HierarchicalGraphRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ModelTest {

    @Test
    public void testContentDefinitionDTO() {

        ContentDefinitionRequest contentDefinitionRequest = new ContentDefinitionRequest();
        contentDefinitionRequest.setDefinition("definition");
        contentDefinitionRequest.setFactoryIdShortForm("factoryIdShortForm");

        ContentDefinitionDescr contentDefinitionDescr = new ContentDefinitionDescr("factoryId", "factoryIdShortForm", "definition");

        Assertions.assertThat(contentDefinitionRequest.equals(contentDefinitionDescr)).isTrue();
        Assertions.assertThat(contentDefinitionDescr.equals(contentDefinitionRequest)).isTrue();
    }

    @Test
    public void testHierarchicalGraphDTO() {

        HierarchicalGraphRequest hierarchicalGraphRequest = new HierarchicalGraphRequest();
        hierarchicalGraphRequest.setId("identifier");

        HierarchicalGraphDescr hierarchicalGraphDescr = new HierarchicalGraphDescr("identifier");

        Assertions.assertThat(hierarchicalGraphRequest.equals(hierarchicalGraphDescr)).isTrue();
        Assertions.assertThat(hierarchicalGraphDescr.equals(hierarchicalGraphDescr)).isTrue();
    }
}
