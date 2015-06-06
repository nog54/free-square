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

package org.nognog.freeSquare.square2d.object.types.life;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/11
 */
public class FlyingLifeObject extends LifeObject {

	/**
	 * @param life
	 */
	public FlyingLifeObject(Life life) {
		this(LifeObjectTypeManager.getInstance().getBindingLifeObjectType(life), life);
	}

	/**
	 * @param type
	 */
	public FlyingLifeObject(LifeObjectType type) {
		this(type, new Life(type.getFamily()));
	}

	/**
	 * @param type
	 * @param life
	 */
	private FlyingLifeObject(LifeObjectType type, Life life) {
		super(type, life);
	}

	@Override
	public Vector2 nextTargetPosition() {
		if (this.getSquare() == null) {
			return null;
		}
		final float x = MathUtils.random(0, this.getSquare().getWidth());
		final float y = MathUtils.random(0, this.getSquare().getHeight());
		return new Vector2(x, y);
	}

	@Override
	public EatableObject getEasyReachableNearestEatableLandingObject() {
		EatableObject result = null;
		float resultDistance = 0;
		for (Square2dObject object : this.getSquare().getObjects()) {
			if (object instanceof EatableObject && object.isLandingOnSquare()) {
				if (result == null) {
					result = (EatableObject) object;
					resultDistance = this.getDistanceTo(object);
				}
				final float objectDistance = this.getDistanceTo(object);
				if (objectDistance < resultDistance) {
					result = (EatableObject) object;
					resultDistance = objectDistance;
				}
			}
		}
		return result;
	}
}
