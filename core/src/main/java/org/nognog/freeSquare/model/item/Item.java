package org.nognog.freeSquare.model.item;

import org.nognog.freeSquare.model.Savable;

/**
 * @author goshi 2015/01/16
 * @param <T1> item class
 * @param <T2> type in T1
 */
public interface Item<T1 extends Item<T1, T2>, T2 extends TypeInItem<T1, T2>> extends Savable {

	/**
	 * @param obj
	 * @return true if same item-type
	 */
	public boolean isSameItem(Object obj);

	/**
	 * @return type in the item
	 */
	public T2 getTypeInItem();
	
}
