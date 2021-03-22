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

import io.codekontor.slizaa.core.job.*;
import io.codekontor.slizaa.server.service.provisioning.IProvisioningService;
import io.codekontor.slizaa.server.service.provisioning.internal.tasks.*;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.IHierarchicalGraphDTO;
import io.codekontor.slizaa.server.service.provisioning.model.descr.ProvisioningPlanDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.SlizaaServerConfigurationDescr;
import io.codekontor.slizaa.server.service.provisioning.model.diff.IServerConfigurationDiff;
import io.codekontor.slizaa.server.service.provisioning.model.diff.ServerConfigurationDiffCreator;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.codekontor.slizaa.core.job.JobFactory.*;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class ProvisioningService extends AbstractServerDescriptionProviderService implements IProvisioningService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvisioningService.class);

    private IProvisioningPlanExecutorService _provisioningPlanExecutorService;

    private IProvisioningPlanExecution _currentProvisioningPlanExecution;

    @Override
    public void afterPropertiesSet() throws Exception {
        _provisioningPlanExecutorService = JobFactory.createProvisioningPlanExecutorService();
    }

    @Override
    public SlizaaServerConfigurationDescr fetchServerDescription() {

        SlizaaServerConfigurationDescr serverConfigurationDescr = super.fetchServerDescription();
        if (_currentProvisioningPlanExecution != null) {
            ProvisioningPlanDescr provisioningPlanDescr = new ProvisioningPlanDescr();
            provisioningPlanDescr.setJobGroups(_currentProvisioningPlanExecution.getJobGroups().stream().map(jobGroup -> convertToJobGroupDescription(jobGroup)).collect(Collectors.toList()));
            serverConfigurationDescr.setProvisioningPlanDescr(provisioningPlanDescr);
        }
        // TODO
        //serverConfigurationDescr.setProvisioningPlanDescr(_graphDatabase2JobGroupMap.values().stream());

        return serverConfigurationDescr;
    }

    @Override
    public IProvisioningPlanExecution provision(SlizaaServerConfigurationRequest serverConfigurationRequest) {

        checkNotNull(serverConfigurationRequest);

        LOGGER.info("Provision configuration request.");

        SlizaaServerConfigurationDescr currentConfiguration = this.fetchServerDescription();

        LOGGER.info("Computing provisioning plan.");

        IServerConfigurationDiff<IGraphDatabaseDTO> graphDatabaseDiff = ServerConfigurationDiffCreator.createGraphDatabaseDiff(currentConfiguration, serverConfigurationRequest);

        LOGGER.info("Executing provisioning plan.");

        List<IJobGroup> jobGroups = new ArrayList<>();
        jobGroups.addAll(graphDatabaseDiff.getComponentsToRemove().stream().map(graphDatabaseDTO -> removeGraphDatabase(graphDatabaseDTO)).collect(Collectors.toList()));
        jobGroups.addAll(graphDatabaseDiff.getComponentsToCreate().stream().map(graphDatabaseDTO -> createGraphDatabase(graphDatabaseDTO)).collect(Collectors.toList()));
        jobGroups.addAll(graphDatabaseDiff.getComponentsToModify().stream().map(dtoValueDifference -> modifyGraphDatabase(dtoValueDifference.leftValue(), dtoValueDifference.rightValue())).collect(Collectors.toList()));


        // TODO: ID
        // TODO: WHAT SHOULD I SAVE?
        IProvisioningPlan provisioningPlan = createProvisioningPlan("123", jobGroups);
        _currentProvisioningPlanExecution = _provisioningPlanExecutorService.executeProvisioningPlan(provisioningPlan);
        _currentProvisioningPlanExecution.registerCompletionCallback(e -> {
            LOGGER.info("Provisioning plan executed.");
            ProvisioningService.this.getSlizaaService().storeConfiguration();
        });
        return _currentProvisioningPlanExecution;
    }

    private IJobGroup modifyGraphDatabase(IGraphDatabaseDTO oldGraphDatabaseDTO, IGraphDatabaseDTO newGraphDatabaseDTO) {

        checkNotNull(oldGraphDatabaseDTO);
        checkNotNull(newGraphDatabaseDTO);
        checkState(oldGraphDatabaseDTO.getId().equals(newGraphDatabaseDTO.getId()));

        IJobGroup jobGroup = createJobGroup(newGraphDatabaseDTO.getId());

        ISlizaaDatabase slizaaDatabase = this.getSlizaaService().getGraphDatabase(oldGraphDatabaseDTO.getId());
        IJob stopGraphDatabaseJob = createJob(new StopGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase), jobGroup);

        IJob parseGraphDatabaseJob = createJob(new ParseGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase, false), jobGroup, stopGraphDatabaseJob);

        IJob startGraphDatabaseJob = createJob(new StartGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase), jobGroup, parseGraphDatabaseJob);

        for (IHierarchicalGraphDTO hierarchicalGraph : newGraphDatabaseDTO.getHierarchicalGraphs()) {
            createJob(new CreateHierarchicalGraphJobTask(this.getSlizaaService(), slizaaDatabase, hierarchicalGraph.getId()), jobGroup, startGraphDatabaseJob);
        }

        return jobGroup;
    }

    private IJobGroup removeGraphDatabase(IGraphDatabaseDTO graphDatabaseToRemove) {
        IJobGroup jobGroup = createJobGroup(checkNotNull(graphDatabaseToRemove).getId());
        createJob(new TerminateGraphDatabaseJobTask(this.getSlizaaService(), this.getSlizaaService().getGraphDatabase(graphDatabaseToRemove.getId())), jobGroup);
        return jobGroup;
    }

    protected IJobGroup createGraphDatabase(IGraphDatabaseDTO graphDatabaseToCreate) {

        IJobGroup jobGroup = createJobGroup(checkNotNull(graphDatabaseToCreate).getId());

        ISlizaaDatabase slizaaDatabase = getSlizaaService().newGraphDatabase(graphDatabaseToCreate.getId());
        slizaaDatabase.setContentDefinitionProvider(
                graphDatabaseToCreate.getContentDefinition().getFactoryIdShortForm(),
                graphDatabaseToCreate.getContentDefinition().getDefinition());

        IJob parseGraphDatabaseJob = createJob(new ParseGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase, false), jobGroup);

        IJob startGraphDatabaseJob = createJob(new StartGraphDatabaseJobTask(this.getSlizaaService(), slizaaDatabase), jobGroup, parseGraphDatabaseJob);

        for (IHierarchicalGraphDTO hierarchicalGraph : graphDatabaseToCreate.getHierarchicalGraphs()) {
            createJob(new CreateHierarchicalGraphJobTask(this.getSlizaaService(), slizaaDatabase, hierarchicalGraph.getId()), jobGroup, startGraphDatabaseJob);
        }

        return jobGroup;
    }
}
