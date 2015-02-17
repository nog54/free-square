package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;

import com.badlogic.gdx.math.Interpolation;
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
		this.addAction(Square2dActions.excludeObjectOtherAction(moveToNearestSquareVertexAction));
	}

	@Override
	public Vector2 nextTargetPosition() {
		return Square2dUtils.getRandomPointOn(this.square);
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
}
