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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class NamedLock {

  /** logger */
  public static final Log                   LOGGER  = LogFactory.getLog(NamedLock.class);

  /** - */
  private static final long                 TIMEOUT = 10000;

  /** - */
  private final ConcurrentMap<String, Lock> _lockByName;

  /** - */
  private final ConcurrentMap<String, Long> _lockTime;

  /** - */
  private final ScheduledExecutorService    _scheduler;

  /**
   * <p>
   * Creates a new instance of type {@link NamedLock}.
   * </p>
   */
  public NamedLock() {
    _lockByName = new ConcurrentHashMap<>();
    _lockTime = new ConcurrentHashMap<>();
    _scheduler = Executors.newScheduledThreadPool(1);
    _scheduler.scheduleAtFixedRate(() -> {

      //
      try {
        long currentTime = System.currentTimeMillis();
        _lockTime.forEach((key, locktime) -> {
          if ((currentTime - locktime) >= TIMEOUT) {
            LOGGER.warn(String.format("ALERT: Key '%s' is locked for %s ms!", key, currentTime - locktime));
          }
        });
      } catch (Exception e) {
        // ignore silently...
      }

    }, 1, 1, TimeUnit.MINUTES);
  }

  /**
   * <p>
   * </p>
   * 
   * @param key
   */
  public void lock(String key) {
    checkNotNull(key);

    //
    if (!_lockByName.containsKey(key)) {
      _lockByName.putIfAbsent(key, new ReentrantLock());
    }

    //
    _lockByName.get(key).lock();
    _lockTime.put(key, System.currentTimeMillis());
  }

  private void checkNotNull(String key) {
  }

  /**
   * <p>
   * </p>
   * 
   * @param key
   */
  public void unlock(String key) {
    checkNotNull(key);

    //
    _lockByName.get(key).unlock();
    _lockTime.remove(key);
  }
}