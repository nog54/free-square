package org.nognog.freeSquare.model.item;

import org.nognog.freeSquare.model.Savable;

/**
 * @author goshi 2015/01/16
 * @param <T>
 */
public interface Item<T extends Item<T>> extends Savable {

	/**
	 * @param obj
	 * @return true if same item-type
	 */
	public boolean isSameItem(Object obj);

	/**
	 * @return type in the item
	 */
	public TypeInItem<T> getTypeInItem();
}
