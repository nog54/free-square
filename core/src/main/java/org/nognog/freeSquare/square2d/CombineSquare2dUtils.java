package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/25
 */
public class CombineSquare2dUtils {
	/**
	 * threshold of regard as sufficiently close.
	 */
	public static final float regardAsSufficientlyCloseThreshold = 5;
	/**
	 * threshold of regard as sufficiently same radian.
	 */
	public static final float regardAsSufficientlySameRadianThreashold = (float) (Math.PI / 135); // 1.5deg

	private CombineSquare2dUtils() {
	}

	/**
	 * @param r
	 * @return true if r can be regarded as sufficiently small.
	 */
	public static boolean isSufficientlyCloseDistance(float r) {
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
			final Vertex[] currentVerticesArray = vertices.toArray(Vertex.class);
			for (Vertex vertex : currentVerticesArray) {
				if (hasEqualityAreaEvenIfRemoveVertices(currentVerticesArray, vertex)) {
					vertices.removeValue(vertex, true);
					changed = true;
					break;
				}
			}
		} while (changed);
	}

	/**
	 * @param vertices
	 * @param removeVertices
	 * @return true if change of square area is sufficiently small.
	 */
	public static boolean hasEqualityAreaEvenIfRemoveVertices(Vertex[] vertices, Vertex removeVertices) {
		Edge edge1 = null, edge2 = null;
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] == removeVertices) {
				edge1 = new Edge(vertices[(i - 1 + vertices.length) % vertices.length], vertices[i]);
				edge2 = new Edge(vertices[i], vertices[(i + 1) % vertices.length]);
				break;
			}
		}
		if (edge1 == null || edge2 == null) {
			return false;
		}
		final float theta1 = edge1.getTheta();
		final float theta2 = edge2.getTheta();
		if (theta1 == 0 || theta2 == 0) {
			return true;
		}
		return isSufficientlySameSlope(theta1, theta2);
	}

	private static boolean isSufficientlySameSlope(float theta1, float theta2) {
		float absoluteErrorRadian = Math.abs(theta1 - theta2);
		return absoluteErrorRadian < regardAsSufficientlySameRadianThreashold;
	}

	/**
	 * @param vertices
	 * @param insertVertex
	 * @return unchange area insert index. if not exists, return -1.
	 */
	public static int getUnchangeSquareAreaIndexEvenIfInsert(Vertex[] vertices, Vertex insertVertex) {
		for (int i = 1; i <= vertices.length; i++) {
			Edge edge1 = new Edge(insertVertex, vertices[i % vertices.length]);
			Edge edge2 = new Edge(vertices[i - 1], insertVertex);
			if (isSufficientlySameSlope(edge1.getTheta(), edge2.getTheta())) {
				return i % vertices.length;
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
	public static Edge getNearestSufficientlyCloseEdge(Vertex checkVertex, Array<Vertex> vertices) {
		return getNearestSufficientlyCloseEdge(checkVertex, vertices.<Vertex> toArray(Vertex.class));
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

	/**
	 * @param findVertex
	 * @param vertices
	 * @return same value vertex
	 */
	public static Vertex getSameValueVertex(Vertex findVertex, Vertex[] vertices) {
		for (Vertex vertex : vertices) {
			if (vertex.equals(findVertex)) {
				return vertex;
			}
		}
		return null;
	}

	/**
	 * @param square
	 * @return root square2d
	 */
	public static Square2d getRootSquare(Square2d square) {
		if (square.getParent() instanceof Square2d) {
			return getRootSquare((Square2d) square.getParent());
		}
		return square;
	}

}
