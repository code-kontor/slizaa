/**
 * slizaa-hierarchicalgraph-graphdb-mapping-spi - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

public interface ILabelDefinitionProvider {

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public static enum OverlayPosition {
    TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT;
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
   */
  public interface ILabelDefinition {

    /**
     * 
     * @return
     */
    boolean isOverlayImage();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    boolean hasBaseImage();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getBaseImagePath();

    /**
     * <p>
     * </p>
     *
     * @param overlayPosition
     * @return
     */
    boolean hasOverlayImage(OverlayPosition overlayPosition);

    /**
     * <p>
     * </p>
     *
     * @param overlayPosition
     * @return
     */
    String getOverlayImagePath(OverlayPosition overlayPosition);

    /**
     * <p>
     * </p>
     *
     * @return
     */
    String getText();
  }

  /**
   * <p>
   * </p>
   */
  ILabelDefinition getLabelDefinition(HGNode node);
}
