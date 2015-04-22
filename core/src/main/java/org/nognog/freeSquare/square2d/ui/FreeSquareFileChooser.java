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

import net.dermetfan.gdx.scenes.scene2d.ui.FileChooser.Listener;
import net.dermetfan.gdx.scenes.scene2d.ui.ListFileChooser;
import net.dermetfan.gdx.scenes.scene2d.ui.ListFileChooser.Style;

import org.nognog.freeSquare.CameraObserver;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/04/20
 */
public class FreeSquareFileChooser extends Group implements CameraObserver {

	private ListFileChooser chooser;

	/**
	 * @param camera
	 * @param font
	 * @param listener
	 */
	public FreeSquareFileChooser(Camera camera, BitmapFont font, Listener listener) {
		final float width = camera.viewportWidth;
		final float height = camera.viewportHeight;
		this.setSize(width, height);
		this.chooser = new ListFileChooser(createFileChooserStyle(font), listener);
		this.chooser.setSize(width, height);
		this.addActor(this.chooser);
	}

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x - currentCameraZoom * (camera.viewportWidth / 2);
		final float newY = camera.position.y + currentCameraZoom * (camera.viewportHeight / 2 - this.getHeight());
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

	private static Style createFileChooserStyle(BitmapFont font) {
		final TextureRegionDrawable clear1 = UiUtils.getPlaneTextureRegionDrawable(1, 1, Color.CLEAR);
		final TextureRegionDrawable clearBlack1 = UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.clearBlack);
		final TextureRegionDrawable softClearBlack1 = UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.softClearBlack);
		final TextureRegionDrawable softClearPeterRiver1 = UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.softClearPeterRiver);
		final TextureRegionDrawable softClearBelizeHose1 = UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.softClearBelizeHole);

		final TextFieldStyle textFieldStyle = new TextFieldStyle(font, ColorUtils.carrot, clear1, clear1, clear1);
		final ListStyle listStyle = new ListStyle(font, ColorUtils.carrot, Color.WHITE, softClearBlack1);
		final TextButtonStyle buttonStyles = new TextButtonStyle(softClearPeterRiver1, softClearBelizeHose1, softClearPeterRiver1, font);
		final Style style = new Style(textFieldStyle, listStyle, buttonStyles, clearBlack1);
		style.space = 16;
		return style;
	}
}
