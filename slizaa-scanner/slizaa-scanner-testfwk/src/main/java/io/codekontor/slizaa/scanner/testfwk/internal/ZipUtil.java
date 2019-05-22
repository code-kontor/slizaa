/**
 * slizaa-scanner-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.testfwk.internal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

  private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

  static public void zipFile(String fileToZip, String zipFile, boolean excludeContainingFolder)
    throws IOException {		
    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));		
    File srcFile = new File(fileToZip);
    if(excludeContainingFolder && srcFile.isDirectory()) {
      for(String fileName : srcFile.list()) {
        addToZip("", fileToZip + "/" + fileName, zipOut);
      }
    } else {
      addToZip("", fileToZip, zipOut);
    }

    zipOut.flush();
    zipOut.close();

    System.out.println("Successfully created " + zipFile);
  }

  static private void addToZip(String path, String srcFile, ZipOutputStream zipOut)
    throws IOException {		
    File file = new File(srcFile);
    String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();
    if (file.isDirectory()) {
      for (String fileName : file.list()) {				
        addToZip(filePath, srcFile + "/" + fileName, zipOut);
      }
    } else {
      zipOut.putNextEntry(new ZipEntry(filePath));
      FileInputStream in = new FileInputStream(srcFile);

      byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
      int len;
      while ((len = in.read(buffer)) != -1) {
        zipOut.write(buffer, 0, len);
      }
    }
  }
}
