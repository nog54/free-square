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

package org.nognog.freeSquare.square2d.object;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/06/02
 */
@SuppressWarnings("rawtypes")
public class Square2dObjectSerializer implements Json.Serializer<Square2dObject> {

	private static final Square2dObjectSerializer instance = new Square2dObjectSerializer();

	private Square2dObjectSerializer() {
	}

	/**
	 * @return instance
	 */
	public static Square2dObjectSerializer getInstance() {
		return instance;
	}

	@Override
	public void write(Json json, Square2dObject object, Class knownType) {
		json.writeObjectStart();
		json.writeType(object.getClass());
		json.writeField(object, "type"); //$NON-NLS-1$
		json.writeValue("positionX", Float.valueOf(object.getX())); //$NON-NLS-1$
		json.writeValue("positionY", Float.valueOf(object.getY())); //$NON-NLS-1$
		json.writeObjectEnd();
	}

	@Override
	public Square2dObject read(Json json, JsonValue jsonData, Class type) {
		final Square2dObjectType<?> readType = json.readValue("type", Square2dObjectType.class, jsonData); //$NON-NLS-1$
		final Square2dObject result = new Square2dObject(readType);
		result.setX(json.readValue("positionX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		result.setY(json.readValue("positionY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		return result;
	}

}
