/**
 * slizaa-core-job - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.job;

import io.codekontor.slizaa.core.job.internal.Job;
import io.codekontor.slizaa.core.job.internal.JobGroup;
import io.codekontor.slizaa.core.job.internal.ProvisioningPlan;
import io.codekontor.slizaa.core.job.internal.ProvisioningPlanExecutorService;

import java.util.Arrays;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class JobFactory {

    public static IProvisioningPlanExecutorService createProvisioningPlanExecutorService() {
        return new ProvisioningPlanExecutorService();
    }

    public static IJobGroup createJobGroup(String id) {
        return new JobGroup(id);
    }

    public static IJob createJob(IJobTask jobTask, IJobGroup jobGroup) {
        return new Job(jobTask, jobGroup);
    }

    public static IJob createJob(IJobTask jobTask, IJobGroup jobGroup, IJob... ancestors) {
        return createJob(jobTask, jobGroup, Arrays.asList(ancestors));
    }

    public static IJob createJob(IJobTask jobTask, IJobGroup jobGroup, Collection<IJob> ancestors) {
        Job result = new Job(jobTask, jobGroup);
        checkNotNull(ancestors).forEach(a -> result.addAncestor(a));
        return result;
    }

    public static IProvisioningPlan createProvisioningPlan(String id,  Collection<IJobGroup> jobGroups) {
        ProvisioningPlan result = new ProvisioningPlan(id);
        result.addJobGroups(jobGroups);
        return result;
    }

    public static IProvisioningPlan createProvisioningPlan(String id,  IJobGroup... jobGroups) {
        return createProvisioningPlan(id, Arrays.asList(jobGroups));
    }
}
