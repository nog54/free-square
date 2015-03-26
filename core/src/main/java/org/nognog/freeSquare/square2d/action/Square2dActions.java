package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.event.UpdateObjectEvent;
import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

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
	 * @param eatObject
	 * @param eatAmount
	 * @param moveSpeed 
	 * @return action
	 */
	public static EatAction eat(EatableObject eatObject, int eatAmount) {
		return eat(eatObject, eatAmount, 1);
	}

	/**
	 * @param eatObject
	 * @param eatAmount
	 * @param eatCount 
	 * @param eatInterval 
	 * @param moveSpeed 
	 * @return action
	 */
	public static EatAction eat(EatableObject eatObject, int eatAmount, int eatCount) {
		final EatAction action = Actions.action(EatAction.class);
		action.setEatObject(eatObject);
		action.setEatAmount(eatAmount);
		action.setEatCount(eatCount);
		action.setEatInterval(1);
		return action;
	}
	
	/**
	 * @param targetObject
	 * @param speed
	 * @return action
	 */
	public static MoveToSquareObjectAction moveToSquareObject(Square2dObject targetObject, float speed) {
		return moveToSquareObject(targetObject, speed, 0);
	}

	/**
	 * @param targetObject
	 * @param speed
	 * @param tolerance
	 * @return action
	 */
	public static MoveToSquareObjectAction moveToSquareObject(Square2dObject targetObject, float speed, float tolerance) {
		final MoveToSquareObjectAction action = Actions.action(MoveToSquareObjectAction.class);
		action.setTargetObject(targetObject);
		action.setSpeed(speed);
		action.setTolerance(tolerance);
		return action;
	}

	/**
	 * @param stopTimeGenerator
	 * @return action
	 */
	public static DelayNextStopTimeAction delayNextStopTime(StopTimeGenerator stopTimeGenerator) {
		final DelayNextStopTimeAction action = Actions.action(DelayNextStopTimeAction.class);
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
		final MoveToNextTargetPositionAction action = Actions.action(MoveToNextTargetPositionAction.class);
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
		final MoveToTargetPositionAction action = Actions.action(MoveToTargetPositionAction.class);
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
		final FreeRunningAction action = Actions.action(FreeRunningAction.class);
		Action moveAction = moveToNextTargetPosition(targetPositionGenerator, speed);
		Action delayAction = delayNextStopTime(stopTimeGenerator);
		action.setMoveActionDelayActionSequence(Actions.sequence(moveAction, delayAction));
		return action;
	}

	/**
	 * @param mainAction
	 * @return action
	 */
	public static ExcludeObjectOtherActionAction excludeObjectOtherAction(Action mainAction) {
		final ExcludeObjectOtherActionAction action = Actions.action(ExcludeObjectOtherActionAction.class);
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
		final Action firstRotateLeft = Actions.rotateBy(degree, cycleTime / 4, interpolation);
		final Action rotateRight = Actions.rotateBy(-degree * 2, cycleTime / 2, interpolation);
		final Action rotateLeft = Actions.rotateBy(degree * 2, cycleTime / 2, interpolation);
		final Action rotateSquence = Actions.sequence(rotateRight, rotateLeft);

		return Actions.sequence(firstRotateLeft, Actions.forever(rotateSquence));
	}

	/**
	 * @param upDownAmount
	 * @param cycleTime
	 * @param interpolation
	 * @return forever up down action
	 */
	public static Action foreverUpdown(float upDownAmount, float cycleTime, Interpolation interpolation) {
		final Action up = Actions.moveBy(0, upDownAmount, cycleTime / 2, interpolation);
		final Action down = Actions.moveBy(0, -upDownAmount, cycleTime / 2, interpolation);

		return Actions.forever(Actions.sequence(up, down));
	}

	/**
	 * @param eventListener
	 * @param event
	 * @return fire event action
	 */
	public static FireEventAction fireEventAction(Square2dObject eventListener, UpdateObjectEvent event) {
		final FireEventAction fireEventAction = Actions.action(FireEventAction.class);
		fireEventAction.setEventListener(eventListener);
		fireEventAction.setEvent(event);
		return fireEventAction;
	}

	/**
	 * @param freeSquare
	 * @param setSquare
	 * @param direction
	 * @return change square action
	 */
	public static Action changeSquare(FreeSquare freeSquare, Square2d setSquare, Direction direction) {
		final ChangeSquareAction changeSquareAction = Actions.action(ChangeSquareAction.class);
		changeSquareAction.setFreeSquare(freeSquare);
		changeSquareAction.setSquare(setSquare);
		changeSquareAction.setDirection(direction);
		return changeSquareAction;
	}

}
