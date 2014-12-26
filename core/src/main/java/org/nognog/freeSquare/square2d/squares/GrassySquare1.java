package org.nognog.freeSquare.square2d.squares;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.Square2D;
import org.nognog.freeSquare.square2d.Square2DSize;

import static org.nognog.freeSquare.square2d.Square2D.Vertex.vertex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2014/12/15
 */
public class GrassySquare1 extends Square2D {

	private static final Vertex baseVertex1 = vertex(511.5f, 64f);
	private static final Vertex baseVertex2 = vertex(1010f, 272f);
	private static final Vertex baseVertex3 = vertex(511.5f, 478f);
	private static final Vertex baseVertex4 = vertex(14f, 272f);

	/**
	 * @param size
	 */
	public GrassySquare1(Square2DSize size) {
		super(size, baseVertex1, baseVertex2, baseVertex3, baseVertex4, new Texture(
				Gdx.files.internal(Resources.grassy1Path)));
	}

}
