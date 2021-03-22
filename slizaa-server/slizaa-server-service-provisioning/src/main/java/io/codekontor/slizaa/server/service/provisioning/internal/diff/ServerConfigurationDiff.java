/**
 * slizaa-server-service-provisioning - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.provisioning.internal.diff;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.MapDifference.ValueDifference;
import io.codekontor.slizaa.server.service.provisioning.model.diff.IServerConfigurationDiff;

public class ServerConfigurationDiff<T> implements IServerConfigurationDiff<T> {

  /** - */
  private List<T>                  _componentsToRemove;

  /** - */
  private List<T>                  _componentsToCreate;

  /** - */
  private List<ValueDifference<T>> _componentsToModify;


  /**
   * <p>
   * Creates a new instance of type {@link ServerConfigurationDiff}.
   * </p>
   */
  public ServerConfigurationDiff(Collection<T> componentsToRemove,
                                 Collection<T> componentsToCreate,
                                 Collection<ValueDifference<T>> componentsToModify) {

    checkNotNull(componentsToRemove);
    checkNotNull(componentsToCreate);
    checkNotNull(componentsToModify);

    _componentsToRemove = Collections.unmodifiableList(new LinkedList<>(componentsToRemove));
    _componentsToCreate = Collections.unmodifiableList(new LinkedList<>(componentsToCreate));
    _componentsToModify = Collections.unmodifiableList(new LinkedList<>(componentsToModify));
  }

  @Override
  public List<T> getComponentsToRemove() {
    return _componentsToRemove;
  }

  @Override
  public List<T> getComponentsToCreate() {
    return _componentsToCreate;
  }

  @Override
  public List<ValueDifference<T>> getComponentsToModify() {
    return _componentsToModify;
  }
}
