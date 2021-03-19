package io.codekontor.slizaa.server.service.provisioning.model;

import java.util.List;

public interface IGraphDatabaseDTO {

    String getId();

    IContentDefinitionDTO getContentDefinition();

    List<? extends IHierarchicalGraphDTO> getHierarchicalGraphs();
}
