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
		if (this.actor instanceof LandingLifeObject && !((LandingLifeObject) this.actor).isLandingOnVertex()) {
			Vertex[] vertices = ((LandingLifeObject) this.actor).getSquare().getVertices();
			for (int i = 0; i < vertices.length; i++) {
				final Vertex v1 = vertices[i];
				final Vertex v2 = vertices[(i + 1) % vertices.length];
				if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, this.actor.getX(), this.actor.getY(), this.targetPositionX, this.targetPositionY, null)) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("move to (").append(this.getTargetPositionX()).append(", ").append(this.getTargetPositionY()).append(")").toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
