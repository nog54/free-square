package org.nognog.freeSquare.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi
 * 2015/01/17
 */
public class UiUtils {
	/**
	 * @param width
	 * @param height
	 * @param color
	 * @return plane texture region
	 */
	public static TextureRegionDrawable createPlaneTextureRegionDrawable(int width, int height, Color color) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
	}
}
