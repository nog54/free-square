package org.nognog.freeSquare.activity;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.square2d.Square2d;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/01/17
 */
public class MainActivityInputProcessor extends InputMultiplexer {

	boolean isLastLongPressed;

	/**
	 * @param activity
	 */
	public MainActivityInputProcessor(MainActivity activity) {
		GestureDetector gestureDetector = new GestureDetector(this.createFreeSquareGestureListener(activity));
		gestureDetector.setLongPressSeconds(0.2f);
		InputAdapter longTouchTapCanceller = new InputAdapter() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (MainActivityInputProcessor.this.isLastLongPressed) {
					return true;
				}
				return false;
			}
		};
		this.addProcessor(gestureDetector);
		this.addProcessor(longTouchTapCanceller);
	}

	private GestureListener createFreeSquareGestureListener(final MainActivity activity) {
		final FreeSquare freeSquare = activity.getFreeSquare();
		GestureListener listener = new GestureDetector.GestureAdapter() {
			private float initialScale = 1;
			private Actor lastTouchDownActor;

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				Vector2 worldPosition = freeSquare.getStage().getViewport().unproject(new Vector2(x, y));
				this.lastTouchDownActor = freeSquare.getStage().hit(worldPosition.x, worldPosition.y, true);
				OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				this.initialScale = camera.zoom;
				MainActivityInputProcessor.this.isLastLongPressed = false;
				return false;
			}

			private boolean isLastTouchBackGround() {
				return this.lastTouchDownActor == null || this.lastTouchDownActor instanceof Square2d;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (activity.getSquare() == null) {
					return false;
				}
				if (!this.isLastTouchBackGround()) {
					return false;
				}
				if (activity.isLockingCameraMove()) {
					return false;
				}
				final OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				final float currentZoom = camera.zoom;
				final float cameraMoveX = -deltaX * currentZoom;
				final float cameraMoveY = deltaY * currentZoom;
				camera.translate(cameraMoveX, cameraMoveY, 0);
				activity.adjustCameraZoomAndPositionIfRangeOver();
				activity.getStage().cancelTouchFocus(activity.getSquare());
				activity.getFreeSquare().notifyCameraObservers();
				return true;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				if (!this.isLastTouchBackGround()) {
					return false;
				}
				if (activity.isLockingCameraZoom()) {
					return false;
				}
				final float ratio = initialDistance / distance;
				final float nextZoom = this.initialScale * ratio;
				final OrthographicCamera camera = (OrthographicCamera) freeSquare.getStage().getCamera();
				camera.zoom = nextZoom;
				activity.adjustCameraZoomAndPositionIfRangeOver();
				activity.getStage().cancelTouchFocus(activity.getSquare());
				freeSquare.notifyCameraObservers();
				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				if (this.lastTouchDownActor != null) {
					if (activity.isSeparateSquareMode() && this.lastTouchDownActor instanceof Square2d) {
						activity.separateSquare((Square2d) this.lastTouchDownActor);
						return true;
					}
					return false;
				}
				final int beforeShowingUICount = activity.getViewableUICount();
				activity.showSquareOnly();
				final int afterShowingUICount = activity.getViewableUICount();
				if (beforeShowingUICount == afterShowingUICount) {
					Vector2 menuPosition = freeSquare.getStage().screenToStageCoordinates(new Vector2(x, y));
					activity.showMenu(menuPosition.x, menuPosition.y);
				}
				return true;
			}

			@Override
			public boolean longPress(float x, float y) {
				if (this.isLastTouchBackGround()) {
					Vector2 menuPosition = freeSquare.getStage().screenToStageCoordinates(new Vector2(x, y));
					activity.showMenu(menuPosition.x, menuPosition.y);
					MainActivityInputProcessor.this.isLastLongPressed = true;
					return true;
				}
				return false;
			}
		};
		return listener;

	}
}
