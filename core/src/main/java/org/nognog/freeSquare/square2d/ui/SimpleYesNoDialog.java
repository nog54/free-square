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

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

/**
 * @author goshi 2015/04/24
 */
public abstract class SimpleYesNoDialog extends SimpleDialog {

	/**
	 * @param width
	 * @param height
	 * @param text
	 * @param textFont
	 * @param yesButtonStyle
	 * @param noButtonStyle
	 */
	public SimpleYesNoDialog(float width, float height, String text, BitmapFont textFont, TextButtonStyle yesButtonStyle, TextButtonStyle noButtonStyle) {
		super(width, height, text, textFont, "Yes", "No", yesButtonStyle, noButtonStyle); //$NON-NLS-1$ //$NON-NLS-2$
		this.setListener(new SimpleDialog.SimpleDialogListener() {

			@Override
			public void rightButtonClicked() {
				SimpleYesNoDialog.this.no();
			}

			@Override
			public void leftButtonClicked() {
				SimpleYesNoDialog.this.yes();
			}
		});

	}

	/**
	 * called when yes clicked
	 */
	protected abstract void yes();

	/**
	 * called when no clicked
	 */
	protected abstract void no();

}
