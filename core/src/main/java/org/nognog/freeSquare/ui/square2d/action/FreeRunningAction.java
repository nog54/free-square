package org.nognog.freeSquare.ui.square2d.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

/**
 * @author goshi 2015/01/29
 */
public class FreeRunningAction extends RepeatAction {

	/**
	 * @param stopTimeGenerator
	 * @param targetPositionGenerator
	 * @param speed
	 */
	public FreeRunningAction(StopTimeGenerator stopTimeGenerator, TargetPositionGenerator targetPositionGenerator, float speed) {
		this.setCount(RepeatAction.FOREVER);

		Action moveAction = new MoveToNextTargetPositionAction(targetPositionGenerator, speed);
		Action delay = new DelayNextStopTimeAction(stopTimeGenerator);
		this.setAction(Actions.sequence(moveAction, delay));
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
