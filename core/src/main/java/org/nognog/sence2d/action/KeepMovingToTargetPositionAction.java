package org.nognog.sence2d.action;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/10
 */
public class KeepMovingToTargetPositionAction extends Action {

	private float targetPositionX;
	private float targetPositionY;
	private float speed;

	/**
	 * @param x
	 * @param y
	 * @param speed
	 *            [logical distance / s]
	 */
	public KeepMovingToTargetPositionAction(float x, float y, float speed) {
		this.targetPositionX = x;
		this.targetPositionY = y;
		this.speed = speed;
	}

	@Override
	public boolean act(float delta) {
		final float remainingDistanceX = this.targetPositionX - this.actor.getX();
		final float remainingDistanceY = this.targetPositionY - this.actor.getY();
		final float theta = MathUtils.atan2(this.targetPositionY - this.actor.getY(), this.targetPositionX - this.actor.getX());
		final float r = delta * this.speed;
		final float moveX = r * MathUtils.cos(theta);
		if (Math.abs(remainingDistanceX) < Math.abs(moveX)) {
			this.actor.setPosition(this.targetPositionX, this.targetPositionY);
			return true;
		}
		final float moveY = r * MathUtils.sin(theta);
		if (Math.abs(remainingDistanceY) < Math.abs(moveY)) {
			this.actor.setPosition(this.targetPositionX, this.targetPositionY);
			return true;
		}

		this.actor.moveBy(moveX, moveY);
		return false;
	}

	/**
	 * @return target position x
	 */
	public float getTargetPositionY() {
		return this.targetPositionY;
	}

	/**
	 * @param targetPositionY
	 */
	public void setTargetPositionY(float targetPositionY) {
		this.targetPositionY = targetPositionY;
	}

	/**
	 * @return target position x
	 */
	public float getTargetPositionX() {
		return this.targetPositionX;
	}

	/**
	 * @param targetPositionX
	 */
	public void setTargetPositionX(float targetPositionX) {
		this.targetPositionX = targetPositionX;
	}

}
