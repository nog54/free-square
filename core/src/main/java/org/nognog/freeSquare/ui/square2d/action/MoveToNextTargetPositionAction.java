package org.nognog.freeSquare.ui.square2d.action;

import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2015/01/29
 */
public class MoveToNextTargetPositionAction extends MoveToTargetPositionAction {
	private TargetPositionGenerator targetPositionGenerator;


	/**
	 * 
	 */
	public MoveToNextTargetPositionAction() {
		super();
	}
	
	/**
	 * @param targetPositionGenerator
	 * @param speed
	 */
	public MoveToNextTargetPositionAction(TargetPositionGenerator targetPositionGenerator, float speed) {
		super(targetPositionGenerator.nextTargetPosition(), speed);
		this.targetPositionGenerator = targetPositionGenerator;
	}
	
	/**
	 * @param targetPositionGenerator
	 */
	public void setTargetPositionGenerator(TargetPositionGenerator targetPositionGenerator){
		this.targetPositionGenerator = targetPositionGenerator;
	}
	
	/**
	 * @return using generator
	 */
	public TargetPositionGenerator getTargetPositionGenerator(){
		return this.targetPositionGenerator;
	}


	@Override
	public void restart() {
		super.restart();
		Vector2 nextTargetPosition = this.targetPositionGenerator.nextTargetPosition();
		this.setTargetPositionX(nextTargetPosition.x);
		this.setTargetPositionY(nextTargetPosition.y);
	}

}
