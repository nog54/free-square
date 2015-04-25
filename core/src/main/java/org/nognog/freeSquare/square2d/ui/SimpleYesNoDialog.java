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

import org.nognog.freeSquare.Settings;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * @author goshi 2015/04/24
 */
public abstract class SimpleYesNoDialog extends Group {

	/**
	 * @param width
	 * @param height
	 * @param text
	 * @param textFont
	 * @param yesButtonStyle
	 * @param noButtonStyle
	 */
	public SimpleYesNoDialog(float width, float height, String text, BitmapFont textFont, TextButtonStyle yesButtonStyle, TextButtonStyle noButtonStyle) {
		final Table table = new Table();
		final Label label = new Label(text, new LabelStyle(textFont, ColorUtils.carrot));
		label.setWidth(width);
		label.setWrap(true);
		final float goldenRatio = Settings.getGoldenRatio();
		final float leftSpace = width / (3 + 2 * goldenRatio) / 2;
		final float rightSpace = leftSpace;
		final float textTopSpace = leftSpace;
		table.add(label).left().width(width - leftSpace - rightSpace).padLeft(leftSpace).padRight(rightSpace).padTop(textTopSpace).row();
		final TextButton yesButton = new TextButton("Yes", yesButtonStyle); //$NON-NLS-1$
		final TextButton noButton = new TextButton("No", noButtonStyle); //$NON-NLS-1$
		yesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SimpleYesNoDialog.this.yes();
			}
		});
		noButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SimpleYesNoDialog.this.no();
			}
		});
		final float centerSpace = leftSpace;
		final float upSpace = width / (2 + goldenRatio);
		final float downSpace = upSpace;
		table.add(yesButton).expandX().fillX().expandY().fillY().padLeft(leftSpace).padRight(centerSpace / 2).padTop(upSpace).padBottom(downSpace).uniform();
		table.add(noButton).expandX().fillX().expandY().fillY().padRight(rightSpace).padLeft(centerSpace / 2).padTop(upSpace).padBottom(downSpace).uniform();
		table.setBackground(UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.clearBlack));
		table.setWidth(width);
		table.setHeight(height);
		this.addActor(table);
		this.setDebug(true);
	}

	protected abstract void yes();

	protected abstract void no();
}
