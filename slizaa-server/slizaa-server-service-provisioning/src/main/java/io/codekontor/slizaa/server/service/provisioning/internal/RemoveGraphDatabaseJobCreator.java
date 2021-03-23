package io.codekontor.slizaa.server.service.provisioning.internal;

import io.codekontor.slizaa.core.job.IJobGroup;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.TerminateGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.codekontor.slizaa.core.job.JobFactory.createJob;
import static io.codekontor.slizaa.core.job.JobFactory.createJobGroup;

public class RemoveGraphDatabaseJobCreator {

    public static IJobGroup create(ISlizaaService slizaaService, IGraphDatabaseDTO graphDatabaseToRemove) {
        IJobGroup jobGroup = createJobGroup(checkNotNull(graphDatabaseToRemove).getId());
        createJob(new TerminateGraphDatabaseJobTask(slizaaService, slizaaService.getGraphDatabase(graphDatabaseToRemove.getId())), jobGroup);
        return jobGroup;
    }
}
