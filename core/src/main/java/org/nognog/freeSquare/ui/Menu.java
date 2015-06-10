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

package org.nognog.freeSquare.ui;

import org.nognog.freeSquare.Settings;
import org.nognog.gdx.util.camera.Camera;
import org.nognog.gdx.util.camera.CameraObserver;
import org.nognog.gdx.util.ui.MultiLevelFlickButtonController;

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
		final float cameraViewingWidth = camera.getZoom() * camera.getViewportWidth();
		final float goldenRatio = Settings.getGoldenRatio();
		this.setScale(cameraViewingWidth / (this.getButtonWidthHeight() * 3) * (goldenRatio / (1 + goldenRatio)));
	}
}
