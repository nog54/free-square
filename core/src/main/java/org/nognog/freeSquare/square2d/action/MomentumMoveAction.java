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

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/05/24
 */
public class MomentumMoveAction extends Action {

	private float deceleration;
	private float velocityX;
	private float velocityY;

	/**
	 * 
	 */
	public MomentumMoveAction() {
	}

	/**
	 * @param deceleration
	 * @param velocityX
	 * @param velocityY
	 */
	public MomentumMoveAction(float deceleration, float velocityX, float velocityY) {
		this.deceleration = deceleration;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	@Override
	public boolean act(float delta) {
		if (this.deceleration == 0) {
			return true;
		}
		this.calcCameraMomentum(delta);
		return this.velocityX == 0 && this.velocityY == 0;
	}

	/**
	 * @param delta
	 */
	private void calcCameraMomentum(float delta) {
		this.calcCameraMomentumX(delta);
		this.calcCameraMomentumY(delta);
	}

	private void calcCameraMomentumX(float delta) {
		if (this.velocityX == 0) {
			return;
		}
		this.actor.moveBy(delta * this.velocityX, 0);
		if (Math.abs(this.velocityX) < this.deceleration) {
			this.velocityX = 0;
			return;
		}
		final float signum = Math.signum(this.velocityX);
		this.velocityX += -signum * this.deceleration;
	}

	private void calcCameraMomentumY(float delta) {
		if (this.velocityY == 0) {
			return;
		}
		this.actor.moveBy(0, delta * this.velocityY);
		if (Math.abs(this.velocityY) < this.deceleration) {
			this.velocityY = 0;
			return;
		}
		final float signum = Math.signum(this.velocityY);
		this.velocityY += -signum * this.deceleration;
	}

	/**
	 * @return the deceleration
	 */
	public float getDeceleration() {
		return this.deceleration;
	}

	/**
	 * @param deceleration
	 *            the deceleration to set
	 */
	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}

	/**
	 * @return the velocityX
	 */
	public float getVelocityX() {
		return this.velocityX;
	}

	/**
	 * @param velocityX
	 *            the velocityX to set
	 */
	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	/**
	 * @return the velocityY
	 */
	public float getVelocityY() {
		return this.velocityY;
	}

	/**
	 * @param velocityY
	 *            the velocityY to set
	 */
	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

}
