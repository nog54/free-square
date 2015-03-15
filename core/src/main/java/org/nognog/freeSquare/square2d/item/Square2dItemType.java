package org.nognog.freeSquare.square2d.item;

import org.nognog.freeSquare.model.item.TypeInItem;
import org.nognog.freeSquare.square2d.squares.Square2dType;

/**
 * Adapter to TypeInItem　from Square2dObjectType
 * 
 * @author goshi 2015/01/16
 */
public class Square2dItemType implements TypeInItem<Square2dItem, Square2dItemType> {

	private Square2dType type;

	@SuppressWarnings("unused")
	private Square2dItemType() {
		// used by json
	}

	/**
	 * @param type
	 */
	Square2dItemType(Square2dType type) {
		this.type = type;
	}

	@Override
	public boolean isSameTypeTo(Square2dItem obj) {
		return this.equals(obj.getTypeInItem());
	}

	/**
	 * @param obj
	 * @return true if both object indicate same item-type
	 */
	@Override
	public boolean equals(Square2dItemType obj) {
		return this.type == obj.type;
	}

	@Override
	public String toString() {
		return this.type.getName();
	}

	/**
	 * @return square2d-object-type
	 */
	public Square2dType getSquare2dType() {
		return this.type;
	}
}
