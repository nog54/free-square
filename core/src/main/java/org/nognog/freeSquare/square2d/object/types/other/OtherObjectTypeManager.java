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

package org.nognog.freeSquare.square2d.object.types.other;

/**
 * @author goshi 2015/05/07
 */
public class OtherObjectTypeManager {

	private static ExternalOtherObjectTypeDictionary externalOtherObjectTypes = new ExternalOtherObjectTypeDictionary();

	/**
	 * @param name
	 * @param texturePath
	 * @return created new instance
	 */
	public static ExternalOtherObjectType createExternalOtherObjectType(String name, String texturePath) {
		final ExternalOtherObjectType newInstance = new ExternalOtherObjectType(name, texturePath);
		externalOtherObjectTypes.addExternalObjectType(newInstance);
		return newInstance;
	}

	/**
	 * @return all type
	 */
	public static OtherObjectType[] getAllTypes() {
		final OtherObjectType[] preparedTypes = PreparedOtherObjectType.values();
		final OtherObjectType[] externalTypes = externalOtherObjectTypes.getAllExternalOtherObjectType();
		final OtherObjectType[] result = new OtherObjectType[preparedTypes.length + externalTypes.length];
		int i = 0;
		for (OtherObjectType type : preparedTypes) {
			result[i] = type;
			i++;
		}
		for (OtherObjectType type : externalTypes) {
			result[i] = type;
			i++;
		}
		return result;
	}

	/**
	 * @return the externalOtherObjectTypes
	 */
	public static ExternalOtherObjectTypeDictionary getExternalOtherObjectTypeDictionary() {
		return externalOtherObjectTypes;
	}

	/**
	 * @param externalOtherObjectTypes
	 *            the externalOtherObjectTypes to set
	 */
	public static void setExternalOtherObjectTypeDictionary(ExternalOtherObjectTypeDictionary externalOtherObjectTypes) {
		OtherObjectTypeManager.externalOtherObjectTypes = externalOtherObjectTypes;
	}

	/**
	 * dispose
	 */
	public static void dispose() {
		for (OtherObjectType type : PreparedOtherObjectType.values()) {
			type.getTexture().dispose();
		}
	}
}
