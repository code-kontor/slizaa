/**
 * slizaa-server-slizaadb - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.slizaadb;

public class SlizaaDatabaseConfiguration implements ISlizaaDatabaseConfiguration {

    private String _identifier;

    private int _port;

    private SlizaaDatabaseState _state;

    private String _contentDefinitionFactoryId;

    private String _contentDefinitionExternalRepresentation;

    public SlizaaDatabaseConfiguration(String identifier, int port, SlizaaDatabaseState state, String contentDefinitionFactoryId, String contentDefinitionExternalRepresentation) {
        this._identifier = identifier;
        this._port = port;
        this._state = state;
        this._contentDefinitionFactoryId = contentDefinitionFactoryId;
        this._contentDefinitionExternalRepresentation = contentDefinitionExternalRepresentation;
    }

    @Override
    public String getIdentifier() {
        return _identifier;
    }

    @Override
    public int getPort() {
        return _port;
    }

    @Override
    public SlizaaDatabaseState getState() {
        return _state;
    }

    @Override
    public String getContentDefinitionFactoryId() {
        return _contentDefinitionFactoryId;
    }

    @Override
    public String getContentDefinitionExternalRepresentation() {
        return _contentDefinitionExternalRepresentation;
    }

    @Override
    public boolean hasContentDefinition() {
        return _contentDefinitionFactoryId != null && _contentDefinitionExternalRepresentation != null;
    }
}
