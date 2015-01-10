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

	private static final float defaultMoveSpeed = 32;

	private static final StopTimeGenerator defaultGenerator = new StopTimeGenerator() {
		@Override
		public float generateStopTime() {
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
		this.stopTime = this.stopTimeGenerator.generateStopTime();
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
	protected long independentAction(float delta, long previousInterval, long minInterval) {
		super.independentAction(delta, previousInterval, minInterval);
		if (!this.isEnableFreeRun) {
			return minInterval;
		}

		if (this.freeRunAction == null) {
			this.stoppingTime += delta;
			if (this.stoppingTime > this.stopTime) {
				this.stoppingTime = 0;
				this.stopTime = this.stopTimeGenerator.generateStopTime();
				final Vector2 dest = this.generateNextTargetPosition();
				this.freeRunAction = new KeepMovingToTargetPositionAction(dest.x - this.getOriginX(), dest.y, this.moveSpeed);
				this.freeRunAction.setActor(this);
				System.out.println(dest);
			}
			return minInterval;
		}
		boolean isDoneFreeAction = this.freeRunAction.act(delta);
		if (isDoneFreeAction) {
			this.freeRunAction = null;
		}
		Gdx.graphics.requestRendering();
		this.getSquare().requestDrawOrderUpdate();
		return minInterval;
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
	public void setEnableFreeRun(boolean enable) {
		this.isEnableFreeRun = enable;
		if (enable == false) {
			if (this.getActions().contains(this.freeRunAction, true)) {
				this.removeAction(this.freeRunAction);
			}
		}

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
		float generateStopTime();
	}

}
