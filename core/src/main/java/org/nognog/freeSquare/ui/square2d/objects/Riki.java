package org.nognog.freeSquare.ui.square2d.objects;

import org.nognog.freeSquare.ui.SquareEvent;
import org.nognog.freeSquare.ui.SquareEvent.EventType;
import org.nognog.freeSquare.ui.square2d.FreeRunningLandObject;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends FreeRunningLandObject {

	private static final float moveSpeed = 100;
	private float stopFreeRunTime = 0;

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

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (count == 2) {
					//
				}
			}
		});
	}

	@Override
	public void notify(SquareEvent event) {
		if (event.getEventType() == EventType.ADD_OBJECT) {
			this.stopFreeRunTime = 1;
			this.setEnableFreeRun(false);
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (this.stopFreeRunTime > 0) {
			this.stopFreeRunTime -= delta;
			if (this.stopFreeRunTime <= 0) {
				this.setEnableFreeRun(true);
			}
		}
	}
}
