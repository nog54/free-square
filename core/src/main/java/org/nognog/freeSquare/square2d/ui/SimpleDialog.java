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
public class SimpleDialog extends Group {

	private Label textLabel;
	private TextButton leftButton;
	private TextButton rightButton;
	private SimpleDialogListener listener;

	/**
	 * @param width
	 * @param height
	 * @param text
	 * @param textFont
	 * @param leftButtonText
	 * @param rightButtonText
	 * @param leftButtonStyle
	 * @param rightButtonStyle
	 */
	public SimpleDialog(float width, float height, String text, BitmapFont textFont, String leftButtonText, String rightButtonText, TextButtonStyle leftButtonStyle, TextButtonStyle rightButtonStyle) {
		this.setWidth(width);
		this.setHeight(height);
		final Table table = new Table();
		this.textLabel = new Label(text, new LabelStyle(textFont, ColorUtils.carrot));
		this.textLabel.setWidth(width);
		this.textLabel.setWrap(true);
		final float goldenRatio = Settings.getGoldenRatio();
		final float leftSpace = width / (3 + 2 * goldenRatio) / 2;
		final float rightSpace = leftSpace;
		final float textTopSpace = leftSpace;
		table.add(this.textLabel).left().width(width - leftSpace - rightSpace).padLeft(leftSpace).padRight(rightSpace).padTop(textTopSpace).row();
		this.leftButton = new TextButton(leftButtonText, leftButtonStyle);
		this.rightButton = new TextButton(rightButtonText, rightButtonStyle);
		this.leftButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (SimpleDialog.this.getListener() != null) {
					SimpleDialog.this.getListener().leftButtonClicked();
				}
			}
		});
		this.rightButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (SimpleDialog.this.getListener() != null) {
					SimpleDialog.this.getListener().rightButtonClicked();
				}
			}
		});
		final float centerSpace = leftSpace;
		final float upSpace = width / (2 + goldenRatio);
		final float downSpace = upSpace;
		table.add(this.leftButton).expandX().fillX().expandY().fillY().padLeft(leftSpace).padRight(centerSpace / 2).padTop(upSpace).padBottom(downSpace).uniform();
		table.add(this.rightButton).expandX().fillX().expandY().fillY().padRight(rightSpace).padLeft(centerSpace / 2).padTop(upSpace).padBottom(downSpace).uniform();
		table.setBackground(UiUtils.getPlaneTextureRegionDrawable(1, 1, ColorUtils.clearBlack));
		table.setWidth(width);
		table.setHeight(height);
		this.addActor(table);
	}

	/**
	 * @return listener
	 */
	public SimpleDialogListener getListener() {
		return this.listener;
	}

	/**
	 * @param listener
	 */
	public void setListener(SimpleDialogListener listener) {
		this.listener = listener;
	}

	/**
	 * @return left button text
	 */
	public CharSequence getLeftButtonText() {
		return this.leftButton.getText();
	}

	/**
	 * @param text
	 */
	public void setLeftButtonText(String text) {
		this.leftButton.setText(text);
	}

	/**
	 * @return right button text
	 */
	public CharSequence getRightButtonText() {
		return this.rightButton.getText();
	}

	/**
	 * @param text
	 */
	public void setRightButtonText(String text) {
		this.rightButton.setText(text);
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.textLabel.setText(text);
	}

	/**
	 * @author goshi 2015/05/05
	 */
	public static interface SimpleDialogListener {
		/**
		 * called when left button clicked
		 */
		void leftButtonClicked();

		/**
		 * called when right button clicked
		 */
		void rightButtonClicked();
	}
}
