package org.nognog.freeSquare.ui.square2d.actions;

import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/29
 */
public interface TargetPositionGenerator {
	/**
	 * @return generated position (x, y)
	 */
	Vector2 nextTargetPosition();
}
