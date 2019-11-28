package io.codekontor.slizaa.server.service.slizaa.internal.graphdatabase;

import sun.misc.FileURLMapper;

import java.io.File;

public interface IGraphDatabaseConfiguration {
    String getIdentifier();

    int getPort();

    File getDirectory();
}
