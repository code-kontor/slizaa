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
import io.codekontor.slizaa.core.job.internal.sorter.DependencyGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class JobGroup implements IJobGroup {

    private String _id;

    private List<Job> _unorderedJobs;

    private List<IJob> _orderedJobs;

    /**
     * <p>
     * Creates a new instance of type {@link JobGroup}.
     * </p>
     */
    public JobGroup(String id) {
        _id = checkNotNull(id);
        _unorderedJobs = new ArrayList<>();
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public String getId() {
        return _id;
    }

    public List<IJob> getJobs() {
        if (_orderedJobs == null) {
            DependencyGraph<IJob> dependencyGraph = new DependencyGraph<>();
            _unorderedJobs.forEach(job -> dependencyGraph.addVertex(job));
            _unorderedJobs.forEach(job -> (job).getAncestors().forEach(ancestor -> dependencyGraph.addEdge(job, ancestor)));
            _orderedJobs = Collections.unmodifiableList(
                    dependencyGraph.calculateOrder().stream().map(j -> (Job) j).collect(Collectors.toList()));
        }
        return _orderedJobs;
    }

    void addJob(Job job) {
        _unorderedJobs.add(job);
    }

    /* *//**
     * <p>
     * </p>
     *
     * @return
     *//*
    public boolean hasPendingJobs() {
        for (JobFutureTask task : _jobTasks) {
            if (!(task.isDone() || task.isCancelled())) {
                return true;
            }
        }
        return false;
    }

    *//**
     * <p>
     * </p>
     *//*
    public void cancelPendingJobs() {
        for (JobFutureTask task : _jobTasks) {
            try {
                task.cancel(true);
            } catch (Exception e) {
                // ignore
                e.printStackTrace();
            }
        }
    }

    public boolean hasFailedJobs() {
        for (JobFutureTask task : _jobTasks) {
            JobState jobState = task.getJob().getJobState();
            if (JobState.FAILED.equals(jobState) || JobState.ANCESTOR_FAILED.equals(jobState)) {
                return true;
            }
        }
        return false;
    }*/
}