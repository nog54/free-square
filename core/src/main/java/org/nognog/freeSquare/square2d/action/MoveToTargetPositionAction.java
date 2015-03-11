package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.object.LandingLifeObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/10
 */
public class MoveToTargetPositionAction extends Action {

	private float targetPositionX;
	private float targetPositionY;
	private float speed;
	private boolean isFinished = false;

	/**
	 * 
	 */
	public MoveToTargetPositionAction() {
		this(0, 0, 0);
	}

	/**
	 * @param targetPosition
	 * @param speed
	 */
	public MoveToTargetPositionAction(Vector2 targetPosition, float speed) {
		this(targetPosition.x, targetPosition.y, speed);
	}

	/**
	 * @param x
	 * @param y
	 * @param speed
	 *            [logical distance / s]
	 */
	public MoveToTargetPositionAction(float x, float y, float speed) {
		this.targetPositionX = x;
		this.targetPositionY = y;
		this.speed = speed;
	}

	@Override
	public boolean act(float delta) {
		if (this.isFinished) {
			return true;
		}
		if (this.actor instanceof LandingLifeObject) {
			Vertex[] vertices = ((LandingLifeObject) this.actor).getSquare().getVertices();
			for (int i = 0; i < vertices.length; i++) {
				final Vertex v1 = vertices[i];
				final Vertex v2 = vertices[(i + 1) % vertices.length];
				if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, this.actor.getX(), this.actor.getY(), this.targetPositionX, this.targetPositionY, null)) {
					this.isFinished = true;
					return true;
				}
			}
		}
		final float remainingDistanceX = this.targetPositionX - this.actor.getX();
		final float remainingDistanceY = this.targetPositionY - this.actor.getY();
		final float theta = MathUtils.atan2(this.targetPositionY - this.actor.getY(), this.targetPositionX - this.actor.getX());
		final float r = delta * this.speed;
		final float moveX = r * MathUtils.cos(theta);
		if (Math.abs(remainingDistanceX) < Math.abs(moveX)) {
			this.actor.setPosition(this.targetPositionX, this.targetPositionY);
			this.isFinished = true;
			return true;
		}
		final float moveY = r * MathUtils.sin(theta);
		if (Math.abs(remainingDistanceY) < Math.abs(moveY)) {
			this.actor.setPosition(this.targetPositionX, this.targetPositionY);
			this.isFinished = true;
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
		super.reset();
		this.isFinished = false;
	}

	@Override
	public void restart() {
		super.restart();
		this.isFinished = false;
	}

}
