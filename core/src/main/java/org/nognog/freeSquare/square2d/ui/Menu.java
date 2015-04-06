package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.Settings;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author goshi 2015/01/24
 */
public class Menu extends MultiLevelFlickButtonController implements CameraObserver {
	
	/**
	 * @param font
	 * @param buttonWidthHeight
	 * @param inputListener
	 */
	public Menu(BitmapFont font, float buttonWidthHeight) {
		super(font, buttonWidthHeight);
	}

	@Override
	public void updateCamera(Camera camera) {
		final OrthographicCamera orthographicCamera = (OrthographicCamera) camera;
		final float cameraViewingWidth = orthographicCamera.zoom * orthographicCamera.viewportWidth;
		final float goldenRatio = Settings.getGoldenRatio();
		this.setScale(cameraViewingWidth / (this.getButtonWidthHeight() * 3) * (goldenRatio / (1 + goldenRatio)));
	}
}
