/**
 * slizaa-server-service-svg - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.service.svg.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLWriterDOM {

  public static Document read(byte[] bytes) throws Exception {
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    fac.setNamespaceAware(false);
    fac.setValidating(false);
    fac.setFeature("http://xml.org/sax/features/namespaces", false);
    fac.setFeature("http://xml.org/sax/features/validation", false);
    fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    DocumentBuilder builder = fac.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(bytes));
  }

  public static void dump(Node node) throws ParserConfigurationException, TransformerException {
    checkNotNull(node);

    // for output to file, console
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    // for pretty print
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(node);

    // write to console or file
    StreamResult console = new StreamResult(System.out);

    // write data
    transformer.transform(source, console);
  }
}
