
package org.nognog.gdx.util.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;

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

/**
 * Adapter class of OrthographicCamera
 * 
 * @author goshi 2015/06/09
 */
public class ObservableOrthographicCamera extends ObservableCamera {
	private final OrthographicCamera baseCamera;

	/**
	 * @param baseCamera
	 * 
	 */
	public ObservableOrthographicCamera(OrthographicCamera baseCamera) {
		this.baseCamera = baseCamera;
	}

	@Override
	public void move(float x, float y) {
		this.baseCamera.position.x += x;
		this.baseCamera.position.y += y;
	}

	/**
	 * move camera and notify if x != 0 or y != 0
	 * 
	 * @param x
	 * @param y
	 */
	public void moveAndNotifyObservers(float x, float y) {
		this.move(x, y);
		if (x != 0 || y != 0) {
			this.notifyCameraObservers();
		}
	}

	@Override
	public void zoom(float x) {
		this.baseCamera.zoom += x;
	}

	/**
	 * zoom camera and notify if x != 0
	 * 
	 * @param x
	 */
	public void zoomAndNotifyObservers(float x) {
		this.zoom(x);
		if (x != 0) {
			this.notifyCameraObservers();
		}
	}

	@Override
	public float getViewportWidth() {
		return this.baseCamera.viewportWidth;
	}

	@Override
	public float getViewportHeight() {
		return this.baseCamera.viewportHeight;
	}

	@Override
	public float getX() {
		return this.baseCamera.position.x;
	}

	@Override
	public float getY() {
		return this.baseCamera.position.y;
	}

	@Override
	public float getZoom() {
		return this.baseCamera.zoom;
	}

	@Override
	public void setX(float x) {
		this.baseCamera.position.x = x;
	}

	/**
	 * @param x
	 */
	public void setXAndNotify(float x) {
		if (this.getX() == x) {
			return;
		}
		this.setX(x);
		this.notifyCameraObservers();
	}

	@Override
	public void setY(float y) {
		this.baseCamera.position.y = y;
	}

	/**
	 * @param y
	 */
	public void setYAndNotify(float y) {
		if (this.getY() == y) {
			return;
		}
		this.setY(y);
		this.notifyCameraObservers();
	}

	@Override
	public void setZoom(float zoom) {
		this.baseCamera.zoom = zoom;
	}

	/**
	 * @param zoom
	 */
	public void setZoomAndNotify(float zoom) {
		if (this.getZoom() == zoom) {
			return;
		}
		this.setZoom(zoom);
		this.notifyCameraObservers();
	}

	@Override
	public void setPosition(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setPositionAndNotify(float x, float y) {
		if (this.getX() == x && this.getY() == y) {
			return;
		}
		this.setPosition(x, y);
		this.notifyCameraObservers();
	}

}
