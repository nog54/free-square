package org.nognog.freeSquare.ui.square2d.objects;

import org.nognog.freeSquare.ui.square2d.EatableObject;
import org.nognog.freeSquare.ui.square2d.FreeRunningLandObject;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;
import org.nognog.freeSquare.ui.square2d.Square2dObject;
import org.nognog.freeSquare.ui.square2d.actions.EatAction;
import org.nognog.freeSquare.ui.square2d.actions.Square2dActions;
import org.nognog.freeSquare.ui.square2d.events.AddObjectEvent;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends FreeRunningLandObject {

	private static final float moveSpeed = 100;
	private static final int eatAmountPerOnce = 20;
	private static final float eatInterval = 1;
	protected SequenceAction currectTryingMoveAndEatAction;
	protected EatAction currentEatAction;
	protected Action setFreeRunModeAction;

	/**
	 * create Riki
	 */
	public Riki() {
		super(Square2dObjectType.RIKI, moveSpeed);
	}

	@Override
	public void act(float delta) {
		final EatableObject nearestEatableLandingObject = this.getNearestEatableLandingObject();
		if (nearestEatableLandingObject != null) {
			if (this.isMovingToTargetObject() && (nearestEatableLandingObject != this.currentEatAction.getEatObject())) {
				this.changeEatTargetTo(nearestEatableLandingObject);
			} else if (!this.isMovingToTargetObject()) {
				this.setEatAction(nearestEatableLandingObject);
			}
		} else {
			if (this.isMovingToTargetObject()) {
				this.currentEatAction.requestForceFinish();
			}
		}
		super.act(delta);
	}

	private EatableObject getNearestEatableLandingObject() {
		EatableObject result = null;
		float resultDistance = 0;
		for (Square2dObject object : this.square.getObjects()) {
			if (object instanceof EatableObject && object.isLandingOnSquare()) {
				if (result == null) {
					result = (EatableObject) object;
					resultDistance = this.getDistanceTo(object);
				}
				final float objectDistance = this.getDistanceTo(object);
				if (objectDistance < resultDistance) {
					result = (EatableObject) object;
					resultDistance = objectDistance;
				}
			}
		}
		return result;
	}

	@Override
	public void notify(Square2dEvent event) {
		if (!(event instanceof AddObjectEvent)) {
			return;
		}
		Action up = Actions.moveBy(0, 30, 0.25f, Interpolation.pow3);
		Action down = Actions.moveBy(0, -30, 0.25f, Interpolation.pow3);
		Action hop = Actions.sequence(up, down);
		this.addAction(hop);
	}

	private void setEatAction(EatableObject eatObject) {
		final boolean wasEnabledFreeRun = this.isEnabledFreeRun();
		this.setEnableFreeRun(false);
		EatAction eatAction = Square2dActions.eat(eatObject, eatAmountPerOnce, EatAction.UNTIL_RUN_OUT, eatInterval, moveSpeed);
		this.setFreeRunModeAction = new Action() {
			@Override
			public boolean act(float delta) {
				Riki.this.currectTryingMoveAndEatAction = null;
				Riki.this.currentEatAction = null;
				Riki.this.setFreeRunModeAction = null;
				Riki.this.setEnableFreeRun(wasEnabledFreeRun);
				return true;
			}
		};

		this.currectTryingMoveAndEatAction = Actions.sequence(eatAction, this.setFreeRunModeAction);
		this.currentEatAction = eatAction;
		this.addAction(this.currectTryingMoveAndEatAction);
	}

	private void changeEatTargetTo(EatableObject newEatObject) {
		this.currentEatAction.setEatObject(newEatObject);
	}

	private boolean isMovingToTargetObject() {
		return this.currectTryingMoveAndEatAction != null && this.currentEatAction != null && this.setFreeRunModeAction != null;
	}

}
