/**
 * slizaa-scanner-contentdefinition - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.contentdefinition.internal;

import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class LogicalJarVersionResolver {

  /** - */
  private static final String VERSION_PATTERN_STRING = "(\\d+\\.){1,3}.*";

  /** - */
  private static Pattern      VERSION_PATTERN        = Pattern.compile(VERSION_PATTERN_STRING);

  /**
   * <p>
   * Returns the version from the Bundle-Version manifest attribute if (correctly) set
   * </p>
   * 
   * @param manifest
   * @return
   */
  public static String extractNameFromBundleVersion(Manifest manifest) {
    // get the 'Bundle-Version' attribute
    String result = manifest.getMainAttributes().getValue("Bundle-Version");

    //
    return returnIfValid(result);
  }

  /**
   * <p>
   * </p>
   * 
   * @param manifest
   * @return
   */
  public static String extractNameFromImplementationVersion(Manifest manifest) {

    // get the 'Implementation-Title' attribute
    String result = manifest.getMainAttributes().getValue("Implementation-Version");

    if (result != null && result.startsWith("\"") && result.endsWith("\"")) {
      result = result.substring(1, result.length() - 1);
    }

    //
    return returnIfValid(result);
  }

  /**
   * <p>
   * </p>
   * 
   * @param manifest
   * @return
   */
  public static String extractNameFromSpecificationVersion(Manifest manifest) {

    // get the 'Implementation-Title' attribute
    String result = manifest.getMainAttributes().getValue("Specification-Version");

    //
    return returnIfValid(result);
  }

  /**
   * <p>
   * </p>
   * 
   * @param inputStr
   * @return
   */
  public static String extractVersionFromName(CharSequence inputStr) {

    Matcher matcher = VERSION_PATTERN.matcher(inputStr);

    if (matcher.find()) {

      String result = matcher.group();
      if (result.endsWith(".jar") || result.endsWith(".zip")) {
        result = result.substring(0, result.length() - 4);
      }

      if (result.length() > 1) {
        return result;
      }

      return null;
    }

    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param string
   * @return
   */
  private static String returnIfValid(String string) {

    //
    if (string == null) {
      return null;
    }

    //
    if (isValidVersion(string)) {
      return string;
    }

    //
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param string
   * @return
   */
  private static boolean isValidVersion(String string) {

    // Compile and use regular expression
    Matcher matcher = VERSION_PATTERN.matcher(string);
    return matcher.matches();
  }
}
