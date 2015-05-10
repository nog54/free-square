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

package org.nognog.freeSquare.square2d.object.types.life;

import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.object.LifeObject;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/05/07
 */
public class LifeObjectTypeManager {

	private static ExternalLifeObjectTypeDictionary externalLifeObjectTypes = new ExternalLifeObjectTypeDictionary();

	static {
		validateFamilyDuplication();
	}

	private static void validateFamilyDuplication() {
		Array<Family> families = new Array<>();
		for (LifeObjectType type : PreparedLifeObjectType.values()) {
			assert !families.contains(type.getFamily(), true);
			families.add(type.getFamily());
		}
	}

	/**
	 * @param name
	 * @param texturePath
	 * @param moveSpeed
	 * @param eatAmountPerSec
	 * @param squareObjectClass
	 * @return create new instance
	 */
	public static <T extends LifeObject> ExternalLifeObjectType createExternalLifeObjectType(String name, String texturePath, float moveSpeed, int eatAmountPerSec, Class<T> squareObjectClass) {
		final ExternalLifeObjectType newInstance = new ExternalLifeObjectType(name, texturePath, moveSpeed, eatAmountPerSec, squareObjectClass);
		externalLifeObjectTypes.addExternalObjectType(newInstance);
		return newInstance;
	}

	/**
	 * @param life
	 * @return bind type
	 */
	public static LifeObjectType getBindingLifeObjectType(Life life) {
		for (LifeObjectType type : LifeObjectTypeManager.getPreparedLifeObjectTypes()) {
			if (life.getFamily() == type.getFamily()) {
				return type;
			}
		}
		for (LifeObjectType type : LifeObjectTypeManager.getExternalLifeObjectTypes()) {
			if (life.getFamily().getName().equals(type.getFamily().getName())) {
				return type;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private static LifeObjectType[] getPreparedLifeObjectTypes() {
		return PreparedLifeObjectType.values();
	}

	/**
	 * @return
	 */
	private static LifeObjectType[] getExternalLifeObjectTypes() {
		return LifeObjectTypeManager.getExternalLifeObjectTypeDictionary().getAllExternalLifeObjectType();
	}

	/**
	 * @return all type
	 */
	public static LifeObjectType[] getAllTypes() {
		final LifeObjectType[] preparedTypes = PreparedLifeObjectType.values();
		final LifeObjectType[] externalTypes = externalLifeObjectTypes.getAllExternalLifeObjectType();
		final LifeObjectType[] result = new LifeObjectType[preparedTypes.length + externalTypes.length];
		int i = 0;
		for (LifeObjectType type : preparedTypes) {
			result[i] = type;
			i++;
		}
		for (LifeObjectType type : externalTypes) {
			result[i] = type;
			i++;
		}
		return result;
	}

	/**
	 * @return the externalLifeObjectTypes
	 */
	public static ExternalLifeObjectTypeDictionary getExternalLifeObjectTypeDictionary() {
		return externalLifeObjectTypes;
	}

	/**
	 * @param externalLifeObjectTypes
	 *            the externalLifeObjectTypes to set
	 */
	public static void setExternalLifeObjectTypeDictionary(ExternalLifeObjectTypeDictionary externalLifeObjectTypes) {
		LifeObjectTypeManager.externalLifeObjectTypes = externalLifeObjectTypes;
	}

	/**
	 * dispose prepares
	 */
	public static void dispose() {
		for (LifeObjectType type : PreparedLifeObjectType.values()) {
			type.getTexture().dispose();
		}
	}
}
