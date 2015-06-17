/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.action.activity;

import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.END;
import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.SET_SQUARE;
import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.SLIDE_IN;
import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.SLIDE_OUT;
import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.START;
import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.ZOOM_IN;
import static org.nognog.freeSquare.square2d.action.activity.ChangeSquareAction.ChangeSquareActionPhase.ZOOM_OUT;

import org.nognog.freeSquare.activity.main.MainActivity;
import org.nognog.freeSquare.activity.main.MainActivityInputProcessor;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.gdx.util.camera.Camera;
import org.nognog.gdx.util.camera.ObservableOrthographicCamera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * @author goshi 2015/03/25
 */
public class ChangeSquareAction extends Action {

	private MainActivity activity;
	private Square2d square;
	private Direction direction;

	private float zoomInTargetValue;

	private ChangeSquareActionPhase phase;

	private static final float zoomSpeed = 10; // [zoomAmount / sec]
	private static final float slideSpeed = 10000; // [logicalWidth / sec]

	/**
	 * 
	 */
	public ChangeSquareAction() {
		this.phase = START;
	}

	/**
	 * @param activity
	 * @param square
	 * @param direction
	 */
	public ChangeSquareAction(MainActivity activity, Square2d square, Direction direction) {
		this();
		if (activity == null || direction == null) {
			throw new IllegalArgumentException(""); //$NON-NLS-1$
		}
		this.activity = activity;
		this.square = square;
		this.direction = direction;
	}

	@Override
	public void setActor(Actor actor) {
		if (actor != null && !(actor instanceof MainActivity)) {
			throw new IllegalArgumentException();
		}
		super.setActor(actor);
	}

	@Override
	public boolean act(float delta) {
		if (this.phase == START) {
			this.performStartPhase();
			return false;
		}
		if (this.phase == ZOOM_OUT) {
			this.performZoomOut(delta);
			return false;
		}
		if (this.phase == SLIDE_OUT) {
			this.performSlideOut(delta);
			return false;
		}
		if (this.phase == SET_SQUARE) {
			this.performSetSquare();
			return false;
		}
		if (this.phase == SLIDE_IN) {
			this.performSlideIn(delta);
			return false;
		}
		if (this.phase == ZOOM_IN) {
			this.performZoomIn(delta);
			return false;
		}
		if (this.phase == END) {
			this.performEndPhase();
		}
		return true;
	}

	private void performStartPhase() {
		final Group stageRoot = this.activity.getStage().getRoot();
		for (Action stageRootAction : stageRoot.getActions()) {
			if (stageRootAction instanceof ChangeSquareAction) {
				if (((ChangeSquareAction) stageRootAction).phase != START) {
					return;
				}
			}
		}
		stageRoot.setTouchable(Touchable.disabled);
		((MainActivityInputProcessor) this.activity.getInputProcesser()).disable();
		if (this.activity.getSquare() == null) {
			this.zoomInTargetValue = 1;
		} else {
			this.zoomInTargetValue = this.activity.getCamera().getZoom();
		}
		this.phase = ZOOM_OUT;
		return;
	}

	private void performZoomOut(float delta) {
		ObservableOrthographicCamera camera = this.activity.getCamera();
		camera.setZoom(MathUtils.clamp(camera.getZoom() + delta * zoomSpeed, this.activity.getMinZoom(), this.activity.getMaxZoom()), false);
		if (camera.getZoom() == this.activity.getMaxZoom()) {
			this.phase = SLIDE_OUT;
		}
	}

	private void performSlideOut(float delta) {
		if (this.activity.getSquare() == null) {
			this.phase = SET_SQUARE;
			return;
		}

		this.moveCamera(delta, this.direction);
		final Camera camera = this.activity.getCamera();
		final float viewingWidth = camera.getViewportWidth() * camera.getZoom();
		final float viewingHeight = camera.getViewportHeight() * camera.getZoom();
		final boolean completeSlideOut = (camera.getX() + viewingWidth / 2 < this.activity.getSquare().getLeftEndX()) || (camera.getX() - viewingWidth / 2 > this.activity.getSquare().getRightEndX())
				|| (camera.getY() + viewingHeight / 2 < this.activity.getSquare().getBottomEndY()) || (camera.getY() - viewingHeight / 2 > this.activity.getSquare().getTopEndY());
		if (completeSlideOut) {
			this.phase = SET_SQUARE;
		}
	}

