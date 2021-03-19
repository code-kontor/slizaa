/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.server.service.provisioning.internal;

import io.codekontor.slizaa.server.service.provisioning.IProvisioningService;
import io.codekontor.slizaa.server.service.provisioning.internal.job.Job;
import io.codekontor.slizaa.server.service.provisioning.internal.job.JobExecuter;
import io.codekontor.slizaa.server.service.provisioning.internal.job.JobGroup;
import io.codekontor.slizaa.server.service.provisioning.internal.job.JobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.CreateHierarchicalGraphJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.ParseGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.StopGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.TerminateGraphDatabaseJobTask;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.IHierarchicalGraphDTO;
import io.codekontor.slizaa.server.service.provisioning.model.descr.SlizaaServerConfigurationDescr;
import io.codekontor.slizaa.server.service.provisioning.model.diff.IServerConfigurationDiff;
import io.codekontor.slizaa.server.service.provisioning.model.diff.ServerConfigurationDiffCreator;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class ProvisioningService extends AbstractServerDescriptionProviderService implements IProvisioningService, InitializingBean, DisposableBean {

    @Autowired
    private JobExecuter _jobExecutor;

    private ConcurrentHashMap<String, JobGroup> _graphDatabase2JobGroupMap;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        _graphDatabase2JobGroupMap = new ConcurrentHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() throws Exception {
        _jobExecutor.destroy();
        _jobExecutor = null;

        _graphDatabase2JobGroupMap.clear();
        _graphDatabase2JobGroupMap = null;
    }

    @Override
    public SlizaaServerConfigurationDescr fetchServerDescription() {

        SlizaaServerConfigurationDescr serverConfigurationDescr = super.fetchServerDescription();
        serverConfigurationDescr.setJobGroups(_graphDatabase2JobGroupMap.values().stream().map(jobGroup -> convertToJobGroupDescription(jobGroup)).collect(Collectors.toList()));

        return serverConfigurationDescr;
    }

    @Override
    public void provision(SlizaaServerConfigurationRequest serverConfigurationRequest) {
        checkNotNull(serverConfigurationRequest);

        SlizaaServerConfigurationDescr currentConfiguration = this.fetchServerDescription();

        IServerConfigurationDiff<IGraphDatabaseDTO> graphDatabaseDiff = ServerConfigurationDiffCreator.createGraphDatabaseDiff(currentConfiguration, serverConfigurationRequest);

        graphDatabaseDiff.getComponentsToRemove().forEach(graphDatabaseDTO -> removeGraphDatabase(graphDatabaseDTO));
        graphDatabaseDiff.getComponentsToCreate().forEach(graphDatabaseDTO -> createGraphDatabase(graphDatabaseDTO));
        graphDatabaseDiff.getComponentsToModify().forEach(dtoValueDifference -> modifyGraphDatabase(dtoValueDifference.leftValue(), dtoValueDifference.rightValue()));
    }

    @Override
    public void cancelPendingJobs() {
        for (JobGroup jobGroup : _graphDatabase2JobGroupMap.values()) {
            jobGroup.cancelPendingJobs();
        }
    }

    @Override
    public boolean hasPendingJobs(String id) {
        JobGroup jobGroup = _graphDatabase2JobGroupMap.get(checkNotNull(id));
        return jobGroup != null && jobGroup.hasPendingJobs();
    }

    @Override
    public JobGroup getJobGroup(String id) {
        return _graphDatabase2JobGroupMap.get(checkNotNull(id));
    }

    @Override
    public boolean hasJobGroup(String id) {
        return _graphDatabase2JobGroupMap.containsKey(checkNotNull(id));
    }

    protected void removeGraphDatabase(IGraphDatabaseDTO graphDatabaseToRemove) {

        checkNotNull(graphDatabaseToRemove);

        // execute job
        ISlizaaDatabase slizaaDatabase = this.getSlizaaService().getGraphDatabase(graphDatabaseToRemove.getId());
        Job terminateGraphDatabaseJob = new Job(new TerminateGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase));
        Job removeJobGroupJob = new Job(new RemoveJobGroupTask(graphDatabaseToRemove.getId()));
        removeJobGroupJob.addAncestor(terminateGraphDatabaseJob);
        JobGroup jobGroup = new JobGroup(graphDatabaseToRemove.getId(), terminateGraphDatabaseJob, removeJobGroupJob);

        //
        executeJobGroup(jobGroup);
    }

    protected void createGraphDatabase(IGraphDatabaseDTO graphDatabaseToCreate) {

        checkNotNull(graphDatabaseToCreate);

        ISlizaaDatabase slizaaDatabase = getSlizaaService().newGraphDatabase(graphDatabaseToCreate.getId());
        slizaaDatabase.setContentDefinitionProvider(
                graphDatabaseToCreate.getContentDefinition().getFactoryIdShortForm(),
                graphDatabaseToCreate.getContentDefinition().getDefinition());

        List<Job> jobList = new ArrayList<>();

        Job parseGraphDatabaseJob = new Job(new ParseGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase, true));
        jobList.add(parseGraphDatabaseJob);

        for (IHierarchicalGraphDTO hierarchicalGraph : graphDatabaseToCreate.getHierarchicalGraphs()) {
            Job createHierarchicalGraphJob = new Job(new CreateHierarchicalGraphJobTask(this.getSlizaaService(), slizaaDatabase, hierarchicalGraph.getId()));
            createHierarchicalGraphJob.addAncestor(parseGraphDatabaseJob);
            jobList.add(createHierarchicalGraphJob);
        }

        executeJobGroup(new JobGroup(graphDatabaseToCreate.getId(), jobList.toArray(new Job[0])));
    }

    protected void modifyGraphDatabase(IGraphDatabaseDTO oldGraphDatabaseDTO, IGraphDatabaseDTO newGraphDatabaseDTO) {

        checkNotNull(oldGraphDatabaseDTO);
        checkNotNull(newGraphDatabaseDTO);
        checkState(oldGraphDatabaseDTO.getId().equals(newGraphDatabaseDTO.getId()));

        List<Job> jobList = new ArrayList<>();

        ISlizaaDatabase slizaaDatabase = this.getSlizaaService().getGraphDatabase(oldGraphDatabaseDTO.getId());
        Job stopGraphDatabaseJob = new Job(new StopGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase));
        jobList.add(stopGraphDatabaseJob);

        Job parseGraphDatabaseJob = new Job(new ParseGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase, true));
        parseGraphDatabaseJob.addAncestor(stopGraphDatabaseJob);
        jobList.add(parseGraphDatabaseJob);

        for (IHierarchicalGraphDTO hierarchicalGraph : newGraphDatabaseDTO.getHierarchicalGraphs()) {
            Job createHierarchicalGraphJob = new Job(new CreateHierarchicalGraphJobTask(this.getSlizaaService(), slizaaDatabase, hierarchicalGraph.getId()));
            createHierarchicalGraphJob.addAncestor(parseGraphDatabaseJob);
            jobList.add(createHierarchicalGraphJob);
        }

        executeJobGroup(new JobGroup(newGraphDatabaseDTO.getId(), jobList.toArray(new Job[0])));
    }

    private void executeJobGroup(JobGroup jobGroup) {

        // add to map...
        _graphDatabase2JobGroupMap.put(jobGroup.getId(), jobGroup);

        // ...and execute
        _jobExecutor.executeJobGroup(jobGroup);
    }

    class RemoveJobGroupTask implements JobTask {

        private String _identifier;

        public RemoveJobGroupTask(String identifier) {
            this._identifier = identifier;
        }

        @Override
        public String getDescription() {
            return String.format("RemoveJobGroupTask [%s]", _identifier);
        }

        @Override
        public Boolean call() throws Exception {
            ProvisioningService.this._graphDatabase2JobGroupMap.remove(_identifier);
            return true;
        }
    }
}
