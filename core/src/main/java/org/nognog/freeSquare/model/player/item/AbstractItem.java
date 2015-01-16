package org.nognog.freeSquare.model.player.item;

/**
 * @author goshi 2015/01/15
 * @param <T>
 *            extends class
 */
public abstract class AbstractItem<T extends AbstractItem<T>> implements Item<T> {

	TypeInItem<T> type;

	/**
	 * @param type
	 */
	public AbstractItem(TypeInItem<T> type) {
		this.type = type;
	}


	/**
	 * @return type in the item
	 */
	@Override
	public TypeInItem<T> getTypeInItem() {
		return this.type;
	}

}
