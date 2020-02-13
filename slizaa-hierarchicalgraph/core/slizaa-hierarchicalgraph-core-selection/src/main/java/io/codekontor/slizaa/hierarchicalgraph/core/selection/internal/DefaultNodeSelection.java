/**
 * slizaa-hierarchicalgraph-core-selection - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.selection.internal;

import static com.google.common.base.Preconditions.*;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.INodeSelection;

import java.util.Collection;

public class DefaultNodeSelection implements INodeSelection {

    private Collection<HGNode> _nodes;

    private SourceOrTarget _type;

    public DefaultNodeSelection(Collection<HGNode> _nodes, SourceOrTarget _type) {
        this._nodes = checkNotNull(_nodes) ;
        this._type = checkNotNull(_type);
    }

    @Override
    public Collection<HGNode> getNodes() {
        return _nodes;
    }

    @Override
    public SourceOrTarget getType() {
        return _type;
    }
}
