package org.nognog.freeSquare.ui.square2d.objects;

import static org.nognog.freeSquare.Messages.getString;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.ui.square2d.EatableObject;
import org.nognog.freeSquare.ui.square2d.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public enum Square2dObjectType {
	// EatableObject : otherValues[0] = quantity

	RIKI(getString("riki"), Resources.rikiPath, 100, Riki.class), //$NON-NLS-1$
	EATABLE_RIKI(getString("riki"), Resources.rikiPath, 80, EatableObject.class, 100), //$NON-NLS-1$
	TOFU(getString("tofu"), Resources.tofuPath, 75, EatableObject.class, 100), //$NON-NLS-1$
	RED_PEPPER_TOFU(TOFU, getString("red-pepper-tofu"), Colors.RED), //$NON-NLS-1$
	MINT_TOFU(TOFU, getString("mint-tofu"), Colors.CYAN), //$NON-NLS-1$
	WORMWOOD_TOFU(TOFU, getString("wormwood-tofu"), Colors.OLIVE), //$NON-NLS-1$
	FREEZE_DRIED_TOFU(TOFU, getString("freeze-dried-tofu"), Colors.KHAKI), //$NON-NLS-1$
	MASTATD_TOFU(TOFU, getString("mastard-tofu"), Colors.YELLOW), //$NON-NLS-1$
	ICE_TOFU(TOFU, getString("ice-tofu"), Colors.ICE), //$NON-NLS-1$
	GOLD_SESAME_TOFU(TOFU, getString("gold-sesame-tofu"), Colors.GOLD), //$NON-NLS-1$
	BLACK_SESAME_TOFU(TOFU, getString("black-sesame-tofu"), Colors.LIGHT_GRAY), //$NON-NLS-1$
	;

	private Square2dObjectType(String name, String texturePath, float logicalWidth) {
		this(name, texturePath, logicalWidth, Square2dObject.class);
	}

	private Square2dObjectType(String name, String texturePath, float logicalWidth, Color color) {
		this(name, texturePath, logicalWidth, color, Square2dObject.class);
	}

	private <T extends Square2dObject> Square2dObjectType(String name, String texturePath, float logicalWidth, Class<T> klass, float... otherValues) {
		this(name, texturePath, logicalWidth, Colors.WHITE, klass, otherValues);
	}

	@SuppressWarnings("unchecked")
	private <T extends Square2dObject> Square2dObjectType(Square2dObjectType type, String name, Color color) {
		this(name, type.texture, type.logicalWidth, color, (Class<T>) type.klass, type.otherValues);
	}

	@SuppressWarnings("unchecked")
	private <T extends Square2dObject> Square2dObjectType(Square2dObjectType type, String name, float logicalWidth) {
		this(name, type.texture, logicalWidth, type.color, (Class<T>) type.klass, type.otherValues);
	}

	@SuppressWarnings("unchecked")
	private <T extends Square2dObject> Square2dObjectType(Square2dObjectType type, String name, float logicalWidth, Color color) {
		this(name, type.texture, logicalWidth, color, (Class<T>) type.klass, type.otherValues);
	}

	private <T extends Square2dObject> Square2dObjectType(String name, String texturePath, float logicalWidth, Color color, Class<T> klass, float... otherValues) {
		this(name, new Texture(texturePath), logicalWidth, color, klass, otherValues);
	}

	private <T extends Square2dObject> Square2dObjectType(String name, Texture texture, float logicalWidth, Color color, Class<T> klass, float... otherValues) {
		this.name = name;
		this.texture = texture;
		this.logicalWidth = logicalWidth;
		this.color = color;
		this.klass = klass;
		this.otherValues = otherValues;
	}

	private final Class<?> klass;
	private final String name;
	private final Texture texture;
	private final float logicalWidth;
	private final Color color;
	private final float[] otherValues;

	public String getName() {
		return this.name;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public float getLogicalWidth() {
		return this.logicalWidth;
	}

	public Color getColor() {
		return this.color;
	}
	
	public float[] getOtherValues(){
		return this.otherValues;
	}

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

	public static void dispose() {
		for (Square2dObjectType type : Square2dObjectType.values()) {
			type.getTexture().dispose();
		}
	}

	private static class Colors {

		public static final Color WHITE = Color.WHITE;

		public static final Color RED = Color.RED;
		public static final Color CYAN = Color.CYAN;
		public static final Color OLIVE = Color.OLIVE;
		public static final Color KHAKI = Color.valueOf("F0E68C"); //$NON-NLS-1$
		public static final Color YELLOW = Color.YELLOW;
		public static final Color ICE = Color.valueOf("87CEEBD0"); //$NON-NLS-1$
		public static final Color GOLD = Color.valueOf("FFD700"); //$NON-NLS-1$
		public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;

		private Colors() {
		}
	}

}
