package org.nognog.freeSquare.model.player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.item.Item;

@SuppressWarnings({ "javadoc", "static-method", "rawtypes", "unchecked", "boxing" })
@RunWith(GdxTestRunner.class)
public class ItemBoxTest {

	@Test
	public final void testToItemArray() {
		ItemBox box = new ItemBox();
		PossessedItem<?>[] array1 = box.toItemArray();
		assertThat(array1, is(not(nullValue())));

		PossessedItem<?>[] array2 = box.toItemArray();
		assertThat(array1 == array2, is(false));
	}

	@Test
	public final void testPutItemTakeOutItem() {
		ItemBox box = new ItemBox();
		ItemBoxObserver mock = mock(ItemBoxObserver.class);
		box.addObserver(mock);
		Item item1 = mock(Item.class);
		Item item2 = mock(Item.class);
		when(item1.isSameItem((Object) item1)).thenReturn(true);
		when(item2.isSameItem((Object) item2)).thenReturn(true);

		box.putItem(item1);

		final int expected1 = 1;
		final int actual1 = box.getItemQuantity(item1);
		assertThat(actual1, is(expected1));
		final int expected2 = 0;
		final int actual2 = box.getItemQuantity(item2);
		assertThat(actual2, is(expected2));
		verify(mock, times(1)).updateItemBox();

		box.putItem(item2);
		box.putItem(item1);

		final int expected3 = 2;
		final int actual3 = box.getItemQuantity(item1);
		assertThat(actual3, is(expected3));
		final int expected4 = 1;
		final int actual4 = box.getItemQuantity(item2);
		assertThat(actual4, is(expected4));
		verify(mock, times(3)).updateItemBox();

		box.takeOutItem(item2);

		final int expected5 = 2;
		final int actual5 = box.getItemQuantity(item1);
		assertThat(actual5, is(expected5));
		final int expected6 = 0;
		final int actual6 = box.getItemQuantity(item2);
		assertThat(actual6, is(expected6));
		verify(mock, times(4)).updateItemBox();

		Item item3 = mock(Item.class);
		when(item3.isSameItem(item3)).thenReturn(true);
		box.takeOutItem(item3);

		final int expected7 = 2;
		final int actual7 = box.getItemQuantity(item1);
		assertThat(actual7, is(expected7));
		final int expected8 = 0;
		final int actual8 = box.getItemQuantity(item2);
		assertThat(actual8, is(expected8));
		verify(mock, times(4)).updateItemBox();
	}

	@Test
	public final void testIncreaseItem() {
		ItemBox box = new ItemBox();
		ItemBoxObserver mock = mock(ItemBoxObserver.class);
		box.addObserver(mock);
		Item item1 = mock(Item.class);
		Item item2 = mock(Item.class);
		when(item1.isSameItem((Object) item1)).thenReturn(true);
		when(item2.isSameItem((Object) item2)).thenReturn(true);

		box.increaseItem(item1, 3);
		box.increaseItem(item1, 2);
		box.increaseItem(item2, -11);

		final int expected1 = 5;
		final int actual1 = box.getItemQuantity(item1);
		assertThat(actual1, is(expected1));
		final int expected2 = 0;
		final int actual2 = box.getItemQuantity(item2);
		assertThat(actual2, is(expected2));

		box.increaseItem(item1, -4);
		box.increaseItem(item2, ItemBox.maxQuantity * 2);

		final int expected3 = 1;
		final int actual3 = box.getItemQuantity(item1);
		assertThat(actual3, is(expected3));
		final int expected4 = ItemBox.maxQuantity;
		final int actual4 = box.getItemQuantity(item2);
		assertThat(actual4, is(expected4));

		box.increaseItem(item1, 0);
		box.increaseItem(item2, -ItemBox.maxQuantity);

		final int expected5 = 1;
		final int actual5 = box.getItemQuantity(item1);
		assertThat(actual5, is(expected5));
		final int expected6 = 0;
		final int actual6 = box.getItemQuantity(item2);
		assertThat(actual6, is(expected6));

		assertThat(box.has(item1), is(true));
		assertThat(box.has(item2), is(false));
		verify(mock, times(5)).updateItemBox();
	}

	@Test
	public final void testDecreaseItem() {
		ItemBox box = new ItemBox();
		ItemBoxObserver mock = mock(ItemBoxObserver.class);
		box.addObserver(mock);
		Item item1 = mock(Item.class);
		Item item2 = mock(Item.class);
		when(item1.isSameItem((Object) item1)).thenReturn(true);
		when(item2.isSameItem((Object) item2)).thenReturn(true);

		box.decreaseItem(item1, 3);
		box.decreaseItem(item1, 2);
		box.decreaseItem(item2, -11);

		final int expected1 = 0;
		final int actual1 = box.getItemQuantity(item1);
		assertThat(actual1, is(expected1));
		final int expected2 = 11;
		final int actual2 = box.getItemQuantity(item2);
		assertThat(actual2, is(expected2));

		box.decreaseItem(item1, -4);
		box.decreaseItem(item2, ItemBox.maxQuantity * 2);

		final int expected3 = 4;
		final int actual3 = box.getItemQuantity(item1);
		assertThat(actual3, is(expected3));
		final int expected4 = 0;
		final int actual4 = box.getItemQuantity(item2);
		assertThat(actual4, is(expected4));

		box.decreaseItem(item1, 0);
		box.decreaseItem(item2, -ItemBox.maxQuantity);

		final int expected5 = 4;
		final int actual5 = box.getItemQuantity(item1);
		assertThat(actual5, is(expected5));
		final int expected6 = ItemBox.maxQuantity;
		final int actual6 = box.getItemQuantity(item2);
		assertThat(actual6, is(expected6));

		assertThat(box.has(item1), is(true));
		assertThat(box.has(item2), is(true));
		verify(mock, times(4)).updateItemBox();
	}
}
