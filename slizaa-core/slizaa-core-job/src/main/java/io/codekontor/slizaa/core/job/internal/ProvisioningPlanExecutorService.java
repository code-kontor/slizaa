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
package io.codekontor.slizaa.core.job.internal;

import io.codekontor.slizaa.core.job.IProvisioningPlan;
import io.codekontor.slizaa.core.job.IProvisioningPlanExecution;
import io.codekontor.slizaa.core.job.IProvisioningPlanExecutorService;
import io.codekontor.slizaa.core.job.internal.striped.StripedExecutorService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ProvisioningPlanExecutorService implements AutoCloseable, IProvisioningPlanExecutorService {

    /**
     * the executor service
     */
    private StripedExecutorService _executorService;

    public ProvisioningPlanExecutorService() {
        this._executorService = new StripedExecutorService();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        _executorService.shutdown();
        _executorService.awaitTermination(300, TimeUnit.SECONDS);
    }

    @Override
    public IProvisioningPlanExecution executeProvisioningPlan(IProvisioningPlan provisioningPlan) {
        checkNotNull(provisioningPlan);

        Map<Job, Future<Void>> futureMap = new HashMap<>();

        ((ProvisioningPlan) provisioningPlan).getJobGroups().forEach(jobGroup -> {
            jobGroup.getJobs().forEach(job -> {
                Future<Void> future = _executorService.submit((Job) job);
                futureMap.put((Job) job, future);
            });
        });

        return new ProvisioningPlanExecution((ProvisioningPlan) provisioningPlan, futureMap, _executorService);
    }
}
