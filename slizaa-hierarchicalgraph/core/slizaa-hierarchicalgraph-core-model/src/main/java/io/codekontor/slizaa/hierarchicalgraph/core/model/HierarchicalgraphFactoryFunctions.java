/**
 * slizaa-hierarchicalgraph-core-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.BasicEList;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGRootNodeImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class HierarchicalgraphFactoryFunctions {

  /**
   * <p>
   * </p>
   *
   * @param parent
   * @param nodeSource
   * @return
   */
  public static HGRootNode createNewRootNode(Supplier<INodeSource> nodeSourceSupplier) {

    //
    HGRootNode result = HierarchicalgraphFactory.eINSTANCE.createHGRootNode();
    result.setNodeSource(checkNotNull(checkNotNull(nodeSourceSupplier)).get());

    //
    return result;
  }

  /**
   * <p>
   * </p>
   *
   * @param parent
   * @param nodeSource
   * @return
   */
  public static HGNode createNewNode(HGRootNode rootNode, HGNode parent, Supplier<INodeSource> nodeSourceSupplier) {

    //
    HGNode result = HierarchicalgraphFactory.eINSTANCE.createHGNode();
    result.setParent(parent);
    result.setNodeSource(checkNotNull(checkNotNull(nodeSourceSupplier)).get());

    // put in cache
    ((ExtendedHGRootNodeImpl) checkNotNull(rootNode)).getIdToNodeMap().put(result.getIdentifier(), result);

    //
    return result;
  }

  public static <T extends HGCoreDependency> void removeDependency(T dependency, boolean invalidateCaches) {

    //
    checkNotNull(dependency).getFrom().getOutgoingCoreDependencies().remove(dependency);
    dependency.getTo().getIncomingCoreDependencies().remove(dependency);

    //
    if (invalidateCaches) {
      dependency.getFrom().getRootNode()
          .invalidateCaches(new BasicEList<HGNode>(Arrays.asList(dependency.getFrom(), dependency.getTo())));
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param source
   * @param target
   * @return
   */
  public static HGProxyDependency createNewProxyDependency(HGNode source, HGNode target, String type,
      Supplier<IDependencySource> dependencySourceSupplier, boolean reinitializeCaches) {

    //
    return _initializeDependency(HierarchicalgraphFactory.eINSTANCE.createHGProxyDependency(), source, target, type,
        dependencySourceSupplier, reinitializeCaches);
  }

  /**
   * <p>
   * </p>
   *
   * @param source
   * @param target
   * @param type
   * @param dependencySourceSupplier
   * @param reinitializeCaches
   * @return
   */
  public static HGCoreDependency createNewCoreDependency(HGNode source, HGNode target, String type,
      Supplier<IDependencySource> dependencySourceSupplier, boolean reinitializeCaches) {

    //
    return _initializeDependency(HierarchicalgraphFactory.eINSTANCE.createHGCoreDependency(), source, target, type,
        dependencySourceSupplier, reinitializeCaches);
  }

  /**
   * <p>
   * </p>
   *
   * @param dependency
   * @param source
   * @param target
   * @param type
   * @param dependencySourceSupplier
   * @param reinitializeCaches
   * @return
   */
  private static <T extends HGCoreDependency> T _initializeDependency(T dependency, HGNode source, HGNode target,
      String type, Supplier<IDependencySource> dependencySourceSupplier, boolean reinitializeCaches) {

    //
    dependency.setFrom(checkNotNull(source));
    dependency.setTo(checkNotNull(target));
    dependency.setType(checkNotNull(type));
    dependency.setDependencySource(checkNotNull(checkNotNull(dependencySourceSupplier)).get());

    //
    source.getOutgoingCoreDependencies().add(dependency);
    target.getIncomingCoreDependencies().add(dependency);

    //
    source.getRootNode().invalidateCaches(new BasicEList<HGNode>(Arrays.asList(source, target)));
    if (reinitializeCaches) {
      source.getRootNode().initializeCaches(new BasicEList<HGNode>(Arrays.asList(source, target)));
    }

    //
    return dependency;
  }
}
