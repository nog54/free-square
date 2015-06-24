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

import org.nognog.freeSquare.square2d.CombineInfo.ReconstructCombineInfo;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/06/02
 */
public class CombineSquare2dSerializer implements Json.Serializer<CombineSquare2d> {
	private static CombineSquare2dSerializer instance = new CombineSquare2dSerializer();

	private CombineSquare2dSerializer() {

	}

	/**
	 * @return instance
	 */
	public static CombineSquare2dSerializer getInstance() {
		return instance;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void write(Json json, CombineSquare2d object, Class knownType) {
		json.writeObjectStart();
		json.writeType(CombineSquare2d.class);
		json.writeValue("squares", object.getSquares()); //$NON-NLS-1$
		json.writeValue("reconstructCombineInfo", object.getCombineInfo().toReconstructCombineInfo()); //$NON-NLS-1$
		json.writeValue("objects", object.objects); //$NON-NLS-1$
		json.writeValue("name", object.getName()); //$NON-NLS-1$
		json.writeObjectEnd();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CombineSquare2d read(Json json, JsonValue jsonData, Class type) {
		final Array<Square2d> combinedSquares = getSquares(json, jsonData);
		final ReconstructCombineInfo reconstructConbineInfo = json.readValue("reconstructCombineInfo", ReconstructCombineInfo.class, jsonData); //$NON-NLS-1$
		if (combinedSquares.size < 1) {
			return null;
		}
		final Array<Vertex> combineVertices1 = reconstructConbineInfo.getVertices1();
		final Array<Vertex> combineVertices2 = reconstructConbineInfo.getVertices2();
		if (combineVertices1.size != combineVertices2.size) {
			return null;
		}

		final Square2d[] allCombinedSquares = combinedSquares.<Square2d> toArray(Square2d.class);
		if (combineVertices1.size != (combinedSquares.size - 1)) {
			System.out.println("read error occured."); //$NON-NLS-1$
			throw new CombineSquare2dReadFailureException(allCombinedSquares);
		}

		final CombineSquare2d baseSquare = new CombineSquare2d(combinedSquares.get(0));
		combinedSquares.removeIndex(0);
		try {
			appendSquares(baseSquare, combinedSquares, combineVertices1, combineVertices2);
		} catch (IllegalStateException e) {
			System.out.println("read error occured."); //$NON-NLS-1$
			e.printStackTrace();
			baseSquare.disposeAndFreeSquares();
			throw new CombineSquare2dReadFailureException(allCombinedSquares);
		}
		Array<Square2dObject> readObjects = json.readValue(Array.class, jsonData.get("objects")); //$NON-NLS-1$
		for (Square2dObject object : readObjects) {
			baseSquare.addSquareObject(object, object.getX(), object.getY(), false);
		}
		String name = json.readValue("name", String.class, jsonData); //$NON-NLS-1$
		baseSquare.setName(name);
		return baseSquare;
	}

	private static Array<Square2d> getSquares(Json json, JsonValue jsonData) {
		final Array<Square2d> combinedSquares = new Array<>();
		JsonValue squaresData = jsonData.get("squares"); //$NON-NLS-1$

		for (JsonValue child = squaresData.child; child != null; child = child.next) {
			try {
				final Square2d readSquare = json.readValue(Square2d.class, null, child);
				combinedSquares.add(readSquare);
			} catch (CombineSquare2dReadFailureException e) {
				combinedSquares.addAll(e.getContainedSquares());
			}
		}
		return combinedSquares;
	}

	private static void appendSquares(final CombineSquare2d baseSquare, final Array<Square2d> combineSquares, final Array<Vertex> combineVertices1, final Array<Vertex> combineVertices2) {
		for (int i = 0; i < combineSquares.size; i++) {
			final Square2d beCombineSquare = combineSquares.get(i);
			final Vertex combineVertex1 = CombineSquare2dUtils.getVertexHavingSameValue(combineVertices1.get(i), baseSquare.getVertices());
			final Vertex combineVertex2 = CombineSquare2dUtils.getVertexHavingSameValue(combineVertices2.get(i), beCombineSquare.getVertices());
			final boolean isCombineSuccess = baseSquare.combineWith(combineVertex1, beCombineSquare, combineVertex2);
			if (isCombineSuccess) {
				combineSquares.removeValue(beCombineSquare, true);
				combineVertices1.removeIndex(i);
				combineVertices2.removeIndex(i);
				i--;
			} else {
				break;
			}
		}
		while (combineSquares.size != 0) {
			boolean isCombined = false;
			for (int i = 0; i < combineSquares.size; i++) {
				final Square2d beCombinedSquare = combineSquares.get(i);
				for (Vertex beCombinedSquareVertex : beCombinedSquare.getVertices()) {
					final Vertex afterBeCombinedSquareVertex = CombineSquare2d.calculateAfterCombineTargetVertex(beCombinedSquareVertex, combineVertices1.get(i), combineVertices2.get(i));
					final Vertex sufficientlyCloseVertex = CombineSquare2dUtils.getSufficientlyCloseVertex(afterBeCombinedSquareVertex, baseSquare.getVertices());
					final boolean isCombineSuccess = baseSquare.combineWith(sufficientlyCloseVertex, beCombinedSquare, beCombinedSquareVertex);
					if (isCombineSuccess) {
						combineSquares.removeValue(beCombinedSquare, true);
						combineVertices1.removeIndex(i);
						combineVertices2.removeIndex(i);
						i--;
						isCombined = true;
						break;
					}
				}
			}
			if (isCombined == false) {
				throw new IllegalStateException();
			}
		}
	}
}