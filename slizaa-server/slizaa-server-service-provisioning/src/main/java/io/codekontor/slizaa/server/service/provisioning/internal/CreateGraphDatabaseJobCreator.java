package io.codekontor.slizaa.server.service.provisioning.internal;

import io.codekontor.slizaa.core.job.IJob;
import io.codekontor.slizaa.core.job.IJobGroup;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.CreateHierarchicalGraphJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.ParseGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.StartGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.IHierarchicalGraphDTO;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.codekontor.slizaa.core.job.JobFactory.createJob;
import static io.codekontor.slizaa.core.job.JobFactory.createJobGroup;

public class CreateGraphDatabaseJobCreator {

    public static IJobGroup create(ISlizaaService slizaaService, IGraphDatabaseDTO graphDatabaseToCreate) {

        IJobGroup jobGroup = createJobGroup(checkNotNull(graphDatabaseToCreate).getId());

        ISlizaaDatabase slizaaDatabase = slizaaService.newGraphDatabase(graphDatabaseToCreate.getId());
        slizaaDatabase.setContentDefinitionProvider(
                graphDatabaseToCreate.getContentDefinition().getFactoryIdShortForm(),
                graphDatabaseToCreate.getContentDefinition().getDefinition());

        IJob parseGraphDatabaseJob = createJob(new ParseGraphDatabaseJobTask(slizaaService, slizaaDatabase, false), jobGroup);

        IJob startGraphDatabaseJob = createJob(new StartGraphDatabaseJobTask(slizaaService, slizaaDatabase), jobGroup, parseGraphDatabaseJob);

        for (IHierarchicalGraphDTO hierarchicalGraph : graphDatabaseToCreate.getHierarchicalGraphs()) {
            createJob(new CreateHierarchicalGraphJobTask(slizaaService, slizaaDatabase, hierarchicalGraph.getId()), jobGroup, startGraphDatabaseJob);
        }

        return jobGroup;
    }
}
