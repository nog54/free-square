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

package org.nognog.freeSquare.square2d.object.types;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/05/10
 * @param <T>
 *            type of external object type
 */
public abstract class ExternalSquare2dObjectTypeDictionary<T extends ExternalSquare2dObjectType<?>> {
	private Array<T> externalObjectTypes;

	/**
	 * 
	 */
	public ExternalSquare2dObjectTypeDictionary() {
		this.externalObjectTypes = new Array<>();
	}

	/**
	 * @param type
	 * @return true if added
	 */
	public boolean addExternalObjectType(T type) {
		if (this.isAlreadyExistsFamilyName(type.getName()) == false) {
			this.externalObjectTypes.add(type);
			return true;
		}
		return false;
	}

	/**
	 * @param type
	 * @return true if remove from dictionary
	 */
	public boolean removeExternalObjectType(T type) {
		return this.externalObjectTypes.removeValue(type, true);
	}

	/**
	 * @param familyName
	 * @return true if already exists name
	 */
	public boolean isAlreadyExistsFamilyName(String familyName) {
		for (T type : this.externalObjectTypes) {
			if (type.getName().equals(familyName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return all type of external LifeObject array (copy)
	 */
	public Array<T> getAllExternalObjectType() {
		return new Array<>(this.externalObjectTypes);
	}

	/**
	 * 
	 */
	public void clear() {
		this.externalObjectTypes.clear();
	}
}
