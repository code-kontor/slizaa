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

import java.util.concurrent.FutureTask;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class JobFutureTask extends FutureTask<Void> {

  /** - */
  private Job _abstractJob;

  /**
   * <p>
   * Creates a new instance of type {@link JobFutureTask}.
   * </p>
   *
   * @param vmJob
   */
  public JobFutureTask(Job job) {
    super(job);

    //
    _abstractJob = checkNotNull(job);
    _abstractJob.setFutureTask(this);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Job getJob() {
    return _abstractJob;
  }
}
