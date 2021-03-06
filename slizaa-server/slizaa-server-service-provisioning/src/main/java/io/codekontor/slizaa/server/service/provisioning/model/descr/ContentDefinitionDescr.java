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
package io.codekontor.slizaa.server.service.provisioning.model.descr;

import io.codekontor.slizaa.server.service.provisioning.model.IContentDefinitionDTO;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ContentDefinitionDescr implements IContentDefinitionDTO {

  private String factoryId;
  private String factoryIdShortForm;
  private String definition;

  public String getFactoryId() {
    return factoryId;
  }

  public String getFactoryIdShortForm() {
    return factoryIdShortForm;
  }

  public String getDefinition() {
    return definition;
  }

  public ContentDefinitionDescr(String factoryId, String factoryIdShortForm, String definition) {
    this.factoryId = checkNotNull(factoryId);
    this.factoryIdShortForm = checkNotNull(factoryIdShortForm);
    this.definition = checkNotNull(definition);
  }

  @Override
  public int hashCode() {
    return IContentDefinitionDTO.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return IContentDefinitionDTO.equals(this, obj);
  }
}
