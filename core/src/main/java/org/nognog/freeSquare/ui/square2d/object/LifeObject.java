package org.nognog.freeSquare.ui.square2d.object;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.ui.square2d.Direction;
import org.nognog.freeSquare.ui.square2d.Square2d;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;
import org.nognog.freeSquare.ui.square2d.action.EatAction;
import org.nognog.freeSquare.ui.square2d.action.Square2dActions;
import org.nognog.freeSquare.ui.square2d.action.StopTimeGenerator;
import org.nognog.freeSquare.ui.square2d.action.TargetPositionGenerator;
import org.nognog.freeSquare.ui.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.ui.square2d.event.EatObjectEvent;
import org.nognog.freeSquare.ui.square2d.object.Square2dObjectType.LifeObjectType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/31
 */
public abstract class LifeObject extends Square2dObject implements TargetPositionGenerator {

	private static final int eatAmountPerOnce = 20;
	private static final float eatInterval = 1;
	
	private static final Texture frameTexture = new Texture(Gdx.files.internal(Resources.frame1Path));
	protected static final float defaultMoveSpeed = 100;
	protected static final StopTimeGenerator defaultStopTimeGenerator = new StopTimeGenerator() {
		@Override
		public float nextStopTime() {
			return MathUtils.random(0, 1f);
		}
	};
	

	private Image frame;
	private float moveSpeed; // [logicalWidth / sec]

	private boolean isEnabledUpDownRoutineAction;
	private Action upDownRoutineAction;

	protected Action freeRunningAction = null;
	private boolean isEnabledFreeRun;
	
	protected SequenceAction currectTryingMoveAndEatAction;
	protected EatAction currentEatAction;
	protected Action setFreeRunModeAction;

	private StopTimeGenerator stopTimeGenerator;

	/**
	 * @param type
	 */
	public LifeObject(LifeObjectType type) {
		this(type, defaultMoveSpeed, defaultStopTimeGenerator);
	}

	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public LifeObject(LifeObjectType type, float moveSpeed, StopTimeGenerator generator) {
		super(type);
		this.isEnabledFreeRun = true;
		this.moveSpeed = moveSpeed;
		this.stopTimeGenerator = generator;
		this.setOriginY(0);
		this.upDownRoutineAction = createUpDownAction();
		this.addAction(this.upDownRoutineAction);
		this.isEnabledUpDownRoutineAction = true;
		
		this.frame = new Image(frameTexture);
		this.frame.setWidth(this.getWidth());
		this.frame.setHeight(this.getHeight());
		this.addActor(this.frame);

		this.addListener(new ActorGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				LifeObject.this.moveBy(deltaX, deltaY);
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
		final int actuallyEatAmount = eatObject.eaten(amount, eatDirection);
		this.square.notifyObservers(new EatObjectEvent(this, eatObject, actuallyEatAmount));
		return actuallyEatAmount;
	}
	
	/**
	 * @param moveSpeed
	 */
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * @return move speed
	 */
	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * @param generator
	 */
	public void setStopTimeGenerator(StopTimeGenerator generator) {
		if (generator != null) {
			this.stopTimeGenerator = generator;
		}
	}

	/**
	 * @return using stopTimeGenerator
	 */
	public StopTimeGenerator getStopTimeGenerator() {
		return this.stopTimeGenerator;
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
			this.freeRunningAction = Square2dActions.freeRunning(this.stopTimeGenerator, this, this.moveSpeed);
			this.addAction(this.freeRunningAction);
		}
	}
	
	@Override
	public void act(float delta) {
		final EatableObject nearestEatableLandingObject = this.getNearestEatableLandingObject();
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

	private EatableObject getNearestEatableLandingObject() {
		EatableObject result = null;
		float resultDistance = 0;
		for (Square2dObject object : this.square.getObjects()) {
			if (object instanceof EatableObject && object.isLandingOnSquare()) {
				if (result == null) {
					result = (EatableObject) object;
					resultDistance = this.getDistanceTo(object);
				}
				final float objectDistance = this.getDistanceTo(object);
				if (objectDistance < resultDistance) {
					result = (EatableObject) object;
					resultDistance = objectDistance;
				}
			}
		}
		return result;
	}
	
	private void setEatAction(EatableObject eatObject) {
		final boolean wasEnabledFreeRun = this.isEnabledFreeRun();
		this.setEnableFreeRun(false);
		EatAction eatAction = Square2dActions.eat(eatObject, eatAmountPerOnce, EatAction.UNTIL_RUN_OUT, eatInterval, this.getMoveSpeed());
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

	@Override
	public void notify(Square2dEvent event) {
		super.notify(event);
		if (!(event instanceof AddObjectEvent)) {
			return;
		}
		Action up = Actions.moveBy(0, 30, 0.25f, Interpolation.pow3);
		Action down = Actions.moveBy(0, -30, 0.25f, Interpolation.pow3);
		Action hop = Actions.sequence(up, down);
		this.addAction(hop);
	}
	
}
