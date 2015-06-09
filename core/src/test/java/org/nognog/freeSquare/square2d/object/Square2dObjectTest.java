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

package org.nognog.freeSquare.square2d.object;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.persist.PersistManager;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObject;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectSerializer;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.eatable.PreparedEatableObjectType;
import org.nognog.freeSquare.square2d.object.types.life.FlyingLifeObject;
import org.nognog.freeSquare.square2d.object.types.life.FlyingLifeObjectSerializer;
import org.nognog.freeSquare.square2d.object.types.life.LandingLifeObject;
import org.nognog.freeSquare.square2d.object.types.life.LandingLifeObjectSerializer;
import org.nognog.freeSquare.square2d.object.types.other.OtherObjectTypeManager;
import org.nognog.freeSquare.square2d.type.Square2dType;
import org.nognog.freeSquare.util.square2d.AllSquare2dObjectTypeManager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Json;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class Square2dObjectTest {

	@BeforeClass
	public static void beforeClass() {
		PersistManager.getUseJson().setSerializer(Square2dObject.class, Square2dObjectSerializer.getInstance());
		PersistManager.getUseJson().setSerializer(EatableObject.class, EatableObjectSerializer.getInstance());
		PersistManager.getUseJson().setSerializer(LandingLifeObject.class, LandingLifeObjectSerializer.getInstance());
		PersistManager.getUseJson().setSerializer(FlyingLifeObject.class, FlyingLifeObjectSerializer.getInstance());
	}

	@AfterClass
	public static void afterClass() {
		PersistManager.getUseJson().setSerializer(Square2dObject.class, null);
		PersistManager.getUseJson().setSerializer(EatableObject.class, null);
		PersistManager.getUseJson().setSerializer(LandingLifeObject.class, null);
		PersistManager.getUseJson().setSerializer(FlyingLifeObject.class, null);
	}

	@Mocked
	private Action mockAction;

	@Injectable
	private SimpleSquare2d mockSquare;

	@Test
	public final void testAct() {
		final Square2dObject object = new Square2dObject(PreparedEatableObjectType.TOFU);
		object.addAction(mockAction);

		final float delta = 0.1f;
		final int actCount = 10;
		for (int i = 0; i < actCount; i++) {
			object.act(delta);
		}

		new Verifications() {
			{
				mockAction.act(delta);
				times = actCount;
			}
		};
	}

	@Test
	public final void testGetX() {
		Square2dObject object = PreparedEatableObjectType.TOFU.create();
		final float expected1 = 1;
		object.setX(expected1);
		float actual1 = object.getX();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testGetY() {
		Square2dObject object = PreparedEatableObjectType.TOFU.create();
		float expected1 = 0.3f;
		object.setY(0.3f);
		float actual1 = object.getY();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testSetPositionFloatFloat() {
		Square2dObject object1 = PreparedEatableObjectType.TOFU.create();
		Square2dObject object2 = PreparedEatableObjectType.TOFU.create();
		final float x = 0.1f;
		final float y = 0.3f;

		object1.setX(x);
		object1.setY(y);
		object2.setPosition(x, y);

		assertThat(object2.getX(), is(object1.getX()));
		assertThat(object2.getY(), is(object1.getY()));
	}

	@Test
	public final void testSetSquare() {
		Square2dObject object = PreparedEatableObjectType.TOFU.create();
		object.setSquare(mockSquare);
		try {
			object.setSquare(mockSquare);
			fail();
		} catch (RuntimeException e) {
			// ok
		}

		assertThat((SimpleSquare2d) object.getSquare(), is(mockSquare));
	}

	@Test
	public final void testGetType() {
		for (Square2dObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			Square2dObject object = type.create();
			assertThat(object.getType(), is(type));
		}
	}

	@Test
	public final void testGetLogicalWidth() {
		for (Square2dObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			Square2dObject object = type.create();
			assertThat(object.getLogicalWidth(), is(type.getLogicalWidth()));
		}
	}

	@Test
	public final void testGetLogicalHeight() {
		for (Square2dObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			Square2dObject object = type.create();
			final float ratio = type.getTexture().getHeight() / type.getTexture().getWidth();
			assertThat(object.getLogicalHeight(), is(type.getLogicalWidth() * ratio));
		}
	}

	@Test
	public final void testIsLandingOnSquare() {
		for (Square2dType type : Square2dType.values()) {
			SimpleSquare2d square = type.create();
			Square2dObject object = PreparedEatableObjectType.TOFU.create();
			square.addSquareObject(object, MathUtils.random(square.getWidth()), MathUtils.random(square.getHeight()));

			boolean expected1 = square.contains(object.getX(), object.getY());
			boolean actual1 = object.isLandingOnSquare();
			assertThat(actual1, is(expected1));

			square.removeSquareObject(object);
			boolean expected2 = false;
			boolean actual2 = object.isLandingOnSquare();
			assertThat(actual2, is(expected2));
		}
	}

	@Test
	public final void testReadWrite() {
		boolean testSquare2dObject = false;
		boolean testEatableObject = false;
		boolean testLandingLifeObject = false;
		boolean testFlyingLifeObject = false;
		for (Square2dObjectType type : AllSquare2dObjectTypeManager.getAllPreparedTypes()) {
			final Class squareObjectClass = type.getSquareObjectClass();
			if (squareObjectClass == Square2dObject.class) {
				testSquare2dObject = true;
			}
			if (squareObjectClass == EatableObject.class) {
				testEatableObject = true;
			}
			if (squareObjectClass == LandingLifeObject.class) {
				testLandingLifeObject = true;
			}
			if (squareObjectClass == FlyingLifeObject.class) {
				testFlyingLifeObject = true;
			}
			this.serializeAndDeserialize(type);
		}

		final boolean testAllSquare2dObjectType = testSquare2dObject && testEatableObject && testLandingLifeObject && testFlyingLifeObject;
		assertThat(testAllSquare2dObjectType, is(true));
	}

	private void serializeAndDeserialize(Square2dObjectType type) {
		final Json json = PersistManager.getUseJson();
		final SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
		final Square2dObject object = type.create();
		square.addSquareObject(object);
		String jsonString = json.toJson(object);
		final Square2dObject deserializedObject = json.fromJson(object.getClass(), jsonString);
		assertThat(object, is(deserializedObject));
	}

}
