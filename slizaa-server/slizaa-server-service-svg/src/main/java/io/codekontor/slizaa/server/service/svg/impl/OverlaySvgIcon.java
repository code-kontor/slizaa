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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

import static com.google.common.base.Preconditions.checkState;

public class OverlaySvgIcon {

    private boolean _isOverlayImage;

    private NodeList _mainNodes;

    private NodeList _ulNodes;

    private NodeList _urNodes;

    private NodeList _llNodes;

    private NodeList _lrNodes;

    public OverlaySvgIcon(boolean isOverlayImage) {
        this._isOverlayImage = isOverlayImage;
    }

    public void setMainNodes(NodeList _mainNodes) {
        this._mainNodes = _mainNodes;
    }

    public void setUpperLeftNodes(NodeList nodes) {
        checkState(_isOverlayImage);
        this._ulNodes = nodes;
    }

    public void setUpperRightNodes(NodeList nodes) {
        checkState(_isOverlayImage);
        this._urNodes = nodes;
    }

    public void setLowerLeftNodes(NodeList nodes) {
        checkState(_isOverlayImage);
        this._llNodes = nodes;
    }

    public void setLowerRightNodes(NodeList nodes) {
        checkState(_isOverlayImage);
        this._lrNodes = nodes;
    }

    public String create() {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {

            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
            rootElement.setAttribute("version", "1.1");
            rootElement.setAttribute("id", "icon");
            rootElement.setAttribute("width", "20px");
            rootElement.setAttribute("height", "20px");
            rootElement.setAttribute("viewBox", "0 0 1000 1000");

            doc.appendChild(rootElement);

            //
            if (_isOverlayImage) {
                Node mainNode = rootElement.appendChild(svgNode(doc, "main", "150", "150", "700", "700", "0 0 1000 1000", null));
                addAsChildNodes(mainNode, _mainNodes);
                Node urNode = rootElement.appendChild(svgNode(doc, "ur", "650", "0", "350", "350", "0 0 1000 1000", null));
                addAsChildNodes(urNode, _urNodes);
                Node lrNode = rootElement.appendChild(svgNode(doc, "lr", "650", "650", "350", "350", "0 0 1000 1000", null));
                addAsChildNodes(lrNode, _lrNodes);
                Node llNode = rootElement.appendChild(svgNode(doc, "ll", "0", "650", "350", "350", "0 0 1000 1000", null));
                addAsChildNodes(llNode, _llNodes);
                Node ulNode = rootElement.appendChild(svgNode(doc, "ul", "0", "0", "350", "350", "0 0 1000 1000", null));
                addAsChildNodes(ulNode, _ulNodes);
            } else {
                Node mainNode = rootElement.appendChild(svgNode(doc, "main", "0", "0", "1000", "1000", "0 0 1000 1000", null));
                addAsChildNodes(mainNode, _mainNodes);
            }

            return toString(doc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addAsChildNodes(Node parent, NodeList nodeList) {
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Node newNode = parent.getOwnerDocument().importNode(node, true);
                    parent.appendChild(newNode);
                }
            }
        }
    }

    private static Node svgNode(Document doc, String id, String x, String y, String width, String height, String viewBox,
                                Node content) {

        Element mainNode = doc.createElement("svg");
        mainNode.setAttribute("id", id);
        mainNode.setAttribute("x", x);
        mainNode.setAttribute("y", y);
        mainNode.setAttribute("width", width);
        mainNode.setAttribute("height", height);
        mainNode.setAttribute("viewBox", viewBox);

        return mainNode;
    }

    private static String toString(Document document) throws TransformerException {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();

    }
}
