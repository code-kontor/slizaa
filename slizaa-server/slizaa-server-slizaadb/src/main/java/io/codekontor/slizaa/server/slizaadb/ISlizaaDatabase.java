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

import io.codekontor.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * </p>
 */
public interface ISlizaaDatabase extends IHierarchicalGraphContainer {

    /**
     * @return
     */
    String getIdentifier();

    /**
     * @return
     */
    SlizaaDatabaseState getState();

    /**
     * @return
     */
    int getPort();

    /**
     * @return
     */
    boolean hasContentDefinition();

    /**
     * @return
     */
    IContentDefinitionProvider<?> getContentDefinition();

    /**
     * @param contentDefinitionFactoryId
     * @param contentDefinition
     */
    void setContentDefinitionProvider(String contentDefinitionFactoryId, String contentDefinition);

    /**
     * <p>
     * </p>
     *
     * @throws IOException
     */
    void parse(boolean startDatabase) throws IOException;

    /**
     *
     */
    void start();

    /**
     *
     */
    void stop();

    /**
     * @return
     */
    boolean isRunning();

    /**
     *
     */
    void terminate();

    /**
     * @return
     */
    List<GraphDatabaseAction> getAvailableActions();

    void awaitState(SlizaaDatabaseState databaseState, long timeToWait) throws TimeoutException;

    /**
     * Possible actions that can executed.
     *
     * @author Gerd W&uuml;therich (gw@code-kontor.io)
     */
    enum GraphDatabaseAction {

        SET_CONTENT_DEFINITION("setContentDefinitionProvider"),
        PARSE("parse"),
        START("start"),
        CREATE_HIERARCHICAL_GRAPH("createHierarchicalGraph"),
        STOP("stop"),
        DELETE("delete");

        public String getName() {
            return _name;
        }

        GraphDatabaseAction(String name) {
            _name = name;
        }

        private String _name;
    }
}
