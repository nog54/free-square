package org.nognog.freeSquare.ui.square2d.objects;

import org.nognog.freeSquare.ui.square2d.FreeRunningLandObject;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;
import org.nognog.freeSquare.ui.square2d.Square2dEvent.EventType;
import org.nognog.freeSquare.ui.square2d.Square2dObject;
import org.nognog.freeSquare.ui.square2d.action.MoveToSquareObjectAction;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends FreeRunningLandObject {

	private static final float moveSpeed = 100;
	protected MoveToSquareObjectAction moveToTargetObjectAction;

	/**
	 * create Riki
	 */
	public Riki() {
		super(Square2dObjectType.RIKI, moveSpeed);
		this.addListener(new ActorGestureListener() {
			@Override
			public boolean longPress(Actor actor, float x, float y) {
				return true;
			}
		});
	}

	@Override
	public void notify(Square2dEvent event) {
		if (event.getEventType() != EventType.ADD_OBJECT) {
			return;
		}
		Square2dObject relatedObject = event.getRelatedObject();
		if (relatedObject != null && relatedObject.getType() == Square2dObjectType.ICE_TOFU) {
			if (this.isMovingToTargetObject()) {
				final float distanceToTarget = this.getDistanceTo(this.moveToTargetObjectAction.getTargetObject());
				final float distanceToRelatedObject = this.getDistanceTo(relatedObject);
				if (distanceToRelatedObject < distanceToTarget) {
					this.moveToTargetObjectAction.setTargetObject(relatedObject);
				}
				return;
			}
			final boolean wasEnabledFreeRun = this.isEnabledFreeRun();
			this.setEnableFreeRun(false);
			this.moveToTargetObjectAction = new MoveToSquareObjectAction(relatedObject, moveSpeed) {
				@Override
				public boolean act(float delta) {
					if (Riki.this.isBeingTouched() || Riki.this.isLongPressedInLastTouch()) {
						return false;
					}

					if (super.act(delta) == true) {
						Riki.this.setEnableFreeRun(wasEnabledFreeRun);
						Riki.this.moveToTargetObjectAction = null;
						return true;
					}
					return false;
				}
			};
			this.addAction(this.moveToTargetObjectAction);
			return;
		}
		Action up = Actions.moveBy(0, 30, 0.25f, Interpolation.pow3);
		Action down = Actions.moveBy(0, -30, 0.25f, Interpolation.pow3);
		Action hop = Actions.sequence(up, down);
		this.addAction(hop);
	}

	private boolean isMovingToTargetObject() {
		if (this.moveToTargetObjectAction == null || this.moveToTargetObjectAction.isFinished()) {
			return false;
		}
		return true;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}
}
