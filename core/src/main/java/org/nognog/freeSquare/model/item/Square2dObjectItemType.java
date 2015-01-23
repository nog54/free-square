package org.nognog.freeSquare.model.item;

import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

/**
 * Adapter to TypeInItem　from Square2dObjectType
 * 
 * @author goshi 2015/01/16
 */
public class Square2dObjectItemType implements TypeInItem<Square2dObjectItem, Square2dObjectItemType> {

	private Square2dObjectType type;

	@SuppressWarnings("unused")
	private Square2dObjectItemType() {
		// used by json
	}

	/**
	 * @param type
	 */
	Square2dObjectItemType(Square2dObjectType type) {
		this.type = type;
	}

	@Override
	public boolean isSameTypeTo(Square2dObjectItem obj) {
		return this.equals(obj.getTypeInItem());
	}

	/**
	 * @param obj
	 * @return true if both object indicate same item-type
	 */
	@Override
	public boolean equals(Square2dObjectItemType obj) {
		return this.type == obj.type;
	}

	@Override
	public String toString() {
		return this.type.getName();
	}

	/**
	 * @return square2d-object-type
	 */
	public Square2dObjectType getSquare2dObjectType() {
		return this.type;
	}
}
