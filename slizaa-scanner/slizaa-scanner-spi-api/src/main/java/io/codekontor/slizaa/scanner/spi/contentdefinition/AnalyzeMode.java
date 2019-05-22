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

/**
 * <p>
 * Specifies how an {@link IContentDefinition} entry returned by a {@link IContentDefinitionProvider} should be analyzed.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 * 
 * @noextend This class is not intended to be extended by clients.
 */
public enum AnalyzeMode {

  /** Analyze only binaries of a content entry */
  BINARIES_ONLY,

  /** Analyze sources and binaries of a content entry */
  BINARIES_AND_SOURCES,

  /** Do not analyze this content at all */
  DO_NOT_ANALYZE;

  /**
   * <p>
   * Returns {@code true} if this instance either is {@link #BINARIES_ONLY} or {@link #BINARIES_AND_SOURCES},
   * {@code false} otherwise.
   * </p>
   * 
   * @return {@code true} if this instance either is {@link #BINARIES_ONLY} or {@link #BINARIES_AND_SOURCES},
   *         {@code false} otherwise.
   */
  public boolean isAnalyze() {
    return this == BINARIES_ONLY || this == BINARIES_AND_SOURCES;
  }
}
