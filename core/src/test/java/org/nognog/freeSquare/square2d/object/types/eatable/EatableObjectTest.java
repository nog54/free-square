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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.GdxTestRunner;
import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.status.Status;
import org.nognog.freeSquare.model.life.status.influence.StatusInfluence;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.persist.PersistManager;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.event.ChangeStatusEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;
import org.nognog.freeSquare.square2d.type.Square2dType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2015/02/14
 */
@SuppressWarnings({ "static-method", "javadoc" })
@RunWith(GdxTestRunner.class)
public class EatableObjectTest {

	@BeforeClass
	public static void beforeClass() {
		PersistManager.getUseJson().setSerializer(EatableObject.class, EatableObjectSerializer.getInstance());
	}

	@AfterClass
	public static void afterClass() {
		PersistManager.getUseJson().setSerializer(EatableObject.class, null);
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testReadWrite() {
		Json json = PersistManager.getUseJson();
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
		for (EatableObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			EatableObject object = type.create();
			object.setSquare(square);
			Vector2 randomPoint = Square2dUtils.getRandomPointOn(square);
			object.setPosition(randomPoint.x, randomPoint.y);
			LifeObject eater = LifeObject.create(new Life(Family.Prepared.RIKI));
			eater.setSquare(square);
			final int baseAmount = object.getAmount();
			final int amount1 = 5, amount2 = 10, amount3 = 15, amount4 = 20;
			object.eatenBy(eater, amount1, Direction.DOWN);
			object.eatenBy(eater, amount2, Direction.RIGHT);
			object.eatenBy(eater, amount3, Direction.UP);
			object.eatenBy(eater, amount4, Direction.LEFT);
			assertThat(object.getAmount(), is(Math.max(0, baseAmount - (amount1 + amount2 + amount3 + amount4))));
			Status expectedStatus = new Status();
			StatusInfluence<?> statusInfluence = type.getStatusInfluence();

			statusInfluence.applyTo(expectedStatus, 5);
			statusInfluence.applyTo(expectedStatus, 10);
			statusInfluence.applyTo(expectedStatus, 15);
			statusInfluence.applyTo(expectedStatus, 20);

			assertThat(eater.getLife().getStatus(), is(expectedStatus));
			String jsonString = json.toJson(object);
			Square2dObject deserializedObject = json.fromJson(object.getClass(), jsonString);
			assertThat(object, is(deserializedObject));
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testResurrection(@Mocked final Square2d square) {
		for (EatableObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			EatableObject object = type.create();
			object.setSquare(square);
			LifeObject eater = LifeObject.create(new Life(Family.Prepared.RIKI));
			eater.setSquare(square);
			final int baseAmount = object.getAmount();
			final int amount1 = 5, amount2 = 10, amount3 = 15, amount4 = 20;
			object.eatenBy(eater, amount1, Direction.DOWN);
			object.eatenBy(eater, amount2, Direction.RIGHT);
			object.eatenBy(eater, amount3, Direction.UP);
			object.eatenBy(eater, amount4, Direction.LEFT);
			assertThat(object.getAmount(), is(Math.max(0, baseAmount - (amount1 + amount2 + amount3 + amount4))));
			object.resurrection();
			assertThat(object.getAmount(), is(baseAmount));
		}
	}

	@SuppressWarnings({ "boxing", "unused" })
	@Test
	public final void testEatenBy(@Mocked("notifyEventListeners(SquareEvent)") final Square2d square) {
		for (EatableObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			final EatableObject object = type.create();
			final LifeObject eater = LifeObject.create(new Life(Family.Prepared.RIKI));
			new NonStrictExpectations(eater) {
				{
					eater.handleEvent((ChangeStatusEvent) any);
				}
			};
			try {
				object.eatenBy(eater, 0, Direction.DOWN);
				fail();
			} catch (IllegalStateException e) {
				// pass
			}
			object.setSquare(square);
			try {
				object.eatenBy(eater, 0, Direction.DOWN);
				fail();
			} catch (IllegalStateException e) {
				// pass
			}
			eater.setSquare(square);
			object.eatenBy(eater, 0, Direction.DOWN);
			assertThat(object.isBeingEaten(), is(false));
			object.eatenBy(eater, 1, Direction.DOWN);
			assertThat(object.isBeingEaten(), is(true));
			object.eatenBy(eater, -1, Direction.DOWN);
			assertThat(object.isBeingEaten(), is(true));
			object.resurrection();
			assertThat(object.isBeingEaten(), is(false));
			new Verifications() {
				{
					square.notifyEventListeners((SquareEvent) any);
					times = 0;
					eater.handleEvent((ChangeStatusEvent) any);
					times = 1;
				}
			};
		}
	}
}
