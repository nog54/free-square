package org.nognog.freeSquare.model.player.item;

/**
 * @author goshi 2015/01/16
 * @param <T> item class
 */
public interface TypeInItem<T extends Item<T>> {
	/**
	 * @param obj
	 * @return true if same item-type
	 */
	boolean isSameTypeTo(T obj);

	/**
	 * @param compareType
	 * @return true if same type
	 */
	boolean equals(TypeInItem<T> compareType);
	
}
