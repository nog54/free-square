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

package org.nognog.freeSquare.activity;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.util.graphic2d.camera.CameraObserver;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * @author goshi 2015/04/14
 */
public abstract class FreeSquareActivity extends Group implements CameraObserver, SquareEventListener {
	private final FreeSquare freeSquare;

	/**
	 * @param freeSquare
	 */
	public FreeSquareActivity(FreeSquare freeSquare) {
		if (freeSquare == null) {
			throw new IllegalArgumentException();
		}
		this.freeSquare = freeSquare;
	}

	/**
	 * @return freeSquare
	 */
	public FreeSquare getFreeSquare() {
		return this.freeSquare;
	}

	/**
	 * @return activity input processor
	 */
	public abstract InputProcessor getInputProcesser();

	/**
	 * Be called when activity end
	 */
	public abstract void resume();

	/**
	 * Be called when activity end
	 */
	public abstract void pause();
}
