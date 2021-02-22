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
package io.codekontor.slizaa.server.gql.hierarchicalgraph;

import graphql.kickstart.tools.GraphQLResolver;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class NodeResolver implements GraphQLResolver<Node> {

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
}
