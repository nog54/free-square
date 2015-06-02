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

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.FreeSquare;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	 * @param freeSquare
	 * @param text
	 * @param leftButtonText
	 * @param rightButtonText
	 */
	public FreeSquareSimpleDialog(FreeSquare freeSquare) {
		this(freeSquare, "", "", "", createButtonStyle(freeSquare.getFont())); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @param freeSquare
	 * @param text
	 * @param leftButtonText
	 * @param rightButtonText
	 */
	private FreeSquareSimpleDialog(FreeSquare freeSquare, String text, String leftButtonText, String rightButtonText, TextButtonStyle buttonStyle) {
		super(freeSquare.getCamera().viewportWidth, freeSquare.getCamera().viewportHeight, text, freeSquare.getFont(), leftButtonText, rightButtonText, buttonStyle, buttonStyle);
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
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x - currentCameraZoom * (camera.viewportWidth / 2);
		final float newY = camera.position.y + currentCameraZoom * (camera.viewportHeight / 2 - this.getHeight());
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

}