package org.nognog.freeSquare.model.player;

import org.nognog.freeSquare.model.SelfValidatable;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/15
 */
public class ItemBox implements SelfValidatable{

	private final Array<PossessedItem<?>> possessedItems;
	private transient final Array<ItemBoxObserver> observers;

	/** アイテムの最大保持数 */
	public static final int maxQuantity = PossessedItem.maxQuantity;

	/**
	 * @param player
	 * 
	 */
	public ItemBox() {
		this.possessedItems = new Array<>();
		this.observers = new Array<>();
	}

	/**
	 * @return new possessedItem array
	 */
	public Array<PossessedItem<?>> toItemArray() {
		return new Array<>(this.possessedItems);
	}

	/**
	 * add item to itembox
	 * 
	 * @param item
	 * @return quantity after the put
	 */
	public <T extends Item<?, ?>> int putItem(T item) {
		final int quantity = this.increaseItem(item, 1);
		return quantity;
	}

	/**
	 * add item to itembox
	 * 
	 * @param item
	 * @return quantity after the take out
	 */
	public <T extends Item<?, ?>> int takeOutItem(T item) {
		final int quantity = this.decreaseItem(item, 1);
		return quantity;
	}

	/**
	 * @param increaseItemClass
	 * @param item
	 * @param amount
	 * @return quantity after the increase
	 */
	public <T extends Item<?, ?>> int increaseItem(T item, int amount) {
		if (amount == 0) {
			return this.getItemQuantity(item);
		}
		if (amount < 0) {
			return this.decreaseItem(item, -amount);
		}

		PossessedItem<?> increasePossessedItem = this.findPossessedItem(item);
		if (increasePossessedItem != null) {
			final int afterQuantity = increasePossessedItem.increaseQuantity(amount);
			this.notifyObservers();
			return afterQuantity;
		}

		PossessedItem<T> newPossessedItem = new PossessedItem<>(item);
		final int afterQuantity = newPossessedItem.increaseQuantity(amount);
		this.possessedItems.add(newPossessedItem);
		this.notifyObservers();
		return afterQuantity;
	}

	/**
	 * @param decreaseItemClass
	 * @param item
	 * @param amount
	 * @return quantity after the decrease
	 */
	public <T extends Item<?, ?>> int decreaseItem(T item, int amount) {
		if (amount == 0) {
			return this.getItemQuantity(item);
		}
		if (amount < 0) {
			return this.increaseItem(item, -amount);
		}

		PossessedItem<?> decreasePossessedItem = this.findPossessedItem(item);
		if (decreasePossessedItem != null) {
			final int afterQuantity = decreasePossessedItem.decreaseQuantity(amount);
			if (afterQuantity == 0) {
				this.possessedItems.removeValue(decreasePossessedItem, true);
			}
			this.notifyObservers();
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

	/**
	 * @param observer
	 */
	public void addObserver(ItemBoxObserver observer) {
		if (!this.observers.contains(observer, true)) {
			this.observers.add(observer);
		}
	}

	/**
	 * @param observer
	 */
	public void removeObserver(ItemBoxObserver observer) {
		this.observers.removeValue(observer, true);
	}

	/**
	 * 
	 */
	public void notifyObservers() {
		for (int i = 0; i < this.observers.size; i++) {
			this.observers.get(i).updateItemBox();
		}
	}
}
