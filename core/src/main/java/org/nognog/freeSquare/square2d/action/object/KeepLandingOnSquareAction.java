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
import org.nognog.freeSquare.square2d.action.AbstractPrioritizableAction;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2015/05/31
 */
public class KeepLandingOnSquareAction extends AbstractPrioritizableAction {

	private Action actionToGotoNearestVertexOfSquare;

	/**
	 * 
	 */
	public KeepLandingOnSquareAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean act(float delta) {
		if (this.isPerformableState() == false) {
			return false;
		}
		if (this.actionToGotoNearestVertexOfSquare == null) {
			this.setupActionToGoToNearestVertexOfSquare();
		}

		if (this.actionToGotoNearestVertexOfSquare.act(delta) == true) {
			this.actionToGotoNearestVertexOfSquare = null;
			final Square2dObject object = (Square2dObject) this.getActor();
			object.handleEvent(new UpdateSquareObjectEvent(object));
		}
		return false;
	}

	private void setupActionToGoToNearestVertexOfSquare() {
		final Square2dObject object = (Square2dObject) this.getActor();
		final Vertex nearestSquareVertex = object.getNearestSquareVertex();
		this.actionToGotoNearestVertexOfSquare = Actions.moveTo(nearestSquareVertex.x, nearestSquareVertex.y, 0.5f, Interpolation.pow2);
		this.actionToGotoNearestVertexOfSquare.setActor(this.getActor());
	}

	@Override
	public boolean isPerformableState() {
		final Square2dObject object = (Square2dObject) this.getActor();
		return object.getSquare() != null && object.isLandingOnSquare() == false;
	}
}
