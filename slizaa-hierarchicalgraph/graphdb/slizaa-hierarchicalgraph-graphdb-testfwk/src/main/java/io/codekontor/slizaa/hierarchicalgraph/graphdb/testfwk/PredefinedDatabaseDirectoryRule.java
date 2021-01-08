/**
 * slizaa-hierarchicalgraph-graphdb-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.testfwk;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.*;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class PredefinedDatabaseDirectoryRule implements TestRule {

  //
  private File parent;
  
  //
  private File databaseDirectory;

  //
  private InputStream inputStream;

  /**
   * @param inputStream
   */
  public PredefinedDatabaseDirectoryRule(InputStream inputStream, File parent) {
    this.inputStream = checkNotNull(inputStream);
    this.parent = checkNotNull(parent);
    
    databaseDirectory = new File(parent, "databases/neo4j");
  }

  /**
   * @param inputStream
   * @throws IOException
   */
  public PredefinedDatabaseDirectoryRule(InputStream inputStream) {
    this(inputStream, createTempDirectory());
  }

  /**
   * @return
   */
  public File getParentDirectory() {
    return parent;
  }
  
  /**
   * 
   * <p>
   * </p>
   *
   * @return
   */
  public File getDatabaseDirectory() {
    return databaseDirectory;
  }

  /**
   * @param base
   * @param description
   * @return
   */
  @Override
  public Statement apply(final Statement base, final Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {
        unzipDatabase(databaseDirectory, inputStream);

        base.evaluate();

        // delete the old database directory...
        try {

          // get the root path
          Path rootPath = Paths.get(parent.getAbsolutePath());

          // delete all contained files
          Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
              .forEach(File::delete);

        } catch (IOException e) {
          // simply ignore
        }
      }
    };
  }

  /**
   * @param databaseDirectory
   * @param zippedDatabaseStream
   * @return
   */
  private static void unzipDatabase(File databaseDirectory, InputStream zippedDatabaseStream) {

    //
    checkNotNull(databaseDirectory);
    checkNotNull(zippedDatabaseStream);

    
    //
    //if (!realDir.exists()) {
      try {
        unzip(zippedDatabaseStream, databaseDirectory);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    //}
  }

  /**
   * <p>
   * </p>
   *
   * @param inputStream
   * @param folder
   */
  private static void unzip(InputStream inputStream, File folder) {

    checkNotNull(inputStream);
    checkNotNull(folder);

    byte[] buffer = new byte[1024];

    try {

      // create output directory is not exists
      if (!folder.exists()) {
        folder.mkdirs();
      }

      // get the zip file content
      try (ZipInputStream zis = new ZipInputStream(inputStream)) {
        // get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {

          if (!ze.isDirectory()) {

            String fileName = ze.getName();
            File newFile = new File(folder + File.separator + fileName);

            // create all non exists folders
            // else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
              int len;
              while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
              }
            }
          }
          ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private static File createTempDirectory() {
    try {
      return Files.createTempDirectory("PredefinedDatabaseDirectoryRule_").toFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
