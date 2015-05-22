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

package org.nognog.freeSquare.util.square2d;

import org.nognog.freeSquare.square2d.object.Square2dObjectType;
import org.nognog.freeSquare.square2d.object.Square2dObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.other.OtherObjectTypeManager;

/**
 * @author goshi 2015/04/23
 */
public class AllSquare2dObjectTypeManager {

	private static Square2dObjectTypeManager<?>[] managers = { LifeObjectTypeManager.getInstance(), EatableObjectTypeManager.getInstance(), OtherObjectTypeManager.getInstance() };

	private static ExternalSquare2dObjectTypeManager<?, ?>[] externalTypeManagers = { LifeObjectTypeManager.getInstance(), OtherObjectTypeManager.getInstance() };

	/**
	 * @return all type
	 */
	public static Square2dObjectType<?>[] getAllTypes() {
		int size = 0;
		for (Square2dObjectTypeManager<?> manager : managers) {
			size += manager.getAllTypes().length;
		}
		final Square2dObjectType<?>[] result = new Square2dObjectType[size];
		int i = 0;
		for (Square2dObjectTypeManager<?> manager : managers) {
			for (Square2dObjectType<?> type : manager.getAllTypes()) {
				result[i] = type;
				i++;
			}
		}
		return result;
	}

	/**
	 * @return all external types
	 */
	public static Square2dObjectType<?>[] getAllExternalTypes() {
		int size = 0;
		for (ExternalSquare2dObjectTypeManager<?, ?> manager : externalTypeManagers) {
			size += manager.getAllExternalTypes().size;
		}
		final Square2dObjectType<?>[] result = new Square2dObjectType[size];
		int i = 0;
		for (ExternalSquare2dObjectTypeManager<?, ?> manager : externalTypeManagers) {
			for (Square2dObjectType<?> type : manager.getAllExternalTypes()) {
				result[i] = type;
				i++;
			}
		}
		return result;
	}

	/**
	 * @return all prepared types
	 */
	public static Square2dObjectType<?>[] getAllPreparedTypes() {
		final Square2dObjectType<?>[] allTypes = getAllTypes();
		final Square2dObjectType<?>[] allExternalTypes = getAllExternalTypes();
		final Square2dObjectType<?>[] result = new Square2dObjectType[allTypes.length - allExternalTypes.length];
		int i = 0;
		for (Square2dObjectType<?> type : allTypes) {
			if (!contains(type, allExternalTypes)) {
				result[i] = type;
				i++;
			}
		}
		return result;
	}

	/**
	 * @param object
	 * @param searchObjects
	 * @return
	 */
	private static boolean contains(Object object, Object[] searchObjects) {
		for (Object searchObject : searchObjects) {
			if (object == searchObject) {
				return true;
			}
		}
		return false;
	}

	/**
	 * dispose all type
	 */
	public static void disposeAll() {
		for (Square2dObjectTypeManager<?> manager : managers) {
			manager.dispose();
		}
	}
}
