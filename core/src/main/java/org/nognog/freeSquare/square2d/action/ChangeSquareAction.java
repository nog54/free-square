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

package org.nognog.freeSquare.square2d.action;

import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.END;
import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.SET_SQUARE;
import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.SLIDE_IN;
import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.SLIDE_OUT;
import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.START;
import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.ZOOM_IN;
import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.ZOOM_OUT;

import org.nognog.freeSquare.activity.MainActivity;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
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
		final Group stageRoot = this.activity.getFreeSquare().getStage().getRoot();
		for (Action stageRootAction : stageRoot.getActions()) {
			if (stageRootAction instanceof ChangeSquareAction) {
				if (((ChangeSquareAction) stageRootAction).phase != START) {
					return;
				}
			}
		}
		stageRoot.setTouchable(Touchable.disabled);
		if (this.activity.getSquare() == null) {
			this.zoomInTargetValue = 1;
		} else {
			this.zoomInTargetValue = ((OrthographicCamera) this.activity.getFreeSquare().getCamera()).zoom;
		}
		this.phase = ZOOM_OUT;
		return;
	}

	private void performZoomOut(float delta) {
		OrthographicCamera camera = (OrthographicCamera) this.activity.getFreeSquare().getCamera();
		camera.zoom = MathUtils.clamp(camera.zoom + delta * zoomSpeed, this.activity.getMinZoom(), this.activity.getMaxZoom());
		if (camera.zoom == this.activity.getMaxZoom()) {
			this.phase = SLIDE_OUT;
		}
	}

	private void performSlideOut(float delta) {
		if (this.activity.getSquare() == null) {
			this.phase = SET_SQUARE;
			return;
		}

		this.moveCamera(delta, this.direction);
		final OrthographicCamera camera = (OrthographicCamera) this.activity.getFreeSquare().getCamera();
		final float viewingWidth = camera.viewportWidth * camera.zoom;
		final float viewingHeight = camera.viewportHeight * camera.zoom;
		final boolean slideOutEnd = (camera.position.x + viewingWidth / 2 < this.activity.getSquare().getLeftEndX())
				|| (camera.position.x - viewingWidth / 2 > this.activity.getSquare().getRightEndX()) || (camera.position.y + viewingHeight / 2 < this.activity.getSquare().getBottomEndY())
				|| (camera.position.y - viewingHeight / 2 > this.activity.getSquare().getTopEndY());
		if (slideOutEnd) {
			this.phase = SET_SQUARE;
		}
	}

	private void performSetSquare() {
		boolean beforeSquareIsNull = this.activity.getSquare() == null;
		this.activity.setSquare(this.square);
		final OrthographicCamera camera = (OrthographicCamera) this.activity.getFreeSquare().getCamera();
		if (beforeSquareIsNull) {
			camera.zoom = this.activity.getMaxZoom();
		}
		if (this.square == null) {
			camera.position.x = 0;
			camera.position.y = 0;
			this.phase = END;
			return;
		}
		if (this.direction == Direction.UP) {
			camera.position.x = this.square.getCenterX();
			camera.position.y = this.square.getBottomEndY() - camera.viewportHeight * camera.zoom;
		} else if (this.direction == Direction.DOWN) {
			camera.position.x = this.square.getCenterX();
			camera.position.y = this.square.getTopEndY() + camera.viewportHeight * camera.zoom;
		} else if (this.direction == Direction.RIGHT) {
			camera.position.x = this.square.getLeftEndX() - camera.viewportWidth * camera.zoom;
			camera.position.y = this.square.getCenterY();
		} else if (this.direction == Direction.LEFT) {
			camera.position.x = this.square.getRightEndX() + camera.viewportWidth * camera.zoom;
			camera.position.y = this.square.getCenterY();
		}
		this.zoomInTargetValue = MathUtils.clamp(this.zoomInTargetValue, this.activity.getMinZoom(), this.activity.getMaxZoom());
		this.phase = SLIDE_IN;
	}

	private void performSlideIn(float delta) {
		this.moveCamera(delta, this.direction);
		final Camera camera = this.activity.getFreeSquare().getCamera();
		final boolean slideInEnd = (this.direction == Direction.DOWN && camera.position.y <= this.square.getCenterY())
				|| (this.direction == Direction.UP && camera.position.y >= this.square.getCenterY()) || (this.direction == Direction.LEFT && camera.position.x <= this.square.getCenterX())
				|| (this.direction == Direction.RIGHT && camera.position.x >= this.square.getCenterX());
		if (slideInEnd) {
			camera.position.x = this.square.getCenterX();
			camera.position.y = this.square.getCenterY();
			this.phase = ZOOM_IN;
		}
	}

	private void performZoomIn(float delta) {
		OrthographicCamera camera = (OrthographicCamera) this.activity.getFreeSquare().getCamera();
		camera.zoom -= delta * zoomSpeed;
		if (camera.zoom < this.zoomInTargetValue) {
			camera.zoom = this.zoomInTargetValue;
			this.phase = END;
		}
	}
	private void performEndPhase() {
		this.activity.getFreeSquare().getStage().getRoot().setTouchable(Touchable.enabled);
	}

	private void moveCamera(float delta, Direction moveDirection) {
		final float speedX = getMoveX(moveDirection);
		final float speedY = getMoveY(moveDirection);
		OrthographicCamera camera = (OrthographicCamera) this.activity.getFreeSquare().getCamera();
		camera.translate(speedX * delta, speedY * delta, 0);
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
