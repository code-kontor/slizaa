/**
 * slizaa-core-job - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.job.internal;

import io.codekontor.slizaa.core.job.IJobGroup;
import io.codekontor.slizaa.core.job.IProvisioningPlan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProvisioningPlan implements IProvisioningPlan {

    private String _identifier;

    private List<IJobGroup> _jobGroups;

    public ProvisioningPlan(String identifier) {
        this._identifier = checkNotNull(identifier);
        _jobGroups = new ArrayList<>();
    }

    public void addJobGroups(Collection<IJobGroup> jobGroups) {
        _jobGroups.addAll(jobGroups);
    }

    public String getIdentifier() {
        return _identifier;
    }

    public List<IJobGroup> getJobGroups() {
        return _jobGroups;
    }


}
