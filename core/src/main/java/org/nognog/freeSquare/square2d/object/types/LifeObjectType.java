package org.nognog.freeSquare.square2d.object.types;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.object.FryingLifeObject;
import org.nognog.freeSquare.square2d.object.LandingLifeObject;
import org.nognog.freeSquare.square2d.object.LifeObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/08
 */
@SuppressWarnings("javadoc")
public enum LifeObjectType implements Square2dObjectType<LifeObject> {
	RIKI(Family.RIKI, Resources.rikiPath, LandingLifeObject.class, 100),

	SMALL_RIKI(Family.SMALL_RIKI, RIKI, 50),

	BIG_RIKI(Family.BIG_RIKI, RIKI, 150),

	FLY_RIKI(Family.FLY_RIKI, RIKI, FryingLifeObject.class, 100),

	FLY_SMALL_RIKI(Family.SMALL_FLY_RIKI, RIKI, 50),

	FLY_BIG_RIKI(Family.BIG_FLY_RIKI, RIKI, 150),

	;

	private <T extends LifeObject> LifeObjectType(Family family, String texturePath, Class<T> klass, float logicalWidth) {
		this(family, texturePath, Colors.WHITE, klass, logicalWidth, 100, 5);
	}

	private <T extends LifeObject> LifeObjectType(Family family, String texturePath, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
		this(family, texturePath, Colors.WHITE, klass, logicalWidth, moveSpeed, eatAmountPerSec);
	}

	private <T extends LifeObject> LifeObjectType(Family family, String texturePath, Color color, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
		this(family, new Texture(texturePath), color, klass, logicalWidth, moveSpeed, eatAmountPerSec);
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends LifeObject> LifeObjectType(Family family, LifeObjectType type, float logicalWidth) {
		this(family, type.texture, type.color, (Class<T>) type.klass, logicalWidth, type.moveSpeed, type.eatAmountPerSec);
	}

	private <T extends LifeObject> LifeObjectType(Family family, LifeObjectType type, Class<T> klass, float logicalWidth) {
		this(family, type.texture, type.color, klass, logicalWidth, type.moveSpeed, type.eatAmountPerSec);
	}

	private <T extends LifeObject> LifeObjectType(Family family, Texture texture, Color color, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
		this.family = family;
		this.texture = texture;
		this.logicalWidth = logicalWidth;
		this.moveSpeed = moveSpeed;
		this.eatAmountPerSec = eatAmountPerSec;
		this.color = color;
		this.klass = klass;
	}

	private final Class<?> klass;
	private final Family family;
	private final Texture texture;
	private final float logicalWidth;
	private final float moveSpeed;
	private final int eatAmountPerSec;
	private final Color color;

	static {
		validateFamilyDuplication();
	}

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

	public Family getFamily() {
		return this.family;
	}

	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	public int getEatAmountPerSec() {
		return this.eatAmountPerSec;
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

	private static void validateFamilyDuplication() {
		Array<Family> families = new Array<>();
		for (LifeObjectType type : LifeObjectType.values()) {
			assert !families.contains(type.family, true);
		}
	}

	public static LifeObjectType getBindingLifeObjectType(Life life) {
		for (LifeObjectType type : LifeObjectType.values()) {
			if (life.getFamily() == type.getFamily()) {
				return type;
			}
		}
		return null;
	}

	public static void dispose() {
		for (LifeObjectType type : LifeObjectType.values()) {
			type.getTexture().dispose();
		}
	}
}