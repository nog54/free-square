package org.nognog.freeSquare.ui.square2d.objects;

import org.nognog.freeSquare.ui.square2d.EatableObject;
import org.nognog.freeSquare.ui.square2d.FreeRunningLandObject;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;
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
	protected SequenceAction currectTryingMoveAndEatAction;
	protected EatableObject currentTryingEatableObject;

	/**
	 * create Riki
	 */
	public Riki() {
		super(Square2dObjectType.RIKI, moveSpeed);
	}

	@Override
	public void notify(Square2dEvent event) {
		if (!(event instanceof AddObjectEvent)) {
			return;
		}
		AddObjectEvent addEvent = (AddObjectEvent) event;
		Action up = Actions.moveBy(0, 30, 0.25f, Interpolation.pow3);
		Action down = Actions.moveBy(0, -30, 0.25f, Interpolation.pow3);
		Action hop = Actions.sequence(up, down);
		this.addAction(hop);
		if (addEvent.getAddedObject() instanceof EatableObject) {
			EatableObject eatObject = (EatableObject) addEvent.getAddedObject();
			if (this.isMovingToTargetObject()) {
				final float distanceToTarget = this.getDistanceTo(this.currentTryingEatableObject);
				final float distanceToRelatedObject = this.getDistanceTo(eatObject);
				if (distanceToRelatedObject < distanceToTarget) {
					this.changeEatTargetTo(eatObject);
				}
				return;
			}

			final boolean wasEnabledFreeRun = this.isEnabledFreeRun();
			this.setEnableFreeRun(false);
			Action moveAndEat = Square2dActions.moveAndEat(eatObject, eatAmountPerOnce, moveSpeed, eatObject.getWidth() / 2);
			Action setFreeRunModeAction = new Action() {
				@Override
				public boolean act(float delta) {
					Riki.this.currectTryingMoveAndEatAction = null;
					Riki.this.currentTryingEatableObject = null;
					Riki.this.setEnableFreeRun(wasEnabledFreeRun);
					return true;
				}
			};

			this.currectTryingMoveAndEatAction = Actions.sequence(moveAndEat, setFreeRunModeAction);
			this.currentTryingEatableObject = eatObject;
			this.addAction(this.currectTryingMoveAndEatAction);
			return;
		}
	}

	private void changeEatTargetTo(EatableObject relatedObject) {
		int index = this.currectTryingMoveAndEatAction.getActions().size - 1;
		Action setFreeRunModeAction = this.currectTryingMoveAndEatAction.getActions().get(index);
		this.removeAction(this.currectTryingMoveAndEatAction);
		Action moveAndEat = Square2dActions.moveAndEat(relatedObject, eatAmountPerOnce, moveSpeed);
		this.currectTryingMoveAndEatAction = Actions.sequence(moveAndEat, setFreeRunModeAction);
		this.currentTryingEatableObject = relatedObject;
		this.addAction(this.currectTryingMoveAndEatAction);
	}

	private boolean isMovingToTargetObject() {
		return this.currectTryingMoveAndEatAction != null && this.currentTryingEatableObject != null;
	}

}
