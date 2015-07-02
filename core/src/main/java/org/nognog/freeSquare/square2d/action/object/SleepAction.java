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
import org.nognog.freeSquare.square2d.action.AbstractPrioritizableAction;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

/**
 * @author goshi 2015/06/17
 */
public class SleepAction extends AbstractPrioritizableAction {

	static final double tirednessRecoveryAmountPerMinute = 0.125f;
	private static final int secondsPerMinute = 60;

	private float totalSleepedTime;

	private float desiredSleepTime; // use if policy is FIX_TIME

	private float timeToNextInfluence = secondsPerMinute;

	private SleepPolicy policy = SleepPolicy.FIX_TIME;

	/**
	 * 
	 */
	public SleepAction() {
	}

	/**
	 * @param sleepTime
	 */
	public SleepAction(float sleepTime) {
		this.desiredSleepTime = sleepTime;
	}

	@Override
	public boolean act(float delta) {
		final LifeObject sleeper = this.getSleeper();
		if (sleeper.isSleeping() == false) {
			return true;
		}
		if (delta <= 0) {
			return false;
		}

		this.totalSleepedTime += delta;
		this.timeToNextInfluence -= delta;

		if (this.timeToNextInfluence <= 0) {
			final int elapsedMinute = Math.round(-this.timeToNextInfluence / secondsPerMinute + 1);
			this.timeToNextInfluence = secondsPerMinute - this.timeToNextInfluence % secondsPerMinute;
			final TirednessInfluence tirednessInfluence = InfluencesUtils.tiredness(-tirednessRecoveryAmountPerMinute * elapsedMinute);
			sleeper.applyStatusInfluence(tirednessInfluence);
		}

		if (this.satisfiesEndCondition()) {
			sleeper.wakeUp();
			return true;
		}

		return false;
	}

	/**
	 * @return
	 */
	private boolean satisfiesEndCondition() {
		final LifeObject sleeper = this.getSleeper();
		if (this.policy == SleepPolicy.FIX_TIME && this.totalSleepedTime >= this.desiredSleepTime) {
			return true;
		}
		if (this.policy == SleepPolicy.COMPLETE_RECOVERY && sleeper.getLife().getStatus().getTiredness() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	private LifeObject getSleeper() {
		return (LifeObject) this.getActor();
	}

	/**
	 * @return the totalSleepedTime
	 */
	public float getTotalSleepedTime() {
		return this.totalSleepedTime;
	}

	/**
	 * @param totalSleepedTime
	 *            the totalSleepedTime to set
	 */
	public void setTotalSleepedTime(float totalSleepedTime) {
		this.totalSleepedTime = totalSleepedTime;
	}

	/**
	 * @return the desired sleep time
	 */
	public float getDesiredSleepTime() {
		return this.desiredSleepTime;
	}

	/**
	 * @param desiredSleepTime
	 */
	public void setDesiredSleepTime(float desiredSleepTime) {
		this.desiredSleepTime = desiredSleepTime;
	}

	/**
	 * @return the timeToNextInfluence
	 */
	public float getTimeToNextInfluence() {
		return this.timeToNextInfluence;
	}

	/**
	 * @param policy
	 */
	public void setPolicy(SleepPolicy policy) {
		this.policy = policy;
	}

	/**
	 * @return the policy
	 */
	public SleepPolicy getPolicy() {
		return this.policy;
	}

	@Override
	public boolean isPerformableState() {
		return true;
	}

	@Override
	public void restart() {
		this.timeToNextInfluence = secondsPerMinute;
		this.totalSleepedTime = 0;
		this.desiredSleepTime = 0;
		this.policy = SleepPolicy.FIX_TIME;
	}

}
