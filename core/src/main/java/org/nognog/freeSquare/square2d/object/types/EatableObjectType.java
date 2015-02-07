package org.nognog.freeSquare.square2d.object.types;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.food.Food;
import org.nognog.freeSquare.square2d.object.EatableObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2015/02/08
 */
@SuppressWarnings("javadoc")
public enum EatableObjectType implements Square2dObjectType<EatableObject> {

	TOFU(Food.TOFU, Resources.tofuPath, 75),

	RED_PEPPER_TOFU(Food.RED_PEPPER_TOFU, TOFU, Colors.RED),

	MINT_TOFU(Food.MINT_TOFU, TOFU, Colors.CYAN),

	WORMWOOD_TOFU(Food.WORMWOOD_TOFU, TOFU, Colors.OLIVE),

	FREEZE_DRIED_TOFU(Food.FREEZE_DRIED_TOFU, TOFU, Colors.KHAKI),

	MASTATD_TOFU(Food.MASTATD_TOFU, TOFU, Colors.YELLOW),

	ICE_TOFU(Food.ICE_TOFU, TOFU, Colors.ICE),

	GOLD_SESAME_TOFU(Food.GOLD_SESAME_TOFU, TOFU, Colors.GOLD),

	BLACK_SESAME_TOFU(Food.BLACK_SESAME_TOFU, TOFU, Colors.LIGHT_GRAY), ;

	private EatableObjectType(Food bindFood, String texturePath, float logicalWidth) {
		this(bindFood, texturePath, logicalWidth, EatableObject.class);
	}

	private EatableObjectType(Food bindFood, String texturePath, float logicalWidth, Color color, int quantity) {
		this(bindFood, texturePath, logicalWidth, color, EatableObject.class);
	}

	private <T extends EatableObject> EatableObjectType(Food bindFood, String texturePath, float logicalWidth, Class<T> klass) {
		this(bindFood, texturePath, logicalWidth, Colors.WHITE, klass);
	}

	private <T extends EatableObject> EatableObjectType(Food bindFood, String texturePath, float logicalWidth, Color color, Class<T> klass) {
		this(bindFood, new Texture(texturePath), logicalWidth, color, klass);
	}

	@SuppressWarnings("unchecked")
	private <T extends EatableObject> EatableObjectType(Food bindFood, EatableObjectType type, Color color) {
		this(bindFood, type.texture, type.logicalWidth, color, (Class<T>) type.klass);
	}

	@SuppressWarnings("unchecked")
	private <T extends EatableObject> EatableObjectType(Food bindFood, EatableObjectType type, float logicalWidth, Color color) {
		this(bindFood, type.texture, logicalWidth, color, (Class<T>) type.klass);
	}

	private <T extends EatableObject> EatableObjectType(Food bindFood, Texture texture, float logicalWidth, Color color, Class<T> klass) {
		this.bindFood = bindFood;
		this.texture = texture;
		this.logicalWidth = logicalWidth;
		this.color = color;
		this.klass = klass;
	}

	private final Class<?> klass;
	private final Food bindFood;
	private final Texture texture;
	private final float logicalWidth;
	private final Color color;

	@Override
	public String getName() {
		return this.bindFood.getName();
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

	public int getQuantity() {
		return this.bindFood.getQuantity();
	}

	public static void dispose() {
		for (EatableObjectType type : EatableObjectType.values()) {
			type.getTexture().dispose();
		}
	}

}