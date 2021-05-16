package io.codekontor.slizaa.hierarchicalgraph.core.transform;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedGraph;
import io.codekontor.slizaa.hierarchicalgraph.core.testfwk.XmiBasedTestGraphProviderRule;
import org.junit.Before;
import org.junit.ClassRule;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractAlgorithmTest {

    @ClassRule
    public static XmiBasedTestGraphProviderRule _graphProvider = new XmiBasedTestGraphProviderRule(XmiBasedGraph.MAP_STRUCT);

	@Before
	public void before() {
		assertThat(rootNode().getChildren()).hasSize(2);
	}

	/**
     * 
     * @param id
     * @return
     */
	protected HGNode node(long id) {
		return _graphProvider.node(id);
	}

	protected HGRootNode rootNode() { return _graphProvider.rootNode(); }

	public static HGNode createGroupNode(long id, String name) {

		// create the node source
		DefaultNodeSource defaultNodeSource = HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
		defaultNodeSource.setIdentifier(id);
		defaultNodeSource.getLabels().add("Group");
		defaultNodeSource.getProperties().put("name", name);
		defaultNodeSource.getProperties().put("fqn", name);

		// create the node
		HGNode node = HierarchicalgraphFactory.eINSTANCE.createHGNode();
		node.setNodeSource(defaultNodeSource);
		return node;
	}
}
