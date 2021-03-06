/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.type;

import static org.nognog.freeSquare.Messages.getString;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Square2dSize;
import org.nognog.freeSquare.square2d.Vertex;
import static org.nognog.freeSquare.square2d.Vertex.vertex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public enum Square2dType {

	GRASSY_SQUARE1_MEDIUM(
			getString("grassy-square1", "medium"), Resources.grassy1Path, vertex(511.5f, 64f), vertex(1010f, 272f), vertex(511.5f, 478f), vertex(14f, 272f), Square2dSize.MEDIUM, HopSquare.class), //$NON-NLS-1$ //$NON-NLS-2$
	GRASSY_SQUARE1_TINY(GRASSY_SQUARE1_MEDIUM, getString("grassy-square1", "tiny"), Square2dSize.TINY), //$NON-NLS-1$ //$NON-NLS-2$
	GRASSY_SQUARE1_SMALL(GRASSY_SQUARE1_MEDIUM, getString("grassy-square1", "small"), Square2dSize.SMALL), //$NON-NLS-1$ //$NON-NLS-2$
	GRASSY_SQUARE1_LARGE(GRASSY_SQUARE1_MEDIUM, getString("grassy-square1", "large"), Square2dSize.LARGE), //$NON-NLS-1$ //$NON-NLS-2$

	;

	@SuppressWarnings("unchecked")
	private <T extends SimpleSquare2d> Square2dType(Square2dType type, String name, Square2dSize size) {
		this(name, type.texture, type.baseVertex1, type.baseVertex2, type.baseVertex3, type.baseVertex4, size, (Class<T>) type.klass);
	}

	private <T extends SimpleSquare2d> Square2dType(String name, String texturePath, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Square2dSize size) {
		this(name, texturePath, baseVertex1, baseVertex2, baseVertex3, baseVertex4, size, SimpleSquare2d.class);
	}

	private <T extends SimpleSquare2d> Square2dType(String name, String texturePath, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Square2dSize size, Class<T> klass) {
		this(name, new Texture(texturePath), baseVertex1, baseVertex2, baseVertex3, baseVertex4, size, klass);
	}

	private <T extends SimpleSquare2d> Square2dType(String name, Texture texture, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Square2dSize size, Class<T> klass) {
		this.name = name;
		this.klass = klass;
		this.texture = texture;
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.baseVertex1 = baseVertex1;
		this.baseVertex2 = baseVertex2;
		this.baseVertex3 = baseVertex3;
		this.baseVertex4 = baseVertex4;
		this.size = size;

		final double scale = size.getWidth() / texture.getWidth();
		this.squarePositionOffsetY = size.getWidth() / 12;
		this.vertex1 = vertex(baseVertex1.x * scale, baseVertex1.y * scale + this.getSquarePositionOffsetY());
		this.vertex2 = vertex(baseVertex2.x * scale, baseVertex2.y * scale + this.getSquarePositionOffsetY());
		this.vertex3 = vertex(baseVertex3.x * scale, baseVertex3.y * scale + this.getSquarePositionOffsetY());
		this.vertex4 = vertex(baseVertex4.x * scale, baseVertex4.y * scale + this.getSquarePositionOffsetY());

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
	private final double squarePositionOffsetY;
	private final Square2dSize size;
	private final boolean isConcave;

	public String getName() {
		return this.name;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public Square2dSize getSize() {
		return this.size;
	}

	/**
	 * @return the squarePositionOffsetY
	 */
	public double getSquarePositionOffsetY() {
		return this.squarePositionOffsetY;
	}

	public boolean isConcave() {
		return this.isConcave;
	}

	public SimpleSquare2d create() {
		try {
			Constructor<?> c = this.klass.getConstructor(Square2dType.class);
			return (SimpleSquare2d) c.newInstance(this);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		try {
			return (SimpleSquare2d) this.klass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void dispose() {
		for (Square2dType type : Square2dType.values()) {
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
