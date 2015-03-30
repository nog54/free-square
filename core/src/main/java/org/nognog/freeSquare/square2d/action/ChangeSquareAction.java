package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.Square2d;

import static org.nognog.freeSquare.square2d.action.ChangeSquareAction.ChangeSquareActionPhase.*;

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

	private FreeSquare freeSquare;
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
	 * @param freeSquare
	 * @param square
	 * @param direction
	 */
	public ChangeSquareAction(FreeSquare freeSquare, Square2d square, Direction direction) {
		this();
		if (freeSquare == null || direction == null) {
			throw new IllegalArgumentException(""); //$NON-NLS-1$
		}
		this.freeSquare = freeSquare;
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
		final Group stageRoot = this.freeSquare.getStage().getRoot();
		for (Action stageRootAction : stageRoot.getActions()) {
			if (stageRootAction instanceof ChangeSquareAction) {
				if (((ChangeSquareAction) stageRootAction).phase != START) {
					return;
				}
			}
		}
		stageRoot.setTouchable(Touchable.disabled);
		if (this.freeSquare.getSquare() == null) {
			this.zoomInTargetValue = 1;
		} else {
			this.zoomInTargetValue = ((OrthographicCamera) this.freeSquare.getStage().getCamera()).zoom;
		}
		this.phase = ZOOM_OUT;
		return;
	}

	private void performZoomOut(float delta) {
		OrthographicCamera camera = (OrthographicCamera) this.freeSquare.getStage().getCamera();
		camera.zoom = MathUtils.clamp(camera.zoom + delta * zoomSpeed, this.freeSquare.getMinZoom(), this.freeSquare.getMaxZoom());
		if (camera.zoom == this.freeSquare.getMaxZoom()) {
			this.phase = SLIDE_OUT;
		}
	}

	private void performSlideOut(float delta) {
		if (this.freeSquare.getSquare() == null) {
			this.phase = SET_SQUARE;
			return;
		}

		this.moveCamera(delta, this.direction);
		final OrthographicCamera camera = (OrthographicCamera) this.freeSquare.getStage().getCamera();
		final float viewingWidth = camera.viewportWidth * camera.zoom;
		final float viewingHeight = camera.viewportHeight * camera.zoom;
		final boolean slideOutEnd = (camera.position.x + viewingWidth / 2 < this.freeSquare.getSquare().getLeftEndX())
				|| (camera.position.x - viewingWidth / 2 > this.freeSquare.getSquare().getRightEndX()) || (camera.position.y + viewingHeight / 2 < this.freeSquare.getSquare().getBottomEndY())
				|| (camera.position.y - viewingHeight / 2 > this.freeSquare.getSquare().getTopEndY());
		if (slideOutEnd) {
			this.phase = SET_SQUARE;
		}
	}

	private void performSetSquare() {
		boolean beforeSquareIsNull = this.freeSquare.getSquare() == null;
		this.freeSquare.setSquare(this.square);
		final OrthographicCamera camera = (OrthographicCamera) this.freeSquare.getStage().getCamera();
		if(beforeSquareIsNull){
			camera.zoom = this.freeSquare.getMaxZoom();
		}
		if (this.square == null) {
			camera.position.x = 0;
			camera.position.y = 0;
			this.phase = END;
			return;
		}
		if (this.direction == Direction.UP) {
			camera.position.x = this.square.getCenterX();
			camera.position.y = this.square.getBottomEndY() - camera.viewportHeight;
		} else if (this.direction == Direction.DOWN) {
			camera.position.x = this.square.getCenterX();
			camera.position.y = this.square.getTopEndY() + camera.viewportHeight;
		} else if (this.direction == Direction.RIGHT) {
			camera.position.x = this.square.getLeftEndX() - camera.viewportWidth;
			camera.position.y = this.square.getCenterY();
		} else if (this.direction == Direction.LEFT) {
			camera.position.x = this.square.getRightEndX() + camera.viewportWidth;
			camera.position.y = this.square.getCenterY();
		}
		this.zoomInTargetValue = MathUtils.clamp(this.zoomInTargetValue, this.freeSquare.getMinZoom(), this.freeSquare.getMaxZoom());
		this.phase = SLIDE_IN;
	}

	private void performSlideIn(float delta) {
		this.moveCamera(delta, this.direction);
		final Camera camera = this.freeSquare.getStage().getCamera();
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
		OrthographicCamera camera = (OrthographicCamera) this.freeSquare.getStage().getCamera();
		camera.zoom -= delta * zoomSpeed;
		if (camera.zoom < this.zoomInTargetValue) {
			camera.zoom = this.zoomInTargetValue;
			this.phase = END;
		}
	}

	private void performEndPhase() {
		this.freeSquare.getStage().getRoot().setTouchable(Touchable.enabled);
	}

	private void moveCamera(float delta, Direction moveDirection) {
		final float speedX = getMoveX(moveDirection);
		final float speedY = getMoveY(moveDirection);
		OrthographicCamera camera = (OrthographicCamera) this.freeSquare.getStage().getCamera();
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
	 * @return the freeSquare
	 */
	public FreeSquare getFreeSquare() {
		return this.freeSquare;
	}

	/**
	 * @param freeSquare
	 *            the freeSquare to set
	 */
	public void setFreeSquare(FreeSquare freeSquare) {
		if (freeSquare == null) {
			throw new IllegalArgumentException();
		}
		this.freeSquare = freeSquare;
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
