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
package io.codekontor.slizaa.server.service.provisioning.internal.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class Job implements Callable<Void> {

    private JobState _jobState;

    private List<Job> _ancestors;

    private Object _sharedLock;

    private JobFutureTask _futureTask;

    private JobTask _jobTask;

    /**
     * <p>
     * Creates a new instance of type {@link Job}.
     * </p>
     */
    public Job(JobTask action) {
        _jobTask = checkNotNull(action);
        _jobState = JobState.NEW;
        _ancestors = new ArrayList<>();
        _sharedLock = new Object();
    }

    public Job(JobTask action, Job... ancestors) {
        this(action);
        for (Job ancestor : ancestors) {
            _ancestors.add(ancestor);
        }
    }

    public JobTask getJobTask() {
        return _jobTask;
    }

    public String getDescription() {
        return _jobTask.getDescription();
    }

    /**
     * <p>
     * </p>
     *
     * @param sharedLock
     */
    protected void setSharedLock(Object sharedLock) {
        _sharedLock = checkNotNull(sharedLock);
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public JobFutureTask getFutureTask() {
        return _futureTask;
    }

    /**
     * <p>
     * </p>
     *
     * @param futureTask
     */
    void setFutureTask(JobFutureTask futureTask) {
        _futureTask = futureTask;
    }

    /**
     * <p>
     * </p>
     *
     * @param ancestor
     */
    public void addAncestor(Job ancestor) {
        _ancestors.add(ancestor);
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

        //
        synchronized (_sharedLock) {
            while (!isExecutable() && !getJobState().equals(JobState.ANCESTOR_FAILED)) {
                try {
                    _sharedLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

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
        } finally {
            synchronized (_sharedLock) {
                _sharedLock.notifyAll();
            }
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

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public Object getSharedLock() {
        return _sharedLock;
    }
}
