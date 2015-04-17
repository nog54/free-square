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

package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.model.square.SquareEventListener;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/25
 */
public class Square2dEvent implements SquareEvent {

	private final SquareEventListener targetObserver;
	private Array<SquareEventListener> exceptObservers;

	/**
	 * 
	 */
	public Square2dEvent() {
		this(null);
	}

	/**
	 * @param targetObserver
	 */
	public Square2dEvent(SquareEventListener targetObserver) {
		this.targetObserver = targetObserver;
		this.exceptObservers = new Array<>();
	}

	/**
	 * @return target observer
	 */
	public SquareEventListener getTargetObserver() {
		return this.targetObserver;
	}

	/**
	 * @param observer
	 */
	public void addExceptObserver(SquareEventListener observer) {
		if (this.targetObserver != null) {
			throw new RuntimeException("target observer should be null when addExceptObserver is called."); //$NON-NLS-1$
		}
		if (!this.exceptObservers.contains(observer, true)) {
			this.exceptObservers.add(observer);
		}
	}

	/**
	 * @return except observers.
	 */
	public Array<SquareEventListener> getExceptObservers() {
		return this.exceptObservers;
	}
}