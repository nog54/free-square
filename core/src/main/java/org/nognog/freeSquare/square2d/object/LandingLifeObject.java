package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.event.UpdateObjectEvent;
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
					LandingLifeObject.this.notify(new UpdateObjectEvent(LandingLifeObject.this));
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
		final Action notifyUpdateEventAction = Square2dActions.fireEventAction(this, new UpdateObjectEvent());
		this.addAction(Square2dActions.excludeObjectOtherAction(Actions.sequence(moveToNearestSquareVertexAction, notifyUpdateEventAction)));
	}

	@Override
	public Vector2 nextTargetPosition() {
		if (!this.isLandingOnSquare()) {
			return Square2dUtils.getRandomPointOn(this.square);
		}
		final float thisX = this.getX();
		final float thisY = this.getY();
		final float angle = MathUtils.random(0, 2 * (float) Math.PI);
		float maxMoveDistance = 1000;
		float maxMoveX = maxMoveDistance * MathUtils.cos(angle);
		float maxMoveY = maxMoveDistance * MathUtils.sin(angle);
		final Vertex[] vertices = this.square.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			Vector2 intersection = new Vector2();
			if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, thisX, thisY, thisX + maxMoveX, thisY + maxMoveY, intersection)) {
				maxMoveX = intersection.x - thisX;
				maxMoveY = intersection.y - thisY;
				maxMoveDistance = (float) Math.sqrt(maxMoveX * maxMoveX + maxMoveY * maxMoveY);
			}
		}
		final float moveDistance = MathUtils.random(0, maxMoveDistance);
		final float moveX = moveDistance * MathUtils.cos(angle);
		final float moveY = moveDistance * MathUtils.sin(angle);
		return new Vector2(thisX + moveX, thisY + moveY);
	}

	@Override
	public boolean isValid() {
		return this.isLandingOnSquare();
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
