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
package io.codekontor.slizaa.server.service.extensions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Default implementation of type {@link IExtensionIdentifier}.
 */
public class ExtensionIdentifier implements IExtensionIdentifier {

    /* the symbolic name */
	@JsonProperty("symbolicName")
    protected String _symbolicName;

    /* the version */
	@JsonProperty("version")
	protected Version _version;

    /**
     * Creates a new instance of type {@link IExtensionIdentifier}.
     *
     * @param symbolicName
     * @param version
     */
    public ExtensionIdentifier(String symbolicName, Version version) {
        this._symbolicName = checkNotNull(symbolicName);
        this._version = checkNotNull(version);
    }
    
    protected ExtensionIdentifier() {
    }

    /**
     * Creates a new instance of type {@link IExtensionIdentifier}.
     *
     * @param symbolicName
     * @param version
     */
    public ExtensionIdentifier(String symbolicName, String version) {
        this._symbolicName = checkNotNull(symbolicName);
        this._version = Version.parseVersion(checkNotNull(version));
    }

    @Override
    public String getSymbolicName() {
        return _symbolicName;
    }

    @Override
    public Version getVersion() {
        return _version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IExtensionIdentifier)) return false;
        IExtensionIdentifier that = (IExtensionIdentifier) o;
        return Objects.equals(_symbolicName, that.getSymbolicName()) &&
                Objects.equals(_version, that.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_symbolicName, _version);
    }
}
