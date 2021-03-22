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

import io.codekontor.slizaa.core.job.*;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.codekontor.slizaa.core.job.JobFactory.*;

public class ProvisioningTest {

    private List<String> _jobTasks = new ArrayList<>();

    @Test
    public void test_one_group() {

        IJobGroup jobGroup1 = createJobGroup("1");
        IJob job1 = createJob(new SimpleAction("1-1"), jobGroup1);
        IJob job2 = createJob(new SimpleAction("1-2"), jobGroup1, job1);
        createJob(new SimpleAction("1-3"), jobGroup1, job2);

        IProvisioningPlan provisioningPlan = createProvisioningPlan("P1", jobGroup1);

        IProvisioningPlanExecution planExecution =
                createProvisioningPlanExecutorService().executeProvisioningPlan(provisioningPlan);

        Awaitility.await().until(() -> !planExecution.hasPendingTasks());

        Assertions.assertThat(_jobTasks).containsExactly("1-1", "1-2", "1-3");
    }

    @Test
    public void test_two_groups() {

        IJobGroup jobGroup_1 = createJobGroup("1");
        IJob job1_1 = createJob(new SimpleAction("1-1"), jobGroup_1);
        IJob job1_2 = createJob(new SimpleAction("1-2"), jobGroup_1, job1_1);
        createJob(new SimpleAction("1-3"), jobGroup_1, job1_2);

        IJobGroup jobGroup_2 = createJobGroup("2");
        IJob job2_1 = createJob(new SimpleAction("2-1"), jobGroup_2);
        IJob job2_2 = createJob(new SimpleAction("2-2"), jobGroup_2, job2_1);
        createJob(new SimpleAction("2-3"), jobGroup_2, job2_2);

        IProvisioningPlan provisioningPlan = createProvisioningPlan("P1", jobGroup_1, jobGroup_2);

        IProvisioningPlanExecution planExecution =
                createProvisioningPlanExecutorService().executeProvisioningPlan(provisioningPlan);

        planExecution.registerCompletionCallback(provisioningPlanExecution -> _jobTasks.add("DONE"));
        planExecution.waitForCompletion();

        Assertions.assertThat(_jobTasks).containsExactlyInAnyOrder("1-1", "1-2", "1-3", "2-1", "2-2", "2-3", "DONE");
        Assertions.assertThat(_jobTasks).containsSubsequence("1-1", "1-2", "1-3", "DONE");
        Assertions.assertThat(_jobTasks).containsSubsequence("2-1", "2-2", "2-3", "DONE");
    }

    public class SimpleAction implements IJobTask {

        private String _description;

        public SimpleAction(String description) {
            this._description = checkNotNull(description);
        }

        @Override
        public String getDescription() {
            return _description;
        }

        @Override
        public Boolean call() throws Exception {
            _jobTasks.add(_description);
            Thread.sleep(500L);
            return true;
        }
    }
}
