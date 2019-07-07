/**
 * slizaa-server-service-selection - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.selection.impl;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.DependencySet;
import io.codekontor.slizaa.server.service.selection.ISelectionService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractSelectionService implements ISelectionService {

    private Map<DependenciesSelectionKey, DependencySet> _selectionMap;

    private ScheduledExecutorService _scheduledExecutorService;

    private Object _lock = new Object();

    @PostConstruct
    public void initialize() {
        _selectionMap = new HashMap<>();

        _scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        _scheduledExecutorService.scheduleAtFixedRate(cleanupRunnable(), 0, 30, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void dispose() {

        _scheduledExecutorService.shutdown();

        try {
            if (!_scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                _scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            _scheduledExecutorService.shutdownNow();
        }
    }

    protected DependencySet getDependenciesSelection(HGAggregatedDependency aggregatedDependency) {
        DependenciesSelectionKey key = new DependenciesSelectionKey(checkNotNull(aggregatedDependency));
        synchronized (_lock) {
            DependencySet dependencySet = _selectionMap.get(key);
            if (dependencySet == null) {
                dependencySet = new DependencySet(aggregatedDependency.getCoreDependencies());
            }
            _selectionMap.put(key, dependencySet);
            return dependencySet;
        }
    }

    protected void dropSelectionsForRootNode(HGRootNode rootNode) {
        checkNotNull(rootNode);
        synchronized (_lock) {
            _selectionMap.entrySet().removeIf(entry -> rootNode.equals(entry.getKey().getAggregatedDependency().getRootNode()));
        }
    }

    private Runnable cleanupRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                // TODO
            }
        };
    }
}