package org.nognog.freeSquare.model.item;

/**
 * @author goshi 2015/01/15
 * @param <T1> item class
 * @param <T2> type in T1
 */
public abstract class AbstractItem<T1 extends AbstractItem<T1, T2>, T2 extends TypeInItem<T1, T2>> implements Item<T1, T2> {

	private T2 type;

	/**
	 * @param type
	 */
	public AbstractItem(T2 type) {
		this.type = type;
	}

	/**
	 * @return type in the item
	 */
	@Override
	public T2 getTypeInItem() {
		return this.type;
	}

}
