
package org.nognog.gdx.util.camera;

import org.nognog.gdx.util.Movable;

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
public class ObservableOrthographicCamera extends ObservableCamera implements Movable {
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
		this.move(x, y, true);
	}

	@Override
	public void moveX(float x) {
		this.move(x, 0, true);
	}

	@Override
	public void moveY(float y) {
		this.move(0, y, true);
	}

	/**
	 * move camera and notify if x != 0 or y != 0
	 * 
	 * @param x
	 * @param y
	 * @param notifyObservers
	 */
	public void move(float x, float y, boolean notifyObservers) {
		this.baseCamera.position.x += x;
		this.baseCamera.position.y += y;
		if (notifyObservers && (x != 0 || y != 0)) {
			this.notifyCameraObservers();
		}
	}

	@Override
	public void zoom(float x) {
		this.zoom(x, true);
	}

	/**
	 * zoom camera and notify if x != 0
	 * 
	 * @param x
	 * @param notifyObservers
	 */
	public void zoom(float x, boolean notifyObservers) {
		this.baseCamera.zoom += x;
		if (notifyObservers && x != 0) {
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
		this.setX(x, true);
	}

	/**
	 * @param x
	 * @param notifyObservers
	 */
	public void setX(float x, boolean notifyObservers) {
		if (this.getX() == x) {
			return;
		}
		this.baseCamera.position.x = x;
		if (notifyObservers) {
			this.notifyCameraObservers();
		}
	}

	@Override
	public void setY(float y) {
		this.setY(y, true);
	}

	/**
	 * @param y
	 * @param notifyObservers
	 */
	public void setY(float y, boolean notifyObservers) {
		if (this.getY() == y) {
			return;
		}
		this.baseCamera.position.y = y;
		if (notifyObservers) {
			this.notifyCameraObservers();
		}
	}

	@Override
	public void setZoom(float zoom) {
		this.setZoom(zoom, true);
	}

	/**
	 * @param zoom
	 * @param notifyObservers
	 */
	public void setZoom(float zoom, boolean notifyObservers) {
		if (this.getZoom() == zoom) {
			return;
		}
		this.baseCamera.zoom = zoom;
		this.notifyCameraObservers();
	}

	@Override
	public void setPosition(float x, float y) {
		this.setPosition(x, y, true);
	}

	/**
	 * @param x
	 * @param y
	 * @param notifyObservers
	 */
	public void setPosition(float x, float y, boolean notifyObservers) {
		if (this.getX() == x && this.getY() == y) {
			return;
		}
		this.baseCamera.position.x = x;
		this.baseCamera.position.y = y;
		this.notifyCameraObservers();
	}

}
