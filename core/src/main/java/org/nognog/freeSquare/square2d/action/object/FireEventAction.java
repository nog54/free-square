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

package org.nognog.freeSquare.square2d.action.object;

import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/29
 */
public class FireEventAction extends Action {

	private Square2dEvent event;
	private Square2dObject eventListener;

	/**
	 * 
	 */
	public FireEventAction() {

	}

	/**
	 * @param event
	 */
	public FireEventAction(Square2dEvent event) {
		this.event = event;
	}

	/**
	 * @param eventListener
	 */
	public void setEventListener(Square2dObject eventListener) {
		this.eventListener = eventListener;
	}

	/**
	 * @return eventListener
	 */
	public Square2dObject getEventListener() {
		return this.eventListener;
	}

	/**
	 * @param event
	 */
	public void setEvent(Square2dEvent event) {
		this.event = event;
	}

	/**
	 * @return be fired event
	 */
	public Square2dEvent getEvent() {
		return this.event;
	}

	@Override
	public boolean act(float delta) {
		this.eventListener.notify(this.event);
		return true;
	}

}
