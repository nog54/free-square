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

package org.nognog.freeSquare.square2d.object.types.life;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.square2d.object.types.Colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi
 * 2015/05/07
 */
/**
 * @author goshi 2015/02/08
 */
@SuppressWarnings("javadoc")
public enum PreparedLifeObjectType implements LifeObjectType {
	RIKI(Family.Prepared.RIKI, Resources.rikiPath, LandingLifeObject.class, 100),

	SMALL_RIKI(Family.Prepared.SMALL_RIKI, RIKI, 50),

	BIG_RIKI(Family.Prepared.BIG_RIKI, RIKI, 150),

	FLY_RIKI(Family.Prepared.FLY_RIKI, RIKI, FlyingLifeObject.class, 100),

	FLY_SMALL_RIKI(Family.Prepared.SMALL_FLY_RIKI, RIKI, 50),

	FLY_BIG_RIKI(Family.Prepared.BIG_FLY_RIKI, RIKI, 150),

	;

	private <T extends LifeObject> PreparedLifeObjectType(Family family, String texturePath, Class<T> klass, float logicalWidth) {
		this(family, texturePath, Colors.WHITE, klass, logicalWidth, 100, 5);
	}

	private <T extends LifeObject> PreparedLifeObjectType(Family family, String texturePath, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
		this(family, texturePath, Colors.WHITE, klass, logicalWidth, moveSpeed, eatAmountPerSec);
	}

	private <T extends LifeObject> PreparedLifeObjectType(Family family, String texturePath, Color color, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
		this(family, new Texture(texturePath), color, klass, logicalWidth, moveSpeed, eatAmountPerSec);
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends LifeObject> PreparedLifeObjectType(Family family, LifeObjectType type, float logicalWidth) {
		this(family, type.getTexture(), type.getColor(), (Class<T>) type.getSquareObjectClass(), logicalWidth, type.getMoveSpeed(), type.getEatAmountPerSec());
	}

	private <T extends LifeObject> PreparedLifeObjectType(Family family, LifeObjectType type, Class<T> klass, float logicalWidth) {
		this(family, type.getTexture(), type.getColor(), klass, logicalWidth, type.getMoveSpeed(), type.getEatAmountPerSec());
	}

	private <T extends LifeObject> PreparedLifeObjectType(Family family, Texture texture, Color color, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
		this.family = family;
		this.texture = texture;
		this.logicalWidth = logicalWidth;
		this.moveSpeed = moveSpeed;
		this.eatAmountPerSec = eatAmountPerSec;
		this.color = color;
		this.squareObjectClass = klass;
	}

	private final Class<?> squareObjectClass;
	private final Family family;
	private final Texture texture;
	private final float logicalWidth;
	private final float moveSpeed;
	private final int eatAmountPerSec;
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
	public Family getFamily() {
		return this.family;
	}

	@Override
	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	@Override
	public int getEatAmountPerSec() {
		return this.eatAmountPerSec;
	}

	@Override
	public Class<?> getSquareObjectClass() {
		return this.squareObjectClass;
	}

	@Override
	public LifeObject create() {
		try {
			Constructor<?> c = this.squareObjectClass.getConstructor(LifeObjectType.class);
			return (LifeObject) c.newInstance(this);
		} catch (Exception e) {
			// nothing
		}

		try {
			return (LifeObject) this.squareObjectClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}