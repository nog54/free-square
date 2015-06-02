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

package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Immutable vector
 * 
 * @author goshi 2014/12/17
 */
public class VertexSerializer implements Json.Serializer<Vertex> {

	private static final VertexSerializer instance = new VertexSerializer();

	/**
	 * 
	 */
	private VertexSerializer() {
	}

	/**
	 * @return instance
	 */
	public static VertexSerializer getInstance() {
		return instance;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void write(Json json, Vertex object, Class knownType) {
		json.writeObjectStart();
		json.writeFields(object);
		json.writeObjectEnd();
	}

	@Override
	@SuppressWarnings({ "boxing", "rawtypes" })
	public Vertex read(Json json, JsonValue jsonData, Class type) {
		final float x = json.readValue("x", Float.class, jsonData); //$NON-NLS-1$
		final float y = json.readValue("y", Float.class, jsonData); //$NON-NLS-1$
		return new Vertex(x, y);
	}
}