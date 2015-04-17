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

package org.nognog.freeSquare.model.player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.nognog.freeSquare.model.item.Item;

@SuppressWarnings({ "javadoc", "static-method", "rawtypes", "boxing" })
public class PossessedItemTest {

	@Test
	public final void testGetItem() {
		Item mock = mock(Item.class);
		PossessedItem<?> items = new PossessedItem<>(mock);
		assertThat(items.getItem(), is(mock));
	}

	@Test
	public final void testGetQuantity() {
		Item mock = mock(Item.class);
		PossessedItem<?> items = new PossessedItem<>(mock);
		final int expected1 = 0;
		final int actual1 = items.getQuantity();
		assertThat(actual1, is(expected1));

		items.increaseQuantity(4);

		final int expected2 = 4;
		final int actual2 = items.getQuantity();
		assertThat(actual2, is(expected2));

		items.increaseQuantity(0);

		final int expected3 = 4;
		final int actual3 = items.getQuantity();
		assertThat(actual3, is(expected3));

		items.increaseQuantity(-5);

		final int expected4 = 0;
		final int actual4 = items.getQuantity();
		assertThat(actual4, is(expected4));

		items.increaseQuantity(PossessedItem.maxQuantity * 2);

		final int expected5 = PossessedItem.maxQuantity;
		final int actual5 = items.getQuantity();
		assertThat(actual5, is(expected5));

		items.decreaseQuantity(-2);

		final int expected6 = PossessedItem.maxQuantity;
		final int actual6 = items.getQuantity();
		assertThat(actual6, is(expected6));

		items.decreaseQuantity(5);
		items.decreaseQuantity(0);

		final int expected7 = PossessedItem.maxQuantity - 5;
		final int actual7 = items.getQuantity();
		assertThat(actual7, is(expected7));

	}

}
