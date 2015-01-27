package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.ui.square2d.Square2d.Vertex;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2015/01/11
 */
public class FreeRunningLandObject extends FreeRunningObject implements LandObject {

	private Action returnToSquareAction;

	/**
	 * @param info
	 */
	public FreeRunningLandObject(Square2dObjectType info) {
		this(info, defaultMoveSpeed);
	}

	/**
	 * @param info
	 * @param moveSpeed
	 */
	public FreeRunningLandObject(Square2dObjectType info, float moveSpeed) {
		this(info, moveSpeed, defaultStopTimeGenerator);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (!this.isBeingTouched && !this.isLandingOnSquare() && !this.isReturningToSquare()) {
			this.addReturnToSquareAction();
		}
	}

	private boolean isReturningToSquare() {
		if (this.returnToSquareAction == null) {
			return false;
		}
		return this.getActions().contains(this.returnToSquareAction, true);
	}

	private void addReturnToSquareAction() {
		Vertex nearestSquareVertex = this.getNearestSquareVertex();
		this.returnToSquareAction = Actions.moveTo(nearestSquareVertex.x, nearestSquareVertex.y, 0.5f, Interpolation.pow2);
		this.addAction(this.returnToSquareAction);
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

	/**
	 * @param info
	 * @param moveSpeed
	 * @param generator
	 */
	public FreeRunningLandObject(Square2dObjectType info, float moveSpeed, StopTimeGenerator generator) {
		super(info, moveSpeed, generator);
	}

	@Override
	protected Vector2 generateNextTargetPosition() {
		return Square2dUtils.getRandomPointOn(this.square);
	}

	@Override
	public boolean isValid() {
		return this.isLandingOnSquare();
	}
}
