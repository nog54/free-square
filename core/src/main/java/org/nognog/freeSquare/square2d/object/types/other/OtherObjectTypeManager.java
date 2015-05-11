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

import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectTypeManager;

/**
 * @author goshi 2015/05/07
 */
public class OtherObjectTypeManager implements Square2dObjectTypeManager<OtherObjectType>, ExternalSquare2dObjectTypeManager<ExternalOtherObjectType> {

	private static final OtherObjectTypeManager instance = new OtherObjectTypeManager();

	private ExternalOtherObjectTypeDictionary externalOtherObjectTypes = new ExternalOtherObjectTypeDictionary();

	private OtherObjectTypeManager() {
	}

	/**
	 * @return singleton instance
	 */
	public static OtherObjectTypeManager getInstance() {
		return instance;
	}

	/**
	 * @param name
	 * @param texturePath
	 * @return created new instance
	 */
	public ExternalOtherObjectType createExternalOtherObjectType(String name, String texturePath) {
		final ExternalOtherObjectType newInstance = new ExternalOtherObjectType(name, texturePath);
		this.externalOtherObjectTypes.addExternalObjectType(newInstance);
		return newInstance;
	}

	/**
	 * @return all type
	 */
	@Override
	public OtherObjectType[] getAllTypes() {
		final OtherObjectType[] preparedTypes = PreparedOtherObjectType.values();
		final OtherObjectType[] externalTypes = this.getAllExternalTypes();
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
	public ExternalOtherObjectTypeDictionary getExternalOtherObjectTypeDictionary() {
		return this.externalOtherObjectTypes;
	}

	/**
	 * @param externalOtherObjectTypes
	 *            the externalOtherObjectTypes to set
	 */
	public void setExternalOtherObjectTypeDictionary(ExternalOtherObjectTypeDictionary externalOtherObjectTypes) {
		this.externalOtherObjectTypes = externalOtherObjectTypes;
	}

	/**
	 * dispose
	 */
	@Override
	public void dispose() {
		for (OtherObjectType type : PreparedOtherObjectType.values()) {
			type.getTexture().dispose();
		}
	}

	@Override
	public ExternalOtherObjectType[] getAllExternalTypes() {
		return this.externalOtherObjectTypes.getAllExternalOtherObjectType();
	}
}
