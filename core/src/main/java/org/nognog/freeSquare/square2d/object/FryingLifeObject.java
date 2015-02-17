package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/11
 */
public class FryingLifeObject extends LifeObject {

	private FryingLifeObject() {
		super();
	}

	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public FryingLifeObject(LifeObjectType type) {
		this();
		this.setupType(type);
		this.setLife(new Life(type.getFamily()));
	}

	@Override
	public Vector2 nextTargetPosition() {
		final float x = MathUtils.random(0, this.square.getWidth());
		final float y = MathUtils.random(0, this.square.getHeight());
		return new Vector2(x, y);
	}
}
