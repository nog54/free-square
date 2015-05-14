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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.life.Family;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.status.Status;
import org.nognog.freeSquare.model.life.status.StatusInfluence;
import org.nognog.freeSquare.persist.PersistManager;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectType;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectTypeManager;
import org.nognog.freeSquare.square2d.squares.Square2dType;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2015/02/14
 */
@SuppressWarnings({ "static-method", "javadoc" })
@RunWith(GdxTestRunner.class)
public class EatableObjectTest {

	@SuppressWarnings("boxing")
	@Test
	public final void testReadWrite() {
		Json json = PersistManager.getUseJson();
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
		for (EatableObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			EatableObject object = type.create();
			Vector2 randomPoint = Square2dUtils.getRandomPointOn(square);
			object.setPosition(randomPoint.x, randomPoint.y);
			LifeObject eater = LifeObject.create(new Life(Family.Prepared.RIKI));
			final int baseAmount = object.getAmount();
			final int amount1 = 5, amount2 = 10, amount3 = 15, amount4 = 20;
			object.eatenBy(eater, amount1, Direction.DOWN);
			object.eatenBy(eater, amount2, Direction.RIGHT);
			object.eatenBy(eater, amount3, Direction.UP);
			object.eatenBy(eater, amount4, Direction.LEFT);
			assertThat(object.getAmount(), is(Math.max(0, baseAmount - (amount1 + amount2 + amount3 + amount4))));
			Status expectedStatus = new Status();
			for (StatusInfluence statusInfluence : type.getStatusInfluences()) {
				statusInfluence.applyTo(expectedStatus, 5);
			}
			for (StatusInfluence statusInfluence : type.getStatusInfluences()) {
				statusInfluence.applyTo(expectedStatus, 10);
			}
			for (StatusInfluence statusInfluence : type.getStatusInfluences()) {
				statusInfluence.applyTo(expectedStatus, 15);
			}
			for (StatusInfluence statusInfluence : type.getStatusInfluences()) {
				statusInfluence.applyTo(expectedStatus, 20);
			}
			assertThat(eater.getLife().getStatus(), is(expectedStatus));
			String jsonString = json.toJson(object);
			Square2dObject deserializedObject = json.fromJson(object.getClass(), jsonString);
			assertThat(object, is(deserializedObject));
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testResurrection() {
		for (EatableObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			EatableObject object = type.create();
			LifeObject eater = LifeObject.create(new Life(Family.Prepared.RIKI));
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

	@SuppressWarnings("boxing")
	@Test
	public final void testIsBeingEaten() {
		for (EatableObjectType type : EatableObjectTypeManager.getInstance().getAllTypes()) {
			EatableObject object = type.create();
			LifeObject eater = LifeObject.create(new Life(Family.Prepared.RIKI));
			object.eatenBy(eater, 0, Direction.DOWN);
			assertThat(object.isBeingEaten(), is(false));
			object.eatenBy(eater, 1, Direction.DOWN);
			assertThat(object.isBeingEaten(), is(true));
			object.eatenBy(eater, -1, Direction.DOWN);
			assertThat(object.isBeingEaten(), is(true));
			object.resurrection();
			assertThat(object.isBeingEaten(), is(false));
		}
	}

}
