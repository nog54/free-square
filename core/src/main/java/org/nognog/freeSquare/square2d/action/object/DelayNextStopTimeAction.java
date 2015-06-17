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

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

/**
 * @author goshi 2015/01/29
 */
public class DelayNextStopTimeAction extends DelayAction {
	private StopTimeGenerator stopTimeGenerator;

	/**
	 * 
	 */
	public DelayNextStopTimeAction() {

	}

	/**
	 * @param stopTimeGenerator
	 */
	public DelayNextStopTimeAction(StopTimeGenerator stopTimeGenerator) {
		super(stopTimeGenerator.nextStopTime());
		this.stopTimeGenerator = stopTimeGenerator;
	}

	/**
	 * @param stopTimeGenerator
	 */
	public void setStopTimeGenerator(StopTimeGenerator stopTimeGenerator) {
		this.stopTimeGenerator = stopTimeGenerator;
	}

	/**
	 * @return using StopTimeGenerator
	 */
	public StopTimeGenerator getStopTimeGenerator() {
		return this.stopTimeGenerator;
	}

	@Override
	public void restart() {
		super.restart();
		this.setDuration(this.stopTimeGenerator.nextStopTime());
	}
}
