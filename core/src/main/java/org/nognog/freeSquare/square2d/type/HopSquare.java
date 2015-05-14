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

package org.nognog.freeSquare.square2d.type;

import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/15
 */
public class HopSquare extends SimpleSquare2d {

	@SuppressWarnings("unused")
	private HopSquare() {
		// used by json
		this(null);
	}

	/**
	 * @param type
	 */
	public HopSquare(Square2dType type) {
		super(type);
		this.addListener(new ActorGestureListener() {
			private static final float hopAmount = 10;
			private static final float hopTime = 0.2f;
			HopSquare target = HopSquare.this;

			@Override
			public void tap(InputEvent event, float x, float y, int pointer, int button) {
				if (this.target.containsPosition(x, y)) {
					Action squareHopAction = Actions.sequence(Actions.moveBy(0, hopAmount, hopTime / 2), Actions.moveBy(0, -hopAmount, hopTime / 2));
					this.target.getSquareImage().addAction(squareHopAction);
					for (Square2dObject object : this.target.getAllLandingSquareObjectsOnStage()) {
						Action objectHopAction = Actions.sequence(Actions.moveBy(0, hopAmount, hopTime / 2), Actions.moveBy(0, -hopAmount, hopTime / 2));
						object.getIcon().addAction(objectHopAction);
					}
				}
			}
		});
	}
}
