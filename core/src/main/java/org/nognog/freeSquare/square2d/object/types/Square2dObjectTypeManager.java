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

import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectType;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.eatable.PreparedEatableObjectType;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectType;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.life.PreparedLifeObjectType;
import org.nognog.freeSquare.square2d.object.types.other.OtherObjectType;
import org.nognog.freeSquare.square2d.object.types.other.OtherObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.other.PreparedOtherObjectType;

/**
 * @author goshi 2015/04/23
 */
public class Square2dObjectTypeManager {

	/**
	 * @return all prepared types
	 */
	public static Square2dObjectType<?>[] getAllPreparedTypes() {
		int allValuesLength = PreparedEatableObjectType.values().length + PreparedLifeObjectType.values().length + PreparedOtherObjectType.values().length;
		final Square2dObjectType<?>[] allValues = new Square2dObjectType[allValuesLength];
		int i = 0;
		for (LifeObjectType type : PreparedLifeObjectType.values()) {
			allValues[i] = type;
			i++;
		}
		for (EatableObjectType type : PreparedEatableObjectType.values()) {
			allValues[i] = type;
			i++;
		}
		for (OtherObjectType type : PreparedOtherObjectType.values()) {
			allValues[i] = type;
			i++;
		}
		return allValues;
	}

	/**
	 * @return all types
	 */
	public static Square2dObjectType<?>[] getAllTypes() {
		final EatableObjectType[] allEatableObjectTypes = EatableObjectTypeManager.getAll();
		final LifeObjectType[] allLifeObjectTypes = LifeObjectTypeManager.getAllTypes();
		final OtherObjectType[] allOtherObjectTypes = OtherObjectTypeManager.getAllTypes();
		int allValuesLength = allEatableObjectTypes.length + allLifeObjectTypes.length + allOtherObjectTypes.length;
		final Square2dObjectType<?>[] allValues = new Square2dObjectType[allValuesLength];
		int i = 0;
		for (LifeObjectType type : allLifeObjectTypes) {
			allValues[i] = type;
			i++;
		}
		for (EatableObjectType type : allEatableObjectTypes) {
			allValues[i] = type;
			i++;
		}
		for (OtherObjectType type : allOtherObjectTypes) {
			allValues[i] = type;
			i++;
		}
		return allValues;
	}

	/**
	 * dispose all type
	 */
	public static void disposeAll() {
		LifeObjectTypeManager.dispose();
		EatableObjectTypeManager.dispose();
		OtherObjectTypeManager.dispose();
	}
}
