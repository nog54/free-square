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

import org.nognog.freeSquare.model.life.status.influence.InfluencesUtils;
import org.nognog.freeSquare.model.life.status.influence.TirednessInfluence;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/06/30
 */
public class ConstantlyTireAction extends Action {

	private double tirednessIncreaseAmountPerMinute;
	private static int secondsPerMinute = 60;

	private float timeToNextTiredness = 0;

	/**
	 * 
	 */
	public ConstantlyTireAction() {
	}

	/**
	 * @param tirednessIncreaseAmountPerMinute
	 */
	public ConstantlyTireAction(double tirednessIncreaseAmountPerMinute) {
		this.tirednessIncreaseAmountPerMinute = tirednessIncreaseAmountPerMinute;
	}

	@Override
	public boolean act(float delta) {
		final LifeObject lifeObject = this.getTireObject();
		if (lifeObject.isSleeping()) {
			return false;
		}
		this.timeToNextTiredness -= delta;
		if (this.timeToNextTiredness <= 0) {
			final int elapsedMinute = (int) (this.timeToNextTiredness / secondsPerMinute) + 1;
			this.timeToNextTiredness += elapsedMinute * secondsPerMinute;
			final TirednessInfluence tirednessInfluence = InfluencesUtils.tiredness(elapsedMinute * this.tirednessIncreaseAmountPerMinute);
			lifeObject.applyStatusInfluence(tirednessInfluence);
		}
		return false;
	}

	/**
	 * @return an object to be tired
	 */
	private LifeObject getTireObject() {
		return (LifeObject) this.getActor();
	}

	@Override
	public void restart() {
		this.tirednessIncreaseAmountPerMinute = 0;
	}

	/**
	 * @return the tirednessIncreaseAmountPerMinute
	 */
	public double getTirednessIncreaseAmountPerMinute() {
		return this.tirednessIncreaseAmountPerMinute;
	}

	/**
	 * @param tirednessIncreaseAmountPerMinute
	 *            the tirednessIncreaseAmountPerMinute to set
	 */
	public void setTirednessIncreaseAmountPerMinute(double tirednessIncreaseAmountPerMinute) {
		this.tirednessIncreaseAmountPerMinute = tirednessIncreaseAmountPerMinute;
	}

}
