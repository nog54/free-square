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

package org.nognog.freeSquare.square2d.ui;

import org.nognog.util.graphic2d.camera.Camera;
import org.nognog.util.graphic2d.camera.CameraObserver;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/05/05
 */
public class FreeSquareSimpleDialog extends SimpleDialog implements CameraObserver {

	private static final TextureRegionDrawable softClearPeterRiverDrawable = UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.softClearPeterRiver);
	private static final TextureRegionDrawable softClearBelizeHoleDrawable = UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.softClearBelizeHole);

	/**
	 * @param camera
	 * @param font
	 */
	public FreeSquareSimpleDialog(Camera camera, BitmapFont font) {
		this(camera.getViewportWidth(), camera.getViewportHeight(), "", "", "", font, createButtonStyle(font)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @param freeSquare
	 * @param text
	 * @param leftButtonText
	 * @param rightButtonText
	 */
	private FreeSquareSimpleDialog(float width, float height, String text, String leftButtonText, String rightButtonText, BitmapFont font, TextButtonStyle buttonStyle) {
		super(width, height, text, font, leftButtonText, rightButtonText, buttonStyle, buttonStyle);
	}

	/**
	 * @param font
	 * @return
	 */
	private static TextButtonStyle createButtonStyle(BitmapFont font) {
		return new TextButtonStyle(softClearPeterRiverDrawable, softClearBelizeHoleDrawable, softClearPeterRiverDrawable, font);
	}

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = camera.getZoom();
		final float newX = camera.getX() - currentCameraZoom * (camera.getViewportWidth() / 2);
		final float newY = camera.getY() + currentCameraZoom * (camera.getViewportHeight() / 2 - this.getHeight());
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}
}
