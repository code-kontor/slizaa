/**
 * slizaa-core-boltclient - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.boltclient.internal.asynch;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class StatementResultConsumerCallable implements Callable<Void> {

    /**
     * -
     */
    private Driver _driver;

    /**
     * -
     */
    private String _statement;

    /**
     * -
     */
    private Map<String, Object> _params;

    /**
     * the consumer
     */
    private Consumer<Result> _consumer;

    /**
     * -
     */
    private IBoltClient _boltClient;

    /**
     * <p>
     * Creates a new instance of type {@link StatementResultConsumerCallable}.
     * </p>
     *
     * @param driver
     * @param statement
     * @param params
     * @param consumer
     * @param boltClient
     */
    public StatementResultConsumerCallable(Driver driver, String statement, Map<String, Object> params,
                                           Consumer<Result> consumer, IBoltClient boltClient) {
        _driver = checkNotNull(driver);
        _statement = checkNotNull(statement);
        _params = params;
        _consumer = consumer;
        _boltClient = checkNotNull(boltClient);
    }

    /**
     * {@inheritDoc}
     */
    public Void call() throws Exception {

        try (Session session = _driver.session()) {

            //
            Result statementResult = _params == null ? session.run(_statement) : session.run(_statement, _params);

            if (_consumer != null) {

                // we have to synchronize the consumption to avoid race conditions
                synchronized (_boltClient) {
                    _consumer.accept(statementResult);
                }
            }
        }

        //
        return null;
    }
}
