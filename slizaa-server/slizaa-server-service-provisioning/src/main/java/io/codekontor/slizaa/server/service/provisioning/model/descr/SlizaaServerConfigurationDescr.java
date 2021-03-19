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

import io.codekontor.slizaa.server.service.provisioning.model.ISlizaaServerConfigurationDTO;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class SlizaaServerConfigurationDescr implements ISlizaaServerConfigurationDTO {

    private List<ServerExtensionDescr> serverExtensions;

    private List<GraphDatabaseDescr> graphDatabases;

    private Collection<JobGroupDescr> jobGroups;

    /**
     * <p>
     * Creates a new instance of type {@link SlizaaServerConfigurationDescr}.
     * </p>
     *
     * @param serverExtensions
     * @param graphDatabases
     */
    public SlizaaServerConfigurationDescr(List<ServerExtensionDescr> serverExtensions, List<GraphDatabaseDescr> graphDatabases) {
        this.serverExtensions = checkNotNull(serverExtensions);
        this.graphDatabases = checkNotNull(graphDatabases);
    }

    public void setJobGroups(Collection<JobGroupDescr> jobGroups) {
        this.jobGroups = jobGroups;
    }

    public Collection<JobGroupDescr> getJobGroups() {
        return jobGroups;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public List<ServerExtensionDescr> getServerExtensions() {
        return serverExtensions;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public List<GraphDatabaseDescr> getGraphDatabases() {
        return graphDatabases;
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphDatabases);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!ISlizaaServerConfigurationDTO.class.isAssignableFrom(obj.getClass()))
            return false;
        ISlizaaServerConfigurationDTO other = (ISlizaaServerConfigurationDTO) obj;
        return Objects.equals(graphDatabases, other.getGraphDatabases());
    }


}
