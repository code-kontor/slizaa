/**
 * slizaa-hierarchicalgraph-graphdb-mapping-spi - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi;

import org.junit.Test;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.DefaultLabelDefinition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl.LabelMappingDsl;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link LabelMappingDsl#propertyValue(String)} function.
 */
public class LabelMappingDsl_PropertyValueTest extends AbstractLabelMappingDslTest {

  /**
   * Tests that the {@link LabelMappingDsl#propertyValue(String)} function returns the selected value.
   */
  @Test
  public void testPropertyValue() {
    DefaultLabelDefinition labelDefinition = process(setLabelText(propertyValue("test")),
        createHGNode(Collections.singletonMap("test", "test")));
    assertThat(labelDefinition.getText()).isNotNull().isEqualTo("test");
  }

  /**
   * Tests that the {@link LabelMappingDsl#propertyValue(String)} function returns '<<UNKNOWN_PROPERTY_KEY: ...' if the value is missing.
   */
  @Test
  public void testPropertyValue_Null_1() {
    DefaultLabelDefinition labelDefinition = process(setLabelText(propertyValue("missing")), createHGNode());
    assertThat(labelDefinition.getText()).isNotNull().isEqualTo("<<UNKNOWN_PROPERTY_KEY: missing (666) >>");
  }

  /**
   * Tests that the {@link LabelMappingDsl#propertyValue(String)} function returns '<<UNKNOWN_PROPERTY_KEY: ...' if the value is missing.
   */
  @Test
  public void testPropertyValue_Null_2() {
    DefaultLabelDefinition labelDefinition = process(setLabelText(propertyValue("missing", str -> str.trim())),
        createHGNode());
    assertThat(labelDefinition.getText()).isNotNull().isEqualTo("<<UNKNOWN_PROPERTY_KEY: missing (666) >>");
  }
}
