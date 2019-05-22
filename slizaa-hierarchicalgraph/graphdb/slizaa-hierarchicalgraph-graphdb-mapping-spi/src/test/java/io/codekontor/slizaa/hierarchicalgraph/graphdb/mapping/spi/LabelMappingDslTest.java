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
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider.OverlayPosition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.DefaultLabelDefinition;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class LabelMappingDslTest extends AbstractLabelMappingDslTest {

    @Test
    public void testSetText() {
        DefaultLabelDefinition labelDefinition = process(setLabelText("test"), createHGNode());
        assertThat(labelDefinition.getText()).isNotNull().isEqualTo("test");
    }

    @Test
    public void testSetText_Null_1() {
        DefaultLabelDefinition labelDefinition = process(setLabelText((String) null), createHGNode());
        assertThat(labelDefinition.getText()).isNull();
    }

    @Test
    public void testSetText_Null_2() {
        DefaultLabelDefinition labelDefinition = process(setLabelText((Function<HGNode, String>) null), createHGNode());
        assertThat(labelDefinition.getText()).isNotNull().isEqualTo("<<LABEL_MAPPING_IS_NULL: (666) >>");
    }

    /**
     * <p>
     * </p>
     */
    @Test
    public void testSetBaseImage() {

        //
        DefaultLabelDefinition labelDefinition = process(setBaseImage("icons/final_co.png"), createHGNode());

        //
        assertThat(labelDefinition.getBaseImagePath()).isNotNull().isEqualTo("icons/final_co.png");
    }

    /**
     * <p>
     * </p>
     */
    @Test
    public void testSetOverlayImage() {

        //
        DefaultLabelDefinition labelDefinition = process(
                setOverlayImage("icons/final_co.png", OverlayPosition.TOP_RIGHT), createHGNode());
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.TOP_RIGHT)).isNotNull()
                .isEqualTo("icons/final_co.png");

        //
        labelDefinition = process(setOverlayImage("icons/final_co.png", OverlayPosition.TOP_LEFT),
                createHGNode());
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.TOP_LEFT)).isNotNull()
                .isEqualTo("icons/final_co.png");

        //
        labelDefinition = process(setOverlayImage("icons/final_co.png", OverlayPosition.BOTTOM_RIGHT),
                createHGNode());
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.BOTTOM_RIGHT)).isNotNull()
                .isEqualTo("icons/final_co.png");

        //
        labelDefinition = process(setOverlayImage("icons/final_co.png", OverlayPosition.BOTTOM_LEFT),
                createHGNode());
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.BOTTOM_LEFT)).isNotNull()
                .isEqualTo("icons/final_co.png");
    }

    /**
     * <p>
     * </p>
     */
    @Test
    public void testExclusiceChoice() {

        //@formatter:off
        DefaultLabelDefinition labelDefinition = process(

                exclusiveChoice().
                        when(n -> false).then(setOverlayImage("icons/final_co.png", OverlayPosition.TOP_RIGHT)).
                        when(n -> false).then(setOverlayImage("icons/final_co.png", OverlayPosition.TOP_LEFT)).
                        otherwise(setOverlayImage("icons/final_co.png", OverlayPosition.BOTTOM_RIGHT))
                , createHGNode());
        //@formatter:on

        //
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.TOP_RIGHT)).isNull();
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.TOP_LEFT)).isNull();
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.BOTTOM_LEFT)).isNull();
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.BOTTOM_RIGHT)).isNotNull();
    }

    /**
     * <p>
     * </p>
     */
    @Test
    public void testExecuteAll() {

        //@formatter:off
        DefaultLabelDefinition labelDefinition = process(
                executeAll(
                        setOverlayImage("icons/final_co.png", OverlayPosition.TOP_RIGHT),
                        setOverlayImage("icons/final_co.png", OverlayPosition.TOP_LEFT),
                        setOverlayImage("icons/final_co.png", OverlayPosition.BOTTOM_RIGHT),
                        setOverlayImage("icons/final_co.png", OverlayPosition.BOTTOM_LEFT)
                )
                , createHGNode());
        //@formatter:on

        //
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.TOP_RIGHT)).isNotNull()
                .isEqualTo("icons/final_co.png");
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.TOP_LEFT)).isNotNull()
                .isEqualTo("icons/final_co.png");
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.BOTTOM_RIGHT)).isNotNull()
                .isEqualTo("icons/final_co.png");
        assertThat(labelDefinition.getOverlayImagePath(OverlayPosition.BOTTOM_LEFT)).isNotNull()
                .isEqualTo("icons/final_co.png");
    }

    /**
     * <p>
     * </p>
     */
    @Test
    public void testWhen_True() {

        //
        DefaultLabelDefinition labelDefinition = process(
                when(n -> true).then(setBaseImage("icons/final_co.png")), createHGNode());

        //
        assertThat(labelDefinition.getBaseImagePath()).isNotNull().isEqualTo("icons/final_co.png");
    }

    /**
     * <p>
     * </p>
     */
    @Test
    public void testWhen_False() {

        //
        DefaultLabelDefinition labelDefinition = process(
                when(n -> false).then(setBaseImage("icons/final_co.png")), createHGNode());

        //
        assertThat(labelDefinition.getBaseImagePath()).isNull();
    }
}
