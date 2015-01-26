package org.nognog.freeSquare.ui.square2d.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/01/26
 */
public class AddAction2 extends Action {

	private ActionFactory actionFactory;
	private Actor targetActor;

	/**
	 * @param factory
	 * @param target
	 */
	public AddAction2(ActionFactory factory, Actor target) {
		this.actionFactory = factory;
		this.targetActor = target;
	}

	@Override
	public boolean act(float delta) {
		if (this.actionFactory != null) {
			if (this.targetActor == null) {
				this.actor.addAction(this.actionFactory.create());
			} else {
				this.targetActor.addAction(this.actionFactory.create());
			}
		}
		return true;
	}

}
