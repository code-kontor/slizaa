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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JobExecuterTest {

  private JobExecuter  _jobExecuter;

  private JobGroup _currentJobGroup;

  private List<String> _result;

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Before
  public void before() throws Exception {
    _jobExecuter = new JobExecuter();
    _jobExecuter.afterPropertiesSet();
    _result = new CopyOnWriteArrayList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @After
  public void after() throws Exception {
    _currentJobGroup.cancelPendingJobs();
    _jobExecuter.destroy();
    _result.clear();
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  public void testJob() throws Exception {

    Job job1 = new Job(simpleAction(1));
    Job job2 = new Job(simpleAction(2), job1);
    Job job3 = new Job(simpleAction(3), job2);

    _currentJobGroup = new JobGroup("test", job1, job2, job3);

    _jobExecuter.executeJobGroup(_currentJobGroup);
    Awaitility.await().until(() -> !_currentJobGroup.hasPendingJobs());

    assertThat(_result).containsExactly("Job started: 1", "Job stopped: 1", "Job started: 2", "Job stopped: 2",
        "Job started: 3", "Job stopped: 3");

    assertThat(job1.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
    assertThat(job2.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
    assertThat(job3.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  public void testParallelJobs() throws Exception {
    for (int i = 0; i < 50; i++) {
      testJobParallel();
      _result.clear();
    }
  }

  private void testJobParallel() throws Exception {

    //
    Job job1 = new Job(new SimpleAction(1));
    Job job2 = new Job(new SimpleAction(2));
    Job job3 = new Job(new SimpleAction(3));
    _currentJobGroup = new JobGroup("test", job1, job2, job3);

    //
    _jobExecuter.executeJobGroup(_currentJobGroup);
    Awaitility.await().until(() -> !_currentJobGroup.hasPendingJobs());

    //
    System.out.println(_result);
    assertThat(_result).containsOnly("Job started: 1", "Job stopped: 1", "Job started: 2", "Job stopped: 2",
        "Job started: 3", "Job stopped: 3");

    //
    assertThat(job1.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
    assertThat(job2.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
    assertThat(job3.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  public void testFailedJob() throws Exception {

    //
    Job job1 = new Job(new SimpleAction(1));
    Job job2 = new Job(new SimpleAction(2) {
      @Override
      public Boolean call() throws Exception {
        _result.add("Job started: 2");
        throw new RuntimeException();
      }
    });
    job2.addAncestor(job1);
    Job job3 = new Job(new SimpleAction(3));
    job3.addAncestor(job2);
    _currentJobGroup = new JobGroup("test", job1, job2, job3);

    //
    _jobExecuter.executeJobGroup(_currentJobGroup);
    Awaitility.await().until(() -> !_currentJobGroup.hasPendingJobs());

    //
    assertThat(_result).containsExactly("Job started: 1", "Job stopped: 1", "Job started: 2");

    //
    assertThat(job1.getJobState()).isEqualTo(JobState.SUCCESSFULLY_COMPLETED);
    assertThat(job2.getJobState()).isEqualTo(JobState.FAILED);
    assertThat(job3.getJobState()).isEqualTo(JobState.ANCESTOR_FAILED);
  }

  public JobTask simpleAction(int jobId) {
    return new SimpleAction(jobId) {
      @Override
      public Boolean call() throws Exception {
        _result.add("Job started: " + jobId);
        _result.add("Job stopped: " + jobId);
        return true;
      }
    };
  }

  public class SimpleAction implements JobTask {

    private int _jobId;

    public SimpleAction(int jobId) {
      _jobId = jobId;
    }

    @Override
    public String getDescription() {
      return "" + _jobId;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public int getJobId() {
      return _jobId;
    }

    @Override
    public Boolean call() throws Exception {
      _result.add("Job started: " + _jobId);
       Thread.sleep(250);
      _result.add("Job stopped: " + _jobId);
      return true;
    }
  }
}
