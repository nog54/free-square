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

package org.nognog.freeSquare.activity;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.square2d.ui.FreeSquareSimpleYesNoDialog;
import org.nognog.freeSquare.square2d.ui.SimpleDialog;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;

/**
 * @author goshi 2015/04/24
 */
public class InitializeActivity extends FreeSquareActivity {
	private SimpleDialog yesNoDialog;

	/**
	 * @param freeSquare
	 */
	public InitializeActivity(final FreeSquare freeSquare) {
		super(freeSquare);

		final float width = freeSquare.getCamera().viewportWidth;
		final float height = freeSquare.getCamera().viewportHeight;
		final String message = "Create New Player?"; //$NON-NLS-1$
		this.yesNoDialog = new FreeSquareSimpleYesNoDialog(freeSquare, message) {

			@Override
			protected void yes() {
				freeSquare.createPlayer();
				freeSquare.setActivity(freeSquare.getActivityFactory().getMainActivity());
			}

			@Override
			protected void no() {
				freeSquare.exit();
			}

		};
		this.yesNoDialog.setWidth(width);
		this.yesNoDialog.setHeight(height);
		this.addActor(this.yesNoDialog);
	}

	@Override
	public void updateCamera(Camera camera) {
		// nothing
	}

	@Override
	public void notify(SquareEvent event) {
		// nothing
	}

	@Override
	public InputProcessor getInputProcesser() {
		return null;
	}

	@Override
	public void resume() {
		// nothing
	}

	@Override
	public void pause() {
		// nothing
	}

}
