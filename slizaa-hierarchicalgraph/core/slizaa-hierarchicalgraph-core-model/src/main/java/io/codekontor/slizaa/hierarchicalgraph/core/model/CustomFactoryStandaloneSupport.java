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

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.CustomHierarchicalgraphFactoryImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CustomFactoryStandaloneSupport {

  /**
   * <p>
   * </p>
   */
  public static void registerCustomHierarchicalgraphFactory() {

    //
    EPackage.Registry.INSTANCE.put(HierarchicalgraphPackage.eNS_URI, new EPackage.Descriptor() {

      @Override
      public EPackage getEPackage() {
        return HierarchicalgraphPackage.eINSTANCE;
      }

      @Override
      public EFactory getEFactory() {
        return new CustomHierarchicalgraphFactoryImpl();
      }
    });
  }
}
