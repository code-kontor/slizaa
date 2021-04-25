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
