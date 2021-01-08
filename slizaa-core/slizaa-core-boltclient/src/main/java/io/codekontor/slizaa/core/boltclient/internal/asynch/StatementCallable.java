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

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class StatementCallable<R> implements Callable<R> {

    private Driver _driver;

    /**
     * -
     */
    private String _statement;

    /**
     * -
     */
    private Function<Result, R> _function;

    /**
     * -
     */
    private Map<String, Object> _params;

    /**
     * <p>
     * Creates a new instance of type {@link StatementCallable}.
     * </p>
     *
     * @param driver
     * @param statement
     * @param params
     */
    public StatementCallable(Driver driver, String statement, Map<String, Object> params,
                             Function<Result, R> function) {
        this._driver = checkNotNull(driver);
        this._statement = checkNotNull(statement);
        this._function = checkNotNull(function);
        this._params = params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public R call() throws Exception {

        try (Session session = this._driver.session()) {

            if (this._params == null) {
                Result statementResult = session.run(this._statement);
                return this._function.apply(statementResult);
            }
            //
            else {
              Result statementResult = session.run(this._statement, this._params);
                return this._function.apply(statementResult);
            }
        }
    }
}
