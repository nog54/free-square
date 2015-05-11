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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.persist.PersistManager;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectType;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectTypeManager;
import org.nognog.freeSquare.square2d.squares.Square2dType;

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

	@SuppressWarnings({ "null", "boxing" })
	@Test
	public final void testGoToSquareNearestVertex() {
		for (LifeObjectType type : LifeObjectTypeManager.getInstance().getAllTypes()) {
			LandingLifeObject object = null;
			try {
				object = (LandingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			try {
				object.goToSquareNearestVertex();
				fail();
			} catch (NullPointerException e) {
				// ok
			}
			SimpleSquare2d square = Square2dType.GRASSY_SQUARE1_MEDIUM.create();
			Vertex[] vertices = square.getVertices();
			final float x = (vertices[1].x + vertices[3].x) / 2;
			final float y = (vertices[0].y + vertices[2].y) / 2;
			square.addSquareObject(object, x, y);
			Vertex nearestVertex = object.getNearestSquareVertex();
			object.goToSquareNearestVertex();
			assertThat(object.getX(), is(not(nearestVertex.x)));
			assertThat(object.getY(), is(not(nearestVertex.y)));
			object.act(Float.MAX_VALUE);
			// assertThat(object.getX(), is(nearestVertex.x)); TODO
			// assertThat(object.getY(), is(nearestVertex.y));
		}
	}

}
