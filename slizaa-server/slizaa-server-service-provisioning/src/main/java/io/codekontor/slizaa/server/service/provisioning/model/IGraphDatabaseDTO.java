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

import java.util.List;
import java.util.Objects;

public interface IGraphDatabaseDTO {

    String getId();

    String getState();

    IContentDefinitionDTO getContentDefinition();

    List<? extends IHierarchicalGraphDTO> getHierarchicalGraphs();

    static int hashCode(IGraphDatabaseDTO graphDatabaseDTO) {
        return Objects.hash(graphDatabaseDTO.getContentDefinition(), graphDatabaseDTO.getHierarchicalGraphs(), graphDatabaseDTO.getId(), graphDatabaseDTO.getState());
    }

    static boolean equals(IGraphDatabaseDTO thizz, Object o) {
        if (thizz == o)
            return true;
        if (o == null)
            return false;
        if (!IGraphDatabaseDTO.class.isAssignableFrom(o.getClass()))
            return false;
        IGraphDatabaseDTO other = (IGraphDatabaseDTO) o;
        return Objects.equals(thizz.getContentDefinition(), other.getContentDefinition())
                && Objects.equals(thizz.getHierarchicalGraphs(), other.getHierarchicalGraphs())
                && Objects.equals(thizz.getId(), other.getId())
                && Objects.equals(thizz.getState(), other.getState());
    }
}
