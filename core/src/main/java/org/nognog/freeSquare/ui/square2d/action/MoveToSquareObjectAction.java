package org.nognog.freeSquare.ui.square2d.action;

import org.nognog.freeSquare.ui.square2d.Square2dObject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/28
 */
public class MoveToSquareObjectAction extends Action {
	private Square2dObject targetObject;
	private float speed;
	private boolean isFinished = false;

	/**
	 * 
	 */
	public MoveToSquareObjectAction() {
		
	}
	
	/**
	 * @param targetObject
	 * @param speed 
	 */
	public MoveToSquareObjectAction(Square2dObject targetObject, float speed) {
		this.targetObject = targetObject;
		this.speed = speed;
	}

	@Override
	public boolean act(float delta) {
		if (this.isFinished) {
			return true;
		}
		if (this.targetObject == null || this.targetObject.getSquare() == null) {
			this.isFinished = true;
			return true;
		}
		final float targetPositionX = this.targetObject.getX();
		final float targetPositionY = this.targetObject.getY();
		final float remainingDistanceX = targetPositionX - this.actor.getX();
		final float remainingDistanceY = targetPositionY - this.actor.getY();
		final float theta = MathUtils.atan2(targetPositionY - this.actor.getY(), targetPositionX - this.actor.getX());
		final float r = delta * this.speed;
		final float moveX = r * MathUtils.cos(theta);
		if (Math.abs(remainingDistanceX) < Math.abs(moveX)) {
			this.actor.setPosition(targetPositionX, targetPositionY);
			this.isFinished = true;
			return true;
		}
		final float moveY = r * MathUtils.sin(theta);
		if (Math.abs(remainingDistanceY) < Math.abs(moveY)) {
			this.actor.setPosition(targetPositionX, targetPositionY);
			this.isFinished = true;
			return true;
		}

		this.actor.moveBy(moveX, moveY);
		return false;
	}

	/**
	 * @return target object
	 */
	public Square2dObject getTargetObject() {
		return this.targetObject;
	}

	/**
	 * @param targetObject
	 */
	public void setTargetObject(Square2dObject targetObject) {
		this.targetObject = targetObject;
	}

	/**
	 * @return speed
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * @param speed
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return true if this action have completed.
	 */
	public boolean isFinished() {
		return this.isFinished;
	}

	@Override
	public void reset() {
		this.isFinished = false;
	}

}
