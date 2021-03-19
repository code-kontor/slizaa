package io.codekontor.slizaa.server.service.provisioning.model;

import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;

import java.util.List;

public interface ISlizaaServerConfigurationDTO {

    List<? extends IGraphDatabaseDTO> getGraphDatabases();
}
