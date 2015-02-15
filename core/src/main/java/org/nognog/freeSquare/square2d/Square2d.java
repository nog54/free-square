package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.SimpleSquare2d.Vertex;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.ui.Square;

/**
 * @author goshi 2015/02/15
 */
public interface Square2d extends Square<Square2dObject> {

	/**
	 * @return vertices
	 */
	Vertex[] getVertices();

	/**
	 * @return image width
	 */
	float getImageWidth();

	/**
	 * @return image height
	 */
	float getImageHeight();

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is contained in square
	 */
	boolean containsInSquare(float x, float y);
}
