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

import org.nognog.freeSquare.square2d.CombinePoint.CombinedVertex;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/02/15
 */
public class CombinedVertexSerializer implements Json.Serializer<CombinedVertex> {

	private static final CombinedVertexSerializer instance = new CombinedVertexSerializer();

	/**
	 * @return instance
	 */
	public static CombinedVertexSerializer getInstance() {
		return instance;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void write(Json json, CombinedVertex object, Class knownType) {
		json.writeObjectStart();
		json.writeFields(object);
		json.writeObjectEnd();
	}

	@Override
	@SuppressWarnings({ "rawtypes", })
	public CombinedVertex read(Json json, JsonValue jsonData, Class type) {
		final Square2d square = json.readValue("square", Square2d.class, jsonData); //$NON-NLS-1$
		final Vertex saveVertex = json.readValue("vertex", Vertex.class, jsonData); //$NON-NLS-1$
		for (Vertex vertex : square.getVertices()) {
			if (vertex.equals(saveVertex)) {
				return new CombinedVertex(square, vertex);
			}
		}
		return null;
	}

	private CombinedVertexSerializer() {
	}

}
