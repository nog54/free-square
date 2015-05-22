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

import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.life.LandingLifeObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/28
 */
public class MoveToSquareObjectAction extends Action {
	private Square2dObject targetObject;
	private float speed;
	private float tolerance;
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
		this(targetObject, speed, targetObject.getWidth() / 2);
	}

	/**
	 * @param targetObject
	 * @param speed
	 * @param tolerance
	 */
	public MoveToSquareObjectAction(Square2dObject targetObject, float speed, float tolerance) {
		this.targetObject = targetObject;
		this.speed = speed;
		this.tolerance = tolerance;
	}

	@Override
	public boolean act(float delta) {
		if (this.isFinished) {
			return true;
		}
		if (this.targetObject == null || ((Square2dObject) this.actor).getSquare() != this.targetObject.getSquare()) {
			this.isFinished = true;
			return true;
		}
		if (this.actor instanceof LandingLifeObject) {
			Vertex[] vertices = ((LandingLifeObject) this.actor).getSquare().getVertices();
			for (int i = 0; i < vertices.length; i++) {
				final Vertex v1 = vertices[i];
				final Vertex v2 = vertices[(i + 1) % vertices.length];
				if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, this.actor.getX(), this.actor.getY(), this.targetObject.getX(), this.targetObject.getY(), null)) {
					this.isFinished = true;
					return true;
				}
			}

		}
		final float targetPositionX = this.targetObject.getX();
		final float targetPositionY = this.targetObject.getY();
		final float remainingDistanceX = targetPositionX - this.actor.getX();
		final float remainingDistanceY = targetPositionY - this.actor.getY();
		final float remainingR = (float) Math.sqrt(remainingDistanceX * remainingDistanceX + remainingDistanceY * remainingDistanceY);
		if (remainingR <= this.tolerance) {
			return true;
		}
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
	 * @return the tolerance
	 */
	public float getTolerance() {
		return this.tolerance;
	}

	/**
	 * @param tolerance
	 *            the tolerance to set
	 */
	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
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
