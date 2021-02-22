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

import io.codekontor.slizaa.server.service.provisioning.internal.job.sorter.DependencyGraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class JobGroup {

  /** - */
  private long                _environmentId;

  /** - */
  private String              _environmentComponentId;

  /** - */
  private List<JobFutureTask> _jobTasks;

  /**
   * <p>
   * Creates a new instance of type {@link JobGroup}.
   * </p>
   *
   * @param environmentId
   */
  public JobGroup(long environmentId, String environmentComponentId, Job... jobs) {

    //
    _environmentId = environmentId;
    _environmentComponentId = checkNotNull(environmentComponentId);
    _jobTasks = new LinkedList<>();

    //
    for (Job job : jobs) {
      JobFutureTask task = new JobFutureTask(checkNotNull(job));
      _jobTasks.add(task);
      job.setSharedLock(this);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public long getEnvironmentId() {
    return _environmentId;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public long getEnvironmentIdAsLong() {
    return _environmentId;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getEnvironmentComponentId() {
    return _environmentComponentId;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<JobFutureTask> getJobTasks() {

    //
    DependencyGraph<JobFutureTask> dependencyGraph = new DependencyGraph<>();

    // step 1: add vertices
    for (JobFutureTask jobFutureTask : _jobTasks) {
      dependencyGraph.addVertex(jobFutureTask);
    }

    // step 2: add edges
    for (JobFutureTask jobFutureTask : _jobTasks) {
      for (Job job : jobFutureTask.getJob().getAncestors()) {
        dependencyGraph.addEdge(jobFutureTask, job.getFutureTask());
      }
    }

    //
    return Collections.unmodifiableList(dependencyGraph.calculateOrder());
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public boolean hasPendingJobs() {
    for (JobFutureTask task : _jobTasks) {
      if (!(task.isDone() || task.isCancelled())) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>
   * </p>
   *
   */
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
  }
}