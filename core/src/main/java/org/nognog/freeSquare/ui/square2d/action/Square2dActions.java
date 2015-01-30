package org.nognog.freeSquare.ui.square2d.action;

import org.nognog.freeSquare.ui.square2d.Square2dObject;

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
	 * @param targetObject
	 * @param speed
	 * @return action
	 */
	public static MoveToSquareObjectAction moveToSquareObject(Square2dObject targetObject, float speed){
		MoveToSquareObjectAction action = Actions.action(MoveToSquareObjectAction.class);
		action.setTargetObject(targetObject);
		action.setSpeed(speed);
		return action;
	}

	/**
	 * @param stopTimeGenerator
	 * @return action
	 */
	public static DelayNextStopTimeAction delayNextStopTime(StopTimeGenerator stopTimeGenerator) {
		DelayNextStopTimeAction action = Actions.action(DelayNextStopTimeAction.class);
		action.setStopTimeGenerator(stopTimeGenerator);
		action.restart();
		return action;
	}

	/**
	 * @param targetPositionGenerator
	 * @param speed
	 * @return action
	 */
	public static MoveToNextTargetPositionAction moveToNextTargetPosition(TargetPositionGenerator targetPositionGenerator, float speed) {
		MoveToNextTargetPositionAction action = Actions.action(MoveToNextTargetPositionAction.class);
		action.setTargetPositionGenerator(targetPositionGenerator);
		action.setSpeed(speed);
		action.restart();
		return action;
	}

	/**
	 * @param x
	 * @param y
	 * @param speed
	 * @return action
	 */
	public static MoveToTargetPositionAction moveToTargetPosition(float x, float y, float speed) {
		MoveToTargetPositionAction action = Actions.action(MoveToTargetPositionAction.class);
		action.setTargetPositionX(x);
		action.setTargetPositionY(y);
		action.setSpeed(speed);
		return action;
	}

	/**
	 * @param stopTimeGenerator
	 * @param targetPositionGenerator
	 * @param speed
	 * @return action
	 */
	public static FreeRunningAction freeRunning(StopTimeGenerator stopTimeGenerator, TargetPositionGenerator targetPositionGenerator, float speed) {
		FreeRunningAction action = Actions.action(FreeRunningAction.class);
		Action moveAction = moveToNextTargetPosition(targetPositionGenerator, speed);
		Action delayAction = delayNextStopTime(stopTimeGenerator);
		action.setMoveActionDelayActionSequence(Actions.sequence(moveAction, delayAction));
		return action;
	}
	
	/**
	 * @param mainAction
	 * @return action
	 */
	public static ExcludeObjectOtherActionAction excludeObjectOtherAction(Action mainAction){
		ExcludeObjectOtherActionAction action = Actions.action(ExcludeObjectOtherActionAction.class);
		action.setAction(mainAction);
		return action;
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