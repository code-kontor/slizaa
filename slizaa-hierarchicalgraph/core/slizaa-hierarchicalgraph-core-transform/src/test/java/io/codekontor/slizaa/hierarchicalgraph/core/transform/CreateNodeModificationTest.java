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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateNodeModificationTest extends AbstractAlgorithmTest {

    private HGNode newNode;

    @Before
    public void before() {
        newNode = createGroupNode(-2, "name");
    }

    @After
    public void after() {
        newNode = null;
    }

    @Test
    public void createNodeTest() {

        // create and execute modifcation
        CreateNodeModification createNodeModification = new CreateNodeModification(rootNode(), () -> newNode);
        createNodeModification.execute();

        // assert modification result
        assertThat(rootNode().getChildren()).hasSize(3);
        rootNode().getChildren().forEach(node -> assertThat(node.getParent()).isEqualTo(rootNode()));

        // undo
        createNodeModification.undo();

        // assert initial state
        assertThat(rootNode().getChildren()).hasSize(2);
        assertThat(newNode.getParent()).isNull();
    }
}
