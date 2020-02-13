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

import com.google.common.base.Preconditions;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;

import java.util.Objects;

public class AggregatedDependencySelectionKey {

    private HGAggregatedDependency _aggregatedDependency;

    private long _lastAccessTime;

    public AggregatedDependencySelectionKey(HGAggregatedDependency aggregatedDependency) {
        _aggregatedDependency = Preconditions.checkNotNull(aggregatedDependency);
        _lastAccessTime = System.currentTimeMillis();
    }

    public HGAggregatedDependency getAggregatedDependency() {
        return _aggregatedDependency;
    }

    public long getLastAccessTime() {
        return _lastAccessTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregatedDependencySelectionKey that = (AggregatedDependencySelectionKey) o;
        return _aggregatedDependency.equals(that._aggregatedDependency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_aggregatedDependency);
    }
}
