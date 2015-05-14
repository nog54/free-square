/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.item;

import org.nognog.freeSquare.model.item.AbstractItem;
import org.nognog.freeSquare.model.item.DrawableItem;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.type.Square2dType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi 2015/01/15
 */
public class Square2dItem extends AbstractItem<Square2dItem, Square2dItemType> implements DrawableItem {

	private static final ObjectMap<Square2dType, Square2dItem> items = new ObjectMap<>();

	private Square2dItem() {
		super(null);
	}

	/**
	 * @param type
	 */
	private Square2dItem(Square2dType type) {
		super(new Square2dItemType(type));
	}

	/**
	 * @param type
	 * @return type instance
	 */
	public static synchronized Square2dItem getInstance(Square2dType type) {
		if (items.containsKey(type)) {
			return items.get(type);
		}
		Square2dItem typeInstance = new Square2dItem(type);
		items.put(type, typeInstance);
		return typeInstance;
	}

	/**
	 * @param item
	 * @return true if same type
	 */
	@Override
	public boolean isSameItem(Square2dItem item) {
		return this.getTypeInItem().equals(item.getTypeInItem());
	}

	@Override
	public Texture getSimpleTexture() {
		return this.getTypeInItem().getSquare2dType().getTexture();
	}

	/**
	 * @return square2d object
	 */
	public Square2d createSquare2d() {
		return this.getTypeInItem().getSquare2dType().create();
	}

	/**
	 * @return all items
	 */
	public static Square2dItem[] getAllItems() {
		final Square2dType[] allTypes = Square2dType.values();
		final Square2dItem[] allItems = new Square2dItem[allTypes.length];
		int i = 0;
		for (Square2dType type : allTypes) {
			allItems[i] = Square2dItem.getInstance(type);
			i++;
		}
		return allItems;
	}

	@Override
	public boolean isValid() {
		return this.getTypeInItem() != null;
	}

	@Override
	public String toString() {
		return this.getTypeInItem().toString();
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}

}
