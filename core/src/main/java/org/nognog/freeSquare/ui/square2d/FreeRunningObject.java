package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.ui.square2d.action.FreeRunningAction;
import org.nognog.freeSquare.ui.square2d.action.StopTimeGenerator;
import org.nognog.freeSquare.ui.square2d.action.TargetPositionGenerator;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/03
 */
public class FreeRunningObject extends LifeObject implements TargetPositionGenerator{

	protected static final float defaultMoveSpeed = 32;

	protected static final StopTimeGenerator defaultStopTimeGenerator = new StopTimeGenerator() {
		@Override
		public float nextStopTime() {
			return MathUtils.random(0, 1f);
		}
	};

	protected Action freeRunningAction = null;
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
		if (this.freeRunningAction == null) {
			this.freeRunningAction = new FreeRunningAction(this.stopTimeGenerator, this, this.moveSpeed);
			this.addAction(this.freeRunningAction);
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
		if (this.isEnabledFreeRun() && this.isPausingAction(this.freeRunningAction)) {
			this.resumePausingAction(this.freeRunningAction);
		}
		if (!this.isEnabledFreeRun() && this.isPerformingAction(this.freeRunningAction) || this.isBeingTouched() || this.isLongPressedInLastTouch()) {
			this.pauseAction(this.freeRunningAction);
		}
		super.act(delta);
	}

	@Override
	public Vector2 nextTargetPosition() {
		final float x = MathUtils.random(0, FreeRunningObject.this.square.getWidth());
		final float y = MathUtils.random(0, FreeRunningObject.this.square.getHeight());
		return new Vector2(x, y);
	}

}
