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

package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

/**
 * @author goshi 2015/02/02
 */
public class AddObjectEvent extends Square2dEvent {
	private final Square2dObject addedObject;

	/**
	 * 
	 */
	public AddObjectEvent() {
		this(null, null);
	}

	/**
	 * @param addedObject
	 */
	public AddObjectEvent(Square2dObject addedObject) {
		this(addedObject, null);
	}

	/**
	 * @param target
	 */
	public AddObjectEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param addedObject
	 * @param target
	 */
	public AddObjectEvent(Square2dObject addedObject, SquareEventListener target) {
		super(target);
		this.addedObject = addedObject;
	}

	/**
	 * @return the addedObject
	 */
	public Square2dObject getAddedObject() {
		return this.addedObject;
	}

}
