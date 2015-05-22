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

import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.freeSquare.square2d.object.types.life.LandingLifeObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/02/01
 */
public class EatAction extends Action {

	/**
	 * keep eating while eatObject amount is not zero.
	 */
	public static final int UNTIL_RUN_OUT = -1;
	private EatableObject eatObject;
	private int eatAmount;
	private int eatCount = UNTIL_RUN_OUT;
	private float eatInterval;

	private float timeFromLastEat = 0;
	private int executedCount = 0;

	private boolean isRequestedForceFinish = false;

	/**
	 * 
	 */
	public EatAction() {
	}

	/**
	 * @param eatObject
	 * @param eatAmount
	 * @param eatInterval
	 */
	public EatAction(EatableObject eatObject, int eatAmount, float eatInterval) {
		this.eatObject = eatObject;
		this.eatAmount = eatAmount;
		this.eatInterval = eatInterval;
	}

	/**
	 * @return the eatObject
	 */
	public EatableObject getEatObject() {
		return this.eatObject;
	}

	/**
	 * @param eatObject
	 *            the eatObject to set
	 */
	public void setEatObject(EatableObject eatObject) {
		this.eatObject = eatObject;
	}

	/**
	 * @return the eatAmount
	 */
	public int getEatAmount() {
		return this.eatAmount;
	}

	/**
	 * @param eatAmount
	 *            the eatAmount to set
	 */
	public void setEatAmount(int eatAmount) {
		this.eatAmount = eatAmount;
	}

	/**
	 * @return the eatCount
	 */
	public int getEatCount() {
		return this.eatCount;
	}

	/**
	 * @param eatCount
	 *            the eatCount to set
	 */
	public void setEatCount(int eatCount) {
		this.eatCount = eatCount;
	}

	/**
	 * @return the eatInterval
	 */
	public float getEatInterval() {
		return this.eatInterval;
	}

	/**
	 * @param eatInterval
	 *            the eatInterval to set
	 */
	public void setEatInterval(float eatInterval) {
		this.eatInterval = eatInterval;
	}

	private LifeObject getEater() {
		return (LifeObject) this.actor;
	}

	@Override
	public boolean act(float delta) {
		if (this.isRequestedForceFinish) {
			return true;
		}

		LifeObject eater = this.getEater();
		if (this.eatCount != UNTIL_RUN_OUT && this.executedCount >= this.eatCount) {
			return true;
		}
		if (this.eatObject.getSquare() == null || eater.getSquare() != this.eatObject.getSquare()) {
			return true;
		}
		if (this.actor instanceof LandingLifeObject) {
			Vertex[] vertices = ((LandingLifeObject) this.actor).getSquare().getVertices();
			for (int i = 0; i < vertices.length; i++) {
				final Vertex v1 = vertices[i];
				final Vertex v2 = vertices[(i + 1) % vertices.length];
				if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, eater.getX(), eater.getY(), this.eatObject.getX(), this.eatObject.getY(), null)) {
					return true;
				}
			}
		}

		this.timeFromLastEat += delta;
		if (eater.getDistanceTo(this.eatObject) > (this.eatObject.getWidth() + eater.getWidth()) / 4) {
			this.moveTowardEatObject(delta);
			return false;
		}

		if (this.timeFromLastEat < this.eatInterval) {
			return false;
		}

		Direction direction = getDirection(eater, this.eatObject);
		eater.eat(this.eatObject, this.eatAmount, direction);
		this.timeFromLastEat = 0;
		this.executedCount++;
		if (this.eatObject.getAmount() == 0) {
			return true;
		}
		if (this.eatCount != UNTIL_RUN_OUT && this.executedCount >= this.eatCount) {
			return true;
		}
		return false;
	}

	private void moveTowardEatObject(float delta) {
		final float targetPositionX = this.eatObject.getX();
		final float targetPositionY = this.eatObject.getY();
		final float remainingDistanceX = targetPositionX - this.actor.getX();
		final float remainingDistanceY = targetPositionY - this.actor.getY();
		final float theta = MathUtils.atan2(targetPositionY - this.actor.getY(), targetPositionX - this.actor.getX());
		final float r = delta * LifeObject.toMoveSpeed(this.getEater());
		final float moveX = r * MathUtils.cos(theta);
		if (Math.abs(remainingDistanceX) < Math.abs(moveX)) {
			this.actor.setPosition(targetPositionX, targetPositionY);
			return;
		}
		final float moveY = r * MathUtils.sin(theta);
		if (Math.abs(remainingDistanceY) < Math.abs(moveY)) {
			this.actor.setPosition(targetPositionX, targetPositionY);
			return;
		}

		this.actor.moveBy(moveX, moveY);
	}

	private static Direction getDirection(Actor actor1, Actor actor2) {
		final Direction defaultDirection = Direction.UP;
		final float distanceX = actor1.getX() - actor2.getX();
		final float distanceY = actor1.getY() - actor2.getY();
		if (distanceX == 0) {
			if (distanceY == 0) {
				return defaultDirection;
			}
			if (distanceY > 0) {
				return Direction.UP;
			}
			return Direction.DOWN;
		}

		final float a = distanceY / distanceX;
		if (distanceX > 0) {
			if (a <= -1) {
				return Direction.DOWN;
			}
			if (a >= 1) {
				return Direction.UP;
			}
			return Direction.RIGHT;
		}

		if (a <= -1) {
			return Direction.UP;
		}
		if (a >= 1) {
			return Direction.DOWN;
		}
		return Direction.LEFT;

	}

	@Override
	public void setActor(Actor actor) {
		if (actor == null || actor instanceof LifeObject) {
			super.setActor(actor);
			return;
		}
		throw new RuntimeException("actor of EatAction must be LifeObject instance."); //$NON-NLS-1$
	}

	@Override
	public void restart() {
		this.isRequestedForceFinish = false;
		super.restart();
	}

	/**
	 * 
	 */
	public void requestForceFinish() {
		this.isRequestedForceFinish = true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("eat to ").append(this.eatObject).append(" action").toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