	private void performSetSquare() {
		boolean beforeSquareIsNull = this.activity.getSquare() == null;
		this.activity.setSquare(this.square);
		final ObservableOrthographicCamera camera = this.activity.getCamera();
		if (beforeSquareIsNull) {
			camera.setZoom(this.activity.getMaxZoom(), false);
		}
		if (this.square == null) {
			camera.setPosition(0, 0, false);
			this.phase = END;
			return;
		}
		if (this.direction == Direction.UP) {
			camera.setPosition(this.square.getCenterX(), this.square.getBottomEndY() - camera.getViewportHeight() * camera.getZoom(), false);
		} else if (this.direction == Direction.DOWN) {
			camera.setPosition(this.square.getCenterX(), this.square.getTopEndY() + camera.getViewportHeight() * camera.getZoom(), false);
		} else if (this.direction == Direction.RIGHT) {
			camera.setPosition(this.square.getLeftEndX() - camera.getViewportWidth() * camera.getZoom(), this.square.getCenterY(), false);
		} else if (this.direction == Direction.LEFT) {
			camera.setPosition(this.square.getRightEndX() + camera.getViewportWidth() * camera.getZoom(), this.square.getCenterY(), false);
		}
		this.zoomInTargetValue = MathUtils.clamp(this.zoomInTargetValue, this.activity.getMinZoom(), this.activity.getMaxZoom());
		this.phase = SLIDE_IN;
	}

	private void performSlideIn(float delta) {
		this.moveCamera(delta, this.direction);
		final ObservableOrthographicCamera camera = this.activity.getCamera();
		final boolean slideInEnd = (this.direction == Direction.DOWN && camera.getY() <= this.square.getCenterY()) || (this.direction == Direction.UP && camera.getY() >= this.square.getCenterY())
				|| (this.direction == Direction.LEFT && camera.getX() <= this.square.getCenterX()) || (this.direction == Direction.RIGHT && camera.getX() >= this.square.getCenterX());
		if (slideInEnd) {
			camera.setPosition(this.square.getCenterX(), this.square.getCenterY(), false);
			this.phase = ZOOM_IN;
		}
	}

	private void performZoomIn(float delta) {
		ObservableOrthographicCamera camera = this.activity.getCamera();
		camera.zoom(-delta * zoomSpeed, false);
		if (camera.getZoom() < this.zoomInTargetValue) {
			camera.setZoom(this.zoomInTargetValue, false);
			this.phase = END;
		}
	}

	private void performEndPhase() {
		this.activity.getStage().getRoot().setTouchable(Touchable.enabled);
		((MainActivityInputProcessor) this.activity.getInputProcesser()).enable();
	}

	private void moveCamera(float delta, Direction moveDirection) {
		final float speedX = getMoveX(moveDirection);
		final float speedY = getMoveY(moveDirection);
		this.activity.getCamera().move(speedX * delta, speedY * delta, false);
	}

	private static float getMoveX(Direction direction) {
		if (direction == Direction.LEFT) {
			return -slideSpeed;
		}
		if (direction == Direction.RIGHT) {
			return slideSpeed;
		}
		return 0;
	}

	private static float getMoveY(Direction direction) {
		if (direction == Direction.DOWN) {
			return -slideSpeed;
		}
		if (direction == Direction.UP) {
			return slideSpeed;
		}
		return 0;
	}

	/**
	 * @return the activity
	 */
	public MainActivity getActivity() {
		return this.activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(MainActivity activity) {
		if (activity == null) {
			throw new IllegalArgumentException();
		}
		this.activity = activity;
	}

	/**
	 * @return the square
	 */
	public Square2d getSquare() {
		return this.square;
	}

	/**
	 * @param square
	 *            the square to set
	 */
	public void setSquare(Square2d square) {
		this.square = square;
	}

	/**
	 * @return the direction
	 */
	public Direction getDirection() {
		return this.direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(Direction direction) {
		if (direction == null) {
			throw new IllegalArgumentException();
		}
		this.direction = direction;
	}

	@Override
	public void restart() {
		this.phase = START;
	}

	enum ChangeSquareActionPhase {
		START, ZOOM_OUT, SLIDE_OUT, SET_SQUARE, SLIDE_IN, ZOOM_IN, END
	}
}
