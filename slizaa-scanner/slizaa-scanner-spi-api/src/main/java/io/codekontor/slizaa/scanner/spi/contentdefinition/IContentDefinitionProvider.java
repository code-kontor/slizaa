/**
 * slizaa-scanner-spi-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.spi.contentdefinition;

import java.util.List;

/**
 * <p>
 * Represents the definition of the system that should be analyzed.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public interface IContentDefinitionProvider<T extends IContentDefinitionProvider<T>> {

  /**
   * 
   * @return
   */
  IContentDefinitionProviderFactory<T> getContentDefinitionProviderFactory();
  
  /**
   * 
   * @return
   */
  String toExternalRepresentation();

  /**
   * <p>
   * Returns a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   * </p>
   *
   * @return a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   */
  List<? extends IContentDefinition> getContentDefinitions();
}
