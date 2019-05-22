/**
 * slizaa-server-service-extensions - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.extensions.mvn;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class MvnDependency {

    /* - */
	@JsonProperty("dependency")
    private String _dependency;

    /* - */
	@JsonProperty("exclusionPatterns")
    private List<String> _exclusionPatterns;

    /**
     *
     */
    public MvnDependency() {
    }

    /**
     *
     * @param dependency
     */
    public MvnDependency(String dependency) {
        this._dependency = dependency;
    }

    /**
     *
     * @param dependency
     * @param exclusionPatterns
     */
    public MvnDependency(String dependency, String... exclusionPatterns) {
        this._dependency = dependency;
        this._exclusionPatterns = Arrays.asList(exclusionPatterns);
    }

    /**
     *
     * @return
     */
    public String getDependency() {
        return _dependency;
    }

    /**
     *
     * @return
     */
    public List<String> getExclusionPatterns() {
        return _exclusionPatterns;
    }
}
