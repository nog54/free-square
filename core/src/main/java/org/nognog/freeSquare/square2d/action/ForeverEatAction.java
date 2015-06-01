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

package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.square2d.object.InterruptOtherActionException;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

/**
 * @author goshi 2015/05/31
 */
public class ForeverEatAction extends EatAction {

	private final InterruptOtherActionException interruptException = new InterruptOtherActionException(this, false);

	@Override
	public boolean act(float delta) {
		final LifeObject eater = this.getEater();
		final EatableObject nearestEatableLandingObject = eater.getEasyReachableNearestEatableLandingObject();
		if (nearestEatableLandingObject == null) {
			return false;
		}
		this.setEatObject(nearestEatableLandingObject);
		if (super.act(delta) == false) {
			throw this.interruptException;
		}
		return false;
	}
}
