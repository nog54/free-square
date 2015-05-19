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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.nognog.freeSquare.persist.PersistItemClass;
import org.nognog.freeSquare.square2d.object.types.other.DictionaryObserver;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/05/10
 * @param <T>
 *            type of external object type
 */
public abstract class ExternalSquare2dObjectTypeDictionary<T extends ExternalSquare2dObjectType<?>> implements PersistItemClass, Json.Serializable {
	private Array<T> externalObjectTypes;
	private transient Array<DictionaryObserver> observers;

	/**
	 * 
	 */
	public ExternalSquare2dObjectTypeDictionary() {
		this.externalObjectTypes = new Array<>();
		this.observers = new Array<>();
	}

	/**
	 * @param type
	 * @return true if added
	 */
	public boolean addExternalObjectType(T type) {
		if (this.isAlreadyExistsName(type.getName()) == false) {
			this.externalObjectTypes.add(type);
			this.notifyObservers();
			return true;
		}
		return false;
	}

	/**
	 * @param type
	 * @return true if remove from dictionary
	 */
	public boolean removeExternalObjectType(T type) {
		final boolean isRemoved = this.externalObjectTypes.removeValue(type, true);
		if (isRemoved) {
			this.notifyObservers();
		}
		return isRemoved;
	}

	/**
	 * @param name
	 * @return true if already exists name
	 */
	public boolean isAlreadyExistsName(String name) {
		for (T type : this.externalObjectTypes) {
			if (type.getName().equals(name)) {
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
		this.notifyObservers();
	}

	private void notifyObservers() {
		for (DictionaryObserver observer : this.observers) {
			observer.updateDictionary();
		}
	}

	/**
	 * @param observer
	 */
	public void addDictionaryObserver(DictionaryObserver observer) {
		if (this.observers.contains(observer, true) == false) {
			this.observers.add(observer);
		}
	}

	/**
	 * @param observer
	 * @return true if removed
	 */
	public boolean removeDictionaryObserver(DictionaryObserver observer) {
		return this.observers.removeValue(observer, true);
	}

	/**
	 * fix me
	 */
	public abstract void fixDictionaryToSavableState();

	@Override
	public void write(Json json) {
		if (!this.isValid()) {
			this.fixDictionaryToSavableState();
		}
		for (Field field : ExternalSquare2dObjectTypeDictionary.class.getDeclaredFields()) {
			final int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
				continue;
			}
			json.writeField(this, field.getName());
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		for (Field field : ExternalSquare2dObjectTypeDictionary.class.getDeclaredFields()) {
			final int modifiers = field.getModifiers();
			if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers)) {
				continue;
			}
			json.readField(this, field.getName(), jsonData);
		}
	}
}
