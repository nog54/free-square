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

package org.nognog.freeSquare.square2d.object.types.other;

import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.ExternalSquare2dObjectType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/05/07
 */
public class ExternalOtherObjectType implements OtherObjectType, ExternalSquare2dObjectType<Square2dObject> {
	private String name;
	private String texturePath;

	private transient Texture texture;

	private static final Json.Serializer<ExternalOtherObjectType> serializer = new Json.Serializer<ExternalOtherObjectType>() {

		@Override
		@SuppressWarnings({ "rawtypes", "synthetic-access" })
		public void write(Json json, ExternalOtherObjectType object, Class knownType) {
			json.writeObjectStart();
			json.writeType(ExternalOtherObjectType.class);
			json.writeValue("name", object.name); //$NON-NLS-1$
			json.writeValue("texturePath", object.texturePath); //$NON-NLS-1$
			json.writeObjectEnd();
		}

		@Override
		@SuppressWarnings({ "rawtypes", "synthetic-access" })
		public ExternalOtherObjectType read(Json json, JsonValue jsonData, Class type) {

			final String name = json.readValue("name", String.class, jsonData); //$NON-NLS-1$
			final ExternalOtherObjectTypeDictionary dictionary = OtherObjectTypeManager.getInstance().getDictionary();
			if (dictionary != null) {
				for (ExternalOtherObjectType alreadyExistsType : dictionary.getAllExternalObjectType()) {
					if (name.equals(alreadyExistsType.getName())) {
						return alreadyExistsType;
					}
				}
			}
			ExternalOtherObjectType result = new ExternalOtherObjectType();
			result.name = name;
			result.texturePath = json.readValue("texturePath", String.class, jsonData); //$NON-NLS-1$
			result.texture = new Texture(result.texturePath);
			return result;
		}
	};

	private ExternalOtherObjectType() {
	}

	/**
	 * @param name
	 * @param texturePath
	 */
	ExternalOtherObjectType(String name, String texturePath) {
		this.name = name;
		this.texturePath = texturePath;
		this.texture = new Texture(this.texturePath);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
	public Square2dObject create() {
		return new Square2dObject(this);
	}

	/**
	 * @return serializer
	 */
	public static Json.Serializer<ExternalOtherObjectType> getExternalOtherObjectTypeSerializer() {
		return serializer;
	}
}