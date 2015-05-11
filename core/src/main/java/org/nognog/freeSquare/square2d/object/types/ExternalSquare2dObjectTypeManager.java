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
 * @author goshi 2015/05/11
 * @param <T1>
 *            external type
 * @param <T2>
 *            external type dictionary
 */
public abstract class ExternalSquare2dObjectTypeManager<T1 extends ExternalSquare2dObjectType<?>, T2 extends ExternalSquare2dObjectTypeDictionary<T1>> {

	private T2 dictionary;
	
	/**
	 * @return all external type
	 */
	public Array<T1> getAllExternalTypes() {
		return this.dictionary.getAllExternalObjectType();
	}

	/**
	 * @param type
	 */
	public void register(T1 type) {
		this.dictionary.addExternalObjectType(type);
	}

	/**
	 * @param type
	 */
	public void deregister(T1 type) {
		this.dictionary.removeExternalObjectType(type);
	}

	/**
	 * @return dictionary
	 */
	public T2 getDictionary() {
		return this.dictionary;
	}

	/**
	 * @param dictionary
	 */
	public void setDictionary(T2 dictionary) {
		this.dictionary = dictionary;
	}
}
