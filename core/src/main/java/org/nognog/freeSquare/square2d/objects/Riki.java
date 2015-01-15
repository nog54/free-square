package org.nognog.freeSquare.square2d.objects;

import org.nognog.freeSquare.square2d.FreeRunningLandObject;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends FreeRunningLandObject {

	private static final float moveSpeed = 100;

	/**
	 * create Riki
	 */
	public Riki() {
		super(Square2dObjectKind.RIKI, moveSpeed);
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
}
