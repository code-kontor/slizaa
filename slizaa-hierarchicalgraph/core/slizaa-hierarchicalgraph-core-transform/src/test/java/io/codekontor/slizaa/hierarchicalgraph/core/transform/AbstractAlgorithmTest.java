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
