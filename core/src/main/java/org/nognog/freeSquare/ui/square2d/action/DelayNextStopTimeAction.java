package org.nognog.freeSquare.ui.square2d.action;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

/**
 * @author goshi
 * 2015/01/29
 */
public class DelayNextStopTimeAction extends DelayAction{
	private final StopTimeGenerator stopTimeGenerator;
	/**
	 * @param stopTimeGenerator
	 */
	public DelayNextStopTimeAction(StopTimeGenerator stopTimeGenerator) {
		super(stopTimeGenerator.nextStopTime());
		this.stopTimeGenerator = stopTimeGenerator;
	}
	
	@Override
	public void restart() {
		super.restart();
		this.setDuration(this.stopTimeGenerator.nextStopTime());
	}
}
