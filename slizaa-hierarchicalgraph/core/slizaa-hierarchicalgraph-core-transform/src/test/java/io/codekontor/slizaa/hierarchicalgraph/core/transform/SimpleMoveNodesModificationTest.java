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
package io.codekontor.slizaa.hierarchicalgraph.core.transform;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.transform.internal.CreateNodeModification;
import io.codekontor.slizaa.hierarchicalgraph.core.transform.internal.SimpleMoveNodesModification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleMoveNodesModificationTest extends AbstractAlgorithmTest {

    private HGNode _newNode;

    private CreateNodeModification _createNodeModification;

    @Before
    public void before() {
        _newNode = createGroupNode(-2, "name");
        _createNodeModification = new CreateNodeModification(rootNode(), () -> _newNode);
        _createNodeModification.execute();
        assertThat(rootNode().getChildren()).hasSize(3);
    }

    @After
    public void after() {
        _createNodeModification.undo();
        _newNode = null;
    }

    @Test
    public void simpleMoveNodesTest() {

        HGNode originalParent = node(1);
        List<HGNode> children = new ArrayList<>(originalParent.getChildren());

        // create and execute modification
        SimpleMoveNodesModification removeAndAddNodesModification = new SimpleMoveNodesModification(_newNode, children);
        removeAndAddNodesModification.execute();

        // assert modification result
        assertThat(_newNode.getChildren()).containsExactlyInAnyOrderElementsOf(children);
        assertThat(originalParent.getChildren()).hasSize(0);
        children.forEach(node -> assertThat(node.getParent()).isEqualTo(_newNode));

        // undo
        removeAndAddNodesModification.undo();

        // assert initial state
        assertThat(originalParent.getChildren()).containsExactlyInAnyOrderElementsOf(children);
        assertThat(_newNode.getChildren()).hasSize(0);
        children.forEach(node -> assertThat(node.getParent()).isEqualTo(originalParent));
    }
}
