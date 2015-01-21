package org.nognog.freeSquare.model.player;

import org.nognog.freeSquare.model.Savable;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author goshi 2015/01/15
 * @param <T>
 */
public class PossessedItem<T extends Item<T, ?>> implements Savable {
	/** アイテムの最大保持数 */
	public static final int maxQuantity = 999; // 現在は適当に決めてる

	private T item;
	private int quantity;

	@SuppressWarnings("unused")
	private PossessedItem() {
		// used by json reader
	}

	/**
	 * @param item
	 */
	public PossessedItem(T item) {
		this.item = item;
	}

	/**
	 * @return type in the item
	 */
	public T getItem() {
		return this.item;
	}

	/**
	 * @return quantity
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * @param amount
	 * @return quantity after the increase
	 */
	public int increaseQuantity(int amount) {
		final int increaseResult = this.quantity + amount;
		this.quantity = MathUtils.clamp(increaseResult, 0, maxQuantity);
		return this.quantity;
	}

	/**
	 * @param amount
	 * @return quantity after the decrease
	 */
	public int decreaseQuantity(int amount) {
		return this.increaseQuantity(-amount);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.item).append(" ×").append(this.quantity); //$NON-NLS-1$
		return sb.toString();
	}

	@Override
	public boolean isValid() {
		return this.item.isValid();
	}
}
