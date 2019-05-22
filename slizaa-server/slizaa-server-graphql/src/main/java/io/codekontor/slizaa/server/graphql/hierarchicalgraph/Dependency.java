/**
 * slizaa-server-graphql - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.graphql.hierarchicalgraph;

/**
 *
 */
public class Dependency {

  private String id;

  private Node sourceNode;

  private Node targetNode;

  private int weight;

  public Dependency(String id, Node sourceNode, Node targetNode, int weight) {
    this.id = id;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.weight = weight;
  }

  public Dependency(Node sourceNode, Node targetNode, int weight) {
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.weight = weight;
  }

/*
  public String getId() {
    return id;
  }
*/

  public Node getSourceNode() {
    return sourceNode;
  }

  public Node getTargetNode() {
    return targetNode;
  }

  public int getWeight() {
    return weight;
  }
}
