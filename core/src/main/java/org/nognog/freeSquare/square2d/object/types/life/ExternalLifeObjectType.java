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
public class ExternalLifeObjectType implements LifeObjectType, ExternalSquare2dObjectType<LifeObject> {
	private String texturePath;
	private Family.OriginalFamily family;
	private float moveSpeed;
	private int eatAmountPerSec;
	private String squareObjectClassName;

	private transient Class<?> squareObjectClass;
	private transient Texture texture;

	private ExternalLifeObjectType(){
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

	/**
	 * @param json
	 */
	public static void addExternalLifeObjectTypeSerializerTo(Json json) {
		json.setSerializer(ExternalLifeObjectType.class, new Json.Serializer<ExternalLifeObjectType>() {

			@Override
			@SuppressWarnings({ "rawtypes", "synthetic-access", "boxing" })
			public void write(@SuppressWarnings("hiding") Json json, ExternalLifeObjectType object, Class knownType) {
				json.writeObjectStart();
				json.writeType(ExternalLifeObjectType.class);
				json.writeValue("family", object.family, object.family.getClass()); //$NON-NLS-1$
				json.writeValue("texturePath", object.texturePath); //$NON-NLS-1$
				json.writeValue("moveSpeed", object.moveSpeed); //$NON-NLS-1$
				json.writeValue("eatAmountPerSec", object.eatAmountPerSec); //$NON-NLS-1$
				json.writeValue("squareObjectClassName", object.squareObjectClassName); //$NON-NLS-1$
				json.writeObjectEnd();
			}

			@Override
			@SuppressWarnings({ "rawtypes", "synthetic-access" })
			public ExternalLifeObjectType read(@SuppressWarnings("hiding") Json json, JsonValue jsonData, Class type) {

				final Family.OriginalFamily family = json.readValue("family", Family.OriginalFamily.class, jsonData); //$NON-NLS-1$
				final ExternalLifeObjectTypeDictionary dictionary = LifeObjectTypeManager.getInstance().getDictionary();
				if(dictionary != null){
					for(ExternalLifeObjectType alreadyExistsType : dictionary.getAllExternalObjectType()){
						if(family.getName().equals(alreadyExistsType.getFamily().getName())){
							return alreadyExistsType;
						}
					}
				}
				ExternalLifeObjectType result = new ExternalLifeObjectType();
				result.family = family;
				result.texturePath = json.readValue("texturePath", String.class, jsonData); //$NON-NLS-1$
				result.texture = new Texture(result.texturePath);
				result.moveSpeed = json.readValue("moveSpeed", Float.class, jsonData).floatValue(); //$NON-NLS-1$
				result.eatAmountPerSec = json.readValue("eatAmountPerSec", Integer.class, jsonData).intValue(); //$NON-NLS-1$
				result.squareObjectClassName = json.readValue("squareObjectClassName", String.class, jsonData); //$NON-NLS-1$
				try {
					result.squareObjectClass = Class.forName(result.squareObjectClassName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				return result;
			}
		});
	}

}
