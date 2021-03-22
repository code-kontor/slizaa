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
import io.codekontor.slizaa.core.job.IJobTask;
import io.codekontor.slizaa.core.job.JobState;
import io.codekontor.slizaa.core.job.internal.striped.StripedCallable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class Job implements IJob, StripedCallable<Void> {

    private JobState _jobState;

    private List<Job> _ancestors;

    private IJobTask _jobTask;

    private JobGroup _jobGroup;

    /**
     * <p>
     * Creates a new instance of type {@link Job}.
     * </p>
     */
    public Job(IJobTask action, IJobGroup jobGroup) {
        _jobTask = checkNotNull(action);
        _jobState = JobState.NEW;
        _jobGroup = (JobGroup) jobGroup;
        _jobGroup.addJob(this);
        _ancestors = new ArrayList<>();
    }

    public IJobTask getJobTask() {
        return _jobTask;
    }

    @Override
    public String getId() {
        return _jobGroup.getId();
    }

    @Override
    public String getDescription() {
        return _jobTask.getDescription();
    }

    @Override
    public Object getStripe() {
        return this.getId();
    }

    /**
     * <p>
     * </p>
     *
     * @param ancestor
     */
    public void addAncestor(IJob ancestor) {
        checkState(_jobGroup.equals(((Job)ancestor)._jobGroup));
        _ancestors.add((Job)ancestor);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public boolean isExecutable() {

        //
        for (Job abstractJob : _ancestors) {
            switch (abstractJob.getJobState()) {
                case SUCCESSFULLY_COMPLETED: {
                    continue;
                }
                case SKIPPED: {
                    continue;
                }
                case ANCESTOR_FAILED: {
                    setJobState(JobState.ANCESTOR_FAILED);
                    return false;
                }
                case FAILED: {
                    setJobState(JobState.ANCESTOR_FAILED);
                    return false;
                }
                default:
                    return false;
            }
        }
        return true;
    }

    @Override
    public final Void call() throws Exception {

        try {
            if (!getJobState().equals(JobState.ANCESTOR_FAILED)) {

                setJobState(JobState.IN_PROGRESS);
                if (_jobTask.call()) {
                    setJobState(JobState.SUCCESSFULLY_COMPLETED);
                } else {
                    setJobState(JobState.SKIPPED);
                }
            }
        } catch (Exception e) {
            setJobState(JobState.FAILED);
            throw e;
        }
        return null;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public List<Job> getAncestors() {
        return Collections.unmodifiableList(_ancestors);
    }

    /**
     * <p>
     * </p>
     *
     * @param jobState
     */
    private void setJobState(JobState jobState) {
        _jobState = checkNotNull(jobState);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public JobState getJobState() {

        //
        for (Job job : _ancestors) {
            if (JobState.ANCESTOR_FAILED.equals(job.getJobState())) {
                setJobState(JobState.ANCESTOR_FAILED);
                break;
            }
        }

        //
        return _jobState;
    }
}
