/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.model.item;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import mockit.Mocked;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.GdxTestRunner;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.Square2dObjectType;
import org.nognog.freeSquare.square2d.object.types.eatable.PreparedEatableObjectType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class Square2dObjectItemTest {
	
	@Mocked private Item item;

	@Test
	public final void testGetInstance() {
		Square2dObjectItem item1 = Square2dObjectItem.getInstance(PreparedEatableObjectType.TOFU);
		Square2dObjectItem item2 = Square2dObjectItem.getInstance(PreparedEatableObjectType.TOFU);
		Square2dObjectItem item3 = Square2dObjectItem.getInstance(PreparedEatableObjectType.BLACK_SESAME_TOFU);

		assertThat(item1, is(item2));
		assertThat(item1, is(not(item3)));
	}

	@Test
	public final void testIsSameItem() {
		Square2dObjectItem item1 = Square2dObjectItem.getInstance(PreparedEatableObjectType.TOFU);
		Square2dObjectItem item2 = Square2dObjectItem.getInstance(PreparedEatableObjectType.TOFU);
		Square2dObjectItem item3 = Square2dObjectItem.getInstance(PreparedEatableObjectType.BLACK_SESAME_TOFU);

		final boolean expected1 = true;
		final boolean actual1 = item1.isSameItem(item2);
		assertThat(actual1, is(expected1));

		final boolean expected2 = true;
		final boolean actual2 = item1.isSameItem((Object) item2);
		assertThat(actual2, is(expected2));

		final boolean expected3 = false;
		final boolean actual3 = item1.isSameItem(item3);
		assertThat(actual3, is(expected3));

		final boolean expected4 = false;
		final boolean actual4 = item1.isSameItem((Object) item3);
		assertThat(actual4, is(expected4));

		final boolean expected5 = false;
		final boolean actual5 = item1.isSameItem(item);
		assertThat(actual5, is(expected5));
	}

	@Test
	public final void testGetTexture() {
		for (Square2dObjectType type : PreparedEatableObjectType.values()) {
			Square2dObjectItem item = Square2dObjectItem.getInstance(type);
			final Texture expected = type.getTexture();
			final Texture actual = item.getSimpleTexture();
			assertThat(actual, is(expected));
		}
	}

	@Test
	public final void testGetColor() {
		for (Square2dObjectType type : PreparedEatableObjectType.values()) {
			Square2dObjectItem item = Square2dObjectItem.getInstance(type);
			final Color expected = type.getColor();
			final Color actual = item.getColor();
			assertThat(actual, is(expected));
		}
	}

	@Test
	public final void testIsValid() {
		for (Square2dObjectType type : PreparedEatableObjectType.values()) {
			Square2dObjectItem item = Square2dObjectItem.getInstance(type);
			final boolean expected = true;
			final boolean actual = item.isValid();
			assertThat(actual, is(expected));
		}
	}

}
