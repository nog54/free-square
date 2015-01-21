package org.nognog.freeSquare.model.player;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/15
 */
public class ItemBox implements Savable {
	private final Array<PossessedItem<?>> possessedItems;
	/** アイテムの最大保持数 */
	public static final int maxQuantity = PossessedItem.maxQuantity;
	/**
	 * 
	 */
	public ItemBox() {
		this.possessedItems = new Array<>();
	}

	/**
	 * @return possessedItem array
	 */
	public Array<PossessedItem<?>> getItemArray() {
		return this.possessedItems;
	}

	/**
	 * add item to itembox
	 * 
	 * @param item
	 * @return quantity after the put
	 */
	public <T extends Item<T, ?>> int putItem(T item) {
		return this.increaseItem(item, 1);
	}

	/**
	 * add item to itembox
	 * 
	 * @param item
	 * @return quantity after the take out
	 */
	public <T extends Item<T, ?>> int takeOutItem(T item) {
		return this.decreaseItem(item, 1);
	}

	/**
	 * @param increaseItemClass
	 * @param item
	 * @param amount
	 * @return quantity after the increase
	 */
	public <T extends Item<T, ?>> int increaseItem(T item, int amount) {
		if (amount < 0) {
			return this.decreaseItem(item, -amount);
		}

		PossessedItem<?> increasePossessedItem = this.findPossessedItem(item);
		if (increasePossessedItem != null) {
			return increasePossessedItem.increaseQuantity(amount);
		}

		PossessedItem<T> newPossessedItem = new PossessedItem<>(item);
		final int afterQuantity = newPossessedItem.increaseQuantity(amount);
		this.possessedItems.add(newPossessedItem);
		return afterQuantity;
	}

	/**
	 * @param decreaseItemClass
	 * @param item
	 * @param amount
	 * @return quantity after the decrease
	 */
	public <T extends Item<T, ?>> int decreaseItem(T item, int amount) {
		if (amount < 0) {
			return this.increaseItem(item, -amount);
		}

		PossessedItem<?> decreasePossessedItem = this.findPossessedItem(item);
		if (decreasePossessedItem != null) {
			final int afterQuantity = decreasePossessedItem.decreaseQuantity(amount);
			if (afterQuantity == 0) {
				this.possessedItems.removeValue(decreasePossessedItem, true);
			}
			return afterQuantity;
		}
		return 0;
	}

	private PossessedItem<?> findPossessedItem(Item<?, ?> item) {
		for (PossessedItem<?> possessedItem : this.possessedItems) {
			if (item.isSameItem((possessedItem.getItem()))) {
				return possessedItem;
			}
		}
		return null;
	}

	/**
	 * @param item
	 * @return true if this has itemClass
	 */
	public boolean has(Item<?, ?> item) {
		return this.findPossessedItem(item) != null;
	}

	/**
	 * @param item
	 * @return true if this has itemClass
	 */
	public int getItemQuantity(Item<?, ?> item) {
		PossessedItem<?> possessedItem = this.findPossessedItem(item);
		if (possessedItem == null) {
			return 0;
		}
		return possessedItem.getQuantity();
	}

	@Override
	public String toString() {
		return this.possessedItems.toString();
	}

	@Override
	public boolean isValid() {
		for (PossessedItem<?> possessedItem : this.possessedItems) {
			if (!possessedItem.isValid()) {
				return false;
			}
		}
		return true;
	}
}
