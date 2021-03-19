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
package io.codekontor.slizaa.server.service.provisioning.model.request;

import io.codekontor.slizaa.server.service.provisioning.model.IContentDefinitionDTO;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

/**
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ContentDefinitionRequest implements IContentDefinitionDTO {

  private String factoryIdShortForm;
  private String definition;

  public ContentDefinitionRequest() {
    // default constructor
  }

  public ContentDefinitionRequest(String factoryIdShortForm, String definition) {
    this.factoryIdShortForm = factoryIdShortForm;
    this.definition = definition;
  }

  public String getFactoryIdShortForm() {
    return factoryIdShortForm;
  }

  public String getDefinition() {
    return definition;
  }

  public void setFactoryIdShortForm(String factoryIdShortForm) {
    this.factoryIdShortForm = checkNotNull(factoryIdShortForm);
  }

  public void setDefinition(String definition) {
    this.definition = checkNotNull(definition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(definition, factoryIdShortForm);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!IContentDefinitionDTO.class.isAssignableFrom(obj.getClass()))
      return false;
    IContentDefinitionDTO other = (IContentDefinitionDTO) obj;
    return Objects.equals(definition, other.getDefinition()) && Objects.equals(factoryIdShortForm, other.getFactoryIdShortForm());
  }
}
