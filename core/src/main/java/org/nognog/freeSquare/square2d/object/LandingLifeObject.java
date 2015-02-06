package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.Square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.object.Square2dObjectType.LifeObjectType;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/01/11
 */
public class LandingLifeObject extends LifeObject implements LandObject {

	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public LandingLifeObject(LifeObjectType type) {
		super(type);
		this.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!LandingLifeObject.this.isLandingOnSquare()) {
					LandingLifeObject.this.goToSquareNearestVertex();
				}
			}
		});
	}

	protected void goToSquareNearestVertex() {
		final Vertex nearestSquareVertex = this.getNearestSquareVertex();
		final Action moveToNearestSquareVertexAction = Actions.moveTo(nearestSquareVertex.x, nearestSquareVertex.y, 0.5f, Interpolation.pow2);
		this.addAction(Square2dActions.excludeObjectOtherAction(moveToNearestSquareVertexAction));
	}

	private Vertex getNearestSquareVertex() {
		final float r1 = this.square.vertex1.calculateR(this.getX(), this.getY());
		final float r2 = this.square.vertex2.calculateR(this.getX(), this.getY());
		final float r3 = this.square.vertex3.calculateR(this.getX(), this.getY());
		final float r4 = this.square.vertex4.calculateR(this.getX(), this.getY());
		final float minR = Math.min(Math.min(Math.min(r1, r2), r3), r4);
		if (minR == r1) {
			return this.square.vertex1;
		}
		if (minR == r2) {
			return this.square.vertex2;
		}
		if (minR == r3) {
			return this.square.vertex3;
		}
		return this.square.vertex4;
	}

	@Override
	public Vector2 nextTargetPosition() {
		return Square2dUtils.getRandomPointOn(this.square);
	}

	@Override
	public boolean isValid() {
		return this.isLandingOnSquare();
	}
}
