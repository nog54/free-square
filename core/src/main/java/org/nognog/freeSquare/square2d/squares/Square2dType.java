package org.nognog.freeSquare.square2d.squares;

import static org.nognog.freeSquare.Messages.getString;
import static org.nognog.freeSquare.square2d.Square2d.Vertex.vertex;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dSize;
import org.nognog.freeSquare.square2d.Square2d.Vertex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public enum Square2dType {

	GRASSY_SQUARE1(
			getString("grassy-square1", "medium"), Resources.grassy1Path, vertex(511.5f, 64f), vertex(1010f, 272f), vertex(511.5f, 478f), vertex(14f, 272f), Square2dSize.MEDIUM, HopSquare.class), //$NON-NLS-1$ //$NON-NLS-2$
	GRASSY_SQUARE1_SMALL(GRASSY_SQUARE1, getString("grassy-square1", "small"), Square2dSize.SMALL), //$NON-NLS-1$ //$NON-NLS-2$
	GRASSY_SQUARE1_LARGE(GRASSY_SQUARE1, getString("grassy-square1", "large"), Square2dSize.LARGE), //$NON-NLS-1$ //$NON-NLS-2$

	;

	@SuppressWarnings("unchecked")
	private <T extends Square2d> Square2dType(Square2dType type, String name, Square2dSize size) {
		this(name, type.texture, type.baseVertex1, type.baseVertex2, type.baseVertex3, type.baseVertex4, size, (Class<T>) type.klass);
	}

	private <T extends Square2d> Square2dType(String name, String texturePath, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Square2dSize size) {
		this(name, texturePath, baseVertex1, baseVertex2, baseVertex3, baseVertex4, size, Square2d.class);
	}

	private <T extends Square2d> Square2dType(String name, String texturePath, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Square2dSize size, Class<T> klass) {
		this(name, new Texture(texturePath), baseVertex1, baseVertex2, baseVertex3, baseVertex4, size, klass);
	}

	private <T extends Square2d> Square2dType(String name, Texture texture, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Square2dSize size, Class<T> klass) {
		this.name = name;
		this.klass = klass;
		this.texture = texture;
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.baseVertex1 = baseVertex1;
		this.baseVertex2 = baseVertex2;
		this.baseVertex3 = baseVertex3;
		this.baseVertex4 = baseVertex4;
		this.size = size;

		final float scale = size.getWidth() / texture.getWidth();
		this.squarePositionOffsetY = size.getWidth() / 12;
		this.vertex1 = vertex(baseVertex1.x * scale, baseVertex1.y * scale + this.squarePositionOffsetY);
		this.vertex2 = vertex(baseVertex2.x * scale, baseVertex2.y * scale + this.squarePositionOffsetY);
		this.vertex3 = vertex(baseVertex3.x * scale, baseVertex3.y * scale + this.squarePositionOffsetY);
		this.vertex4 = vertex(baseVertex4.x * scale, baseVertex4.y * scale + this.squarePositionOffsetY);

		if (this.isInvalidVertex()) {
			throw new RuntimeException("Square corners are invalid."); //$NON-NLS-1$
		}

		if (Intersector.intersectSegments(this.vertex1.x, this.vertex1.y, this.vertex3.x, this.vertex3.y, this.vertex2.x, this.vertex2.y, this.vertex4.x, this.vertex4.y, null)) {
			this.isConcave = false;
		} else {
			this.isConcave = true;
		}
	}

	private final Class<?> klass;
	private final String name;
	private final Texture texture;
	public final Vertex vertex1;
	public final Vertex vertex2;
	public final Vertex vertex3;
	public final Vertex vertex4;
	public final Vertex baseVertex1;
	private final Vertex baseVertex2;
	private final Vertex baseVertex3;
	private final Vertex baseVertex4;
	private final float squarePositionOffsetY;
	private final Square2dSize size;
	private final boolean isConcave;

	public String getName() {
		return this.name;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public Vertex getVertex1() {
		return this.vertex1;
	}

	public Vertex getVertex2() {
		return this.vertex2;
	}

	public Vertex getVertex3() {
		return this.vertex3;
	}

	public Vertex getVertex4() {
		return this.vertex4;
	}

	public Square2dSize getSize() {
		return this.size;
	}

	public boolean isConcave() {
		return this.isConcave;
	}

	public Square2d create() {
		try {
			Constructor<?> c = this.klass.getConstructor(Square2dType.class);
			return (Square2d) c.newInstance(this);
		} catch (Exception e) {
			// nothing
		}

		try {
			return (Square2d) this.klass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Image createSquareImage() {
		Image squareImage = new Image(this.texture);
		squareImage.setWidth(this.size.getWidth());
		squareImage.setHeight(squareImage.getHeight() * (this.size.getWidth() / this.texture.getWidth()));
		squareImage.setY(this.squarePositionOffsetY);
		squareImage.setName(this.name);
		return squareImage;
	}
	
	public static void dispose(){
		for(Square2dType type : Square2dType.values()){
			type.getTexture().dispose();
		}
	}

	private boolean isInvalidVertex() {
		if (Intersector.intersectSegments(this.vertex1.x, this.vertex1.y, this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y, null)) {
			return true;
		}
		if (Intersector.intersectSegments(this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y, this.vertex1.x, this.vertex1.y, null)) {
			return true;
		}
		return false;
	}

}
