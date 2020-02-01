/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

import com.google.common.base.Preconditions;
import io.codekontor.slizaa.hierarchicalgraph.core.model.AbstractHGDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;

/**
 *
 */
public class Dependency {

    public static final String AGGREGATED_DEPENDENCY_TYPE = "DEPENDS ON";

    private AbstractHGDependency _hgDependency;

    public Dependency(AbstractHGDependency hgDependency) {
        _hgDependency = Preconditions.checkNotNull(hgDependency);
    }

    public Node getSourceNode() {
        return new Node(_hgDependency.getFrom());
    }

    public Node getTargetNode() {
        return new Node(_hgDependency.getTo());
    }

    public int getWeight() {
        return _hgDependency instanceof HGCoreDependency ?
                ((HGCoreDependency) _hgDependency).getWeight() :
                ((HGAggregatedDependency) _hgDependency).getAggregatedWeight();
    }

    public String getType() {
        return _hgDependency instanceof HGCoreDependency ?
                ((HGCoreDependency) _hgDependency).getType() :
                AGGREGATED_DEPENDENCY_TYPE;
    }
}
