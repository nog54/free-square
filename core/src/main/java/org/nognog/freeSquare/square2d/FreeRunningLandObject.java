package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi
 * 2015/01/11
 */
public class FreeRunningLandObject extends FreeRunningObject implements LandObject {

	/**
	 * @param texture
	 * @param width
	 */
	public FreeRunningLandObject(Texture texture, float width) {
		super(texture, width);
	}

	/**
	 * @param texture
	 * @param width
	 * @param moveSpeed
	 */
	public FreeRunningLandObject(Texture texture, float width, float moveSpeed) {
		super(texture, width, moveSpeed);
	}

	
	/**
	 * @param texture
	 * @param width
	 * @param moveSpeed
	 * @param generator
	 */
	public FreeRunningLandObject(Texture texture, float width, float moveSpeed, StopTimeGenerator generator) {
		super(texture, width, moveSpeed, generator);
	}


	@Override
	protected Vector2 generateNextTargetPosition() {
		return Square2DUtils.getRandomPointOn(this.square);
	}

}
