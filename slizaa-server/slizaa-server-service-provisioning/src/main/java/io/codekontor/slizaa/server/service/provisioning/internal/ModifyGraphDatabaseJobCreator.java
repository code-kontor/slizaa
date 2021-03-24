package io.codekontor.slizaa.server.service.provisioning.internal;

import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import io.codekontor.slizaa.core.job.IJob;
import io.codekontor.slizaa.core.job.IJobGroup;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.*;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.IHierarchicalGraphDTO;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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
        boolean createHierarchicalGraphs = SlizaaDatabaseState.RUNNING.name().equals(newGraphDatabaseDTO.getState());

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
        if (createHierarchicalGraphs) {

            MapDifference<String, IHierarchicalGraphDTO> diff = computeHierarchicalGraphDiff(oldGraphDatabaseDTO, newGraphDatabaseDTO);

            // remove old hierarchical graphs
            for (IHierarchicalGraphDTO hierarchicalGraph : diff.entriesOnlyOnLeft().values()) {
                createJob(new RemoveHierarchicalGraphJobTask(slizaaService, slizaaDatabase, hierarchicalGraph.getId()), jobGroup, ancestor != null ? Collections.singleton(ancestor) : Collections.emptyList());
            }

            // add new hierarchical graphs
            for (IHierarchicalGraphDTO hierarchicalGraph : diff.entriesOnlyOnRight().values()) {
                createJob(new CreateHierarchicalGraphJobTask(slizaaService, slizaaDatabase, hierarchicalGraph.getId()), jobGroup, ancestor != null ? Collections.singleton(ancestor) : Collections.emptyList());
            }

            // modify existing
            for (IHierarchicalGraphDTO hierarchicalGraph : newGraphDatabaseDTO.getHierarchicalGraphs()) {
                if (start) {
                    createJob(new CreateHierarchicalGraphJobTask(slizaaService, slizaaDatabase, hierarchicalGraph.getId()), jobGroup, ancestor != null ? Collections.singleton(ancestor) : Collections.emptyList());
                }
            }
        }
        return jobGroup;
    }

    private static MapDifference<String, IHierarchicalGraphDTO> computeHierarchicalGraphDiff(IGraphDatabaseDTO oldGraphDatabaseDTO, IGraphDatabaseDTO newGraphDatabaseDTO) {
        Map<String, IHierarchicalGraphDTO> oldHierarchicalGraphs = oldGraphDatabaseDTO.getHierarchicalGraphs().stream().collect(Collectors.toMap(hg -> hg.getId(), hg -> hg));
        Map<String, IHierarchicalGraphDTO> newHierarchicalGraphs = newGraphDatabaseDTO.getHierarchicalGraphs().stream().collect(Collectors.toMap(hg -> hg.getId(), hg -> hg));
        return Maps.difference(oldHierarchicalGraphs,
                newHierarchicalGraphs, new Equivalence<IHierarchicalGraphDTO>() {
                    @Override
                    protected boolean doEquivalent(IHierarchicalGraphDTO hg1, IHierarchicalGraphDTO hg2) {
                        return hg1.equals(hg2);
                    }

                    @Override
                    protected int doHash(IHierarchicalGraphDTO hg) {
                        return hg.hashCode();
                    }
                });
    }
}
