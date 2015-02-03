package org.nognog.freeSquare.ui.square2d.actions;

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
	public void setSpeed(float speed) {
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

	@Override
	protected boolean delegate(float delta) {
		return super.delegate(delta);
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
