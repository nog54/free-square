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

package org.nognog.freeSquare.square2d.object.types.life;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.status.Status;
import org.nognog.freeSquare.model.life.status.influence.StatusInfluence;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.action.Square2dActionUtlls;
import org.nognog.freeSquare.square2d.action.object.EatAction;
import org.nognog.freeSquare.square2d.action.object.FreeRunningAction;
import org.nognog.freeSquare.square2d.action.object.StopTimeGenerator;
import org.nognog.freeSquare.square2d.action.object.TargetPositionGenerator;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.ChangeStatusEvent;
import org.nognog.freeSquare.square2d.event.CollectObjectRequestEvent;
import org.nognog.freeSquare.square2d.event.RenameRequestEvent;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.MovableSquare2dObject;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/31
 */
public abstract class LifeObject extends MovableSquare2dObject implements TargetPositionGenerator {

	private static final Texture frameTexture = new Texture(Gdx.files.internal(Resources.frame1Path));
	protected static final StopTimeGenerator defaultStopTimeGenerator = new StopTimeGenerator() {
		@Override
		public float nextStopTime() {
			return MathUtils.random(0, 1f);
		}
	};

	private Life life;

	protected FreeRunningAction freeRunningAction;

	private StopTimeGenerator stopTime;

	/**
	 * @param type
	 * @param life
	 */
	public LifeObject(LifeObjectType type, Life life) {
		super(type);
		this.life = life;
		this.stopTime = defaultStopTimeGenerator;
		this.setOriginY(0);
		this.getIcon().addAction(LifeObject.createUpDownAction());
		final Image frame = new Image(frameTexture);
		frame.setWidth(this.getIcon().getWidth());
		frame.setHeight(this.getIcon().getHeight());
		this.getIcon().addActor(frame);
		this.addMainAction(Square2dActionUtlls.foreverTryToEat(this.getEatAmountPerSecond(), EatAction.UNTIL_RUN_OUT_EAT_OBJECT));
		this.freeRunningAction = Square2dActionUtlls.freeRunning(this.stopTime, this, LifeObject.toMoveSpeed(this));
		this.addMainAction(this.freeRunningAction);
		this.addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (this.isTripleTapped(count)) {
					LifeObject.this.getSquare().notifyEventListeners(new CollectObjectRequestEvent(LifeObject.this));
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				LifeObject.this.getSquare().notifyEventListeners(new RenameRequestEvent(LifeObject.this.getLife()));
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
		Action foreverRotate = Square2dActionUtlls.foreverRotate(degree, cycleTime, Interpolation.sine);
		final float upDownAmount = 5;
		Action foreverUpDown = Square2dActionUtlls.foreverUpdown(upDownAmount, cycleTime / 2, Interpolation.pow5);
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
		if (this.getSquare() == null || eatObject == null || eatObject.getAmount() == 0) {
			return 0;
		}
		final int actuallyEatAmount = eatObject.eatenBy(this, amount, eatDirection);
		return actuallyEatAmount;
	}

	/**
	 * @return the life
	 */
	public Life getLife() {
		return this.life;
	}

	/**
	 * @return eat amount / second
	 */
	public int getEatAmountPerSecond() {
		return ((LifeObjectType) this.getType()).getEatAmountPerSec();
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

	@Override
	public void setSquare(Square2d square) {
		super.setSquare(square);
	}

	/**
	 * @param influence
	 */
	public void applyStatusInfluence(StatusInfluence<?> influence) {
		influence.applyTo(this.life.getStatus());
		this.handleEvent(new ChangeStatusEvent(this, influence));
	}

	/**
	 * @return the nearest eatable object
	 */
	public abstract EatableObject getEasyReachableNearestEatableLandingObject();

	@Override
	public void handleEvent(SquareEvent event) {
		super.handleEvent(event);
		if (event instanceof UpdateSquareObjectEvent && ((UpdateSquareObjectEvent) event).getUpdatedObject() == this) {
			this.freeRunningAction.resetTargetPosition();
		}
		if (event instanceof ChangeStatusEvent && ((ChangeStatusEvent) event).getChangedObject() == this) {
			this.freeRunningAction.setMoveSpeed(LifeObject.toMoveSpeed(this));
			final double tiredness = this.getLife().getStatus().getTiredness();
			if (tiredness == Status.StatusRange.TIREDNESS.getMax()) {
				this.addMainAction(Square2dActionUtlls.sleepUntilCompleteRecovery());
			}
		}
		if (event instanceof AddObjectEvent) {
			Action up = Actions.moveBy(0, 30, 0.25f, Interpolation.pow3);
			Action down = Actions.moveBy(0, -30, 0.25f, Interpolation.pow3);
			Action hop = Actions.sequence(up, down);
			this.getIcon().addAction(hop);
		}
	}

	/**
	 * @param life
	 * @return lifeObject
	 */
	public static LifeObject create(Life life) {
		LifeObject newInstance = LifeObjectTypeManager.getInstance().getBindingLifeObjectType(life).create();
		newInstance.life = life;
		return newInstance;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof LifeObject) {
			return this.equals((LifeObject) object);
		}
		return super.equals(object);
	}

	/**
	 * @param object
	 * @return true if object has same position, type, and life.
	 */
	public boolean equals(LifeObject object) {
		final boolean isSamePosition = this.getX() == object.getX() && this.getY() == object.getY();
		return isSamePosition && this.getType().equals(object.getType()) && this.getLife().equals(object.getLife());
	}

}
