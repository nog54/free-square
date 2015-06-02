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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/02/15
 */
public class CombinePointSerializer implements Json.Serializer<CombinePoint> {

	private static final CombinePointSerializer instance = new CombinePointSerializer();
	
	/**
	 * @return instance
	 */
	public static CombinePointSerializer getInstance(){
		return instance;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes" })
	public void write(Json json, CombinePoint object, Class knownType) {
		json.writeObjectStart();
		json.writeFields(object);
		json.writeObjectEnd();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CombinePoint read(Json json, JsonValue jsonData, Class type) {
		final Vertex actualVertex = json.readValue("actualVertex", Vertex.class, jsonData); //$NON-NLS-1$
		final Array<CombinedVertex> combinedVertices = json.readValue("combinedVertices", Array.class, CombinedVertex.class, jsonData); //$NON-NLS-1$
		if (combinedVertices.size < 1) {
			throw new RuntimeException("failed to read CombinePoint"); //$NON-NLS-1$
		}
		CombinePoint result = new CombinePoint(actualVertex, combinedVertices.get(0).square, combinedVertices.get(0).vertex);
		for (int i = 1; i < combinedVertices.size; i++) {
			result.addCombinedVertex(combinedVertices.get(i));
		}
		return result;

	}

	private CombinePointSerializer() {
	}

}
