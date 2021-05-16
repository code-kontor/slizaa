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
