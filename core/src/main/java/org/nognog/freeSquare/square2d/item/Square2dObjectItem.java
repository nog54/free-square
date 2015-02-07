package org.nognog.freeSquare.square2d.item;

import org.nognog.freeSquare.model.item.AbstractItem;
import org.nognog.freeSquare.model.item.DrawableItem;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi 2015/01/15
 */
public class Square2dObjectItem extends AbstractItem<Square2dObjectItem, Square2dObjectItemType> implements DrawableItem {

	private static final ObjectMap<Square2dObjectType<?>, Square2dObjectItem> items = new ObjectMap<>();

	private Square2dObjectItem() {
		super(null);
	}

	/**
	 * @param type
	 */
	private Square2dObjectItem(Square2dObjectType<?> type) {
		super(new Square2dObjectItemType(type));
	}

	/**
	 * @param type
	 * @return type instance
	 */
	public static synchronized Square2dObjectItem getInstance(Square2dObjectType<?> type) {
		if (items.containsKey(type)) {
			return items.get(type);
		}
		Square2dObjectItem typeInstance = new Square2dObjectItem(type);
		items.put(type, typeInstance);
		return typeInstance;
	}

	/**
	 * @param item
	 * @return true if same type
	 */
	@Override
	public boolean isSameItem(Square2dObjectItem item) {
		return this.getTypeInItem().equals(item.getTypeInItem());
	}

	@Override
	public Texture getTexture() {
		return this.getTypeInItem().getSquare2dObjectType().getTexture();
	}

	@Override
	public Color getColor() {
		return this.getTypeInItem().getSquare2dObjectType().getColor();
	}
	
	/**
	 * @return square2d object
	 */
	public Square2dObject createSquare2dObject(){
		return this.getTypeInItem().getSquare2dObjectType().create();
	}

	/**
	 * @return all items
	 */
	public static Square2dObjectItem[] getAllItems() {
		final Square2dObjectType<?>[] allTypes = Square2dObjectType.Manager.getAllTypeValues();
		final Square2dObjectItem[] allItems = new Square2dObjectItem[allTypes.length];
		int i = 0;
		for (Square2dObjectType<?> type : allTypes) {
			allItems[i] = Square2dObjectItem.getInstance(type);
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


}
