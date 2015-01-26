package org.nognog.freeSquare.ui.square2d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;
import org.nognog.freeSquare.ui.square2d.squares.Square2dType;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class Square2dObjectTest {

	@Test
	public final void testAct() {
		final Action mock = Mockito.mock(Action.class);
		final Square2dObject object = new Square2dObject(Square2dObjectType.TOFU);
		object.addAction(mock);
		
		final float delta = 0.1f;
		final int actCount = 10;
		for (int i = 0; i < actCount; i++) {
			object.act(delta);
		}
		verify(mock, times(actCount)).act(delta);
		object.setEnabledAction(false);
		for (int i = 0; i < actCount; i++) {
			object.act(delta);
		}
		verify(mock, times(actCount)).act(delta);
	}

	@Test
	public final void testGetX() {
		Square2dObject object = Square2dObjectType.TOFU.create();
		object.setX(1);
		float expected1 = object.getX(Align.center);
		float actual1 = object.getX();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testGetY() {
		Square2dObject object = Square2dObjectType.TOFU.create();
		object.setY(0.3f);
		float expected1 = object.getY(Align.bottom) + object.getOriginY();
		float actual1 = object.getY();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testSetPositionFloatFloat() {
		Square2dObject object1 = Square2dObjectType.TOFU.create();
		Square2dObject object2 = Square2dObjectType.TOFU.create();
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
		Square2dObject object = Square2dObjectType.TOFU.create();
		Square2d square = mock(Square2d.class);
		object.setSquare(square);
		try {
			object.setSquare(square);
			fail();
		} catch (RuntimeException e) {
			// ok
		}

		assertThat(object.getSquare(), is(square));
	}

	@Test
	public final void testGetType() {
		for (Square2dObjectType type : Square2dObjectType.values()) {
			Square2dObject object = type.create();
			assertThat(object.getType(), is(type));
		}
	}

	@Test
	public final void testGetLogicalWidth() {
		for (Square2dObjectType type : Square2dObjectType.values()) {
			Square2dObject object = type.create();
			assertThat(object.getLogicalWidth(), is(type.getLogicalWidth()));
		}
	}

	@Test
	public final void testGetLogicalHeight() {
		for (Square2dObjectType type : Square2dObjectType.values()) {
			Square2dObject object = type.create();
			final float ratio = type.getTexture().getHeight() / type.getTexture().getWidth();
			assertThat(object.getLogicalHeight(), is(type.getLogicalWidth() * ratio));
		}
	}

	@Test
	public final void testIsLandingOnSquare() {
		for (Square2dType type : Square2dType.values()) {
			Square2d square = type.create();
			Square2dObject object = Square2dObjectType.TOFU.create();
			square.addSquareObject(object, MathUtils.random(square.getWidth()), MathUtils.random(square.getHeight()));

			boolean expected1 = square.containsInSquareArea(object.getX(), object.getY());
			boolean actual1 = object.isLandingOnSquare();
			assertThat(actual1, is(expected1));

			square.removeSquareObject(object);
			boolean expected2 = false;
			boolean actual2 = object.isLandingOnSquare();
			assertThat(actual2, is(expected2));
		}
	}

}
