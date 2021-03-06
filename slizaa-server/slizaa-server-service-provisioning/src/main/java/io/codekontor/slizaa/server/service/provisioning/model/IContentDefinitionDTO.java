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
package io.codekontor.slizaa.server.service.provisioning.model;

import java.util.Objects;

public interface IContentDefinitionDTO {

    String getFactoryIdShortForm();

    String getDefinition();

    static int hashCode(IContentDefinitionDTO contentDefinition) {
        return Objects.hash(contentDefinition.getDefinition(), contentDefinition.getFactoryIdShortForm());
    }

    static boolean equals(IContentDefinitionDTO thizz, Object o) {
        if (thizz == o)
            return true;
        if (o == null)
            return false;
        if (!IContentDefinitionDTO.class.isAssignableFrom(o.getClass()))
            return false;
        IContentDefinitionDTO other = (IContentDefinitionDTO) o;
        return Objects.equals(thizz.getDefinition(), other.getDefinition()) && Objects.equals(thizz.getFactoryIdShortForm(), other.getFactoryIdShortForm());
    }
}
