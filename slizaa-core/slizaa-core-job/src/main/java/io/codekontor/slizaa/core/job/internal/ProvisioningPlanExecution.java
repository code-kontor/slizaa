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


import io.codekontor.slizaa.core.job.IJob;
import io.codekontor.slizaa.core.job.IJobGroup;
import io.codekontor.slizaa.core.job.IProvisioningPlanExecution;
import io.codekontor.slizaa.core.job.internal.striped.StripedExecutorService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProvisioningPlanExecution implements IProvisioningPlanExecution {

    private ProvisioningPlan _provisioningPlan;

    private Map<Job, Future<Void>> _futureMap;

    private Map<ICompletionCallback, Future<Void>> _completionCallbackFutureMap;

    private StripedExecutorService _executorService;

    public ProvisioningPlanExecution(ProvisioningPlan provisioningPlan, Map<Job, Future<Void>> futureMap, StripedExecutorService executorService) {
        this._provisioningPlan = provisioningPlan;
        this._futureMap = futureMap;
        this._executorService = executorService;
    }

    @Override
    public boolean hasPendingTasks() {
        return _futureMap.values().stream().anyMatch(future -> !future.isCancelled() && !future.isDone());
    }

    @Override
    public List<IJobGroup> getJobGroups() {
        return Collections.unmodifiableList(_provisioningPlan.getJobGroups());
    }

    @Override
    public List<IJob> getJobs() {
        return Collections.unmodifiableList(_provisioningPlan.getJobGroups().stream().flatMap(jobGroup -> jobGroup.getJobs().stream()).collect(Collectors.toList()));
    }

    @Override
    public void waitForCompletion() {
        waitForCompletionWithoutCompletionCallbacks();
        _completionCallbackFutureMap.values().forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException exception) {
                // ignore
            }
        });
    }

    @Override
    // TODO
    public void registerCompletionCallback(ICompletionCallback completionCallback) {
        Future<Void> future =  _executorService.submit(new BouncerTask(checkNotNull(completionCallback)));
        if (_completionCallbackFutureMap == null) {
            _completionCallbackFutureMap = new HashMap<>();
        }
        _completionCallbackFutureMap.put(completionCallback, future);
    }

    private void waitForCompletionWithoutCompletionCallbacks() {
        _futureMap.values().forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException exception) {
                // ignore
            }
        });
    }

    private class BouncerTask implements Callable<Void> {

        private ICompletionCallback _completionCallback;

        public BouncerTask(ICompletionCallback completionCallback) {
            this._completionCallback = completionCallback;
        }

        @Override
        public Void call() throws Exception {
            waitForCompletionWithoutCompletionCallbacks();
            _completionCallback.executionCompleted(ProvisioningPlanExecution.this);
            return null;
        }
    }
}
