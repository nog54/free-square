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

package org.nognog.freeSquare.square2d.action.object;

import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2015/05/31
 */
public class KeepLandingOnSquareAction extends Action {
	/**
	 * 
	 */
	public KeepLandingOnSquareAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean act(float delta) {
		final Square2dObject object = (Square2dObject) this.getActor();
		if (object.getSquare() != null && object.isLandingOnSquare() == false && object.isPerformingPriorityAction() == false) {
			final Vertex nearestSquareVertex = object.getNearestSquareVertex();
			final Action moveToNearestSquareVertexAction = Actions.moveTo(nearestSquareVertex.x, nearestSquareVertex.y, 0.5f, Interpolation.pow2);
			final Action notifyUpdateEventAction = Square2dActions.fireEventAction(object, new UpdateSquareObjectEvent(object));
			final Action goToSquareNearestVertexAction = Actions.sequence(moveToNearestSquareVertexAction, notifyUpdateEventAction);
			object.setPriorityAction(goToSquareNearestVertexAction);
		}
		return false;
	}
}
