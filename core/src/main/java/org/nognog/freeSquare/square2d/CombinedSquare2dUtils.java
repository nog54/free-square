package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/25
 */
public class CombinedSquare2dUtils {
	private CombinedSquare2dUtils() {
	}

	private static boolean isSufficientlyCloseDistance(float a) {
		final float regardAsSufficientlyCloseThreshold = 5;
		return Math.abs(a) < regardAsSufficientlyCloseThreshold;
	}

	/**
	 * @param vertex1
	 * @param vertex2
	 * @return true if distance to vertex2 from vertex1
	 */
	public static boolean canBeRegardedAsSameVertex(Vertex vertex1, Vertex vertex2) {
		return isSufficientlyCloseDistance(vertex1.calculateR(vertex2));
	}

	/**
	 * @param checkVertex
	 * @param vertices
	 * @return true if exists vertex that is sufficiently close to checkVertex
	 *         in vertices.
	 */
	public static boolean existsSufficientlyCloseVertex(Vertex checkVertex, Vertex[] vertices) {
		for (int i = 0; i < vertices.length; i++) {
			if (canBeRegardedAsSameVertex(checkVertex, vertices[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param checkVertices
	 * @return common vertices
	 */
	public static Vertex[][] getCommonVertex(Vertex[] checkVertices) {
		Vertex[][] result = new Vertex[checkVertices.length][];
		for (int i = 0; i < checkVertices.length; i++) {
			Vertex checkVertex = checkVertices[i];
			Array<Vertex> commonVertices = new Array<>();
			for (int j = 0; j < checkVertices.length; j++) {
				Vertex compareVertex = checkVertices[j];
				if (checkVertex == compareVertex) {
					continue;
				}

				if (canBeRegardedAsSameVertex(checkVertex, compareVertex)) {
					commonVertices.add(compareVertex);
				}
				result[i] = commonVertices.toArray(Vertex.class);
			}
		}
		return result;
	}

	/**
	 * @param vertices
	 */
	public static void normalizeVertices(Array<Vertex> vertices) {
		// Vertex[] verticesArray = vertices.toArray(Vertex.class);
		// Vertex[][] commonVertex =
		// CombinedSquare2dUtils.getCommonVertex(verticesArray);
		// for (int i = 0; i < verticesArray.length; i++) {
		// if (commonVertex[i].length == 0 ||
		// !vertices.contains(verticesArray[i], true)) {
		// continue;
		// }
		// Vertex[] removeCandidateVertices = new Vertex[commonVertex[i].length
		// + 1];
		// removeCandidateVertices[0] = verticesArray[i];
		// System.arraycopy(commonVertex[i], 0, removeCandidateVertices, 1,
		// commonVertex[i].length);
		// if (hasSameAreaEvenIfRemoveVertices(verticesArray,
		// removeCandidateVertices)) {
		// for (int j = 0; j < removeCandidateVertices.length; j++) {
		// vertices.removeValue(removeCandidateVertices[j], true);
		// }
		// }
		// }

		boolean changed;
		do {
			changed = false;
			for (Vertex vertex : vertices) {
				Vertex[] currentVerticesArray = vertices.toArray(Vertex.class);
				if (hasSameAreaEvenIfRemoveVertices(currentVerticesArray, vertex)) {
					vertices.removeValue(vertex, true);
					changed = true;
				}
			}
		} while (changed);
	}

	/**
	 * @param vertices
	 * @param removeVertices
	 * @return true if change of square area is sufficiently small.
	 */
	public static boolean hasSameAreaEvenIfRemoveVertices(Vertex[] vertices, Vertex... removeVertices) {
		float beforeArea = Math.abs(Square2dUtils.createPolygon(vertices).area());
		float afterArea = Math.abs(Square2dUtils.createPolygon(vertices, removeVertices).area());
		return isSufficientlySameArea(beforeArea, afterArea);
	}

	private static boolean isSufficientlySameArea(float area1, float area2) {
		float relativeError = Math.abs((area2 - area1) / area1);
		final float permissibleRelativeError = 1 / 100f;
		return relativeError < permissibleRelativeError;
	}

	/**
	 * @param vertices
	 * @param insertVertex
	 * @return unchange area insert index. if not exists, return -1.
	 */
	public static int getUnchangeSquareAreaIndexEvenIfInsert(Vertex[] vertices, Vertex insertVertex) {
		final float baseArea = Square2dUtils.createPolygon(vertices).area();
		for (int insertIndex = 0; insertIndex <= vertices.length; insertIndex++) {
			final Vertex[] afterInsertVertices = new Vertex[vertices.length + 1];
			System.arraycopy(vertices, 0, afterInsertVertices, 0, insertIndex);
			afterInsertVertices[insertIndex] = insertVertex;
			System.arraycopy(vertices, insertIndex, afterInsertVertices, insertIndex + 1, vertices.length - insertIndex);
			final float afterInsertArea = Square2dUtils.createPolygon(afterInsertVertices).area();
			if (isSufficientlySameArea(baseArea, afterInsertArea)) {
				return insertIndex;
			}
		}
		return -1;
	}
}
