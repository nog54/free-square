package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2014/12/24
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
	 * @param polygonVertices
	 * @param checkVertex
	 * @return true if exists sufficiently close edge
	 */
	public static boolean existsSufficientlyCloseEdge(Vertex[] polygonVertices, Vertex checkVertex) {
		for (int i = 0; i < polygonVertices.length; i++) {
			final Vertex vertex1 = polygonVertices[i];
			final Vertex vertex2 = polygonVertices[(i + 1) % polygonVertices.length];
			float distance = Intersector.distanceSegmentPoint(vertex1.x, vertex1.y, vertex2.x, vertex2.y, checkVertex.x, checkVertex.y);
			if (isSufficientlyCloseDistance(distance)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param polygonVertices
	 * @param checkVertices
	 * @return true if exists vertex that is contained in polygonVertices and
	 *         not exists sufficiently close edge.
	 */
	public static boolean existsInvalidContainedVertex(Vertex[] polygonVertices, Vertex[] checkVertices) {
		Polygon polygon = Square2dUtils.createPolygon(polygonVertices);
		for (int i = 0; i < checkVertices.length; i++) {
			Vertex checkVertex = checkVertices[i];
			if (polygon.contains(checkVertex.x, checkVertex.y) == false) {
				continue;
			}
			if (existsSufficientlyCloseEdge(polygonVertices, checkVertex) == false) {
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
	 * @param vertices1
	 * @param vertices2
	 * @return count of adjacent point
	 */
	public static int countAdjacentPoint(Vertex[] vertices1, Vertex[] vertices2) {
		int count1 = 0;
		for (Vertex checkVertex : vertices1) {
			if (existsSufficientlyCloseEdge(vertices2, checkVertex)) {
				count1++;
			}
		}
		int count2 = 0;
		for (Vertex checkVertex : vertices2) {
			if (existsSufficientlyCloseEdge(vertices1, checkVertex)) {
				count2++;
			}
		}
		return Math.max(count1, count2);
	}

}
