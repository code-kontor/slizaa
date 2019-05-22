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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.codekontor.slizaa.scanner.cypherregistry.impl.CypherNormalizer;

import com.google.common.io.ByteSource;

public class SlizaaCypherFileParser {

  private static final String COMMENT_PATTERN = "/\\*(?:.|[\\n\\r])*?\\*/";

  private static final String DOCLET_PATTERN  = "\\x40(\\S*)\\s*(.*)";


  public static DefaultCypherStatement parse(String relativePath, InputStream inputStream) {

    //
    String fileContent;

    //
    try {
      fileContent = readFile(inputStream, Charset.defaultCharset());
    } catch (IOException e) {
      return null;
    }

    //
    return parse(relativePath, fileContent);
  }

  public static DefaultCypherStatement parse(String relativePath, String content) {

    //
    Pattern pattern_comment = Pattern.compile(COMMENT_PATTERN);
    Pattern pattern_doclet = Pattern.compile(DOCLET_PATTERN);
    Matcher matcher_comment = pattern_comment.matcher(content);

    //
    DefaultCypherStatement defaultCypherStatement = new DefaultCypherStatement();

    //
    defaultCypherStatement.setStatement(CypherNormalizer.normalize(content));

    //
    if (matcher_comment.find()) {

      //
      String comment = matcher_comment.group(0);

      //
      Matcher matcher_doclet = pattern_doclet.matcher(comment);
      while (matcher_doclet.find()) {

        //
        if ("slizaa.groupId".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setGroupId(matcher_doclet.group(2));
        }

        //
        else if ("slizaa.statementId".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setStatementId(matcher_doclet.group(2));
        }

        //
        else if ("slizaa.description".equals(matcher_doclet.group(1))) {
          defaultCypherStatement.setDescription(matcher_doclet.group(2));
        }

        //
        else if ("slizaa.requiredStatements".equals(matcher_doclet.group(1))) {

          //
          String requiredStatements = matcher_doclet.group(2);

          //
          defaultCypherStatement.setRequiredStatements(Arrays.asList(requiredStatements.split(",")).stream()
              .map(statement -> statement.trim()).collect(Collectors.toList()));
        }

        else if (matcher_doclet.group(1).startsWith("slizaa")) {
          new RuntimeException("Unknown doclet '@" + matcher_doclet.group(1) + "' in " + relativePath + ".")
              .printStackTrace();
        }
      }
    }

    // TODO
    return defaultCypherStatement;
  }

  /**
   * <p>
   * </p>
   */
  private static String readFile(InputStream inputStream, Charset encoding) throws IOException {

    ByteSource byteSource = new ByteSource() {
      @Override
      public InputStream openStream() throws IOException {
        return inputStream;
      }
    };

    return byteSource.asCharSource(encoding).read();
  }
}
