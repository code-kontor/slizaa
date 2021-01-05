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
package io.codekontor.slizaa.server.slizaadb.internal;

import io.codekontor.slizaa.server.slizaadb.AbstractSlizaaDatabaseTest;
import io.codekontor.slizaa.server.slizaadb.SlizaaDatabaseState;
import io.codekontor.slizaa.server.slizaadb.ISlizaaDatabase;
import org.junit.Assert;

import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

public class Assertions {

    /**
     *
     * @param graphDatabase
     */
    public static void assertConfigured(ISlizaaDatabase graphDatabase) {

        assertThat(graphDatabase).isNotNull();

        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.CONFIGURED);
        assertThat(graphDatabase.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.PARSE, ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION, ISlizaaDatabase.GraphDatabaseAction.DELETE);

        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasGraphDb()).isFalse();
        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasBoltClient()).isFalse();

        assertIllegalStateException(() -> graphDatabase.start());
        assertIllegalStateException(() -> graphDatabase.stop());

        assertIllegalStateException(() -> graphDatabase.newHierarchicalGraph("hg01"));
        assertIllegalStateException(() -> graphDatabase.removeHierarchicalGraph("hg01"));
    }

    public static void assertNotRunning(ISlizaaDatabase graphDatabase) {

        assertThat(graphDatabase).isNotNull();

        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.NOT_RUNNING);
        assertThat(graphDatabase.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.PARSE, ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION, ISlizaaDatabase.GraphDatabaseAction.DELETE, ISlizaaDatabase.GraphDatabaseAction.START);

        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasGraphDb()).isFalse();
        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasBoltClient()).isFalse();

        Assertions.assertIllegalStateException(() -> graphDatabase.stop());

        Assertions.assertIllegalStateException(() -> graphDatabase.newHierarchicalGraph("hg01"));
        Assertions.assertIllegalStateException(() -> graphDatabase.removeHierarchicalGraph("hg01"));
    }

    /**
     *
     * @param graphDatabase
     */
    public static void assertRunning(ISlizaaDatabase graphDatabase) throws TimeoutException {

        assertThat(graphDatabase).isNotNull();

        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.RUNNING);
        assertThat(graphDatabase.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.DELETE, ISlizaaDatabase.GraphDatabaseAction.STOP);

        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasGraphDb()).isTrue();
        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasBoltClient()).isTrue();

        graphDatabase.newHierarchicalGraph("hg01");
        graphDatabase.awaitState(SlizaaDatabaseState.RUNNING, AbstractSlizaaDatabaseTest.TIMEOUT);
        graphDatabase.removeHierarchicalGraph("hg01");

        Assertions.assertIllegalStateException(() -> graphDatabase.start());
        Assertions.assertIllegalStateException(() -> graphDatabase.parse(true));
        Assertions.assertIllegalStateException(() -> graphDatabase.parse(false));
    }

    public static void assertInitial(ISlizaaDatabase graphDatabase) {

        assertThat(graphDatabase).isNotNull();

        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.INITIAL);
        assertThat(graphDatabase.getAvailableActions()).containsExactlyInAnyOrder(ISlizaaDatabase.GraphDatabaseAction.SET_CONTENT_DEFINITION, ISlizaaDatabase.GraphDatabaseAction.DELETE);

        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasGraphDb()).isFalse();
        assertThat((((IInternalSlizaaDatabase)graphDatabase).getStateMachineContext()).hasBoltClient()).isFalse();

        Assertions.assertIllegalStateException(() -> graphDatabase.start());
        Assertions.assertIllegalStateException(() -> graphDatabase.stop());
        Assertions.assertIllegalStateException(() -> graphDatabase.parse(true));
        Assertions.assertIllegalStateException(() -> graphDatabase.parse(false));

        Assertions.assertIllegalStateException(() -> graphDatabase.newHierarchicalGraph("hg01"));
        Assertions.assertIllegalStateException(() -> graphDatabase.removeHierarchicalGraph("hg01"));
    }

    public static void assertParsing(ISlizaaDatabase graphDatabase) {
        assertThat(graphDatabase).isNotNull();
        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.PARSING);
    }

    public static void assertStarting(ISlizaaDatabase graphDatabase) {
        assertThat(graphDatabase).isNotNull();
        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.STARTING);
    }

    public static void assertStopping(ISlizaaDatabase graphDatabase) {
        assertThat(graphDatabase).isNotNull();
        assertThat(graphDatabase.getState()).isEqualTo(SlizaaDatabaseState.STOPPING);
    }

    static void assertIllegalStateException(Assertions.Action action) {

        try {
            action.execute();
        } catch (IllegalStateException exception) {
            return;
        } catch (Exception exception) {
            exception.printStackTrace();
            Assert.fail(exception.getMessage());
        }
        Assert.fail();
    }

    @FunctionalInterface
    interface Action {
        void execute() throws Exception;
    }
}
