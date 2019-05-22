/**
 * slizaa-core-progressmonitor - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.progressmonitor;

/**
 *
 */
public class NullProgressMonitor implements IProgressMonitor {

    @Override
    public void close() {
        //
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getCurrentStep() {
        return null;
    }

    @Override
    public int getWorkDoneInTicks() {
        return 0;
    }

    @Override
    public int getWorkDoneInPercentage() {
        return 0;
    }

    @Override
    public int getTotalWorkTicks() {
        return 0;
    }

    @Override
    public void step(String name) {

    }

    @Override
    public void advance(int work) {

    }

    @Override
    public ISubProgressMonitorCreator subTask(String taskName) {
        return new NullProgressMonitorCreator();
    }

    private static class NullProgressMonitorCreator implements ISubProgressMonitorCreator {
        @Override
        public ISubProgressMonitorCreator withParentConsumptionInPercentage(int percentage) {
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withTotalWorkTicks(int totalWork) {
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withParentConsumptionInWorkTicks(int parentWorkTicks) {
            return this;
        }

        @Override
        public IProgressMonitor create() {
            return new NullProgressMonitor();
        }
    }

}
