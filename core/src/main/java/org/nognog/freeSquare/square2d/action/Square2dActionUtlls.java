/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.activity.main.MainActivity;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction;
import org.nognog.freeSquare.square2d.action.object.ConstantlyTireAction;
import org.nognog.freeSquare.square2d.action.object.DelayNextStopTimeAction;
import org.nognog.freeSquare.square2d.action.object.DoNothingAction;
import org.nognog.freeSquare.square2d.action.object.EatAction;
import org.nognog.freeSquare.square2d.action.object.FireEventAction;
import org.nognog.freeSquare.square2d.action.object.ForeverTryToEatAction;
import org.nognog.freeSquare.square2d.action.object.FreeRunningAction;
import org.nognog.freeSquare.square2d.action.object.KeepLandingOnSquareAction;
import org.nognog.freeSquare.square2d.action.object.MomentumMoveAction;
import org.nognog.freeSquare.square2d.action.object.MoveToNextTargetPositionAction;
import org.nognog.freeSquare.square2d.action.object.MoveToSquareObjectAction;
import org.nognog.freeSquare.square2d.action.object.MoveToTargetPositionAction;
import org.nognog.freeSquare.square2d.action.object.SleepAction;
import org.nognog.freeSquare.square2d.action.object.SleepPolicy;
import org.nognog.freeSquare.square2d.action.object.StopTimeGenerator;
import org.nognog.freeSquare.square2d.action.object.TargetPositionGenerator;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.gdx.util.Movable;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2014/12/23
 */
public class Square2dActionUtlls {
	private Square2dActionUtlls() {
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
	public static FireEventAction fireEventAction(Square2dObject eventListener, UpdateSquareObjectEvent event) {
		final FireEventAction fireEventAction = Actions.action(FireEventAction.class);
		fireEventAction.setEventListener(eventListener);
		fireEventAction.setEvent(event);
		return fireEventAction;
	}

	/**
	 * @param activity
	 * @param setSquare
	 * @param direction
	 * @return change square action
	 */
	public static ChangeSquareAction changeSquare(MainActivity activity, Square2d setSquare, Direction direction) {
		final ChangeSquareAction changeSquareAction = Actions.action(ChangeSquareAction.class);
		changeSquareAction.setActivity(activity);
		changeSquareAction.setSquare(setSquare);
		changeSquareAction.setDirection(direction);
		return changeSquareAction;
	}
	
	/**
	 * @param deceleration
	 * @param velocityX
	 * @param velocityY
	 * @return momentum move action
	 */
	public static MomentumMoveAction momentumMove(float deceleration, float velocityX, float velocityY) {
		final MomentumMoveAction momentumMoveAction = Actions.action(MomentumMoveAction.class);
		momentumMoveAction.setDeceleration(deceleration);
		momentumMoveAction.setVelocity(velocityX, velocityY);
		return momentumMoveAction;
	}


	/**
	 * @param target
	 * @param deceleration
	 * @param velocityX
	 * @param velocityY
	 * @return momentum move action
	 */
	public static MomentumMoveAction momentumMove(Movable target, float deceleration, float velocityX, float velocityY) {
		final MomentumMoveAction momentumMoveAction = Actions.action(MomentumMoveAction.class);
		momentumMoveAction.setDeceleration(deceleration);
		momentumMoveAction.setVelocity(velocityX, velocityY);
		momentumMoveAction.setMovable(target);
		return momentumMoveAction;
	}

	/**
	 * @param eatObject
	 * @param eatAmount
	 * @param eatCount
	 * @param eatInterval
	 * @param moveSpeed
	 * @return action
	 */
	public static EatAction foreverTryToEat(int eatAmount, int eatCount) {
		final ForeverTryToEatAction action = Actions.action(ForeverTryToEatAction.class);
		action.setEatAmount(eatAmount);
		action.setEatCount(eatCount);
		action.setEatInterval(1);
		return action;
	}

	/**
	 * @return action
	 */
	public static KeepLandingOnSquareAction keepLandingOnSquare() {
		final KeepLandingOnSquareAction action = Actions.action(KeepLandingOnSquareAction.class);
		return action;
	}

	/**
	 * @param sleepTime
	 * @return action
	 */
	public static SleepAction sleep(float sleepTime) {
		final SleepAction action = Actions.action(SleepAction.class);
		action.setPolicy(SleepPolicy.FIX_TIME);
		action.setDesiredSleepTime(sleepTime);
		return action;
	}

	/**
	 * @return action
	 */
	public static SleepAction sleepUntilCompleteRecovery() {
		final SleepAction action = Actions.action(SleepAction.class);
		action.setPolicy(SleepPolicy.COMPLETE_RECOVERY);
		return action;
	}

	/**
	 * @return action
	 */
	public static DoNothingAction doNothing() {
		final DoNothingAction action = Actions.action(DoNothingAction.class);
		return action;
	}

	/**
	 * @param tirednessIncreaseAmountPerMinute 
	 * @return action
	 */
	public static ConstantlyTireAction constantlyTired(float tirednessIncreaseAmountPerMinute) {
		final ConstantlyTireAction action = Actions.action(ConstantlyTireAction.class);
		action.setTirednessIncreaseAmountPerMinute(tirednessIncreaseAmountPerMinute);
		return action;
	}
}
