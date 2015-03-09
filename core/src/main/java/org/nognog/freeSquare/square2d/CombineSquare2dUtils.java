package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/25
 */
public class CombineSquare2dUtils {
	private CombineSquare2dUtils() {
	}

	/**
	 * @param r
	 * @return true if r can be regarded as sufficiently small.
	 */
	public static boolean isSufficientlyCloseDistance(float r) {
		final float regardAsSufficientlyCloseThreshold = 5;
		return Math.abs(r) < regardAsSufficientlyCloseThreshold;
	}

	/**
	 * @param vector1
	 * @param vector2
	 * @return true if distance to vertex2 from vertex1
	 */
	public static boolean canBeRegardedAsSameVertex(Vector2 vector1, Vector2 vector2) {
		return canBeRegardedAsSameVertex(Square2dUtils.toVertex(vector1), Square2dUtils.toVertex(vector2));
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
		return getSufficientlyCloseVertex(checkVertex, vertices) != null;
	}

	/**
	 * @param checkVertex
	 * @param vertices
	 * @return may be null.
	 */
	public static Vertex getSufficientlyCloseVertex(Vertex checkVertex, Vertex[] vertices) {
		for (int i = 0; i < vertices.length; i++) {
			if (canBeRegardedAsSameVertex(checkVertex, vertices[i])) {
				return vertices[i];
			}
		}
		return null;
	}

	/**
	 * @param vertices
	 */
	public static void normalizeVertices(Array<Vertex> vertices) {
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

	/**
	 * @param checkVertex
	 * @param vertices
	 * @return distance to most nearest point from checkVertex.
	 */
	public static float getDistanceToNearestEdge(Vertex checkVertex, Vertex[] vertices) {
		float minR = Float.MAX_VALUE;
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			final float r = Intersector.distanceSegmentPoint(v1.x, v1.y, v2.x, v2.y, checkVertex.x, checkVertex.y);
			if (r < minR) {
				minR = r;
			}
		}
		return minR;
	}

	/**
	 * @param checkVertex
	 * @param vertices
	 * @return true if checkVertex can be regarded as online.
	 */
	public static boolean canBeRegardedAsOnline(Vertex checkVertex, Vertex[] vertices) {
		final float distanceToNearestEdge = getDistanceToNearestEdge(checkVertex, vertices);
		return isSufficientlyCloseDistance(distanceToNearestEdge);
	}

	/**
	 * @param checkVertex
	 * @param vertices
	 * @return edge under vertex. may be null.
	 */
	public static Edge[] getOnlineEdge(Vertex checkVertex, Vertex[] vertices) {
		Array<Edge> result = new Array<>();
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			final float r = Intersector.distanceSegmentPoint(v1.x, v1.y, v2.x, v2.y, checkVertex.x, checkVertex.y);
			if (isSufficientlyCloseDistance(r)) {
				result.add(new Edge(v1, v2));
			}
		}
		return result.toArray(Edge.class);
	}


	/**
	 * @param checkVertex
	 * @param vertices
	 * @return edge under vertex. may be null.
	 */
	public static Edge getNearestSufficientlyCloseEdge(Vertex checkVertex, Vertex[] vertices) {
		Vertex minV1 = null;
		Vertex minV2 = null;
		float minR = Float.MAX_VALUE;
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			final float r = Intersector.distanceSegmentPoint(v1.x, v1.y, v2.x, v2.y, checkVertex.x, checkVertex.y);
			if (r < minR) {
				minR = r;
				minV1 = v1;
				minV2 = v2;
			}
		}
		if (!isSufficientlyCloseDistance(minR)) {
			return null;
		}
		return new Edge(minV1, minV2);
	}
}
