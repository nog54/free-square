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

package org.nognog.freeSquare.square2d.object.types.eatable;

import static org.nognog.freeSquare.model.life.status.influence.InfluencesUtils.agility;

import java.lang.reflect.Constructor;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.model.food.Food;
import org.nognog.freeSquare.model.food.Taste;
import org.nognog.freeSquare.model.life.status.influence.MultipleStatusInfluence;
import org.nognog.freeSquare.model.life.status.influence.SingleStatusInfluence;
import org.nognog.freeSquare.square2d.object.types.Colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2015/05/07
 */
@SuppressWarnings("javadoc")
public enum PreparedEatableObjectType implements EatableObjectType {

	TOFU(Food.TOFU, Resources.tofuPath, 75, agility(0.1)),

	RED_PEPPER_TOFU(Food.RED_PEPPER_TOFU, TOFU, Colors.RED),

	MINT_TOFU(Food.MINT_TOFU, TOFU, Colors.CYAN),

	WORMWOOD_TOFU(Food.WORMWOOD_TOFU, TOFU, Colors.OLIVE),

	FREEZE_DRIED_TOFU(Food.FREEZE_DRIED_TOFU, TOFU, Colors.KHAKI),

	MASTATD_TOFU(Food.MASTATD_TOFU, TOFU, Colors.YELLOW),

	ICE_TOFU(Food.ICE_TOFU, TOFU, Colors.ICE),

	GOLD_SESAME_TOFU(Food.GOLD_SESAME_TOFU, TOFU, Colors.GOLD),

	BLACK_SESAME_TOFU(Food.BLACK_SESAME_TOFU, TOFU, Colors.LIGHT_GRAY), ;

	private PreparedEatableObjectType(Food bindFood, String texturePath, float logicalWidth, SingleStatusInfluence<?>... singleInfluences) {
		this(bindFood, texturePath, logicalWidth, EatableObject.class, singleInfluences);
	}

	private PreparedEatableObjectType(Food bindFood, String texturePath, float logicalWidth, Color color, SingleStatusInfluence<?>... singleInfluences) {
		this(bindFood, texturePath, logicalWidth, color, EatableObject.class, singleInfluences);
	}

	private <T extends EatableObject> PreparedEatableObjectType(Food bindFood, String texturePath, float logicalWidth, Class<T> klass, SingleStatusInfluence<?>... singleInfluences) {
		this(bindFood, texturePath, logicalWidth, Colors.WHITE, klass, singleInfluences);
	}

	private <T extends EatableObject> PreparedEatableObjectType(Food bindFood, String texturePath, float logicalWidth, Color color, Class<T> klass, SingleStatusInfluence<?>... singleInfluences) {
		this(bindFood, new Texture(texturePath), logicalWidth, color, klass, singleInfluences);
	}

	@SuppressWarnings("unchecked")
	private <T extends EatableObject> PreparedEatableObjectType(Food bindFood, EatableObjectType type, Color color, SingleStatusInfluence<?>... singleInfluences) {
		this(bindFood, type.getTexture(), type.getLogicalWidth(), color, (Class<T>) type.getSquareObjectClass(), singleInfluences);
	}

	@SuppressWarnings("unchecked")
	private <T extends EatableObject> PreparedEatableObjectType(Food bindFood, EatableObjectType type, float logicalWidth, Color color, SingleStatusInfluence<?>... singleInfluences) {
		this(bindFood, type.getTexture(), logicalWidth, color, (Class<T>) type.getSquareObjectClass(), singleInfluences);
	}

	private <T extends EatableObject> PreparedEatableObjectType(Food bindFood, Texture texture, float logicalWidth, Color color, Class<T> klass, SingleStatusInfluence<?>... singleInfluences) {
		this.bindFood = bindFood;
		this.texture = texture;
		this.logicalWidth = logicalWidth;
		this.color = color;
		this.klass = klass;
		this.influence = new MultipleStatusInfluence(singleInfluences);
	}

	private final Class<?> klass;
	private final Food bindFood;
	private final Texture texture;
	private final float logicalWidth;
	private final Color color;
	private final MultipleStatusInfluence influence;

	@Override
	public Class<?> getSquareObjectClass() {
		return this.klass;
	}

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

	@Override
	public int getQuantity() {
		return this.bindFood.getQuantity();
	}

	@Override
	public Taste[] getTaste() {
		return this.bindFood.getTaste();
	}

	@Override
	public MultipleStatusInfluence getStatusInfluence() {
		return this.influence;
	}

}