/**
 * slizaa-scanner-cypherregistry - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatement;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CypherRegistryUtils {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static List<ICypherStatement> getCypherStatementsFromClasspath(Class<?> clazz) {

    // create test class loader
    ClassLoader classLoader = new URLClassLoader(
        new URL[] { checkNotNull(clazz).getProtectionDomain().getCodeSource().getLocation() });

    //
    return getCypherStatementsFromClasspath(classLoader);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static List<ICypherStatement> getCypherStatementsFromClasspath(ClassLoader... classLoaders) {

    //
    List<ICypherStatement> statements = new ArrayList<>();

    //
    try (ScanResult scanResult =
             new ClassGraph()
                 .overrideClassLoaders(classLoaders)
                 .scan()) {

      scanResult
          .getResourcesWithExtension("cypher")
          .forEachByteArray((Resource res, byte[] fileContent) -> {
            DefaultCypherStatement cypherStatement = SlizaaCypherFileParser
                .parse(res.getPathRelativeToClasspathElement(), new String(fileContent));
            cypherStatement.setRelativePath(res.getPathRelativeToClasspathElement());
            if (cypherStatement.isValid()) {
              statements.add(cypherStatement);
            }
          });
    }

    //
    return statements;
  }
}
