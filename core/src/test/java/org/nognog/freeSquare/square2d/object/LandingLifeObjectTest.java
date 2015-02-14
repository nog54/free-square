package org.nognog.freeSquare.square2d.object;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.model.persist.PersistManager;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2d.Vertex;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;
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
		for (LifeObjectType type : LifeObjectType.values()) {
			LandingLifeObject object = null;
			try {
				object = (LandingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			final boolean expected1 = false;
			final boolean actual1 = object.isValid();
			assertThat(actual1, is(expected1));

			Square2d square = Square2dType.GRASSY_SQUARE1.create();
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
		for (LifeObjectType type : LifeObjectType.values()) {
			LandingLifeObject object = null;
			try {
				object = (LandingLifeObject) type.create();
			} catch (ClassCastException e) {
				continue;
			}
			Square2d square = Square2dType.GRASSY_SQUARE1.create();
			square.addSquareObject(object);

			String jsonString = json.toJson(object);
			Square2dObject deserializedObject = json.fromJson(object.getClass(), jsonString);
			assertThat(object, is(deserializedObject));

			object.setX(square.getVertex1().x);
			object.setY(square.getVertex1().y - 1);
			assertThat(object.isLandingOnSquare(), is(false));
			jsonString = json.toJson(object);
			assertThat(object.isLandingOnSquare(), is(true));
		}
	}

	@SuppressWarnings({ "null", "boxing" })
	@Test
	public final void testGoToSquareNearestVertex() {
		for (LifeObjectType type : LifeObjectType.values()) {
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
			Square2d square = Square2dType.GRASSY_SQUARE1.create();
			final float x = (square.getVertex2().x + square.getVertex4().x) / 2;
			final float y = (square.getVertex1().y + square.getVertex3().y) / 2;
			square.addSquareObject(object, x, y);
			Vertex nearestVertex = object.getNearestSquareVertex();
			object.goToSquareNearestVertex();
			assertThat(object.getX(), is(not(nearestVertex.x)));
			assertThat(object.getY(), is(not(nearestVertex.y)));
			object.act(Float.MAX_VALUE);
			assertThat(object.getX(), is(nearestVertex.x));
			assertThat(object.getY(), is(nearestVertex.y));
		}
	}

}
