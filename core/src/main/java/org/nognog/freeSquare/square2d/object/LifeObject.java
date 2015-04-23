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

package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.action.EatAction;
import org.nognog.freeSquare.square2d.action.FreeRunningAction;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.action.StopTimeGenerator;
import org.nognog.freeSquare.square2d.action.TargetPositionGenerator;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.CollectObjectRequestEvent;
import org.nognog.freeSquare.square2d.event.EatObjectEvent;
import org.nognog.freeSquare.square2d.event.RenameRequestEvent;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2014/12/31
 */
public abstract class LifeObject extends Square2dObject implements TargetPositionGenerator {

	private static final Texture frameTexture = new Texture(Gdx.files.internal(Resources.frame1Path));
	protected static final StopTimeGenerator defaultStopTimeGenerator = new StopTimeGenerator() {
		@Override
		public float nextStopTime() {
			return MathUtils.random(0, 1f);
		}
	};

	private Life life;

	private Action upDownRoutineAction;
	private boolean isEnabledUpDownRoutineAction;

	protected FreeRunningAction freeRunningAction = null;
	private boolean isEnabledFreeRun;

	protected SequenceAction currectTryingMoveAndEatAction;
	protected EatAction currentEatAction;
	protected Action setFreeRunModeAction;

	private StopTimeGenerator stopTime;

	protected LifeObject() {
		super();
	}

	/**
	 * @param life
	 */
	public LifeObject(Life life) {
		this(LifeObjectType.Manager.getBindingLifeObjectType(life), life);
	}

	/**
	 * @param type
	 */
	public LifeObject(LifeObjectType type) {
		this(type, new Life(type.getFamily()));
	}

	private LifeObject(LifeObjectType type, Life life) {
		this();
		this.setupType(type);
		this.life = life;
	}

	@Override
	protected void setupType(Square2dObjectType<?> type) {
		super.setupType(type);
		if (type instanceof LifeObjectType) {
			this.setupLifeType((LifeObjectType) type);
		}
	}

