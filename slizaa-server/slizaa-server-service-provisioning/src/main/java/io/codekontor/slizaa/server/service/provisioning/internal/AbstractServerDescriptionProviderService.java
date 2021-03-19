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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.provisioning.IProvisioningService;
import io.codekontor.slizaa.server.service.provisioning.internal.job.JobExecuter;
import io.codekontor.slizaa.server.service.provisioning.internal.job.JobGroup;
import io.codekontor.slizaa.server.service.provisioning.model.descr.*;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import io.codekontor.slizaa.server.slizaadb.IHierarchicalGraph;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public abstract class AbstractServerDescriptionProviderService implements IProvisioningService {

    @Autowired
    private ISlizaaService _slizaaService;

    @Override
    public SlizaaServerConfigurationDescr fetchServerDescription() {

        List<ServerExtensionDescr> extensionDescriptions =
                _slizaaService.getBackendService().getInstalledExtensions()
                        .stream().map(toServerExtensionsDescr()).collect(Collectors.toList());

        List<GraphDatabaseDescr> databaseDescriptions =
                _slizaaService.getGraphDatabases().stream().map(toGraphDatabaseDescr())
                        .collect(Collectors.toList());

        return new SlizaaServerConfigurationDescr(extensionDescriptions, databaseDescriptions);
    }

    @Override
    public GraphDatabaseDescr fetchGraphDatabaseDescription(String identifier) {

        ISlizaaDatabase slizaaDatabase = _slizaaService.getGraphDatabase(identifier);

        if (slizaaDatabase != null) {
            return toGraphDatabaseDescr().apply(slizaaDatabase);
        }

        return null;
    }

    public ISlizaaService getSlizaaService() {
        return _slizaaService;
    }

    protected JobGroupDescr convertToJobGroupDescription(JobGroup jobGroup) {
        JobGroupDescr result = new JobGroupDescr(jobGroup.getId());
        jobGroup.getJobTasks().stream()
                .filter(jobFutureTask -> !(jobFutureTask.getJob().getJobTask() instanceof ProvisioningService.RemoveJobGroupTask))
                .forEach(jobFutureTask -> {
                    result.add(new JobDescr(
                            jobFutureTask.getJob().getDescription(),
                            jobFutureTask.getJob().getJobState().name()
                    ));
                }
        );
        return result;
    }

    private Function<IExtension, ServerExtensionDescr> toServerExtensionsDescr() {
        return extension -> new ServerExtensionDescr(extension.getSymbolicName(),
                extension.getVersion().toString());
    }

    private Function<ISlizaaDatabase, GraphDatabaseDescr> toGraphDatabaseDescr() {
        return graphDatabase -> {

            // map content definition
            String contentDefinitionFactoryId = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory().getFactoryId();
            String contentDefinitionShortForm = graphDatabase.getContentDefinition().getContentDefinitionProviderFactory().getShortForm();
            String contentDefinition = graphDatabase.getContentDefinition().toExternalRepresentation();
            ContentDefinitionDescr contentDefinitionDescr =
                    new ContentDefinitionDescr(contentDefinitionFactoryId, contentDefinitionShortForm, contentDefinition);

            // map hierarchicalGraphs
            List<HierarchicalGraphDescr> hierarchicalGraphDescrs = graphDatabase.getHierarchicalGraphs().stream()
                    .map(toHierarchicalGraphDescr()).collect(Collectors.toList());

            // map available actions
            List<String> availableActions = graphDatabase.getAvailableActions()
                    .stream().map(action -> action.getName()).collect(Collectors.toList());

            // map database specification
            GraphDatabaseDescr graphDatabaseSpec = new GraphDatabaseDescr(graphDatabase.getIdentifier(),
                    contentDefinitionDescr,
                    hierarchicalGraphDescrs,
                    graphDatabase.getState().name(),
                    graphDatabase.getPort(),
                    availableActions);

            // return the result
            return graphDatabaseSpec;
        };
    }

    private Function<IHierarchicalGraph, HierarchicalGraphDescr> toHierarchicalGraphDescr() {
        return hierarchicalGraph -> {

            // map database specification
            HierarchicalGraphDescr hierarchicalGraphDescr = new HierarchicalGraphDescr(hierarchicalGraph.getIdentifier());

            // return the result
            return hierarchicalGraphDescr;
        };
    }
}
