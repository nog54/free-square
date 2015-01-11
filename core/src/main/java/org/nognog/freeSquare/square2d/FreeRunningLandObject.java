package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.Square2D.Vertex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/01/11
 */
public class FreeRunningLandObject extends FreeRunningObject implements LandObject {

	/**
	 * @param texture
	 * @param width
	 */
	public FreeRunningLandObject(Texture texture, float width) {
		this(texture, width, defaultMoveSpeed);
	}

	/**
	 * @param texture
	 * @param width
	 * @param moveSpeed
	 */
	public FreeRunningLandObject(Texture texture, float width, float moveSpeed) {
		this(texture, width, moveSpeed, defaultGenerator);
	}

	/**
	 * @param texture
	 * @param width
	 * @param moveSpeed
	 * @param generator
	 */
	public FreeRunningLandObject(Texture texture, float width, float moveSpeed, StopTimeGenerator generator) {
		super(texture, width, moveSpeed, generator);
		this.addListener(new ActorGestureListener() {
			FreeRunningObject target = FreeRunningLandObject.this;

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!this.target.isLandingOnSquare()) {
					Vertex nearestSquareVertex = this.findNearestSquareVertex();

					this.target.addAction(Actions.moveTo(nearestSquareVertex.x, nearestSquareVertex.y, 0.5f, Interpolation.pow2));
				}
			}

			private Vertex findNearestSquareVertex() {
				final float r1 = this.target.square.vertex1.calculateR(this.target.getX(), this.target.getY());
				final float r2 = this.target.square.vertex2.calculateR(this.target.getX(), this.target.getY());
				final float r3 = this.target.square.vertex3.calculateR(this.target.getX(), this.target.getY());
				final float r4 = this.target.square.vertex4.calculateR(this.target.getX(), this.target.getY());
				System.out.println(r1);
				System.out.println(r2);
				System.out.println(r3);
				System.out.println(r4);
				final float minR = Math.min(Math.min(Math.min(r1, r2), r3), r4);
				if (minR == r1) {
					return this.target.square.vertex1;
				}
				if (minR == r2) {
					return this.target.square.vertex2;
				}
				if (minR == r3) {
					return this.target.square.vertex3;
				}
				return this.target.square.vertex4;
			}

		});
	}

	@Override
	protected Vector2 generateNextTargetPosition() {
		return Square2DUtils.getRandomPointOn(this.square);
	}

}
