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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.GdxTestRunner;
import org.nognog.freeSquare.persist.PersistManager;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.type.Square2dType;

import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2015/02/14
 */
@SuppressWarnings({ "static-method", "javadoc" })
@RunWith(GdxTestRunner.class)
public class FlyingLifeObjectTest {

	@BeforeClass
	public static void beforeClass() {
		PersistManager.getUseJson().setSerializer(FlyingLifeObject.class, FlyingLifeObjectSerializer.getInstance());
	}
	
	@AfterClass
	public static void afterClass(){
		PersistManager.getUseJson().setSerializer(FlyingLifeObject.class, null);
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testIsValid() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			FlyingLifeObject object = null;
			try {
				object = (FlyingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			final boolean expected1 = false;
			final boolean actual1 = object.isValid();
			assertThat(actual1, is(expected1));

			SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
			square.addSquareObject(object);

			final boolean expected2 = true;
			final boolean actual2 = object.isValid();
			assertThat(actual2, is(expected2));

			object.setPosition(square.getVertex1().x, square.getVertex1().y - 1);

			final boolean expected3 = true;
			final boolean actual3 = object.isValid();
			assertThat(actual3, is(expected3));
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testWrite() {
		Json json = PersistManager.getUseJson();
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			FlyingLifeObject object = null;
			try {
				object = (FlyingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
			square.addSquareObject(object);

			String jsonString = json.toJson(object);
			FlyingLifeObject deserializedObject = json.fromJson(object.getClass(), jsonString);
			assertThat(object, is(deserializedObject));

			final boolean actual1 = object.getLife() == deserializedObject.getLife();
			final boolean actual2 = object.getLife().equals(deserializedObject.getLife());
			assertThat(actual1, is(false));
			assertThat(actual2, is(true));

			object.setX(square.getLeftEndX());
			object.setY(square.getBottomEndY() - 1);
			assertThat(object.isLandingOnSquare(), is(false));
			jsonString = json.toJson(object);
			assertThat(object.isLandingOnSquare(), is(false));
		}
	}

}
