/**
 * ecoregenerator-maven-plugin - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.mojos.ecoregenerator;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class MonitorToMavenLogAdapter extends BasicMonitor {

  /** - */
  protected Log _mavenLog;

  public MonitorToMavenLogAdapter(Log mavenLog) {
    this._mavenLog = checkNotNull(mavenLog);
  }

  @Override
  public void beginTask(String name, int totalWork) {
    if (name != null && name.length() != 0) {
      this._mavenLog.info(">>> " + name);
    }
  }

  @Override
  public void setTaskName(String name) {
    if (name != null && name.length() != 0) {
      this._mavenLog.info("<>> " + name);
    }
  }

  @Override
  public void subTask(String name) {
    if (name != null && name.length() != 0) {
      this._mavenLog.info(">>  " + name);
    }
  }

  @Override
  public void setBlocked(Diagnostic reason) {
    super.setBlocked(reason);
    this._mavenLog.info("#>  " + reason.getMessage());
  }

  @Override
  public void clearBlocked() {
    this._mavenLog.info("=>  " + getBlockedReason().getMessage());
    super.clearBlocked();
  }
}
