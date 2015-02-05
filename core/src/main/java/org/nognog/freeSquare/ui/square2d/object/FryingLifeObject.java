package org.nognog.freeSquare.ui.square2d.object;

import org.nognog.freeSquare.ui.square2d.object.Square2dObjectType.LifeObjectType;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/11
 */
public class FryingLifeObject extends LifeObject {


	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public FryingLifeObject(LifeObjectType type) {
		super(type);
	}

	@Override
	public Vector2 nextTargetPosition() {
		final float x = MathUtils.random(0, FryingLifeObject.this.square.getWidth());
		final float y = MathUtils.random(0, FryingLifeObject.this.square.getHeight());
		return new Vector2(x, y);
	}
}
