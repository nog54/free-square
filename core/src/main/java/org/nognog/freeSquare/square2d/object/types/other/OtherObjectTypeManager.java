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

import org.nognog.freeSquare.square2d.object.Square2dObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectTypeManager;

/**
 * @author goshi 2015/05/07
 */
public class OtherObjectTypeManager extends ExternalSquare2dObjectTypeManager<ExternalOtherObjectType, ExternalOtherObjectTypeDictionary> implements Square2dObjectTypeManager<OtherObjectType> {

	private static final OtherObjectTypeManager instance = new OtherObjectTypeManager();

	private OtherObjectTypeManager() {
		this.setDictionary(new ExternalOtherObjectTypeDictionary());
	}

	/**
	 * @return singleton instance
	 */
	public static OtherObjectTypeManager getInstance() {
		return instance;
	}
	

	/**
	 * @param texturePath
	 * @return created new instance
	 */
	public ExternalOtherObjectType createExternalOtherObjectType(String texturePath) {
		return this.createExternalOtherObjectType(this.generateName(), texturePath);
	}
	
	/**
	 * @param name 
	 * @param texturePath
	 * @return created new instance
	 */
	@SuppressWarnings("static-method")
	public ExternalOtherObjectType createExternalOtherObjectType(String name, String texturePath) {
		return new ExternalOtherObjectType(name, texturePath);
	}

	/**
	 * @return all type
	 */
	@Override
	public OtherObjectType[] getAllTypes() {
		final OtherObjectType[] preparedTypes = PreparedOtherObjectType.values();
		final OtherObjectType[] externalTypes = this.getAllExternalTypes().toArray(OtherObjectType.class);
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
	 * dispose
	 */
	@Override
	public void dispose() {
		for (OtherObjectType type : PreparedOtherObjectType.values()) {
			type.getTexture().dispose();
		}
	}

	@Override
	public boolean isRegisterable(ExternalOtherObjectType type) {
		return this.getDictionary().isAlreadyExistsName(type.getName()) == false;
	}
}
