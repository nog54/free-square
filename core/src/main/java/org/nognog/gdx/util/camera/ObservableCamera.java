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

package org.nognog.gdx.util.camera;

import java.util.ArrayList;

/**
 * @author goshi 2015/06/09
 */
public abstract class ObservableCamera implements Camera {

	private final ArrayList<CameraObserver> cameraObservers = new ArrayList<>();

	/**
	 * notify all observers
	 */
	public void notifyCameraObservers() {
		for (CameraObserver observer : this.cameraObservers) {
			observer.updateCamera(this);
		}
	}

	/**
	 * @param observer
	 */
	public void addCameraObserver(CameraObserver observer) {
		if (!this.cameraObservers.contains(observer)) {
			this.cameraObservers.add(observer);
		}
	}

	/**
	 * @param observer
	 */
	public void removeCameraObserver(CameraObserver observer) {
		this.cameraObservers.remove(observer);
	}

}
