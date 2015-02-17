package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2014/12/24
 */
public class Square2dUtils {
	private Square2dUtils() {
	}

	/**
	 * @param square
	 * @return random point in square2d
	 */
	public static Vector2 getRandomPointOn(Square2d square) {
		Array<Vertex> vertices = square.getVertices();
		if (vertices.size < 3) {
			return null;
		}
		Vertex baseVertex = vertices.get(0);
		final int randomVertexIndex1 = MathUtils.random(1, vertices.size - 2);	
		final int randomVertexIndex2 = randomVertexIndex1 + 1;
		return getRandomPointOn(baseVertex, vertices.get(randomVertexIndex1), vertices.get(randomVertexIndex2));
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
