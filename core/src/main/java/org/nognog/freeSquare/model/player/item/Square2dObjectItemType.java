package org.nognog.freeSquare.model.player.item;

import org.nognog.freeSquare.square2d.objects.Square2dObjectType;

/**
 * Adapter to TypeInItem　from Square2dObjectType 
 * 
 * @author goshi 2015/01/16
 */
public class Square2dObjectItemType implements TypeInItem<Square2dObjectItem> {
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
	public boolean equals(Square2dObjectItemType obj) {
		return this.type == obj.type;
	}

	@Override
	public boolean equals(TypeInItem<Square2dObjectItem> compareType) {
		if (compareType instanceof Square2dObjectItemType) {
			return this.equals((Square2dObjectItemType) compareType);
		}
		return false;
	}

	@Override
	public String toString() {
		return this.type.getName();
	}
}
