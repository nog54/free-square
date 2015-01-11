package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.objects.Square2DActionUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/31
 */
public class LifeObject extends SquareObject2D {

	private Image frame;

	private Action upDownRoutineAction;

	/**
	 * @param texture
	 * @param logicalWidth
	 * @param performIndependentAction
	 */
	public LifeObject(Texture texture, float logicalWidth) {
		super(texture, logicalWidth, true);
		final float degree = 5;
		final float cycleTime = 4;
		Action foreverRotate = Square2DActionUtils.foreverRotate(degree, cycleTime, Interpolation.sine);
		final float upDownAmount = 5;
		Action foreverUpDown = Square2DActionUtils.foreverUpdown(upDownAmount, cycleTime / 2, Interpolation.pow5);
		this.upDownRoutineAction = Actions.parallel(foreverRotate, foreverUpDown);
		this.addAction(this.upDownRoutineAction);

		this.frame = new Image(new Texture(Gdx.files.internal(Resources.frame1Path)));
		this.frame.setWidth(this.getWidth());
		this.frame.setHeight(this.getHeight());
		this.frame.setName(Resources.frame1Path);
		this.addActor(this.frame);

		this.addListener(new ActorGestureListener() {
			LifeObject target = LifeObject.this;
			boolean isLongTapped;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.isLongTapped = false;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (!this.isLongTapped) {
					this.target.setEnableUpDownRoutine(true);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				this.isLongTapped = true;
				this.target.setEnableUpDownRoutine(false);
				return true;
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				this.target.moveBy(deltaX, deltaY);
			}

		});
	}

	@Override
	protected long independentAction(float delta, long previousInterval, long defaultInterval) {
		Gdx.graphics.requestRendering();
		return defaultInterval;
	}

	/**
	 * @return true if up-down routine is enable
	 */
	public boolean isEnableUpDownRoutine() {
		return this.getActions().contains(this.upDownRoutineAction, true);
	}

	/**
	 * @param enable
	 */
	public synchronized void setEnableUpDownRoutine(boolean enable) {
		final boolean currentEnable = this.isEnableUpDownRoutine();
		if (enable && !currentEnable) {
			this.getActions().add(this.upDownRoutineAction);
		} else if (!enable && currentEnable) {
			this.getActions().removeValue(this.upDownRoutineAction, true);
		}
	}
}
