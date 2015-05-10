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

import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.square2d.object.LifeObject;
import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi
 * 2015/05/07
 */
/**
 * @author goshi 2015/04/23
 */
public class ExternalLifeObjectType implements LifeObjectType, ExternalSquare2dObjectType<LifeObject>, Json.Serializable {
	private String texturePath;
	private Family family;
	private float moveSpeed;
	private int eatAmountPerSec;
	private String squareObjectClassName;

	private transient Class<?> squareObjectClass;
	private transient Texture texture;

	@SuppressWarnings("unused")
	private <T extends LifeObject> ExternalLifeObjectType() {
		// used by json
	}

	/**
	 * @param name
	 * @param texturePath
	 * @param moveSpeed
	 * @param eatAmountPerSec
	 * @param squareObjectClass
	 */
	<T extends LifeObject> ExternalLifeObjectType(String name, String texturePath, float moveSpeed, int eatAmountPerSec, Class<T> squareObjectClass) {
		this.texturePath = texturePath;
		this.texture = new Texture(this.texturePath);
		this.family = new Family.OriginalFamily(name);
		this.moveSpeed = moveSpeed;
		this.eatAmountPerSec = eatAmountPerSec;
		this.squareObjectClassName = squareObjectClass.getName();
		this.squareObjectClass = squareObjectClass;
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
		return 100;
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}

	@Override
	public Class<?> getSquareObjectClass() {
		return this.squareObjectClass;
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

	@Override
	public void write(Json json) {
		json.writeFields(this);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.texturePath = json.readValue("texturePath", String.class, jsonData); //$NON-NLS-1$
		this.texture = new Texture(this.texturePath);
		this.family = json.readValue("family", Family.class, jsonData); //$NON-NLS-1$
		this.moveSpeed = json.readValue("moveSpeed", Float.class, jsonData).floatValue(); //$NON-NLS-1$
		this.eatAmountPerSec = json.readValue("eatAmountPerSec", Integer.class, jsonData).intValue(); //$NON-NLS-1$
		this.squareObjectClassName = json.readValue("squareObjectClassName", String.class, jsonData); //$NON-NLS-1$
		try {
			this.squareObjectClass = Class.forName(this.squareObjectClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
