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
package io.codekontor.slizaa.server.service.provisioning;

import io.codekontor.slizaa.server.service.provisioning.internal.job.JobGroup;
import io.codekontor.slizaa.server.service.provisioning.model.IGraphDatabaseDTO;
import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.service.provisioning.model.descr.SlizaaServerConfigurationDescr;
import io.codekontor.slizaa.server.service.provisioning.model.diff.IServerConfigurationDiff;
import io.codekontor.slizaa.server.service.provisioning.model.diff.ServerConfigurationDiffCreator;
import io.codekontor.slizaa.server.service.provisioning.model.request.SlizaaServerConfigurationRequest;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IProvisioningService {

    SlizaaServerConfigurationDescr fetchServerDescription();

    GraphDatabaseDescr fetchGraphDatabaseDescription(String identifier);

    void provision(SlizaaServerConfigurationRequest serverConfigurationRequest);

    void cancelPendingJobs();

    boolean hasPendingJobs(String id);

    JobGroup getJobGroup(String id);

    boolean hasJobGroup(String id);
}
