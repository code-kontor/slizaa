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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

public class SynchronizedSlizaaDatabaseInvocationHandler implements InvocationHandler {

    private SlizaaDatabaseImpl _target;

    private NamedLock _namedLock;

    public SynchronizedSlizaaDatabaseInvocationHandler(SlizaaDatabaseImpl target, NamedLock namedLock) {
        this._target = checkNotNull(target);
        this._namedLock = checkNotNull(namedLock);
    }

    public SlizaaDatabaseImpl getTarget() {
        return _target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("getStateMachineContext".equals(method.getName())) {
            return _target.stateMachineContext();
        }

        try {
            _namedLock.lock(_target.getIdentifier());
            return method.invoke(_target, args);
        }
        catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
        finally {
            _namedLock.unlock(_target.getIdentifier());
        }
    }
}
