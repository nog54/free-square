package org.nognog.freeSquare;

import org.nognog.freeSquare.square2d.Square2d;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/01/17
 */
public class FreeSquareGestureDetector extends InputMultiplexer {

	private static final float MAX_CAMERA_ZOOM = 1;
	private static final float MIN_CAMERA_ZOOM = 0.5f;

	boolean isLastLongPressed;

	/**
	 * @param freeSquare
	 */
	FreeSquareGestureDetector(FreeSquare freeSquare) {
		GestureDetector gestureDetector = new GestureDetector(createFreeSquareGestureListener(freeSquare));
		gestureDetector.setLongPressSeconds(0.2f);
		InputAdapter longTouchTapCanceller = new InputAdapter() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (FreeSquareGestureDetector.this.isLastLongPressed) {
					return true;
				}
				return false;
			}
		};
		this.addProcessor(gestureDetector);
		this.addProcessor(longTouchTapCanceller);
	}

	private GestureListener createFreeSquareGestureListener(final FreeSquare freeSquare) {
		GestureListener listener = new GestureDetector.GestureAdapter() {
			private float initialScale = 1;
			private Actor lastTouchDownActor;

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				Vector2 worldPosition = freeSquare.getStage().getViewport().unproject(new Vector2(x, y));
				this.lastTouchDownActor = freeSquare.getStage().hit(worldPosition.x, worldPosition.y, true);
				OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				this.initialScale = camera.zoom;
				FreeSquareGestureDetector.this.isLastLongPressed = false;
				return false;
			}

			private boolean isLastTouchBackGround() {
				return this.lastTouchDownActor == null || this.lastTouchDownActor.getParent() instanceof Square2d;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (!this.isLastTouchBackGround()) {
					return false;
				}
				if (freeSquare.isLockingCameraMove()) {
					return false;
				}
				OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				float currentZoom = camera.zoom;

				camera.translate(-deltaX * currentZoom, deltaY * currentZoom, 0);
				this.adjustCameraPositionIfRangeOver();
				freeSquare.getStage().cancelTouchFocus(freeSquare.getSquare());
				freeSquare.notifyCameraObservers();
				return true;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				if (!this.isLastTouchBackGround()) {
					return false;
				}
				if (freeSquare.isLockingCameraZoom()) {
					return false;
				}
				float ratio = initialDistance / distance;
				float nextZoom = MathUtils.clamp(this.initialScale * ratio, MIN_CAMERA_ZOOM, MAX_CAMERA_ZOOM);
				OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				camera.zoom = nextZoom;
				this.adjustCameraPositionIfRangeOver();
				freeSquare.getStage().cancelTouchFocus(freeSquare.getSquare());
				freeSquare.notifyCameraObservers();
				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				if(this.lastTouchDownActor != null){
					return false;
				}

				if (!freeSquare.isShowingSquare()) {
					freeSquare.showSquareOnly();
					return true;
				}
				Vector2 menuPosition = freeSquare.getStage().screenToStageCoordinates(new Vector2(x, y));
				freeSquare.showSquareOnly();
				freeSquare.showMenu(menuPosition.x, menuPosition.y);
				return true;
			}

			@Override
			public boolean longPress(float x, float y) {
				if (this.isLastTouchBackGround()) {
					Vector2 menuPosition = freeSquare.getStage().screenToStageCoordinates(new Vector2(x, y));
					freeSquare.showMenu(menuPosition.x, menuPosition.y);
					FreeSquareGestureDetector.this.isLastLongPressed = true;
					return true;
				}
				return false;
			}

			private void adjustCameraPositionIfRangeOver() {
				OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
				float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

				float minCameraPositionX = freeSquare.getCameraRangeLowerLeft().x + effectiveViewportWidth / 2f;
				float maxCameraPositionX = freeSquare.getCameraRangeUpperRight().x - effectiveViewportWidth / 2f;
				float minCameraPositionY = freeSquare.getCameraRangeLowerLeft().y + effectiveViewportHeight / 2f;
				float maxCameraPositionY = freeSquare.getCameraRangeUpperRight().y - effectiveViewportHeight / 2f;

				camera.position.x = MathUtils.clamp(camera.position.x, minCameraPositionX, maxCameraPositionX);
				camera.position.y = MathUtils.clamp(camera.position.y, minCameraPositionY, maxCameraPositionY);
			}
		};
		return listener;

	}
}
