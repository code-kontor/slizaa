/**
 * slizaa-hierarchicalgraph-graphdb-mapping-service - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.internal;

import java.util.ArrayList;
import java.util.List;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider.ILabelDefinition;


/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class GraphModifier implements IGraphModifier {
  
  private long counter = -2l;


  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @return
   */
  @Override
  public HGRootNode modify(HGRootNode rootNode) {
    
    //
    rootNode.invalidateNodeIdCache();
    rootNode.invalidateAllCaches();
    
    ILabelDefinitionProvider labelDefinitionProvider = rootNode.getExtension(ILabelDefinitionProvider.class);

    // TODO: do something here
    
    // return 
    return rootNode;
  }
  
  /**
   * 
   * <p>
   * </p>
   *
   * @param name
   * @return
   */
  private HGNode createGroup(String name) {
    
    DefaultNodeSource defaultNodeSource = HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
    defaultNodeSource.setIdentifier(counter--);
    defaultNodeSource.getLabels().add("Group");
    defaultNodeSource.getProperties().put("name", name);
    defaultNodeSource.getProperties().put("fqn", name);
    HGNode node = HierarchicalgraphFactory.eINSTANCE.createHGNode();
    node.setNodeSource(defaultNodeSource);
    
    //
    return node;
  }
}
