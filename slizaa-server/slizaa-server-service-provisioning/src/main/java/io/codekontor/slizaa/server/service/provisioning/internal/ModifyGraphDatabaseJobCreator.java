package io.codekontor.slizaa.server.service.provisioning.internal;

import io.codekontor.slizaa.core.job.IJob;
import io.codekontor.slizaa.core.job.IJobGroup;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.CreateHierarchicalGraphJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.ParseGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.StartGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.StopGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.IHierarchicalGraphDTO;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.codekontor.slizaa.core.job.JobFactory.createJob;
import static io.codekontor.slizaa.core.job.JobFactory.createJobGroup;

public class ModifyGraphDatabaseJobCreator {

    public static IJobGroup create(ISlizaaService slizaaService, IGraphDatabaseDTO oldGraphDatabaseDTO, IGraphDatabaseDTO newGraphDatabaseDTO) {

        checkNotNull(oldGraphDatabaseDTO);
        checkNotNull(newGraphDatabaseDTO);
        checkState(oldGraphDatabaseDTO.getId().equals(newGraphDatabaseDTO.getId()));

        IJobGroup jobGroup = createJobGroup(newGraphDatabaseDTO.getId());
        ISlizaaDatabase slizaaDatabase = slizaaService.getGraphDatabase(oldGraphDatabaseDTO.getId());

        boolean parse = newGraphDatabaseDTO.getContentDefinition() != null &&
                !oldGraphDatabaseDTO.getContentDefinition().equals(newGraphDatabaseDTO.getContentDefinition());
        boolean stop = SlizaaDatabaseState.RUNNING.name().equals(oldGraphDatabaseDTO.getState()) &&
                (parse || SlizaaDatabaseState.NOT_RUNNING.name().equals(newGraphDatabaseDTO.getState()));
        boolean start = SlizaaDatabaseState.RUNNING.name().equals(newGraphDatabaseDTO.getState()) &&
                (stop || SlizaaDatabaseState.NOT_RUNNING.name().equals(oldGraphDatabaseDTO.getState()));
        boolean createHierarchicalGraph = start;

        // rebuild completely
        IJob ancestor = null;
        if (stop) {
            ancestor = createJob(new StopGraphDatabaseJobTask(slizaaService, slizaaDatabase), jobGroup);
        }
        if (parse) {
            ancestor = createJob(new ParseGraphDatabaseJobTask(slizaaService, slizaaDatabase, false), jobGroup, ancestor != null ? Collections.singleton(ancestor) : Collections.emptyList());
        }
        if (start) {
            ancestor = createJob(new StartGraphDatabaseJobTask(slizaaService, slizaaDatabase), jobGroup, ancestor != null ? Collections.singleton(ancestor) : Collections.emptyList());
        }
        if (createHierarchicalGraph) {
            for (IHierarchicalGraphDTO hierarchicalGraph : newGraphDatabaseDTO.getHierarchicalGraphs()) {
                createJob(new CreateHierarchicalGraphJobTask(slizaaService, slizaaDatabase, hierarchicalGraph.getId()), jobGroup, ancestor != null ? Collections.singleton(ancestor) : Collections.emptyList());
            }
        }
        return jobGroup;
    }
}
