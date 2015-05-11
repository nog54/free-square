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
import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectTypeManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/05/07
 */
public class LifeObjectTypeManager extends ExternalSquare2dObjectTypeManager<ExternalLifeObjectType, ExternalLifeObjectTypeDictionary> implements Square2dObjectTypeManager<LifeObjectType> {

	static {
		validatePreparedFamilyDuplication();
	}

	private static LifeObjectTypeManager instance = new LifeObjectTypeManager();

	private LifeObjectTypeManager() {
		this.setDictionary(new ExternalLifeObjectTypeDictionary());
	}

	/**
	 * @return singleton instance
	 */
	public static LifeObjectTypeManager getInstance() {
		return instance;
	}

	private static void validatePreparedFamilyDuplication() {
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
	public <T extends LifeObject> ExternalLifeObjectType createAndRegisterExternalLifeObjectType(String name, String texturePath, float moveSpeed, int eatAmountPerSec, Class<T> squareObjectClass) {
		final ExternalLifeObjectType newType = new ExternalLifeObjectType(name, texturePath, moveSpeed, eatAmountPerSec, squareObjectClass);
		this.register(newType);
		return newType;
	}

	/**
	 * @param life
	 * @return bind type
	 */
	public LifeObjectType getBindingLifeObjectType(Life life) {
		for (LifeObjectType type : getPreparedLifeObjectTypes()) {
			if (life.getFamily() == type.getFamily()) {
				return type;
			}
		}
		for (LifeObjectType type : this.getAllExternalTypes()) {
			if (life.getFamily().getName().equals(type.getFamily().getName())) {
				return type;
			}
		}
		return null;
	}

	/**
	 * @return prepared types
	 */
	public static LifeObjectType[] getPreparedLifeObjectTypes() {
		return PreparedLifeObjectType.values();
	}

	/**
	 * @return all type
	 */
	@Override
	public LifeObjectType[] getAllTypes() {
		final LifeObjectType[] preparedTypes = PreparedLifeObjectType.values();
		final LifeObjectType[] externalTypes = this.getAllExternalTypes().toArray(LifeObjectType.class);
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
	 * dispose prepares
	 */
	@Override
	public void dispose() {
		for (LifeObjectType type : this.getAllTypes()) {
			final Texture texture = type.getTexture();
			if (texture != null) {
				texture.dispose();
			}
		}
	}
}
