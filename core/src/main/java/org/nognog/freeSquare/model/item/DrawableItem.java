package org.nognog.freeSquare.model.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi
 * 2015/01/19
 */
public interface DrawableItem {
	
	/**
	 * @return texture
	 */
	Texture getTexture();

	/**
	 * @return color
	 */
	Color getColor();
}
