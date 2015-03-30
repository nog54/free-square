package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2015/01/11
 */
public class LandingLifeObject extends LifeObject implements LandObject {

	private LandingLifeObject() {
		// used by json
		super();
		this.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!LandingLifeObject.this.isLandingOnSquare()) {
					LandingLifeObject.this.goToSquareNearestVertex();
				} else {
					LandingLifeObject.this.notify(new UpdateSquareObjectEvent(LandingLifeObject.this));
				}
			}
		});
	}

	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public LandingLifeObject(LifeObjectType type) {
		this();
		this.setupType(type);
		this.setLife(new Life(type.getFamily()));
	}

	protected void goToSquareNearestVertex() {
		final Vertex nearestSquareVertex = this.getNearestSquareVertex();
		final Action moveToNearestSquareVertexAction = Actions.moveTo(nearestSquareVertex.x, nearestSquareVertex.y, 0.5f, Interpolation.pow2);
		final Action notifyUpdateEventAction = Square2dActions.fireEventAction(this, new UpdateSquareObjectEvent(this));
		this.addAction(Square2dActions.excludeObjectOtherAction(Actions.sequence(moveToNearestSquareVertexAction, notifyUpdateEventAction)));
	}

	@Override
	public Vector2 nextTargetPosition() {
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
		final Vertex[] vertices = this.square.getVertices();
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
		if(!this.square.containsPosition(targetPositionX, targetPositionY)){
			System.out.println(maxMoveDistance);
			System.out.println(moveDistance);
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
		if (this.square.containsPosition(targetPositionX, targetPositionY)) {
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
	public void notify(SquareEvent event) {
		if (event instanceof UpdateSquareObjectEvent && ((UpdateSquareObjectEvent) event).getUpdatedObject() == this) {
			this.resetFreeRunningTargetPosition();
		}
		super.notify(event);
	}

	@Override
	public void write(Json json) {
		if (!this.isLandingOnSquare()) {
			Vector2 randomPointOnSquare = Square2dUtils.getRandomPointOn(this.square);
			this.setPosition(randomPointOnSquare.x, randomPointOnSquare.y);
		}
		super.write(json);
	}
	
	@Override
	protected EatableObject getEasyReachableNearestEatableLandingObject() {
		EatableObject result = null;
		float resultDistance = Float.MAX_VALUE;
		for (Square2dObject object : this.square.getObjects()) {
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

	private boolean canGoStraightTo(Square2dObject object) {
		Vertex[] vertices = this.getSquare().getVertices();
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, this.getX(), this.getY(), object.getX(), object.getY(), null)) {
				return false;
			}
		}
		return true;
	}
}
