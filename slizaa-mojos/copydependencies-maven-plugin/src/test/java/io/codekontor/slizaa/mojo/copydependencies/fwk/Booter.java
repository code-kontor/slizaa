/**
 * copydependencies-maven-plugin - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.mojo.copydependencies.fwk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;

/**
 * A helper to boot the repository system and a repository system session.
 */
public class Booter
{

  /** - */
  private static final String LOCAL_REPO = System.getProperty("user.home") + File.separator + ".m2"
      + File.separator + "repository";

  /**
   * <p>
   * </p>
   *
   * @param system
   * @return
   */
  public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system)
  {
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

    LocalRepository localRepo = new LocalRepository(LOCAL_REPO);
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

    session.setTransferListener(new ConsoleTransferListener());
    session.setRepositoryListener(new NullConsoleRepositoryListener());

    // uncomment to generate dirty trees
    // session.setDependencyGraphTransformer( null );

    return session;
  }

  /**
   * <p>
   * </p>
   *
   * @param system
   * @param session
   * @return
   */
  public static List<RemoteRepository> newRepositories(RepositorySystem system,
      RepositorySystemSession session)
  {
    return new ArrayList<RemoteRepository>(Arrays.asList(newCentralRepository()));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static RemoteRepository newCentralRepository()
  {
    return new RemoteRepository.Builder("central", "default", "http://repo1.maven.org/maven2")
        .build();
  }
}
