package org.nognog.freeSquare.ui.square2d.object;

import org.nognog.freeSquare.ui.square2d.action.StopTimeGenerator;
import org.nognog.freeSquare.ui.square2d.object.Square2dObjectType.LifeObjectType;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/11
 */
public class FryingLifeObject extends LifeObject {

	/**
	 * @param info
	 */
	public FryingLifeObject(LifeObjectType info) {
		this(info, defaultMoveSpeed);
	}

	/**
	 * @param info
	 * @param moveSpeed
	 */
	public FryingLifeObject(LifeObjectType info, float moveSpeed) {
		this(info, moveSpeed, defaultStopTimeGenerator);
	}

	/**
	 * @param info
	 * @param moveSpeed
	 * @param generator
	 */
	public FryingLifeObject(LifeObjectType info, float moveSpeed, StopTimeGenerator generator) {
		super(info, moveSpeed, generator);
	}

	@Override
	public Vector2 nextTargetPosition() {
		final float x = MathUtils.random(0, FryingLifeObject.this.square.getWidth());
		final float y = MathUtils.random(0, FryingLifeObject.this.square.getHeight());
		return new Vector2(x, y);
	}
}
