package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author goshi 2015/01/24
 */
public class Menu extends FlickButtonController implements CameraObserver {

	/**
	 * @param font
	 * @param buttonWidthHeight
	 * @param inputListener
	 */
	public Menu(BitmapFont font, float buttonWidthHeight, FlickInputListener inputListener) {
		super(font, buttonWidthHeight, inputListener);
	}

	@Override
	public void updateCamera(Camera camera) {
		// nothing
	}
}
