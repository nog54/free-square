package org.nognog.freeSquare.model.player.item;

import org.nognog.freeSquare.square2d.objects.Square2dObjectType;

/**
 * @author goshi 2015/01/15
 */
public class Square2dObjectItem extends AbstractItem<Square2dObjectItem> {

	private Square2dObjectItem() {
		super(null);
	}

	/**
	 * @param type
	 */
	public Square2dObjectItem(Square2dObjectType type) {
		super(new Square2dObjectItemType(type));
	}

	@Override
	public boolean isSameItem(Object obj) {
		if (obj instanceof Square2dObjectItem) {
			return this.isSameType((Square2dObjectItem) obj);
		}
		return false;
	}

	/**
	 * @param item
	 * @return true if same type
	 */
	public boolean isSameType(Square2dObjectItem item) {
		return this.getTypeInItem().equals(item.getTypeInItem());
	}

	@Override
	public boolean isValid() {
		return this.getTypeInItem() != null;
	}

	@Override
	public String toString() {
		return this.getTypeInItem().toString();
	}
}
