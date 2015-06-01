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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.persist.PersistManager;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.type.Square2dType;

import com.badlogic.gdx.utils.Json;

/**
 * @author goshi 2015/02/14
 */
@SuppressWarnings({ "static-method", "javadoc" })
@RunWith(GdxTestRunner.class)
public class LandingLifeObjectTest {

	@SuppressWarnings("boxing")
	@Test
	public final void testIsValid() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			LandingLifeObject object = null;
			try {
				object = (LandingLifeObject) type.create();
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

			final boolean expected3 = false;
			final boolean actual3 = object.isValid();
			assertThat(actual3, is(expected3));
		}
	}

	@SuppressWarnings("boxing")
	@Test
	public final void testWrite() {
		Json json = PersistManager.getUseJson();
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			LandingLifeObject object = null;
			try {
				object = (LandingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
			square.addSquareObject(object);

			String jsonString = json.toJson(object);
			Square2dObject deserializedObject = json.fromJson(object.getClass(), jsonString);
			assertThat(object, is(deserializedObject));

			Vertex[] vertices = square.getVertices();
			object.setX(vertices[0].x);
			object.setY(vertices[0].y - 1);
			assertThat(object.isLandingOnSquare(), is(false));
			jsonString = json.toJson(object);
			assertThat(object.isLandingOnSquare(), is(true));
		}
	}

	@Test
	public final void testBackToSquareNearestVertexIfNotOn() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			LandingLifeObject object = null;
			try {
				object = (LandingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			final SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
			square.addSquareObject(object);
			this.testBackToSquareFrom(square.getLeftEndX() - 1, square.getBottomEndY() - 1, object);
			this.testBackToSquareFrom(square.getLeftEndX() - 1, square.getTopEndY() + 1, object);
			this.testBackToSquareFrom(square.getRightEndX() + 1, square.getBottomEndY() - 1, object);
			this.testBackToSquareFrom(square.getRightEndX() + 1, square.getTopEndY() + 1, object);
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "boxing", "unused" })
	private void testBackToSquareFrom(float x, float y, final LandingLifeObject object) {
		new NonStrictExpectations(object) {
			{
				object.notify((SquareEvent) any);
			}
		};
		object.setPosition(x, y);
		final Vertex nearestVertex = object.getNearestSquareVertex();
		assertThat(object.getX(), is(not(nearestVertex.x)));
		assertThat(object.getY(), is(not(nearestVertex.y)));
		assertThat(object.isLandingOnSquare(), is(false));

		object.act(0); // set a return action here
		assertThat(object.isPerformingPriorityAction(), is(true));

		object.act(0); // not move here
		assertThat(object.getX(), is(not(nearestVertex.x)));
		assertThat(object.getY(), is(not(nearestVertex.y)));
		assertThat(object.isLandingOnSquare(), is(false));

		object.act(Float.MAX_VALUE); // perform the return action
		assertThat(object.getX(), is(nearestVertex.x));
		assertThat(object.getY(), is(nearestVertex.y));
		assertThat(object.isLandingOnSquare(), is(true));
		assertThat(object.isPerformingPriorityAction(), is(true));
		// notify event still remains

		new Verifications() {
			{
				object.notify((SquareEvent) any);
				times = 0;
			}
		};
		object.act(0);
		new Verifications() {
			{
				object.notify((SquareEvent) any);
				times = 1;
			}
		};
	}
}
