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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/03/24
 */
public abstract class ModePresenter extends Group implements CameraObserver {

	protected final TextButton presentTextButton;

	/**
	 * @param camera
	 * @param font
	 */
	public ModePresenter(Camera camera, BitmapFont font) {
		final float width = camera.viewportWidth / 2;
		final float height = camera.viewportWidth / 8;
		TextureRegionDrawable upTexture = UiUtils.getPlaneTextureRegionDrawable((int) width, (int) height, ColorUtils.carrot);
		TextureRegionDrawable downTexture = UiUtils.getPlaneTextureRegionDrawable((int) width, (int) height, ColorUtils.pampkin);
		TextButtonStyle buttonStyle = new TextButtonStyle(upTexture, downTexture, downTexture, font);
		this.presentTextButton = new TextButton("mode presenter", buttonStyle); //$NON-NLS-1$
		this.presentTextButton.setSize(width, height);
		this.presentTextButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ModePresenter.this.tapped();
				ModePresenter.this.presentTextButton.setChecked(false);
			}
		});
		this.addActor(this.presentTextButton);
		this.setSize(width, height);
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.presentTextButton.setText(text);
	}

	/**
	 * called when tapped
	 */
	public abstract void tapped();

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x - currentCameraZoom * (camera.viewportWidth / 2);
		final float newY = camera.position.y + currentCameraZoom * (camera.viewportHeight / 2 - this.getHeight());
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}
}
