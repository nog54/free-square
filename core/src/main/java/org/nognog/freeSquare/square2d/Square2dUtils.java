package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * @author goshi 2014/12/24
 */
public class Square2dUtils {
	private Square2dUtils() {
	}

	/**
	 * @param square
	 * @return true if square vertices is twist.
	 */
	public static boolean isTwist(Square2d square) {
		return isTwist(square.getVertices());
	}

	/**
	 * @param vertices
	 * @return true if vertices if twist.
	 */
	public static boolean isTwist(Vertex[] vertices) {
		for (Vertex vertex1 : vertices) {
			for (Vertex vertex2 : vertices) {
				if (vertex1 == vertex2) {
					continue;
				}
				if (vertex1.equals(vertex2)) {
					return true;
				}
			}
		}
		for (int i = 0; i < vertices.length; i++) {
			Vertex v1 = vertices[i];
			Vertex v2 = vertices[(i + 1) % vertices.length];
			for (int j = i + 2; j < vertices.length; j++) {
				Vertex v3 = vertices[j];
				Vertex v4 = vertices[(j + 1) % vertices.length];
				if (v1 == v3 || v1 == v4 || v2 == v3 || v2 == v4) {
					continue;
				}
				if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y, null)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param vertices
	 * @param exceptVertices
	 * @return polygon instance
	 */
	public static Polygon createPolygon(Vertex[] vertices, Vertex... exceptVertices) {
		int exceptVerticesHitCount = 0;
		for (Vertex vertex : vertices) {
			if (contains(exceptVertices, vertex)) {
				exceptVerticesHitCount++;
			}
		}
		float[] points = new float[vertices.length * 2 - exceptVerticesHitCount * 2];
		int i = 0;
		for (Vertex vertex : vertices) {
			if (contains(exceptVertices, vertex)) {
				continue;
			}

			points[i++] = vertex.x;
			points[i++] = vertex.y;
		}
		return new Polygon(points);
	}

	/**
	 * @param objects
	 * @param findObject
	 * @return true if contains same object.
	 */
	public static boolean contains(Object[] objects, Object findObject) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == findObject) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param vertices
	 * @return float array
	 */
	public static float[] toFloatArray(Vertex[] vertices) {
		float[] result = new float[vertices.length * 2];
		int i = 0;
		for (Vertex vertex : vertices) {
			result[i++] = vertex.x;
			result[i++] = vertex.y;
		}
		return result;
	}

	/**
	 * @param vertex
	 * @return Vector2
	 */
	public static Vector2 toVector2(Vertex vertex) {
		return new Vector2(vertex.x, vertex.y);
	}

	/**
	 * @param vector
	 * @return Vertex
	 */
	public static Vertex toVertex(Vector2 vector) {
		return new Vertex(vector.x, vector.y);
	}

	/**
	 * @param square
	 * @return random point in square2d
	 */
	public static Vector2 getRandomPointOn(Square2d square) {
		Vertex[] vertices = square.getVertices();
		if (vertices.length < 3) {
			return null;
		}
		Vertex baseVertex = vertices[0];
		final int randomVertexIndex1 = MathUtils.random(1, vertices.length - 2);
		final int randomVertexIndex2 = randomVertexIndex1 + 1;
		return getRandomPointOn(baseVertex, vertices[randomVertexIndex1], vertices[randomVertexIndex2]);
	}

	/**
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return random point in 3 points triangle
	 */
	public static Vector2 getRandomPointOn(Vertex v1, Vertex v2, Vertex v3) {
		double sqrtR1 = Math.sqrt(MathUtils.random());
		double r2 = MathUtils.random();
		double x = (1 - sqrtR1) * v1.x + (sqrtR1 * (1 - r2)) * v2.x + (sqrtR1 * r2) * v3.x;
		double y = (1 - sqrtR1) * v1.y + (sqrtR1 * (1 - r2)) * v2.y + (sqrtR1 * r2) * v3.y;
		return new Vector2((float) x, (float) y);
	}

}
