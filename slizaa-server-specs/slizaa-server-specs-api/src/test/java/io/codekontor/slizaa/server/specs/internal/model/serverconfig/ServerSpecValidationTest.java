/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs.internal.model.serverconfig;

import static io.codekontor.slizaa.server.specs.api.ISlizaaServerSpec.Factory.slizaaServerSpec;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.codekontor.slizaa.server.specs.api.ISlizaaServerSpec;
import io.codekontor.slizaa.server.specs.internal.api.SlizaaServerSpec;

public class ServerSpecValidationTest {

  @Test
  public void testServerSpecValidation_1() {
    
    ISlizaaServerSpec slizaaServerSpec = slizaaServerSpec(); 
    
//    assertThat(((SlizaaServerSpec)slizaaServerSpec).validateAndLogErrors()).isTrue();
//    assertThat(((SlizaaServerSpec)slizaaServerSpec).validate()).containsExactly("You must specify at least one server extension. Current specificationn: null");
  }
  
//  @Test
//  public void testServerSpecValidation_2() {
//    
//    ISlizaaServerSpec slizaaServerSpec = slizaaServerSpec(); 
//    
//    assertThat(((SlizaaServerSpec)slizaaServerSpec).validateAndLogErrors()).isTrue();
//    assertThat(((SlizaaServerSpec)slizaaServerSpec).validate()).containsExactly("You must specify at least one server extension. Current specificationn: null");
//  }
}
