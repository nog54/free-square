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

import org.nognog.freeSquare.persist.PersistManager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/02/15
 */
@SuppressWarnings("javadoc")
public class CombinePoint {
	static {
		Json json = PersistManager.getUseJson();
		json.setSerializer(CombinePoint.class, new Json.Serializer<CombinePoint>() {

			@Override
			@SuppressWarnings({ "hiding", "rawtypes" })
			public void write(Json json, CombinePoint object, Class knownType) {
				json.writeObjectStart();
				json.writeFields(object);
				json.writeObjectEnd();
			}

			@Override
			@SuppressWarnings({ "hiding", "rawtypes", "unchecked" })
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
		});
	}
	public final Vertex actualVertex;
	public final Array<CombinedVertex> combinedVertices;

	CombinePoint(Vertex actualVertex, Square2d square, Vertex vertex) {
		this.actualVertex = actualVertex;
		this.combinedVertices = new Array<>();
		this.combinedVertices.add(new CombinedVertex(square, vertex));
	}

	public boolean contains(Square2d square) {
		return this.getVertexOf(square) != null;
	}

	public Vertex getVertexOf(Square2d square) {
		for (CombinedVertex combinedVertex : this.combinedVertices) {
			if (combinedVertex.square == square) {
				return combinedVertex.vertex;
			}
		}
		return null;
	}

	public void addCombinedVertex(Square2d square, Vertex vertex) {
		this.addCombinedVertex(new CombinedVertex(square, vertex));
	}

	public void addCombinedVertex(CombinedVertex combinedVertex) {
		if (this.getVertexOf(combinedVertex.square) != null) {
			throw new IllegalArgumentException();
		}
		this.combinedVertices.add(combinedVertex);
	}

	public void addCombinedVertices(CombinedVertex[] addCombinedVertices) {
		for (CombinedVertex addCombinedVertex : addCombinedVertices) {
			this.addCombinedVertex(addCombinedVertex);
		}
	}

	public void removeCombinedVertex(Square2d square) {
		for (CombinedVertex combinedVertex : this.combinedVertices) {
			if (combinedVertex.square == square) {
				this.combinedVertices.removeValue(combinedVertex, true);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(this.actualVertex).append(" | ").append(this.combinedVertices).toString(); //$NON-NLS-1$
	}

	static class CombinedVertex {
		static {
			Json json = PersistManager.getUseJson();
			json.setSerializer(CombinedVertex.class, new Json.Serializer<CombinedVertex>() {

				@Override
				@SuppressWarnings({ "hiding", "rawtypes" })
				public void write(Json json, CombinedVertex object, Class knownType) {
					json.writeObjectStart();
					json.writeFields(object);
					json.writeObjectEnd();
				}

				@Override
				@SuppressWarnings({ "hiding", "rawtypes", })
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
			});
		}
		public final Square2d square;
		public final Vertex vertex;

		CombinedVertex(Square2d square, Vertex vertex) {
			if (!square.contains(vertex)) {
				throw new IllegalArgumentException();
			}
			this.square = square;
			this.vertex = vertex;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return sb.append(this.square).append(" ").append(this.vertex).toString(); //$NON-NLS-1$
		}
	}
}
