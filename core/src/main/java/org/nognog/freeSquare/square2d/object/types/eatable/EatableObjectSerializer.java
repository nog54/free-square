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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/06/02
 */
@SuppressWarnings("rawtypes")
public class EatableObjectSerializer implements Json.Serializer<EatableObject> {

	private static final EatableObjectSerializer instance = new EatableObjectSerializer();

	private EatableObjectSerializer() {
	}

	/**
	 * @return instance
	 */
	public static EatableObjectSerializer getInstance() {
		return instance;
	}

	@SuppressWarnings("boxing")
	@Override
	public void write(Json json, EatableObject object, Class knownType) {
		json.writeObjectStart();
		json.writeType(object.getClass());
		json.writeValue("type", object.getType(), EatableObjectType.class); //$NON-NLS-1$
		json.writeValue("positionX", Float.valueOf(object.getX())); //$NON-NLS-1$
		json.writeValue("positionY", Float.valueOf(object.getY())); //$NON-NLS-1$
		json.writeField(object, "amount"); //$NON-NLS-1$
		final TextureRegion imageTextureRegion = ((TextureRegionDrawable) object.getIcon().getMainImage().getDrawable()).getRegion();
		json.writeValue("imageX", object.getIcon().getMainImage().getX()); //$NON-NLS-1$
		json.writeValue("imageY", object.getIcon().getMainImage().getY()); //$NON-NLS-1$
		json.writeValue("imageScaleX", object.getIcon().getMainImage().getScaleX()); //$NON-NLS-1$
		json.writeValue("imageScaleY", object.getIcon().getMainImage().getScaleY()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionX", imageTextureRegion.getRegionX()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionY", imageTextureRegion.getRegionY()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionWidth", imageTextureRegion.getRegionWidth()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionHeight", imageTextureRegion.getRegionHeight()); //$NON-NLS-1$
		json.writeObjectEnd();
	}

	@Override
	public EatableObject read(Json json, JsonValue jsonData, Class type) {
		final EatableObjectType readType = json.readValue("type", EatableObjectType.class, jsonData); //$NON-NLS-1$
		final EatableObject result = new EatableObject(readType);

		result.setX(json.readValue("positionX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		result.setY(json.readValue("positionY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		final int amount = json.readValue("amount", Integer.class, jsonData).intValue(); //$NON-NLS-1$
		result.setAmount(amount);

		TextureRegion imageTextureRegion = ((TextureRegionDrawable) result.getIcon().getMainImage().getDrawable()).getRegion();
		final Image iconImage = result.getIcon().getMainImage();
		iconImage.setX(json.readValue("imageX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		iconImage.setY(json.readValue("imageY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		iconImage.setScaleX(json.readValue("imageScaleX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		iconImage.setScaleY(json.readValue("imageScaleY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionX(json.readValue("imageTextureRegionRegionX", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionY(json.readValue("imageTextureRegionRegionY", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionWidth(json.readValue("imageTextureRegionRegionWidth", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionHeight(json.readValue("imageTextureRegionRegionHeight", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		return result;
	}

}
