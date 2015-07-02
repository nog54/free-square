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

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.Square2dUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/06/02
 */
@SuppressWarnings("rawtypes")
public class LandingLifeObjectSerializer implements Json.Serializer<LandingLifeObject> {

	private static final LandingLifeObjectSerializer instance = new LandingLifeObjectSerializer();

	private LandingLifeObjectSerializer() {
	}

	/**
	 * @return instance
	 */
	public static LandingLifeObjectSerializer getInstance() {
		return instance;
	}

	@Override
	public void write(Json json, LandingLifeObject object, Class knownType) {
		if (!object.isLandingOnSquare()) {
			final Vector2 randomPointOnSquare = Square2dUtils.getRandomPointOn(object.getSquare());
			object.setPosition(randomPointOnSquare.x, randomPointOnSquare.y);
		}
		json.writeObjectStart();
		json.writeValue("life", object.getLife()); //$NON-NLS-1$
		json.writeType(object.getClass());
		json.writeValue("positionX", Float.valueOf(object.getX())); //$NON-NLS-1$
		json.writeValue("positionY", Float.valueOf(object.getY())); //$NON-NLS-1$
		json.writeObjectEnd();
	}

	@Override
	public LandingLifeObject read(Json json, JsonValue jsonData, Class type) {
		final Life life = json.readValue("life", Life.class, jsonData); //$NON-NLS-1$
		final LandingLifeObject result = new LandingLifeObject(life);
		result.setX(json.readValue("positionX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		result.setY(json.readValue("positionY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		return result;
	}

}
