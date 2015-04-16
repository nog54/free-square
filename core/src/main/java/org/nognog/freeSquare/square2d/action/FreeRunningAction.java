package org.nognog.freeSquare.square2d.action;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * @author goshi 2015/01/29
 */
public class FreeRunningAction extends RepeatAction {

	/**
	 * 
	 */
	public FreeRunningAction() {
		this.setCount(RepeatAction.FOREVER);
	}

	/**
	 * @param stopTimeGenerator
	 * @param targetPositionGenerator
	 * @param speed
	 */
	public FreeRunningAction(StopTimeGenerator stopTimeGenerator, TargetPositionGenerator targetPositionGenerator, float speed) {
		this();
		Action moveAction = new MoveToNextTargetPositionAction(targetPositionGenerator, speed);
		Action delayAction = new DelayNextStopTimeAction(stopTimeGenerator);
		this.setAction(new SequenceAction(moveAction, delayAction));
	}

	/**
	 * unsafe if used improperly.
	 * 
	 * @param action
	 */
	void setMoveActionDelayActionSequence(SequenceAction action) {
		this.setAction(action);
	}

	/**
	 * @param speed
	 */
	public void setMoveSpeed(float speed) {
		this.getMoveToNextTargetPositionAciton().setSpeed(speed);
	}

	/**
	 * @return speed
	 */
	public float getSpeed() {
		return this.getMoveToNextTargetPositionAciton().getSpeed();
	}

	private MoveToNextTargetPositionAction getMoveToNextTargetPositionAciton() {
		return (MoveToNextTargetPositionAction) ((SequenceAction) this.getAction()).getActions().get(0);
	}

	/**
	 * reset target position.
	 */
	public void resetTargetPosition() {
		this.getMoveToNextTargetPositionAciton().restart();
	}

	/**
	 * @return current target position vector
	 */
	public Vector2 getCurrentTargetPosition() {
		final Vector2 targetPosition = new Vector2();
		targetPosition.x = this.getMoveToNextTargetPositionAciton().getTargetPositionX();
		targetPosition.y = this.getMoveToNextTargetPositionAciton().getTargetPositionY();
		return targetPosition;
	}

	@Override
	public String toString() {
		String name = getClass().getName();
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex != -1)
			name = name.substring(dotIndex + 1);
		if (name.endsWith("Action"))name = name.substring(0, name.length() - 6); //$NON-NLS-1$
		return name;
	}

}
