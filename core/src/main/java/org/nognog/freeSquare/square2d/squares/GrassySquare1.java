package org.nognog.freeSquare.square2d.squares;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.Square2D;
import static org.nognog.freeSquare.square2d.Square2D.Vertex.vertex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2014/12/15
 */
public class GrassySquare1 extends Square2D {

	private static final Vertex near = vertex(511.5f, 64f);
	private static final Vertex right = vertex(1010f, 272f);
	private static final Vertex far = vertex(511.5f, 478f);
	private static final Vertex left = vertex(14f, 272f);

	/**
	 * @param width
	 */
	public GrassySquare1(float width) {
		super(width, near, right, far, left, new Texture(Gdx.files.internal(Resources.GRASSY1_PATH)));
	}

}
