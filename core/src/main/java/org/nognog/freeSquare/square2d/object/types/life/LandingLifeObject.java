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

package org.nognog.freeSquare.square2d.object.types.life;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActionUtlls;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.LandObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/01/11
 */
public class LandingLifeObject extends LifeObject implements LandObject {

	/**
	 * @param life
	 */
	public LandingLifeObject(Life life) {
		this(LifeObjectTypeManager.getInstance().getBindingLifeObjectType(life), life);
	}

	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public LandingLifeObject(LifeObjectType type) {
		this(type, new Life(type.getFamily()));
	}

	/**
	 * @param type
	 * @param life
	 */
	private LandingLifeObject(LifeObjectType type, Life life) {
		super(type, life);
		this.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				LandingLifeObject.this.handleEvent(new UpdateSquareObjectEvent(LandingLifeObject.this));
			}
		});
		this.addMainAction(Square2dActionUtlls.keepLandingOnSquare());
	}

	@Override
	public Vector2 nextTargetPosition() {
		if (this.getSquare() == null) {
			return null;
		}
		final float thisX = this.getX();
		final float thisY = this.getY();
		final float theta = MathUtils.random(0, 2 * (float) Math.PI);
		if (this.isNotMovableTo(theta, thisX, thisY)) {
			return new Vector2(thisX, thisY);
		}
		int maxMoveDistance = 1024;
		double maxMoveX = calcSmallerAbsXThanTrueValueX(maxMoveDistance, theta);
		double maxMoveY = calcSmallerAbsYThanTrueValueY(maxMoveDistance, theta);
		float maxTargetPositionX = addWithRoundToSmallerAbs(thisX, maxMoveX);
		float maxTargetPositionY = addWithRoundToSmallerAbs(thisY, maxMoveY);
		final Vertex[] vertices = this.getSquare().getVertices();
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			Vector2 intersection = new Vector2();
			if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, thisX, thisY, maxTargetPositionX, maxTargetPositionY, intersection)) {
				maxMoveX = addWithRoundToSmallerAbs(intersection.x, -thisX);
				maxMoveY = addWithRoundToSmallerAbs(intersection.y, -thisY);
				maxTargetPositionX = addWithRoundToSmallerAbs(thisX, maxMoveX);
				maxTargetPositionY = addWithRoundToSmallerAbs(thisY, maxMoveY);
				maxMoveDistance = (int) Math.sqrt(maxMoveX * maxMoveX + maxMoveY * maxMoveY);
				if (maxMoveDistance == 0) {
					break;
				}
			}
		}
		final int moveDistance = (maxMoveDistance <= 1) ? 1 : MathUtils.random(maxMoveDistance / 2, maxMoveDistance);
		final double moveX = calcSmallerAbsXThanTrueValueX(moveDistance, theta);
		final double moveY = calcSmallerAbsYThanTrueValueY(moveDistance, theta);
		final float targetPositionX = addWithRoundToSmallerAbs(thisX, moveX);
		final float targetPositionY = addWithRoundToSmallerAbs(thisY, moveY);
		if (!this.getSquare().contains(targetPositionX, targetPositionY)) {
			return new Vector2(thisX, thisY);
		}
		return new Vector2(targetPositionX, targetPositionY);
	}

	private boolean isNotMovableTo(float theta, float thisX, float thisY) {
		final int tryMoveDistance = 1;
		final double moveX = calcSmallerAbsXThanTrueValueX(tryMoveDistance, theta);
		final double moveY = calcSmallerAbsYThanTrueValueY(tryMoveDistance, theta);
		final float targetPositionX = addWithRoundToSmallerAbs(thisX, moveX);
		final float targetPositionY = addWithRoundToSmallerAbs(thisY, moveY);
		if (this.getSquare().contains(targetPositionX, targetPositionY)) {
			return false;
		}
		return true;
	}

	private static double calcSmallerAbsXThanTrueValueX(double r, float theta) {
		double cosTheta = Math.nextAfter(MathUtils.cos(theta), 0);
		return Math.nextAfter(r * cosTheta, 0);
	}

	private static double calcSmallerAbsYThanTrueValueY(double r, float theta) {
		double sinTheta = Math.nextAfter(MathUtils.sin(theta), 0);
		return Math.nextAfter(r * sinTheta, 0);
	}

	/**
	 * not correct rounding
	 */
	private static float addWithRoundToSmallerAbs(double a, double b) {
		return Math.nextAfter((float) (a + b), 0);
	}

	@Override
	public boolean isValid() {
		return this.isLandingOnSquare();
	}

	@Override
	public EatableObject getEasyReachableNearestEatableLandingObject() {
		EatableObject result = null;
		float resultDistance = Float.MAX_VALUE;
		for (Square2dObject object : this.getSquare().getObjects()) {
			if (object instanceof EatableObject && object.isLandingOnSquare() && this.canGoStraightTo(object)) {
				final float objectDistance = this.getDistanceTo(object);
				if (objectDistance < resultDistance) {
					result = (EatableObject) object;
					resultDistance = objectDistance;
				}
			}
		}
		return result;
	}

	/**
	 * @param object
	 * @return true if this can go straight to object
	 */
	public boolean canGoStraightTo(Square2dObject object) {
		Vertex[] vertices = this.getSquare().getVertices();
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			final Vector2 intersectPoint = new Vector2();
			if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, this.getX(), this.getY(), object.getX(), object.getY(), intersectPoint)) {
				if (isSufficientNear(object.getX(), intersectPoint.x) && isSufficientNear(object.getY(), intersectPoint.y)) {
					return true;
				}
				return false;
			}
		}
		return true;
	}

	private static boolean isSufficientNear(final float a, final float b) {
		final float ulpA = Math.ulp(a);
		return (a - ulpA) <= b && b <= (a + ulpA);
	}
}
