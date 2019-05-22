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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 *
 */
@Component
public class NodeResolver implements GraphQLResolver<Node> {

  //
  @Autowired
  private ISlizaaService slizaaService;

  public String getIconIdentifier(Node node) {

    ILabelDefinitionProvider.ILabelDefinition labelDefinition = node.labelDefinitionProvider()
        .getLabelDefinition(node.getHgNode());

    if (labelDefinition.isOverlayImage()) {
      return slizaaService.getSvgService().createSvgAndReturnShortKey(labelDefinition.getBaseImagePath(),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.TOP_LEFT),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.TOP_RIGHT),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.BOTTOM_LEFT),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.BOTTOM_RIGHT));
    } else {
      return slizaaService.getSvgService().createSvgAndReturnShortKey(labelDefinition.getBaseImagePath());
    }
  }

  /**
   * @param node
   * @return
   */
  public Optional<Node> getParent(Node node) {
    return node.getHgNode().getParent() != null ? Optional.of(new Node(node.getHgNode().getParent()))
        : Optional.empty();
  }

  /**
   *
   * @param node
   * @return
   */
  public NodeSet getChildren(Node node) {
    List<HGNode> hgNodes = node.getHgNode().getChildren().stream().collect(Collectors.toList());
    return new NodeSet(hgNodes);
  }

  public boolean hasChildren(Node node) {
    return !node.getHgNode().getChildren().isEmpty();
  }

  /**
   *
   * @param node
   * @return
   */
  public List<Node> getPredecessors(Node node) {
    return node.getHgNode().getPredecessors().stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }

  /**
   * @param node
   * @return
   */
  public List<MapEntry> getProperties(Node node) {

    // TODO: GraphQL - Extension?
    GraphDbNodeSource nodeSource = node.getHgNode().getNodeSource(GraphDbNodeSource.class).get();
    return nodeSource.getProperties().stream().map(entry -> new MapEntry(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  public List<Dependency> getDependenciesTo(Node node, List<String> targetNodeIds) {

    // TODO!
    return Collections.singletonList(new Dependency(null, null, 1));
  }
}
