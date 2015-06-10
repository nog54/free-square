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

package org.nognog.freeSquare.activity.main;

import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.gdx.util.camera.Camera;
import org.nognog.gdx.util.camera.ObservableOrthographicCamera;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/01/17
 */
public class MainActivityInputProcessor extends InputMultiplexer {

	private final GestureDetector gestureDetector;
	private final InputAdapter longTouchTapCanceller;
	private boolean isEnable = false;

	protected boolean isLastLongPressed;

	/**
	 * @param activity
	 */
	public MainActivityInputProcessor(MainActivity activity) {
		this.gestureDetector = new GestureDetector(this.createFreeSquareGestureListener(activity));
		this.gestureDetector.setLongPressSeconds(0.2f);
		this.longTouchTapCanceller = new InputAdapter() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (MainActivityInputProcessor.this.isLastLongPressed) {
					return true;
				}
				return false;
			}
		};
		this.enable();
	}

	/**
	 * enable this processor
	 */
	public void enable() {
		if (this.isEnable()) {
			return;
		}
		this.addProcessor(this.gestureDetector);
		this.addProcessor(this.longTouchTapCanceller);
		this.isEnable = true;
	}

	/**
	 * disable this processor
	 */
	public void disable() {
		if (!this.isEnable()) {
			return;
		}
		this.removeProcessor(this.gestureDetector);
		this.removeProcessor(this.longTouchTapCanceller);
		this.isEnable = false;
	}

	/**
	 * @return true if this is enable
	 */
	public boolean isEnable() {
		return this.isEnable;
	}

	private GestureListener createFreeSquareGestureListener(final MainActivity activity) {
		final GestureListener listener = new GestureDetector.GestureAdapter() {

			private float initialScale = 1;
			private Actor lastTouchDownActor;

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				activity.setCameraVelocityX(0);
				activity.setCameraVelocityY(0);
				Vector2 worldPosition = activity.getStage().getViewport().unproject(new Vector2(x, y));
				this.lastTouchDownActor = activity.getStage().hit(worldPosition.x, worldPosition.y, true);
				Camera camera = activity.getCamera();
				this.initialScale = camera.getZoom();
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
				final ObservableOrthographicCamera camera = activity.getCamera();
				final float currentZoom = camera.getZoom();
				final float cameraMoveX = -deltaX * currentZoom;
				final float cameraMoveY = deltaY * currentZoom;
				camera.move(cameraMoveX, cameraMoveY);
				activity.adjustCameraZoomAndPositionIfRangeOver(false);
				camera.notifyCameraObservers();
				activity.getStage().cancelTouchFocus(activity.getSquare());
				return true;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				if (!this.isLastTouchBackGround()) {
					return false;
				}
				final float ratio = initialDistance / distance;
				final float nextZoom = this.initialScale * ratio;
				final ObservableOrthographicCamera camera = activity.getCamera();
				camera.setZoom(nextZoom);
				activity.adjustCameraZoomAndPositionIfRangeOver(false);
				activity.getStage().cancelTouchFocus(activity.getSquare());
				camera.notifyCameraObservers();
				return true;
			}

			@Override
			public boolean fling(float velocityX, float velocityY, int button) {
				if (activity.getSquare() == null) {
					return false;
				}
				if (!this.isLastTouchBackGround()) {
					return false;
				}
				activity.setCameraVelocityX(-velocityX);
				activity.setCameraVelocityY(velocityY);
				return false;
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
					Vector2 menuPosition = activity.getStage().screenToStageCoordinates(new Vector2(x, y));
					activity.showMenu(menuPosition.x, menuPosition.y);
				}
				return true;
			}

			@Override
			public boolean longPress(float x, float y) {
				if (this.isLastTouchBackGround()) {
					Vector2 menuPosition = activity.getStage().screenToStageCoordinates(new Vector2(x, y));
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
