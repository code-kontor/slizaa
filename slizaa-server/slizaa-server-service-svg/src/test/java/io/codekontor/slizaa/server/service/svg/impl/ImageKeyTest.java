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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ImageKeyTest {

	@Test
	public void longKey_0() {
		String key = ImageKey.longKey(true, "main", "test/upperLeft", "test/upperRight", "test/lowerLeft", "test/lowerRight");
		assertThat(key).isEqualTo("main?ul=test%2FupperLeft&ur=test%2FupperRight&ll=test%2FlowerLeft&lr=test%2FlowerRight");

		ImageKey.DecodedKey decodedKey = ImageKey.decode(key);
		assertThat(decodedKey.main).isEqualTo("main");
		assertThat(decodedKey.isOverlayImage).isEqualTo(true);
		assertThat(decodedKey.upperLeft).isEqualTo("test/upperLeft");
		assertThat(decodedKey.upperRight).isEqualTo("test/upperRight");
		assertThat(decodedKey.lowerLeft).isEqualTo("test/lowerLeft");
		assertThat(decodedKey.lowerRight).isEqualTo("test/lowerRight");
	}

	@Test
	public void longKey_1() {
		String key = ImageKey.longKey(true, "main", "upperLeft", "upperRight", "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void longKey_2() {
		String key = ImageKey.longKey(true,"main", null, "upperRight", "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ur=upperRight&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void longKey_3() {
		String key = ImageKey.longKey(true,"main", "upperLeft", null, "lowerLeft", "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ll=lowerLeft&lr=lowerRight");
	}
	
	@Test
	public void longKey_4() {
		String key = ImageKey.longKey(true,"main", "upperLeft", "upperRight", null, "lowerRight");
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&lr=lowerRight");
	}
	
	@Test
	public void longKey_5() {
		String key = ImageKey.longKey(true,"main", "upperLeft", "upperRight", "lowerLeft", null);
		assertThat(key).isEqualTo("main?ul=upperLeft&ur=upperRight&ll=lowerLeft");
	}
	
	@Test
	public void longKey_6() {
		String key = ImageKey.longKey(true,"main", null, "upperRight", "lowerLeft", null);
		assertThat(key).isEqualTo("main?ur=upperRight&ll=lowerLeft");
	}
	
	@Test
	public void longKey_7() {
		String key = ImageKey.longKey(true,"main", "upperLeft", null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ul=upperLeft&ll=lowerLeft");
	}
	
	@Test
	public void longKey_8() {
		String key = ImageKey.longKey(true,"main", null, null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ll=lowerLeft");
	}

	@Test
	public void longKey_9() {
		String key = ImageKey.longKey(false,"main", null, null, "lowerLeft", null);
		assertThat(key).isEqualTo("main?ol=0&ll=lowerLeft");
	}

	@Test
	public void shortKey_1() {
		String key = ImageKey.shortKey("main?ll=lowerLeft");
		assertThat(key).isEqualTo("425313399");
	}

	@Test
	public void shortKey_2() {
		String key = ImageKey.shortKey("main?ul=upperLeft&ll=lowerLeft");
		assertThat(key).isEqualTo("956358965");
		assertThat(key).isEqualTo(ImageKey.shortKey("main?ul=upperLeft&ll=lowerLeft"));
	}

	@Test
	public void shortKey_3() {
		String key1 = ImageKey.shortKey("main?ul=upperLeft");
		assertThat(key1).isEqualTo("653000350");
		String key2 = ImageKey.shortKey("main?ul=upperleft");
		assertThat(key2).isEqualTo("773054235");
		assertThat(key1).isNotEqualTo(key2);
	}

	@Test(expected = NullPointerException.class)
	public void test_NotNull() {
		String key = ImageKey.longKey(true, null, null, null, "lowerLeft", null);
	}
}
