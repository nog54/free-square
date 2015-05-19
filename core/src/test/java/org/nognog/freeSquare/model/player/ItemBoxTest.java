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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.item.Item;

@SuppressWarnings({ "javadoc", "static-method", "rawtypes", "boxing", "unused" })
@RunWith(GdxTestRunner.class)
public class ItemBoxTest {

	@Mocked
	ItemBoxObserver itemBoxObserver;

	@Test
	public final void testToItemArray() {
		ItemBox box = new ItemBox();
		PossessedItem<?>[] array1 = box.toItemArray();
		assertThat(array1, is(not(nullValue())));

		PossessedItem<?>[] array2 = box.toItemArray();
		assertThat(array1 == array2, is(false));
	}

	@Test
	public final void testPutItemTakeOutItem(@Mocked final Item mockItem1, @Mocked final Item mockItem2, @Mocked final Item mockItem3) {
		ItemBox box = new ItemBox();
		box.addObserver(this.itemBoxObserver);

		new NonStrictExpectations() {
			{
				mockItem1.isSameItem((Object) mockItem1);
				result = true;
				mockItem2.isSameItem((Object) mockItem2);
				result = true;
				mockItem3.isSameItem((Object) mockItem3);
				result = true;
			}
		};

		box.putItem(mockItem1);

		final int expected1 = 1;
		final int actual1 = box.getItemQuantity(mockItem1);
		assertThat(actual1, is(expected1));
		final int expected2 = 0;
		final int actual2 = box.getItemQuantity(mockItem2);
		assertThat(actual2, is(expected2));
		new Verifications() {
			{
				ItemBoxTest.this.itemBoxObserver.updateItemBox();
				times = 1;
			}
		};

		box.putItem(mockItem2);
		box.putItem(mockItem1);

		final int expected3 = 2;
		final int actual3 = box.getItemQuantity(mockItem1);
		assertThat(actual3, is(expected3));
		final int expected4 = 1;
		final int actual4 = box.getItemQuantity(mockItem2);
		assertThat(actual4, is(expected4));
		new Verifications() {
			{
				ItemBoxTest.this.itemBoxObserver.updateItemBox();
				times = 3;
			}
		};
		box.takeOutItem(mockItem2);

		final int expected5 = 2;
		final int actual5 = box.getItemQuantity(mockItem1);
		assertThat(actual5, is(expected5));
		final int expected6 = 0;
		final int actual6 = box.getItemQuantity(mockItem2);
		assertThat(actual6, is(expected6));
		new Verifications() {
			{
				ItemBoxTest.this.itemBoxObserver.updateItemBox();
				times = 4;
			}
		};

		box.takeOutItem(mockItem3);

		final int expected7 = 2;
		final int actual7 = box.getItemQuantity(mockItem1);
		assertThat(actual7, is(expected7));
		final int expected8 = 0;
		final int actual8 = box.getItemQuantity(mockItem2);
		assertThat(actual8, is(expected8));
		new Verifications() {
			{
				ItemBoxTest.this.itemBoxObserver.updateItemBox();
				times = 4;
			}
		};
	}

	@Test
	public final void testIncreaseItem(@Mocked final Item mockItem1, @Mocked final Item mockItem2) {
		ItemBox box = new ItemBox();
		new NonStrictExpectations() {
			{
				mockItem1.isSameItem((Object) mockItem1);
				result = true;
				mockItem2.isSameItem((Object) mockItem2);
				result = true;
			}
		};
		box.addObserver(this.itemBoxObserver);

		box.increaseItem(mockItem1, 3);
		box.increaseItem(mockItem1, 2);
		box.increaseItem(mockItem2, -11);

		final int expected1 = 5;
		final int actual1 = box.getItemQuantity(mockItem1);
		assertThat(actual1, is(expected1));
		final int expected2 = 0;
		final int actual2 = box.getItemQuantity(mockItem2);
		assertThat(actual2, is(expected2));

		box.increaseItem(mockItem1, -4);
		box.increaseItem(mockItem2, ItemBox.maxQuantity * 2);

		final int expected3 = 1;
		final int actual3 = box.getItemQuantity(mockItem1);
		assertThat(actual3, is(expected3));
		final int expected4 = ItemBox.maxQuantity;
		final int actual4 = box.getItemQuantity(mockItem2);
		assertThat(actual4, is(expected4));

		box.increaseItem(mockItem1, 0);
		box.increaseItem(mockItem2, -ItemBox.maxQuantity);

		final int expected5 = 1;
		final int actual5 = box.getItemQuantity(mockItem1);
		assertThat(actual5, is(expected5));
		final int expected6 = 0;
		final int actual6 = box.getItemQuantity(mockItem2);
		assertThat(actual6, is(expected6));

		assertThat(box.has(mockItem1), is(true));
		assertThat(box.has(mockItem2), is(false));
		new Verifications() {
			{
				ItemBoxTest.this.itemBoxObserver.updateItemBox();
				times = 5;
			}
		};
	}

	@Test
	public final void testDecreaseItem(@Mocked final Item mockItem1, @Mocked final Item mockItem2) {
		ItemBox box = new ItemBox();
		new NonStrictExpectations() {
			{
				mockItem1.isSameItem((Object) mockItem1);
				result = true;
				mockItem2.isSameItem((Object) mockItem2);
				result = true;
			}
		};
		box.addObserver(this.itemBoxObserver);
		box.decreaseItem(mockItem1, 3);
		box.decreaseItem(mockItem1, 2);
		box.decreaseItem(mockItem2, -11);

		final int expected1 = 0;
		final int actual1 = box.getItemQuantity(mockItem1);
		assertThat(actual1, is(expected1));
		final int expected2 = 11;
		final int actual2 = box.getItemQuantity(mockItem2);
		assertThat(actual2, is(expected2));

		box.decreaseItem(mockItem1, -4);
		box.decreaseItem(mockItem2, ItemBox.maxQuantity * 2);

		final int expected3 = 4;
		final int actual3 = box.getItemQuantity(mockItem1);
		assertThat(actual3, is(expected3));
		final int expected4 = 0;
		final int actual4 = box.getItemQuantity(mockItem2);
		assertThat(actual4, is(expected4));

		box.decreaseItem(mockItem1, 0);
		box.decreaseItem(mockItem2, -ItemBox.maxQuantity);

		final int expected5 = 4;
		final int actual5 = box.getItemQuantity(mockItem1);
		assertThat(actual5, is(expected5));
		final int expected6 = ItemBox.maxQuantity;
		final int actual6 = box.getItemQuantity(mockItem2);
		assertThat(actual6, is(expected6));

		assertThat(box.has(mockItem1), is(true));
		assertThat(box.has(mockItem2), is(true));
		new Verifications() {
			{
				ItemBoxTest.this.itemBoxObserver.updateItemBox();
				times = 4;
			}
		};
	}
}
