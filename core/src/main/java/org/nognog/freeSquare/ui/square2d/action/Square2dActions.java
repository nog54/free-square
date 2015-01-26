package org.nognog.freeSquare.ui.square2d.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2014/12/23
 */
public class Square2dActions {
	private Square2dActions() {
	}

	/**
	 * @param degree
	 * @param cycleTime
	 * @param interpolation
	 * @return forever rotate action
	 */
	public static Action foreverRotate(float degree, float cycleTime, Interpolation interpolation) {
		Action firstRotateLeft = Actions.rotateBy(degree, cycleTime / 4, interpolation);
		Action rotateRight = Actions.rotateBy(-degree * 2, cycleTime / 2, interpolation);
		Action rotateLeft = Actions.rotateBy(degree * 2, cycleTime / 2, interpolation);
		Action rotateSquence = Actions.sequence(rotateRight, rotateLeft);

		return Actions.sequence(firstRotateLeft, Actions.forever(rotateSquence));
	}

	/**
	 * @param upDownAmount
	 * @param cycleTime
	 * @param interpolation
	 * @return forever up down action
	 */
	public static Action foreverUpdown(float upDownAmount, float cycleTime, Interpolation interpolation) {
		Action up = Actions.moveBy(0, upDownAmount, cycleTime / 2, interpolation);
		Action down = Actions.moveBy(0, -upDownAmount, cycleTime / 2, interpolation);

		return Actions.forever(Actions.sequence(up, down));
	}
	
}
