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
