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

/**
 * @author goshi 2015/04/23
 */
public class Square2dObjectTypeManager {

	/**
	 * @return all type
	 */
	public static Square2dObjectType<?>[] getAllPreparedTypeValues() {
		int allValuesLength = EatableObjectType.Prepared.values().length + LifeObjectType.Prepared.values().length + OtherObjectType.Prepared.values().length;
		final Square2dObjectType<?>[] allValues = new Square2dObjectType[allValuesLength];
		int i = 0;
		for (LifeObjectType type : LifeObjectType.Prepared.values()) {
			allValues[i] = type;
			i++;
		}
		for (EatableObjectType type : EatableObjectType.Prepared.values()) {
			allValues[i] = type;
			i++;
		}
		for (OtherObjectType type : OtherObjectType.Prepared.values()) {
			allValues[i] = type;
			i++;
		}
		return allValues;
	}

	/**
	 * dispose all type
	 */
	public static void disposeAll() {
		LifeObjectType.Manager.dispose();
		EatableObjectType.Manager.dispose();
		OtherObjectType.Manager.dispose();
	}
}
