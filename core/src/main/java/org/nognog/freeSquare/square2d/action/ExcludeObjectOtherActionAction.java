package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/30
 */
public class ExcludeObjectOtherActionAction extends Action {

	private Action action;
	private boolean isActing;

	private Array<Action> pausingAction;

	/**
	 * 
	 */
	public ExcludeObjectOtherActionAction() {
		this(null);
	}

	/**
	 * @param action
	 * @param squareObject
	 */
	public ExcludeObjectOtherActionAction(Action action) {
		this.setAction(action);
		this.isActing = false;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return this.action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public void setActor(Actor actor) {
		if (actor == null || actor instanceof Square2dObject) {
			super.setActor(actor);
			this.action.setActor(actor);
			return;
		}
		throw new RuntimeException("actor of ExcludeOtherObjectActionAction must be Square2dObject instance."); //$NON-NLS-1$
	}

	@Override
	public boolean act(float delta) {
		if (this.action == null) {
			return false;
		}
		Square2dObject target = ((Square2dObject) this.actor);
		if (this.isActing == false) {
			this.pausingAction = target.pausePerformingActionsExcept(this);
			target.lockAddAction();
			this.isActing = true;
		}

		if (this.action.act(delta)) {
			target.unlockAddAction();
			target.resumePausingAction(this.pausingAction);
			return true;
		}
		return false;
	}

	@Override
	public void restart() {
		super.restart();
		this.isActing = false;
		this.pausingAction = null;
		if (this.getAction() != null) {
			this.getAction().restart();
		}
	}
}
