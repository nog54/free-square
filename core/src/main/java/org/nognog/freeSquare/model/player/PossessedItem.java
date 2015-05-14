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

import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author goshi 2015/01/15
 * @param <T>
 */
public class PossessedItem<T extends Item<?, ?>> {
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

	/**
	 * @return true if valid
	 */
	public boolean isValid() {
		return this.item.isValid();
	}
}
