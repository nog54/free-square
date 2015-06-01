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

import org.nognog.freeSquare.square2d.action.MomentumMoveAction;
import org.nognog.freeSquare.square2d.action.Square2dActions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/05/25
 */
public class MovableSquare2dObject extends Square2dObject {

	/**
	 * 
	 */
	public MovableSquare2dObject() {
		super();
		this.addListener();
	}

	/**
	 * @param type
	 */
	public MovableSquare2dObject(Square2dObjectType<?> type) {
		super(type);
		this.addListener();
	}

	private void addListener() {
		this.addListener(new ActorGestureListener() {

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				MovableSquare2dObject.this.clearMomentumAction();
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				MovableSquare2dObject.this.moveBy(deltaX, deltaY);
			}

			@Override
			public void fling(InputEvent event, float velocityX, float velocityY, int button) {
				final float deceleration = 200;
				if (MovableSquare2dObject.this.isPerformingPriorityAction() == false) {
					MovableSquare2dObject.this.setPriorityAction(Square2dActions.momentumMove(deceleration, velocityX, velocityY));
				}
			}
		});
	}

	/**
	 * 
	 */
	public void clearMomentumAction() {
		for (Action action : this.getActions().<Action> toArray(Action.class)) {
			if (action instanceof MomentumMoveAction) {
				this.removeAction(action);
			}
		}
		if (this.getPriorityAction() instanceof MomentumMoveAction) {
			this.setPriorityAction(null);
		}
	}

	/**
	 * @return true if this is moving
	 */
	public boolean isMovingWithMomentum() {
		for (Action action : this.getActions().<Action> toArray(Action.class)) {
			if (action instanceof MomentumMoveAction) {
				return true;
			}
		}
		return false;
	}

}
