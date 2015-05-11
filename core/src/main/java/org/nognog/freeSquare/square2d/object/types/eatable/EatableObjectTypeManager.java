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

package org.nognog.freeSquare.square2d.object.types.eatable;

import org.nognog.freeSquare.square2d.object.types.Square2dObjectTypeManager;

/**
 * @author goshi 2015/05/07
 */
public class EatableObjectTypeManager implements Square2dObjectTypeManager<EatableObjectType> {

	private static final EatableObjectTypeManager instance = new EatableObjectTypeManager();

	private EatableObjectTypeManager() {
	}

	/**
	 * @return singleton instance
	 */
	public static EatableObjectTypeManager getInstance() {
		return instance;
	}

	/**
	 * dispose texture data
	 */
	@Override
	public void dispose() {
		for (EatableObjectType type : PreparedEatableObjectType.values()) {
			type.getTexture().dispose();
		}
	}

	/**
	 * @return all type
	 */
	@Override
	public EatableObjectType[] getAllTypes() {
		return PreparedEatableObjectType.values();
	}
}