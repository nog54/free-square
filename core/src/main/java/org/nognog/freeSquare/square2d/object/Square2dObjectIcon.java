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

package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.square2d.action.Square2dActionUtlls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author goshi 2015/03/13
 */
public class Square2dObjectIcon extends Group {
	private final Image mainImage;
	private boolean enableUpDown = false;

	private Action upDownAction;

	Square2dObjectIcon(Image mainIcon) {
		if (mainIcon == null) {
			throw new IllegalArgumentException();
		}
		this.mainImage = mainIcon;
		this.addActor(this.mainImage);
		this.setWidth(mainIcon.getWidth());
		this.setHeight(mainIcon.getHeight());
		this.setOriginX(this.getWidth() / 2);

	}

	/**
	 * @return the mainImage
	 */
	public Image getMainImage() {
		return this.mainImage;
	}

	/**
	 * @param color
	 */
	public void setChildrenColor(Color color) {
		for (Actor actor : this.getChildren()) {
			actor.setColor(color);
		}
	}

	private static Action createUpDownAction() {
		final float degree = 5;
		final float cycleTime = 4;
		Action foreverRotate = Square2dActionUtlls.foreverRotate(degree, cycleTime, Interpolation.sine);
		final float upDownAmount = 5;
		Action foreverUpDown = Square2dActionUtlls.foreverUpdown(upDownAmount, cycleTime / 2, Interpolation.pow5);
		return Actions.parallel(foreverRotate, foreverUpDown);
	}

	/**
	 * @param enable
	 */
	public void setEnableUpDown(boolean enable) {
		if (this.enableUpDown == enable) {
			return;
		}
		this.enableUpDown = enable;
		if (this.enableUpDown) {
			if (this.upDownAction == null) {
				this.upDownAction = createUpDownAction();
				this.upDownAction.setActor(this);
			}
			this.getActions().add(this.upDownAction);
			return;
		}
		this.getActions().removeValue(this.upDownAction, true);
	}
}
