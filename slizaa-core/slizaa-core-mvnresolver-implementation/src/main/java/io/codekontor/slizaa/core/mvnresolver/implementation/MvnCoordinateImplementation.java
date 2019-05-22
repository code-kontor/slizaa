/**
 * slizaa-core-mvnresolver-implementation - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.mvnresolver.implementation;

import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinate;
import io.codekontor.slizaa.core.mvnresolver.api.IMvnCoordinate;

import static com.google.common.base.Preconditions.checkNotNull;

public class MvnCoordinateImplementation implements IMvnCoordinate {

  /** - */
  private MavenCoordinate _mavenCoordinate;

  /**
   *
   * @param mavenCoordinate
   */
  public MvnCoordinateImplementation(MavenCoordinate mavenCoordinate) {
    this._mavenCoordinate = checkNotNull(mavenCoordinate);
  }

  /**
   *
   * @return
   */
  @Override
  public String getGroupId() {
    return _mavenCoordinate.getGroupId();
  }

  /**
   *
   * @return
   */
  @Override
  public String getArtifactId() {
    return _mavenCoordinate.getArtifactId();
  }

  /**
   *
   * @return
   */
  @Override
  public String getPackagingType() {
    return _mavenCoordinate.getPackaging().toString();
  }

  /**
   *
   * @return
   */
  @Override
  public String getClassifier() {
    return _mavenCoordinate.getClassifier();
  }

  /**
   *
   * @return
   */
  @Override
  public String getVersion() {
    return _mavenCoordinate.getVersion();
  }

  /**
   *
   * @return
   */
  @Override
  public String toCanonicalForm() {
    return _mavenCoordinate.toCanonicalForm();
  }
}
