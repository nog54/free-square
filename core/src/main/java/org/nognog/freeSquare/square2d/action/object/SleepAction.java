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

import org.nognog.freeSquare.square2d.action.AbstractPrioritizableAction;

/**
 * @author goshi 2015/06/17
 */
public class SleepAction extends AbstractPrioritizableAction {

	private float sleepTime;

	/**
	 * 
	 */
	public SleepAction() {

	}

	/**
	 * @param sleepTime
	 */
	public SleepAction(float sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public boolean act(float delta) {
		this.sleepTime -= delta;
		if (this.sleepTime <= 0) {
			this.sleepTime = 0;
			return true;
		}
		return false;
	}

	/**
	 * @return the sleep time
	 */
	public float getSleepTime() {
		return this.sleepTime;
	}

	/**
	 * @param sleepTime
	 */
	public void setSleepTime(float sleepTime) {
		this.sleepTime = sleepTime;
	}

	@Override
	public boolean isPerformableState() {
		return true;
	}

}