	private void setupLifeType(LifeObjectType type) {
		this.life = new Life(type.getFamily());
		this.isEnabledFreeRun = true;
		this.stopTime = defaultStopTimeGenerator;
		this.setOriginY(0);
		this.upDownRoutineAction = createUpDownAction();
		this.icon.addAction(this.upDownRoutineAction);
		this.isEnabledUpDownRoutineAction = true;
		final Image frame = new Image(frameTexture);
		frame.setWidth(this.icon.getWidth());
		frame.setHeight(this.icon.getHeight());
		this.icon.addActor(frame);

		this.addListener(new ActorGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				LifeObject.this.moveBy(deltaX, deltaY);
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (this.isTripleTapped(count)) {
					LifeObject.this.square.notifyObservers(new CollectObjectRequestEvent(LifeObject.this));
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				LifeObject.this.square.notifyObservers(new RenameRequestEvent(LifeObject.this.getLife()));
				return true;
			}

			private boolean isTripleTapped(int count) {
				return count == 3;
			}
		});
	}

	private static Action createUpDownAction() {
		final float degree = 5;
		final float cycleTime = 4;
		Action foreverRotate = Square2dActions.foreverRotate(degree, cycleTime, Interpolation.sine);
		final float upDownAmount = 5;
		Action foreverUpDown = Square2dActions.foreverUpdown(upDownAmount, cycleTime / 2, Interpolation.pow5);
		return Actions.parallel(foreverRotate, foreverUpDown);
	}

	/**
	 * @param eatObject
	 * @param quantity
	 * @return actually eat amount
	 */
	public int eat(EatableObject eatObject, int quantity) {
		return this.eat(eatObject, quantity, Direction.UP);
	}

	/**
	 * @param eatObject
	 * @param amount
	 * @param eatDirection
	 * @return actually eat amount
	 */
	public int eat(EatableObject eatObject, int amount, Direction eatDirection) {
		if (this.square == null || eatObject == null || eatObject.getAmount() == 0) {
			return 0;
		}
		final int actuallyEatAmount = eatObject.eatenBy(this, amount, eatDirection);
		this.square.notifyObservers(new EatObjectEvent(this, eatObject, actuallyEatAmount));
		return actuallyEatAmount;
	}

	/**
	 * @return the life
	 */
	public Life getLife() {
		return this.life;
	}

	protected void setLife(Life life) {
		this.life = life;
	}

	/**
	 * @return eat amount / second
	 */
	public int getEatAmountPerSecond() {
		return LifeObjectType.Manager.getBindingLifeObjectType(this.life).getEatAmountPerSec();
	}

	/**
	 * @param lifeObject
	 * @return calculated moveSpeed
	 */
	public static float toMoveSpeed(LifeObject lifeObject) {
		final float moveSpeed = (float) (lifeObject.getLife().getStatus().getAgility() / 4);
		return Math.max(50, moveSpeed);
	}

	/**
	 * @param generator
	 */
	public void setStopTimeGenerator(StopTimeGenerator generator) {
		if (generator != null) {
			this.stopTime = generator;
		}
	}

	/**
	 * @return using stopTimeGenerator
	 */
	public StopTimeGenerator getStopTimeGenerator() {
		return this.stopTime;
	}

	/**
	 * @return true if up-down routine is enable
	 */
	public boolean isEnabledUpDownRoutine() {
		return this.isEnabledUpDownRoutineAction;
	}

	/**
	 * @param enable
	 */
	public void setEnableUpDownRoutine(boolean enable) {
		if (this.isEnabledUpDownRoutineAction == enable) {
			return;
		}
		if (enable) {
			this.resumePausingAction(this.upDownRoutineAction);
		} else {
			this.pauseAction(this.upDownRoutineAction);
		}
		this.isEnabledUpDownRoutineAction = enable;
	}

	/**
	 * @param enable
	 * 
	 */
	public void setEnableFreeRun(boolean enable) {
		if (this.isEnabledFreeRun == enable) {
			return;
		}
		if (enable) {
			this.resumePausingAction(this.freeRunningAction);
		} else {
			this.pauseAction(this.freeRunningAction);
		}
		this.isEnabledFreeRun = enable;
	}

	/**
	 * @return true if free run is enabled.
	 */
	public boolean isEnabledFreeRun() {
		return this.isEnabledFreeRun;
	}

	@Override
	public void setSquare(Square2d square) {
		super.setSquare(square);
		if (this.freeRunningAction == null) {
			this.freeRunningAction = Square2dActions.freeRunning(this.stopTime, this, LifeObject.toMoveSpeed(this));
			this.addAction(this.freeRunningAction);
		}
	}

	@Override
	public void act(float delta) {
		final EatableObject nearestEatableLandingObject = this.getEasyReachableNearestEatableLandingObject();
		if (nearestEatableLandingObject != null) {
			if (this.isMovingToTargetObject() && (nearestEatableLandingObject != this.currentEatAction.getEatObject())) {
				this.changeEatTargetTo(nearestEatableLandingObject);
			} else if (!this.isMovingToTargetObject()) {
				this.setEatAction(nearestEatableLandingObject);
			}
		} else {
			if (this.isMovingToTargetObject()) {
				this.currentEatAction.requestForceFinish();
			}
		}
		super.act(delta);
	}

	abstract protected EatableObject getEasyReachableNearestEatableLandingObject();

	private void setEatAction(EatableObject eatObject) {
		final boolean wasEnabledFreeRun = this.isEnabledFreeRun();
		this.setEnableFreeRun(false);
		EatAction eatAction = Square2dActions.eat(eatObject, this.getEatAmountPerSecond(), EatAction.UNTIL_RUN_OUT);
		this.setFreeRunModeAction = new Action() {
			@Override
			public boolean act(float delta) {
				LifeObject.this.currectTryingMoveAndEatAction = null;
				LifeObject.this.currentEatAction = null;
				LifeObject.this.setFreeRunModeAction = null;
				LifeObject.this.setEnableFreeRun(wasEnabledFreeRun);
				return true;
			}
		};

		this.currectTryingMoveAndEatAction = Actions.sequence(eatAction, this.setFreeRunModeAction);
		this.currentEatAction = eatAction;
		this.addAction(this.currectTryingMoveAndEatAction);
	}

	private void changeEatTargetTo(EatableObject newEatObject) {
		this.currentEatAction.setEatObject(newEatObject);
	}

	private boolean isMovingToTargetObject() {
		return this.currectTryingMoveAndEatAction != null && this.currentEatAction != null && this.setFreeRunModeAction != null;
	}

	/**
	 * reset target position of free running.
	 */
	public void resetFreeRunningTargetPosition() {
		this.freeRunningAction.resetTargetPosition();
	}

	@Override
	public void notify(SquareEvent event) {
		super.notify(event);
		if (event instanceof EatObjectEvent && ((EatObjectEvent) event).getEater() == this) {
			this.freeRunningAction.setMoveSpeed(LifeObject.toMoveSpeed(this));
		}
		if (event instanceof AddObjectEvent) {
			Action up = Actions.moveBy(0, 30, 0.25f, Interpolation.pow3);
			Action down = Actions.moveBy(0, -30, 0.25f, Interpolation.pow3);
			Action hop = Actions.sequence(up, down);
			this.icon.addAction(hop);
		}
	}

	/**
	 * @param life
	 * @return lifeObject
	 */
	public static LifeObject create(Life life) {
		LifeObject newInstance = LifeObjectType.Manager.getBindingLifeObjectType(life).create();
		newInstance.setLife(life);
		return newInstance;
	}

	@Override
	public void write(Json json) {
		super.write(json);
		json.writeField(this, "life"); //$NON-NLS-1$
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		json.readField(this, "life", jsonData); //$NON-NLS-1$
	}

}
