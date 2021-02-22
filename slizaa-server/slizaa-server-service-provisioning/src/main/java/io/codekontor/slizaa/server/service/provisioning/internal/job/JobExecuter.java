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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
@Component
public class JobExecuter implements InitializingBean, DisposableBean {

  /** the executor service */
  private ExecutorService _executorService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    _executorService = Executors.newFixedThreadPool(2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void destroy() throws Exception {
    _executorService.shutdown();
    _executorService.awaitTermination(300, TimeUnit.SECONDS);
  }

  /**
   * <p>
   * </p>
   *
   * @param job
   */
  public void executeJobGroup(JobGroup jobGroup) {

    // execute asynchronous
    for (JobFutureTask task : jobGroup.getJobTasks()) {
      _executorService.execute(task);
    }
  }
}
