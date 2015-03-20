package org.nognog.freeSquare.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi
 * 2015/01/19
 */
public interface SimpleDrawable {
	
	/**
	 * @return texture
	 */
	Texture getSimpleTexture();

	/**
	 * @return color
	 */
	Color getColor();
}
