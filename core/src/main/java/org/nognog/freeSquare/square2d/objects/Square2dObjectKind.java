package org.nognog.freeSquare.square2d.objects;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Messages;
import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.Square2dObject;

import com.badlogic.gdx.graphics.Color;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public enum Square2dObjectKind {

	RIKI(Messages.getString("riki"), Resources.rikiPath, 100, Riki.class), //$NON-NLS-1$
	TOFU(Messages.getString("tofu"), Resources.tofuPath, 75), //$NON-NLS-1$
	RED_PEPPER_TOFU(Messages.getString("red-pepper-tofu"), Resources.tofuPath, 75, Colors.RED), //$NON-NLS-1$
	MINT_TOFU(Messages.getString("mint-tofu"), Resources.tofuPath, 75, Colors.CYAN), //$NON-NLS-1$
	WORMWOOD_TOFU(Messages.getString("wormwood-tofu"), Resources.tofuPath, 75, Colors.OLIVE), //$NON-NLS-1$
	FREEZE_DRIED_TOFU(Messages.getString("freeze-dried-tofu"), Resources.tofuPath, 75, Colors.KHAKI), //$NON-NLS-1$
	MASTATD_TOFU(Messages.getString("mastard-tofu"), Resources.tofuPath, 75, Colors.YELLOW), //$NON-NLS-1$
	ICE_TOFU(Messages.getString("ice-tofu"), Resources.tofuPath, 75, Colors.ICE), //$NON-NLS-1$
	GOLD_SESAME_TOFU(Messages.getString("gold-sesame-tofu"), Resources.tofuPath, 75, Colors.GOLD), //$NON-NLS-1$
	BLACK_SESAME_TOFU(Messages.getString("black-sesame-tofu"), Resources.tofuPath, 75, Colors.LIGHT_GRAY), //$NON-NLS-1$
	;

	private Square2dObjectKind(String name, String texturePath, float logicalWidth) {
		this(name, texturePath, logicalWidth, Square2dObject.class);
	}

	private Square2dObjectKind(String name, String texturePath, float logicalWidth, Color color) {
		this(name, texturePath, logicalWidth, color, Square2dObject.class);
	}

	private <T extends Square2dObject> Square2dObjectKind(String name, String texturePath, float logicalWidth, Class<T> klass) {
		this(name, texturePath, logicalWidth, Colors.WHITE, klass);
	}

	private <T extends Square2dObject> Square2dObjectKind(String name, String texturePath, float logicalWidth, Color color, Class<T> klass) {
		this.name = name;
		this.texturePath = texturePath;
		this.logicalWidth = logicalWidth;
		this.color = color;
		this.klass = klass;
	}

	private final Class<?> klass;
	private final String name;
	private final String texturePath;
	private final float logicalWidth;
	private final Color color;

	public String getName() {
		return this.name;
	}

	public String getTexturePath() {
		return this.texturePath;
	}

	public float getLogicalWidth() {
		return this.logicalWidth;
	}

	public Color getColor() {
		return this.color;
	}

	public Square2dObject create() {
		try {
			Constructor<?> c = this.klass.getConstructor(Square2dObjectKind.class);
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
