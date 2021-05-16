/**
 * slizaa-hierarchicalgraph-core-transform - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.transform.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.transform.IHierarchicalGraphModification;

import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

public class CreateNodeModification implements IHierarchicalGraphModification {

    private HGNode _parent;

    private Supplier<HGNode> _nodeSupplier;

    private HGNode _newNode;

    public CreateNodeModification(HGNode parent, Supplier<HGNode> nodeSupplier) {
        this._parent = checkNotNull(parent);
        this._nodeSupplier = checkNotNull(nodeSupplier);
    }

    @Override
    public void execute() {
        _newNode = checkNotNull(_nodeSupplier.get());
        _parent.getChildren().add(_newNode);
    }

    @Override
    public void undo() {
        _parent.getChildren().remove(_newNode);
        _newNode = null;
    }
}
