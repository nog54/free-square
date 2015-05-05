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

import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/02/08
 */
public interface OtherObjectType extends Square2dObjectType<Square2dObject> {

	/**
	 * @author goshi 2015/04/23
	 */
	public static enum Prepared implements OtherObjectType {
		;

		private Prepared(String name, String texturePath, float logicalWidth) {
			this(name, texturePath, logicalWidth, EatableObject.class);
		}

		private Prepared(String name, String texturePath, float logicalWidth, Color color) {
			this(name, texturePath, logicalWidth, color, EatableObject.class);
		}

		private <T extends Square2dObject> Prepared(String name, String texturePath, float logicalWidth, Class<T> klass) {
			this(name, texturePath, logicalWidth, Colors.WHITE, klass);
		}

		private <T extends Square2dObject> Prepared(String name, String texturePath, float logicalWidth, Color color, Class<T> klass) {
			this(name, new Texture(texturePath), logicalWidth, color, klass);
		}

		@SuppressWarnings("unchecked")
		private <T extends Square2dObject> Prepared(OtherObjectType type, String name, Color color) {
			this(name, type.getTexture(), type.getLogicalWidth(), color, (Class<T>) type.getSquareObjectClass());
		}

		@SuppressWarnings("unchecked")
		private <T extends EatableObject> Prepared(OtherObjectType type, String name, float logicalWidth, Color color) {
			this(name, type.getTexture(), logicalWidth, color, (Class<T>) type.getSquareObjectClass());
		}

		private <T extends Square2dObject> Prepared(String name, Texture texture, float logicalWidth, Color color, Class<T> klass) {
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
		public Class<?> getSquareObjectClass() {
			return this.klass;
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
	}

	/**
	 * @author goshi 2015/04/23
	 */
	public static class LoadedObject implements OtherObjectType, Json.Serializable {
		private String name;
		private String texturePath;
		
		private transient Texture texture;

		/**
		 * 
		 */
		public LoadedObject(){
			
		}
		
		/**
		 * @param name
		 * @param texturePath
		 */
		public LoadedObject(String name, String texturePath) {
			this.name = name;
			this.texturePath = texturePath;
			this.texture = new Texture(this.texturePath);
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
		public Square2dObject create() {
			return new Square2dObject(this);
		}

		@Override
		public void write(Json json) {
			json.writeFields(this);
		}

		@Override
		public void read(Json json, JsonValue jsonData) {
			this.name = json.readValue("name", String.class, jsonData); //$NON-NLS-1$
			this.texturePath = json.readValue("texturePath", String.class, jsonData); //$NON-NLS-1$
			this.texture = new Texture(this.texturePath);
		}

	}

	/**
	 * @author goshi 2015/04/23
	 */
	public static class Manager {
		/**
		 * dispose
		 */
		public static void dispose() {
			for (OtherObjectType type : OtherObjectType.Prepared.values()) {
				type.getTexture().dispose();
			}
		}

		/**
		 * @return all type
		 */
		public static OtherObjectType[] getAll() {
			return OtherObjectType.Prepared.values();
		}
	}

}