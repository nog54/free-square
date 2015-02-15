package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.UnsupportedYetException;
import org.nognog.freeSquare.square2d.SimpleSquare2d.Vertex;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

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
		if (square instanceof SimpleSquare2d) {
			return getRandomPointOn((SimpleSquare2d) square);
		}
		return null;
	}

	/**
	 * @param square
	 * @return random point in square2d
	 */
	public static Vector2 getRandomPointOn(CombinedSquare2d square) {
		return null;
	}

	/**
	 * @param square
	 * @return random point in square2d
	 */
	public static Vector2 getRandomPointOn(SimpleSquare2d square) {
		if (square.isConcave()) {
			throw new UnsupportedYetException("concave square is not supported yet"); //$NON-NLS-1$
		}
		Vertex vertex1 = square.getVertex1();
		Vertex vertex2 = square.getVertex2();
		Vertex vertex3 = square.getVertex3();
		Vertex vertex4 = square.getVertex4();
		boolean useV1V2V3Triangle = MathUtils.randomBoolean();
		if (useV1V2V3Triangle) {
			return getRandomPointOn(vertex1, vertex2, vertex3);
		}
		return getRandomPointOn(vertex1, vertex4, vertex3);
	}

	/**
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return random point in 3 points triangle
	 */
	public static Vector2 getRandomPointOn(SimpleSquare2d.Vertex v1, SimpleSquare2d.Vertex v2, SimpleSquare2d.Vertex v3) {
		double sqrtR1 = Math.sqrt(MathUtils.random());
		double r2 = MathUtils.random();
		double x = (1 - sqrtR1) * v1.x + (sqrtR1 * (1 - r2)) * v2.x + (sqrtR1 * r2) * v3.x;
		double y = (1 - sqrtR1) * v1.y + (sqrtR1 * (1 - r2)) * v2.y + (sqrtR1 * r2) * v3.y;
		return new Vector2((float) x, (float) y);
	}

}
