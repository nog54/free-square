package org.nognog.freeSquare.square2d;

import org.nognog.sence2d.action.KeepMovingToTargetPositionAction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/01/03
 */
public abstract class FreeRunningObject extends LifeObject {

	protected static final float defaultMoveSpeed = 32;

	protected static final StopTimeGenerator defaultGenerator = new StopTimeGenerator() {
		@Override
		public float generateNextStopTime() {
			return MathUtils.random(0, 1f);
		}
	};

	private KeepMovingToTargetPositionAction freeRunAction = null;

	private boolean isEnableFreeRun;
	private float moveSpeed; // [logicalWidth / sec]

	private float stopTime;
	private StopTimeGenerator stopTimeGenerator;
	private float stoppingTime;

	protected FreeRunningObject(Texture texture, float width) {
		this(texture, width, defaultMoveSpeed);
	}

	protected FreeRunningObject(Texture texture, float width, float moveSpeed) {
		this(texture, width, moveSpeed, defaultGenerator);
	}

	protected FreeRunningObject(Texture texture, float width, float moveSpeed, StopTimeGenerator generator) {
		super(texture, width);
		this.isEnableFreeRun = true;
		this.moveSpeed = moveSpeed;
		this.stopTimeGenerator = generator;
		this.stopTime = this.stopTimeGenerator.generateNextStopTime();
		this.stoppingTime = 0;

		this.addListener(new ActorGestureListener() {
			FreeRunningObject target = FreeRunningObject.this;
			boolean isLongTapped;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.target.setEnableFreeRun(false);
				this.isLongTapped = false;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!this.isLongTapped) {
					this.target.setEnableFreeRun(true);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				this.isLongTapped = true;
				return true;
			}

		});
	}

	@Override
	protected long independentAction(float delta, long previousInterval, long defaultInterval) {
		super.independentAction(delta, previousInterval, defaultInterval);
		if (!this.isEnableFreeRun) {
			return defaultInterval;
		}

		if (this.freeRunActionIsAlreadyEnded() || this.freeRunAction == null) {
			this.stoppingTime += delta;
			if (this.stoppingTime > this.stopTime) {
				this.stoppingTime = 0;
				this.stopTime = this.stopTimeGenerator.generateNextStopTime();
				final Vector2 dest = this.generateNextTargetPosition();
				this.freeRunAction = new KeepMovingToTargetPositionAction(dest.x, dest.y, this.moveSpeed);
				this.addAction(this.freeRunAction);
			}
			return defaultInterval;
		}

		Gdx.graphics.requestRendering();
		this.getSquare().requestDrawOrderUpdate();
		return defaultInterval;
	}

	private boolean freeRunActionIsAlreadyEnded() {
		return !this.getActions().contains(this.freeRunAction, true);
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
		this.stopTimeGenerator = generator;
	}

	/**
	 * @param enable
	 * 
	 */
	public synchronized void setEnableFreeRun(boolean enable) {
		if (this.freeRunAction != null) {
			if (!enable && this.getActions().contains(this.freeRunAction, true)) {
				this.getActions().removeValue(this.freeRunAction, true);
			}
			if (enable && !this.freeRunAction.isFinished()) {
				this.getActions().add(this.freeRunAction);
			}
		}
		this.isEnableFreeRun = enable;
	}

	/**
	 * @return true if free run is enabled.
	 */
	public boolean isEnableFreeRun() {
		return this.isEnableFreeRun;
	}

	protected Vector2 generateNextTargetPosition() {
		final float x = MathUtils.random(0, this.square.getWidth());
		final float y = MathUtils.random(0, this.square.getHeight());
		return new Vector2(x, y);
	}

	/**
	 * @author goshi 2015/01/03
	 */
	public static interface StopTimeGenerator {
		/**
		 * @return generated stop time
		 */
		float generateNextStopTime();
	}

}
