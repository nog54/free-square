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
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dEvent;

/**
 * @author goshi 2015/02/02
 */
public class UpdateSquareEvent extends Square2dEvent {
	private final Square2d updatedSquare;

	/**
	 * 
	 */
	public UpdateSquareEvent() {
		this(null, null);
	}

	/**
	 * @param updatedSquare
	 */
	public UpdateSquareEvent(Square2d updatedSquare) {
		this(updatedSquare, null);
	}

	/**
	 * @param target
	 */
	public UpdateSquareEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param updatedSquare
	 * @param target
	 */
	public UpdateSquareEvent(Square2d updatedSquare, SquareEventListener target) {
		super(target);
		this.updatedSquare = updatedSquare;
	}

	/**
	 * @return the updatedObject
	 */
	public Square2d getUpdatedSquare() {
		return this.updatedSquare;
	}

}
