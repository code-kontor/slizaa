package io.codekontor.slizaa.server.service.provisioning.internal.job;

import java.util.concurrent.Callable;

public interface JobTask extends Callable<Boolean> {

    String getDescription();
}
