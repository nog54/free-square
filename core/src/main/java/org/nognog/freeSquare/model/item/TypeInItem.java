package org.nognog.freeSquare.model.item;

/**
 * @author goshi 2015/01/16
 * @param <T1> item class
 * @param <T2> type in T1
 */
public interface TypeInItem<T1 extends Item<T1, T2>, T2 extends TypeInItem<T1, T2>> {
	/**
	 * @param obj
	 * @return true if same item-type
	 */
	boolean isSameTypeTo(T1 obj);

	/**
	 * @param compareType
	 * @return true if same type
	 */
	boolean equals(T2 compareType);

}
