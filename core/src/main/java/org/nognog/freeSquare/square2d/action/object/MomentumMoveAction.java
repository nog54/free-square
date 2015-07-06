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
import org.nognog.gdx.util.Movable;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author goshi 2015/05/24
 */
public class MomentumMoveAction extends AbstractPrioritizableAction {

	private Movable movable;

	private float deceleration;
	private float decelerationX;
	private float decelerationY;
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
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.setDeceleration(deceleration);
	}

	/**
	 * @param movable
	 * @param deceleration
	 * @param velocityX
	 * @param velocityY
	 */
	public MomentumMoveAction(Movable movable, float deceleration, float velocityX, float velocityY) {
		this.movable = movable;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.setDeceleration(deceleration);
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
		this.calcMomentumX(delta);
		this.calcMomentumY(delta);
	}

	private void calcMomentumX(float delta) {
		if (this.velocityX == 0) {
			return;
		}
		this.getMovable().move(delta * this.velocityX, 0);
		final float decreaseAmountX = Math.abs(this.deceleration * delta * MathUtils.cos(MathUtils.atan2(this.velocityY, this.velocityX)));
		if (Math.abs(this.velocityX) < decreaseAmountX) {
			this.velocityX = 0;
			return;
		}
		final float signum = Math.signum(this.velocityX);
		this.velocityX += -signum * decreaseAmountX;
	}

	private void calcMomentumY(float delta) {
		if (this.velocityY == 0) {
			return;
		}
		this.getMovable().move(0, delta * this.velocityY);
		final float decreateAmountY = Math.abs(this.deceleration * delta * MathUtils.sin(MathUtils.atan2(this.velocityY, this.velocityX)));
		if (Math.abs(this.velocityY) < decreateAmountY) {
			this.velocityY = 0;
			return;
		}
		final float signum = Math.signum(this.velocityY);
		this.velocityY += -signum * decreateAmountY;
	}

	/**
	 * @return the deceleration
	 */
	public float getDeceleration() {
		return this.deceleration;
	}

	/**
	 * @return the decelerationX
	 */
	public float getDecelerationX() {
		return this.decelerationX;
	}

	/**
	 * @return the decelerationY
	 */
	public float getDecelerationY() {
		return this.decelerationY;
	}

	/**
	 * @param deceleration
	 *            the deceleration to set
	 */
	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
		final float theta = MathUtils.atan2(this.velocityY, this.velocityX);
		this.decelerationX = Math.abs(this.deceleration * MathUtils.cos(theta));
		this.decelerationY = Math.abs(this.deceleration * MathUtils.sin(theta));
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
		this.setDeceleration(this.deceleration);
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
		this.setDeceleration(this.deceleration);
	}

	/**
	 * @param velocityX
	 * @param velocityY
	 */
	public void setVelocity(float velocityX, float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.setDeceleration(this.deceleration);
	}

	/**
	 * @return the movable
	 */
	public Movable getMovable() {
		if (this.movable == null) {
			if (this.getActor() instanceof Movable) {
				return (Movable) this.getActor();
			}
		}
		return this.movable;
	}

	/**
	 * @param movable
	 *            the movable to set
	 */
	public void setMovable(Movable movable) {
		this.movable = movable;
	}

	@Override
	public boolean isPerformableState() {
		return true;
	}

	@Override
	public void restart() {
		super.restart();
		this.movable = null;
		this.deceleration = 0;
		this.decelerationX = 0;
		this.decelerationY = 0;
		this.velocityX = 0;
		this.velocityY = 0;
	}
}
