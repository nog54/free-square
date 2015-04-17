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

import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public interface Square2dObjectType<T extends Square2dObject> {

	String getName();

	Texture getTexture();

	float getLogicalWidth();

	Color getColor();

	T create();

	public static class Manager {
		public static Square2dObjectType<?>[] getAllTypeValues() {
			int allValuesLength = EatableObjectType.values().length + LifeObjectType.values().length + OtherObjectType.values().length;
			final Square2dObjectType<?>[] allValues = new Square2dObjectType[allValuesLength];
			int i = 0;
			for (LifeObjectType type : LifeObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			for (EatableObjectType type : EatableObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			for (OtherObjectType type : OtherObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			return allValues;

		}

		public static void dispose() {
			LifeObjectType.dispose();
			EatableObjectType.dispose();
			OtherObjectType.dispose();
		}
	}

}
