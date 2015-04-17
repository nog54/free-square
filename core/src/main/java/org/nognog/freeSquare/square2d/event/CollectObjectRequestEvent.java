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
public class CollectObjectRequestEvent extends Square2dEvent {
	private final Square2dObject collectRequestedObject;

	/**
	 * 
	 */
	public CollectObjectRequestEvent() {
		this(null, null);
	}

	/**
	 * @param collectRequestedObject
	 */
	public CollectObjectRequestEvent(Square2dObject collectRequestedObject) {
		this(collectRequestedObject, null);
	}

	/**
	 * @param target
	 */
	public CollectObjectRequestEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param collectRequestedObject
	 * @param target
	 */
	public CollectObjectRequestEvent(Square2dObject collectRequestedObject, SquareEventListener target) {
		super(target);
		this.collectRequestedObject = collectRequestedObject;
	}

	/**
	 * @return the collectRequestedObject
	 */
	public Square2dObject getCollectRequestedObject() {
		return this.collectRequestedObject;
	}

}
