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

import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

/**
 * @author goshi 2015/06/26
 */
public class EatEvent extends Square2dEvent {
	private final LifeObject eater;
	private final EatableObject eatenObject;
	private final int eatAmount;

	/**
	 * @param eater
	 * @param eatenObject
	 * @param eatAmount 
	 */
	public EatEvent(LifeObject eater, EatableObject eatenObject, int eatAmount) {
		this.eater = eater;
		this.eatenObject = eatenObject;
		this.eatAmount = eatAmount;
	}

	/**
	 * @return the eater
	 */
	public LifeObject getEater() {
		return this.eater;
	}

	/**
	 * @return the eatenObject
	 */
	public EatableObject getEatenObject() {
		return this.eatenObject;
	}

	/**
	 * @return the eatAmount
	 */
	public int getEatAmount() {
		return this.eatAmount;
	}

}
