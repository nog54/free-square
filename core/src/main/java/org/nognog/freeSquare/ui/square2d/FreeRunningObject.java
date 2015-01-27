package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.ui.square2d.action.ActionFactory;
import org.nognog.freeSquare.ui.square2d.action.AddAction2;
import org.nognog.freeSquare.ui.square2d.action.KeepMovingToTargetPositionAction;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2015/01/03
 */
public class FreeRunningObject extends LifeObject {

	protected static final float defaultMoveSpeed = 32;

	protected static final StopTimeGenerator defaultStopTimeGenerator = new StopTimeGenerator() {
		@Override
		public float generateNextStopTime() {
			return MathUtils.random(0, 1f);
		}
	};

	protected Action foreverFreeRunAction = null;
	private boolean isEnabledFreeRun;

	private float moveSpeed; // [logicalWidth / sec]

	private StopTimeGenerator stopTimeGenerator;

	/**
	 * @param info
	 */
	public FreeRunningObject(Square2dObjectType info) {
		this(info, defaultMoveSpeed);
	}

	/**
	 * @param info
	 * @param moveSpeed
	 */
	public FreeRunningObject(Square2dObjectType info, float moveSpeed) {
		this(info, moveSpeed, defaultStopTimeGenerator);
	}

	/**
	 * @param type
	 * @param moveSpeed
	 * @param generator
	 */
	public FreeRunningObject(Square2dObjectType type, float moveSpeed, StopTimeGenerator generator) {
		super(type);
		this.isEnabledFreeRun = true;
		this.moveSpeed = moveSpeed;
		this.stopTimeGenerator = generator;
	}

	@Override
	public void setSquare(Square2d square) {
		super.setSquare(square);
		if (this.foreverFreeRunAction == null) {
			ActionFactory factory = new ActionFactory() {
				@Override
				public Action create() {
					Vector2 dest = FreeRunningObject.this.generateNextTargetPosition();
					Action moveAction = new KeepMovingToTargetPositionAction(dest.x, dest.y, FreeRunningObject.this.getMoveSpeed());
					Action delay = Actions.delay(FreeRunningObject.this.getStopTimeGenerator().generateNextStopTime());
					Action addAction = new AddAction2(this, FreeRunningObject.this);
					FreeRunningObject.this.foreverFreeRunAction = Actions.sequence(moveAction, delay, addAction);
					return FreeRunningObject.this.foreverFreeRunAction;
				}
			};
			this.foreverFreeRunAction = factory.create();
			this.addAction(this.foreverFreeRunAction);
		}
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
	 * @param enable
	 * 
	 */
	public void setEnableFreeRun(boolean enable) {
		this.isEnabledFreeRun = enable;
	}

	/**
	 * @return true if free run is enabled.
	 */
	public boolean isEnabledFreeRun() {
		return this.isEnabledFreeRun;
	}

	@Override
	public void act(float delta) {
		if (this.isEnabledFreeRun() && this.isPausingAction(this.foreverFreeRunAction)) {
			this.resumePausingAction(this.foreverFreeRunAction);
		}
		if (!this.isEnabledFreeRun() && this.isPerformingAction(this.foreverFreeRunAction) || this.isBeingTouched() || this.isLongPressedInLastTouch()) {
			this.pauseAction(this.foreverFreeRunAction);
		}
		super.act(delta);
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
