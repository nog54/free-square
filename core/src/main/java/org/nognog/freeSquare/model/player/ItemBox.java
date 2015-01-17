package org.nognog.freeSquare.model.player;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/15
 */
public class ItemBox implements Savable {
	private final Array<PossessedItem<?>> possessedItems;

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
	 * @param increaseItemClass
	 * @param item
	 * @param amount
	 * @return quantity after the increase
	 */
	public <T extends Item<T>> int increaseItem(T item, int amount) {
		if (amount < 0) {
			return this.decreaseItem(item, -amount);
		}

		PossessedItem<T> increasePossessedItem = this.findPossessedItem(item);
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
	public <T extends Item<T>> int decreaseItem(T item, int amount) {
		if (amount < 0) {
			return this.increaseItem(item, -amount);
		}

		PossessedItem<T> decreasePossessedItem = this.findPossessedItem(item);
		if (decreasePossessedItem != null) {
			final int afterQuantity = decreasePossessedItem.decreaseQuantity(amount);
			if (afterQuantity == 0) {
				this.possessedItems.removeValue(decreasePossessedItem, true);
			}
			return afterQuantity;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private <T extends Item<T>> PossessedItem<T> findPossessedItem(T item) {
		for (PossessedItem<?> possessedItem : this.possessedItems) {
			if (item.isSameItem((possessedItem.getItem()))) {
				return (PossessedItem<T>) possessedItem;
			}
		}
		return null;
	}

	/**
	 * @param item
	 * @return true if this has itemClass
	 */
	public <T extends Item<T>> boolean has(T item) {
		return this.findPossessedItem(item) != null;
	}

	/**
	 * @param item
	 * @return true if this has itemClass
	 */
	public <T extends Item<T>> int getItemQuantity(T item) {
		PossessedItem<T> possessedItem = this.findPossessedItem(item);
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
