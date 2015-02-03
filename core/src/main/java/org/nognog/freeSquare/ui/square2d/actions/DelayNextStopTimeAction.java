package org.nognog.freeSquare.ui.square2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

/**
 * @author goshi 2015/01/29
 */
public class DelayNextStopTimeAction extends DelayAction {
	private StopTimeGenerator stopTimeGenerator;

	/**
	 * 
	 */
	public DelayNextStopTimeAction() {

	}

	/**
	 * @param stopTimeGenerator
	 */
	public DelayNextStopTimeAction(StopTimeGenerator stopTimeGenerator) {
		super(stopTimeGenerator.nextStopTime());
		this.stopTimeGenerator = stopTimeGenerator;
	}

	/**
	 * @param stopTimeGenerator
	 */
	public void setStopTimeGenerator(StopTimeGenerator stopTimeGenerator) {
		this.stopTimeGenerator = stopTimeGenerator;
	}


	/**
	 * @return using StopTimeGenerator
	 */
	public StopTimeGenerator getStopTimeGenerator() {
		return this.stopTimeGenerator;
	}

	@Override
	public void restart() {
		super.restart();
		this.setDuration(this.stopTimeGenerator.nextStopTime());
	}
}
