package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.ui.square2d.action.Square2dActions;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/31
 */
public class LifeObject extends Square2dObject {

	private Image frame;

	private boolean isEnabledUpDownRoutineAction;
	private Action upDownRoutineAction;

	/**
	 * @param type
	 */
	public LifeObject(Square2dObjectType type) {
		super(type);
		this.setOriginY(0);
		final float degree = 5;
		final float cycleTime = 4;
		Action foreverRotate = Square2dActions.foreverRotate(degree, cycleTime, Interpolation.sine);
		final float upDownAmount = 5;
		Action foreverUpDown = Square2dActions.foreverUpdown(upDownAmount, cycleTime / 2, Interpolation.pow5);
		this.upDownRoutineAction = Actions.parallel(foreverRotate, foreverUpDown);
		this.addAction(this.upDownRoutineAction);
		this.isEnabledUpDownRoutineAction = true;

		this.frame = new Image(new Texture(Gdx.files.internal(Resources.frame1Path)));
		this.frame.setWidth(this.getWidth());
		this.frame.setHeight(this.getHeight());
		this.frame.setName(Resources.frame1Path);
		this.addActor(this.frame);

		this.addListener(new ActorGestureListener() {
			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				LifeObject.this.moveBy(deltaX, deltaY);
			}
		});
	}

	/**
	 * @return true if up-down routine is enable
	 */
	public boolean isEnabledUpDownRoutine() {
		return this.isEnabledUpDownRoutineAction;
	}

	/**
	 * @param enable
	 */
	public void setEnableUpDownRoutine(boolean enable) {
		this.isEnabledUpDownRoutineAction = enable;
	}

	@Override
	public void act(float delta) {
		if (this.isEnabledUpDownRoutine() && this.isPausingAction(this.upDownRoutineAction)) {
			this.resumePausingAction(this.upDownRoutineAction);
		}
		if (!this.isEnabledUpDownRoutine() && this.isPerformingAction(this.upDownRoutineAction) || this.isBeingTouched() || this.isLongPressedInLastTouch()) {
			this.pauseAction(this.upDownRoutineAction);
		}
		super.act(delta);
	}
}
