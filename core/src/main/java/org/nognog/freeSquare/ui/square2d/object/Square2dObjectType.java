package org.nognog.freeSquare.ui.square2d.object;

import static org.nognog.freeSquare.Messages.getString;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.life.Family;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public interface Square2dObjectType<T extends Square2dObject> {

	String getName();

	Texture getTexture();

	float getLogicalWidth();

	Color getColor();

	T create();

	public static enum LifeObjectType implements Square2dObjectType<LifeObject> {
		RIKI(Family.RIKI, Resources.rikiPath, 100, FryingLifeObject.class),
		SMALL_RIKI(Family.SMALL_RIKI, RIKI, 80, RIKI.getColor()),
		BIG_RIKI(Family.BIG_RIKI, RIKI, 120, RIKI.getColor()),
		FLY_RIKI(Family.FLY_RIKI, Resources.rikiPath, 100, FryingLifeObject.class),
		FLY_SMALL_RIKI(Family.SMALL_FLY_RIKI, RIKI, 80, RIKI.getColor()),
		FLY_BIG_RIKI(Family.BIG_FLY_RIKI, RIKI, 120, RIKI.getColor()),

		;


		private <T extends LifeObject> LifeObjectType(Family family, String texturePath, float logicalWidth, Class<T> klass) {
			this(family, texturePath, logicalWidth, Colors.WHITE, klass);
		}

		private <T extends LifeObject> LifeObjectType(Family family, String texturePath, float logicalWidth, Color color, Class<T> klass) {
			this(family, new Texture(texturePath), logicalWidth, color, klass);
		}

		@SuppressWarnings("unchecked")
		private <T extends LifeObject> LifeObjectType(Family family, LifeObjectType type, float logicalWidth, Color color) {
			this(family, type.texture, logicalWidth, color, (Class<T>) type.klass);
		}

		private <T extends LifeObject> LifeObjectType(Family family, Texture texture, float logicalWidth, Color color, Class<T> klass) {
			this.family = family;
			this.texture = texture;
			this.logicalWidth = logicalWidth;
			this.color = color;
			this.klass = klass;
		}

		private final Class<?> klass;
		private final Family family;
		private final Texture texture;
		private final float logicalWidth;
		private final Color color;

		@Override
		public String getName() {
			return this.family.getName();
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
		public LifeObject create() {
			try {
				Constructor<?> c = this.klass.getConstructor(LifeObjectType.class);
				return (LifeObject) c.newInstance(this);
			} catch (Exception e) {
				// nothing
			}

			try {
				return (LifeObject) this.klass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static void dispose() {
			for (LifeObjectType type : LifeObjectType.values()) {
				type.getTexture().dispose();
			}
		}
	}

	public static enum EatableObjectType implements Square2dObjectType<EatableObject> {

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

		private EatableObjectType(String name, String texturePath, float logicalWidth, int quantity) {
			this(name, texturePath, logicalWidth, EatableObject.class, quantity);
		}

		private EatableObjectType(String name, String texturePath, float logicalWidth, Color color, int quantity) {
			this(name, texturePath, logicalWidth, color, EatableObject.class, quantity);
		}

		private <T extends EatableObject> EatableObjectType(String name, String texturePath, float logicalWidth, Class<T> klass, int quantity) {
			this(name, texturePath, logicalWidth, Colors.WHITE, klass, quantity);
		}

		private <T extends EatableObject> EatableObjectType(String name, String texturePath, float logicalWidth, Color color, Class<T> klass, int quantity) {
			this(name, new Texture(texturePath), logicalWidth, color, klass, quantity);
		}

		@SuppressWarnings("unchecked")
		private <T extends EatableObject> EatableObjectType(EatableObjectType type, String name, Color color) {
			this(name, type.texture, type.logicalWidth, color, (Class<T>) type.klass, type.quantity);
		}

		@SuppressWarnings("unchecked")
		private <T extends EatableObject> EatableObjectType(EatableObjectType type, String name, float logicalWidth, Color color, int quantity) {
			this(name, type.texture, logicalWidth, color, (Class<T>) type.klass, quantity);
		}

		private <T extends EatableObject> EatableObjectType(String name, Texture texture, float logicalWidth, Color color, Class<T> klass, int quantity) {
			this.name = name;
			this.texture = texture;
			this.logicalWidth = logicalWidth;
			this.color = color;
			this.klass = klass;
			this.quantity = quantity;
		}

		private final Class<?> klass;
		private final String name;
		private final Texture texture;
		private final float logicalWidth;
		private final Color color;
		private final int quantity;

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

		public int getQuantity() {
			return this.quantity;
		}

		@Override
		public EatableObject create() {
			try {
				Constructor<?> c = this.klass.getConstructor(EatableObjectType.class);
				return (EatableObject) c.newInstance(this);
			} catch (Exception e) {
				// nothing
			}

			try {
				return (EatableObject) this.klass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static void dispose() {
			for (EatableObjectType type : EatableObjectType.values()) {
				type.getTexture().dispose();
			}
		}

	}

	public static enum OtherObjectType implements Square2dObjectType<Square2dObject> {

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

		public static void dispose() {
			for (OtherObjectType type : OtherObjectType.values()) {
				type.getTexture().dispose();
			}
		}

	}

	public static class Manager {
		public static Square2dObjectType<?>[] getAllTypeValues() {
			int allValuesLength = EatableObjectType.values().length + LifeObjectType.values().length + OtherObjectType.values().length;
			final Square2dObjectType<?>[] allValues = new Square2dObjectType[allValuesLength];
			int i = 0;
			for (LifeObjectType type : LifeObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			for (EatableObjectType type : EatableObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			for (OtherObjectType type : OtherObjectType.values()) {
				allValues[i] = type;
				i++;
			}
			return allValues;

		}
	}

	static class Colors {

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
