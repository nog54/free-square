package org.nognog.freeSquare.square2d.object.types;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi
 * 2015/02/08
 */
public enum OtherObjectType implements Square2dObjectType<Square2dObject> {

	;
	
	private OtherObjectType(String name, String texturePath, float logicalWidth) {
		this(name, texturePath, logicalWidth, EatableObject.class);
	}

	private OtherObjectType(String name, String texturePath, float logicalWidth, Color color) {
		this(name, texturePath, logicalWidth, color, EatableObject.class);
	}

	private <T extends Square2dObject> OtherObjectType(String name, String texturePath, float logicalWidth, Class<T> klass) {
		this(name, texturePath, logicalWidth, Colors.WHITE, klass);
	}

	private <T extends Square2dObject> OtherObjectType(String name, String texturePath, float logicalWidth, Color color, Class<T> klass) {
		this(name, new Texture(texturePath), logicalWidth, color, klass);
	}

	@SuppressWarnings("unchecked")
	private <T extends Square2dObject> OtherObjectType(OtherObjectType type, String name, Color color) {
		this(name, type.texture, type.logicalWidth, color, (Class<T>) type.klass);
	}

	@SuppressWarnings("unchecked")
	private <T extends EatableObject> OtherObjectType(OtherObjectType type, String name, float logicalWidth, Color color) {
		this(name, type.texture, logicalWidth, color, (Class<T>) type.klass);
	}

	private <T extends Square2dObject> OtherObjectType(String name, Texture texture, float logicalWidth, Color color, Class<T> klass) {
		this.name = name;
		this.texture = texture;
		this.logicalWidth = logicalWidth;
		this.color = color;
		this.klass = klass;
	}

	private final Class<?> klass;
	private final String name;
	private final Texture texture;
	private final float logicalWidth;
	private final Color color;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Texture getTexture() {
		return this.texture;
	}

	@Override
	public float getLogicalWidth() {
		return this.logicalWidth;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public Square2dObject create() {
		try {
			Constructor<?> c = this.klass.getConstructor(Square2dObjectType.class);
			return (Square2dObject) c.newInstance(this);
		} catch (Exception e) {
			// nothing
		}

		try {
			return (Square2dObject) this.klass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * dispose
	 */
	public static void dispose() {
		for (OtherObjectType type : OtherObjectType.values()) {
			type.getTexture().dispose();
		}
	}

}