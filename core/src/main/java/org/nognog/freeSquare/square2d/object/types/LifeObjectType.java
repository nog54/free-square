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

package org.nognog.freeSquare.square2d.object.types;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.object.FryingLifeObject;
import org.nognog.freeSquare.square2d.object.LandingLifeObject;
import org.nognog.freeSquare.square2d.object.LifeObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/04/23
 */
public interface LifeObjectType extends Square2dObjectType<LifeObject> {

	/**
	 * @return the family
	 */
	Family getFamily();

	/**
	 * @return the move speed
	 */
	float getMoveSpeed();

	/**
	 * @return the eat amount per second
	 */
	int getEatAmountPerSec();

	/**
	 * @author goshi 2015/02/08
	 */
	@SuppressWarnings("javadoc")
	public static enum Prepared implements LifeObjectType {
		RIKI(Family.RIKI, Resources.rikiPath, LandingLifeObject.class, 100),

		SMALL_RIKI(Family.SMALL_RIKI, RIKI, 50),

		BIG_RIKI(Family.BIG_RIKI, RIKI, 150),

		FLY_RIKI(Family.FLY_RIKI, RIKI, FryingLifeObject.class, 100),

		FLY_SMALL_RIKI(Family.SMALL_FLY_RIKI, RIKI, 50),

		FLY_BIG_RIKI(Family.BIG_FLY_RIKI, RIKI, 150),

		;

		private <T extends LifeObject> Prepared(Family family, String texturePath, Class<T> klass, float logicalWidth) {
			this(family, texturePath, Colors.WHITE, klass, logicalWidth, 100, 5);
		}

		private <T extends LifeObject> Prepared(Family family, String texturePath, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
			this(family, texturePath, Colors.WHITE, klass, logicalWidth, moveSpeed, eatAmountPerSec);
		}

		private <T extends LifeObject> Prepared(Family family, String texturePath, Color color, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
			this(family, new Texture(texturePath), color, klass, logicalWidth, moveSpeed, eatAmountPerSec);
		}

		@SuppressWarnings({ "unchecked" })
		private <T extends LifeObject> Prepared(Family family, LifeObjectType type, float logicalWidth) {
			this(family, type.getTexture(), type.getColor(), (Class<T>) type.getSquareObjectClass(), logicalWidth, type.getMoveSpeed(), type.getEatAmountPerSec());
		}

		private <T extends LifeObject> Prepared(Family family, LifeObjectType type, Class<T> klass, float logicalWidth) {
			this(family, type.getTexture(), type.getColor(), klass, logicalWidth, type.getMoveSpeed(), type.getEatAmountPerSec());
		}

		private <T extends LifeObject> Prepared(Family family, Texture texture, Color color, Class<T> klass, float logicalWidth, float moveSpeed, int eatAmountPerSec) {
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

	/**
	 * @author goshi 2015/04/23
	 */
	public static class LoadedObject implements LifeObjectType {
		private final String name;
		private final Texture texture;
		private final float moveSpeed;
		private final int eatAmountPerSec;
		private final Class<?> squareObjectClass;

		/**
		 * @param name
		 * @param texturePath
		 * @param moveSpeed
		 * @param eatAmountPerSec
		 * @param squareObjectClass
		 */
		public <T extends LifeObject> LoadedObject(String name, String texturePath, float moveSpeed, int eatAmountPerSec, Class<T> squareObjectClass) {
			this.name = name;
			this.texture = new Texture(texturePath);
			this.moveSpeed = moveSpeed;
			this.eatAmountPerSec = eatAmountPerSec;
			this.squareObjectClass = squareObjectClass;
		}

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
			return 100;
		}

		@Override
		public Color getColor() {
			return Color.WHITE;
		}

		@Override
		public Class<?> getSquareObjectClass() {
			return Square2dObject.class;
		}

		@Override
		public Family getFamily() {
			return null;
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

	/**
	 * @author goshi 2015/04/23
	 */
	public static class Manager {
		static {
			validateFamilyDuplication();
		}

		private static void validateFamilyDuplication() {
			Array<Family> families = new Array<>();
			for (LifeObjectType type : Prepared.values()) {
				assert !families.contains(type.getFamily(), true);
			}
		}

		/**
		 * @param life
		 * @return bind type
		 */
		public static LifeObjectType getBindingLifeObjectType(Life life) {
			for (LifeObjectType type : Prepared.values()) {
				if (life.getFamily() == type.getFamily()) {
					return type;
				}
			}
			return null;
		}

		/**
		 * dispose prepares
		 */
		public static void dispose() {
			for (LifeObjectType type : Prepared.values()) {
				type.getTexture().dispose();
			}
		}

		/**
		 * @return all type
		 */
		public static LifeObjectType[] getAll() {
			return LifeObjectType.Prepared.values();
		}
	}
}
