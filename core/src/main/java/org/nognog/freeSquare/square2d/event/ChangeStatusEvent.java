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

import org.nognog.freeSquare.model.life.status.influence.StatusInfluence;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

/**
 * @author goshi 2015/02/02
 */
public class ChangeStatusEvent extends Square2dEvent {
	private final LifeObject changedObject;
	private final StatusInfluence<?> influence;

	/**
	 * @param changedObject
	 * @param influence 
	 * @param createScaledInfluence
	 */
	public ChangeStatusEvent(LifeObject changedObject, StatusInfluence<?> influence) {
		this.changedObject = changedObject;
		this.influence = influence;
	}

	/**
	 * @return the object
	 */
	public LifeObject getChangedObject() {
		return this.changedObject;
	}

	/**
	 * @return influence
	 */
	public StatusInfluence<?> getInfluences() {
		return this.influence;
	}
}
