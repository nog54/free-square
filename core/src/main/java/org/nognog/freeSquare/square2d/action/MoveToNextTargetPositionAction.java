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

import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/29
 */
public class MoveToNextTargetPositionAction extends MoveToTargetPositionAction {
	private TargetPositionGenerator targetPositionGenerator;
	
	private boolean isGoingToTargetPosition = false;

	/**
	 * 
	 */
	public MoveToNextTargetPositionAction() {
		super();
	}

	/**
	 * @param targetPositionGenerator
	 * @param speed
	 */
	public MoveToNextTargetPositionAction(TargetPositionGenerator targetPositionGenerator, float speed) {
		super(targetPositionGenerator.nextTargetPosition(), speed);
		this.targetPositionGenerator = targetPositionGenerator;
	}

	/**
	 * @param targetPositionGenerator
	 */
	public void setTargetPositionGenerator(TargetPositionGenerator targetPositionGenerator) {
		this.targetPositionGenerator = targetPositionGenerator;
	}

	/**
	 * @return using generator
	 */
	public TargetPositionGenerator getTargetPositionGenerator() {
		return this.targetPositionGenerator;
	}
	
	@Override
	public boolean act(float delta) {
		if(this.isGoingToTargetPosition == false){
			Vector2 nextTargetPosition = this.targetPositionGenerator.nextTargetPosition();
			if(nextTargetPosition == null){
				return false;
			}
			this.setTargetPositionX(nextTargetPosition.x);
			this.setTargetPositionY(nextTargetPosition.y);
			this.isGoingToTargetPosition = true;
		}
		return super.act(delta);
	}
	
	@Override
	public void restart() {
		this.isGoingToTargetPosition = false;
		super.restart();
	}

}
