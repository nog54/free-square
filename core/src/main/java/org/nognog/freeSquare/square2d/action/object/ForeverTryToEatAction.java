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

import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

/**
 * @author goshi 2015/05/31
 */
public class ForeverTryToEatAction extends EatAction {

	private EatableObject currentTarget;

	@Override
	public boolean act(float delta) {
		if (this.currentTarget == null) {
			return false;
		}
		this.setEatObject(this.currentTarget);
		super.act(delta);
		return false;
	}

	@Override
	public boolean isPerformableState() {
		final LifeObject eater = this.getEater();
		this.currentTarget = eater.getEasyReachableNearestEatableLandingObject();
		return this.currentTarget != null;
	}

}
