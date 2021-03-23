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

import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;

import java.util.List;
import java.util.Objects;

public interface ISlizaaServerConfigurationDTO {

    List<? extends IGraphDatabaseDTO> getGraphDatabases();

    static int hashCode(ISlizaaServerConfigurationDTO thizz) {
        return Objects.hash(thizz.getGraphDatabases());
    }

    static boolean equals(ISlizaaServerConfigurationDTO thizz, Object obj) {
        if (thizz == obj)
            return true;
        if (obj == null)
            return false;
        if (!ISlizaaServerConfigurationDTO.class.isAssignableFrom(obj.getClass()))
            return false;
        ISlizaaServerConfigurationDTO other = (ISlizaaServerConfigurationDTO) obj;
        // TODO: check
        return Objects.equals(thizz.getGraphDatabases(), other.getGraphDatabases());
    }
}
