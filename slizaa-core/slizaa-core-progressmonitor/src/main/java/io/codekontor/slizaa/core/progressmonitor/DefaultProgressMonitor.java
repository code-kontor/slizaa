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

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 */
public class DefaultProgressMonitor implements IProgressMonitor {

    //
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProgressMonitor.class);

    //
    protected Consumer<IProgressStatus> _progressStatusConsumer;

    //
    protected String _taskName;

    //
    protected String _subTaskName;

    //
    protected long _workDone;

    //
    protected long _totalWork;

    //
    protected List<SubMonitor> _subMonitors;

    //
    protected boolean _complete;

    /**
     *
     */
    public DefaultProgressMonitor(String name, int totalWork) {
        this(name, totalWork, null);
    }

    /**
     *
     */
    public DefaultProgressMonitor(String name, int totalWork, Consumer<IProgressStatus> progressStatusConsumer) {
        checkNotNull(name);
        checkState(totalWork >= 0, "Parameter 'totalWork' must be is greater than or equal to zero.");

        //
        _taskName = name;
        _totalWork = scaleUp(totalWork);
        _progressStatusConsumer = progressStatusConsumer;

        //
        _subMonitors = new ArrayList<>();
        _workDone = 0;
        _complete = _totalWork == 0;
    }

    /**
     * @return
     */
    public static Consumer<IProgressStatus> consoleLogger() {
        return new ConsoleLogger();
    }

    /**
     * @return
     */
    public static Consumer<IProgressStatus> nullLogger() {
        return new NullLogger();
    }

    private static int scaleDown(long value) {
        return (int) (value / 1000L);
    }

    private static long scaleUp(int value) {
        return value * 1000L;
    }

    @Override
    public String getCurrentStep() {

        //
        if (!_subMonitors.isEmpty()) {
            SubMonitor currentMonitor = _subMonitors.get(_subMonitors.size() - 1);
            if (!currentMonitor.isComplete() && currentMonitor.getCurrentStep() != null) {
                return _subMonitors.get(_subMonitors.size() - 1).getCurrentStep();
            }
        }

        //
        if (_subTaskName != null) {
            return _subTaskName;
        }

        //
        return _taskName;
    }

    @Override
    public boolean isComplete() {
        return _complete;
    }

    @Override
    public int getWorkDoneInTicks() {
        return scaleDown(internalGetWorkDoneInTicks());
    }

    @Override
    public int getTotalWorkTicks() {
        return scaleDown(internalGetTotalWorkTicks());
    }

    @Override
    public int getWorkDoneInPercentage() {
        return (int) (((float) internalGetWorkDoneInTicks() / (float) _totalWork) * 100);
    }


    /**
     * @param name
     */
    @Override
    public void step(String name) {

        //
        if (_complete) {
            LOGGER.warn("Calling 'step({}) on a completed progress monitor.'", name);
            return;
        }

        //
        _subTaskName = name;
    }

    /**
     * @param work
     */
    @Override
    public void advance(int work) {
        checkState(work >= 0, "Parameter work has be greater than or equal 0.");

        //
        if (_complete) {
            LOGGER.warn("Calling 'advance({}) on a completed progress monitor.'", work);
            return;
        }

        // close all sub-monitors (if not already closed)
        _subMonitors.forEach(subMonitor -> {
            if (!subMonitor.isComplete()) {
                subMonitor.close();
            }
        });

        //
        long totalSubMonitorWork = accumulatedSubMonitorTotalWork();
        if (_workDone + totalSubMonitorWork + scaleUp(work) > _totalWork) {
            LOGGER.warn("Calling 'advance({}) results in work done > total work.'", work);
            _workDone = _totalWork - totalSubMonitorWork;
        }
        //
        else {
            _workDone = _workDone + scaleUp(work);
        }

        //
        fireProgressStatus();
    }

    /**
     * @param taskName
     * @return
     */
    @Override
    public ISubProgressMonitorCreator subTask(String taskName) {

        //
        checkState(!_complete,
                "Can not create a new sub task because the parent progress monitor already has been completed.");

        //
        return new DefaultSubProgressMonitorCreator(taskName, this);
    }

    /**
     *
     */
    @Override
    public void close() {
        _workDone = _totalWork;
        _subTaskName = null;
        _complete = true;

        //
        fireProgressStatus();
    }

    /**
     *
     */
    public void dump() {
        System.out.println(dump(0));
    }

    /**
     * @param indent
     */
    protected String dump(int indent) {

        //
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Strings.repeat("  ", indent));
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append(": Work Done: ");
        stringBuilder.append(getWorkDoneInTicks());
        stringBuilder.append(", Work Total: ");
        stringBuilder.append(getTotalWorkTicks());
        stringBuilder.append(", WorkDoneInPercentage: ");
        stringBuilder.append(getWorkDoneInPercentage());
        if (this instanceof SubMonitor) {
            stringBuilder.append(", Parent Ticks: ");
            stringBuilder.append(((SubMonitor) this).parentTicks);
            stringBuilder.append("/");
            stringBuilder.append(((SubMonitor) this).parent._totalWork);
        }

        for (SubMonitor subMonitor : _subMonitors) {
            stringBuilder.append("\n");
            stringBuilder.append(subMonitor.dump(indent + 1));
        }

        //
        return stringBuilder.toString();
    }

    /**
     * @return
     */
    protected DefaultProgressMonitor getRootMonitor() {
        return this;
    }

    /**
     * @return
     */
    protected long internalGetWorkDoneInTicks() {
        return _complete ? _totalWork : _workDone + accumulatedSubMonitorWorkDone();
    }

    /**
     * @return
     */
    protected long internalGetTotalWorkTicks() {
        return _totalWork;
    }

    /**
     * @return
     */
    private long accumulatedSubMonitorWorkDone() {
        return (long) _subMonitors.stream()
                .mapToDouble(sm -> {
                    double workDone =
                            ((double) sm.parentTicks * (double) sm.internalGetWorkDoneInTicks()) / (double) sm._totalWork;
                    return workDone;
                }).sum();
    }

    private long accumulatedSubMonitorTotalWork() {
        return _subMonitors.stream()
                .mapToLong(sm -> sm.parentTicks).sum();
    }

    /**
     *
     */
    private void fireProgressStatus() {

        //
        DefaultProgressMonitor rootMonitor = getRootMonitor();

        if (rootMonitor._progressStatusConsumer != null) {
            rootMonitor._progressStatusConsumer.accept(rootMonitor);
        }
    }

    /**
     * The console logger
     */
    private static class ConsoleLogger implements Consumer<IProgressStatus> {

        //
        private PrintStream printStream;

        //
        private int _lastLoggedPercentage = -1;

        /**
         *
         */
        public ConsoleLogger() {
            this(System.out);
        }


        /**
         * @param printStream
         */
        public ConsoleLogger(PrintStream printStream) {
            this.printStream = checkNotNull(printStream);
        }

        /**
         * @param progressStatus
         */
        @Override
        public void accept(IProgressStatus progressStatus) {
            int workDoneInPercentage = progressStatus.getWorkDoneInPercentage();
            if (_lastLoggedPercentage != workDoneInPercentage) {
                _lastLoggedPercentage = workDoneInPercentage;
                System.out.println(String.format("%s%% (%s)", _lastLoggedPercentage, progressStatus.getCurrentStep()));
            }
        }
    }

    private static class NullLogger implements Consumer<IProgressStatus> {

        @Override
        public void accept(IProgressStatus progressStatus) {
            // ignore
        }
    }

    /**
     *
     */
    private class SubMonitor extends DefaultProgressMonitor {

        /* - */
        private long parentTicks;

        /* - */
        private DefaultProgressMonitor parent;

        /**
         * @param parentTicks
         */
        public SubMonitor(String name, long parentTicks, int totalWork, DefaultProgressMonitor parent) {
            super(name, totalWork);

            //
            this.parentTicks = parentTicks;
            this.parent = checkNotNull(parent);

            //
            parent._subMonitors.add(this);
        }

        public void dump() {
            getRootMonitor().dump();
        }

        protected DefaultProgressMonitor getRootMonitor() {
            if (parent instanceof SubMonitor) {
                return ((SubMonitor) parent).getRootMonitor();
            }
            return parent;
        }
    }

    /**
     *
     */
    private class DefaultSubProgressMonitorCreator implements ISubProgressMonitorCreator {

        //
        private String _name;

        //
        private DefaultProgressMonitor _parent;

        //
        private int _parentPercentage;

        //
        private int _parentWorkTicks = -1;

        //
        private int _totalWork;

        /**
         * @param name
         */
        public DefaultSubProgressMonitorCreator(String name, DefaultProgressMonitor parent) {
            this._name = checkNotNull(name);
            this._parent = parent;
        }

        @Override
        public ISubProgressMonitorCreator withParentConsumptionInPercentage(int percentage) {
            _parentPercentage = percentage;
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withParentConsumptionInWorkTicks(int parentWorkTicks) {
            _parentWorkTicks = parentWorkTicks;
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withTotalWorkTicks(int totalWork) {
            _totalWork = totalWork;
            return this;
        }

        @Override
        public IProgressMonitor create() {
            long parentWorkTicks = _parentWorkTicks > 0 ?
                    scaleUp(_parentWorkTicks) :
                    (long) ((double) _parentPercentage * (double) _parent._totalWork / 100.0);
            return new SubMonitor(this._name, parentWorkTicks, _totalWork, _parent);
        }
    }
}
