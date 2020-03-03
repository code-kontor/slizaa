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

import com.google.common.base.Function;
import com.google.common.math.IntMath;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphUtil;

import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Utils {

  /**
   *
   * @param sortedElements
   * @param pageNumber
   * @param pageSize
   * @param dependencyCreator
   * @param <T>
   * @return
   */
  public static <T> DependencyPage getDependencyPage(List<T> sortedElements,
                                                     int pageNumber,
                                                     int pageSize,
                                                     Function<T, Dependency> dependencyCreator) {

    if (pageNumber < 1) {
      throw new IndexOutOfBoundsException("Invalid");
    }

    if (sortedElements == null || sortedElements.isEmpty()) {
      return new DependencyPage(new PageInfo(1, 0, 0, 0), Collections.emptyList());
    }

    // compute max pages
    int maxPages = IntMath.divide(sortedElements.size(), pageSize, RoundingMode.CEILING);
    if (pageNumber > maxPages) {
      pageNumber = maxPages;
    }

    int startIndex = (pageNumber - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, sortedElements.size());

    List<Dependency> partialResultList = startIndex > sortedElements.size() ?
            Collections.emptyList() :
            sortedElements.subList(startIndex, endIndex)
                    .stream().map(element -> dependencyCreator.apply(element))
                    .collect(Collectors.toList());

    return new DependencyPage(new PageInfo(pageNumber, maxPages, pageSize, sortedElements.size()), partialResultList);
  }

  /**
   * @param node
   * @return
   */
  public static ILabelDefinitionProvider getLabelDefinitionProvider(HGNode node) {

    //
    return checkNotNull(checkNotNull(node).getRootNode()
        .getExtension(ILabelDefinitionProvider.class));
  }


  public static void main(String[] args) {

    List<String> strings = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    System.out.println(strings.size() + " : " + strings.subList(0,10));
  }
}
