package org.nognog.freeSquare.square2d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.event.UpdateObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.EatableObjectType;
import org.nognog.freeSquare.square2d.squares.Square2dType;
import org.nognog.freeSquare.square2d.ui.SquareObserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class SimpleSquare2dTest {

	@Test
	public final void testGetWidth() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		final float expected1 = square.getSquareImage().getWidth();
		final float actual1 = square.getWidth();

		final float newSquareImageWidth = square.getSquareImage().getWidth() + 5;
		square.getSquareImage().setWidth(newSquareImageWidth);

		final float expected2 = newSquareImageWidth;
		final float actual2 = square.getWidth();
		assertThat(actual2, is(expected2));
	}

	@Test
	public final void testGetHeight() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		final float expected1 = square.getSquareImage().getHeight();
		final float actual1 = square.getHeight();
		assertThat(actual1, is(expected1));

		final float newSquareImageHeight = square.getSquareImage().getHeight() + 5;
		square.getSquareImage().setHeight(newSquareImageHeight);

		final float expected2 = newSquareImageHeight;
		final float actual2 = square.getHeight();
		assertThat(actual2, is(expected2));
	}

	@Test
	public final void testDrawBatchFloat() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		square.addActor(mock(Square2dObject.class));
		Batch batch = mock(Batch.class);
		when(batch.getTransformMatrix()).thenReturn(new Matrix4());
		square.getSquareImage().setZIndex(1);
		square.draw(batch, 0);

		final int expected1 = 1;
		final int actual1 = square.getSquareImage().getZIndex();
		assertThat(actual1, is(expected1));

		square.notify(new UpdateObjectEvent());
		square.draw(batch, 0);

		final int expected2 = 0;
		final int actual2 = square.getSquareImage().getZIndex();
		assertThat(actual2, is(expected2));
	}

	@Test
	public final void testAddSquareObjectSquare2dObjectFloatFloat() {
		Square2dObject object = EatableObjectType.TOFU.create();
		final float expected1 = object.getOriginX();
		final float actual1 = object.getX();
		final float expected2 = object.getOriginY();
		final float actual2 = object.getY();
		assertThat(actual1, is(expected1));
		assertThat(actual2, is(expected2));

		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		final float x = -1;
		final float y = 1;
		square.addSquareObject(object, x, y);

		final float expected3 = x;
		final float actual3 = object.getX();
		final float expected4 = y;
		final float actual4 = object.getY();
		assertThat(actual3, is(expected3));
		assertThat(actual4, is(expected4));
	}

	@Test
	public final void testGetObjects() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		for (Square2dObject object : square.getObjects()) {
			fail();
		}
		square.addSquareObject(mock(Square2dObject.class));
		square.addSquareObject(mock(Square2dObject.class));
		int counter = 0;
		for (Square2dObject object : square.getObjects()) {
			counter++;
		}
		assertThat(counter, is(2));
	}

	@Test
	public final void testGetSquareSize() {
		SimpleSquare2d square1 = Square2dType.GRASSY_SQUARE1.create();
		Square2dSize expected1 = Square2dSize.MEDIUM;
		Square2dSize actual1 = square1.getSquareSize();
		assertThat(actual1, is(expected1));

		SimpleSquare2d square2 = Square2dType.GRASSY_SQUARE1_SMALL.create();
		Square2dSize expected2 = Square2dSize.SMALL;
		Square2dSize actual2 = square2.getSquareSize();
		assertThat(actual2, is(expected2));

		SimpleSquare2d square3 = Square2dType.GRASSY_SQUARE1_LARGE.create();
		Square2dSize expected3 = Square2dSize.LARGE;
		Square2dSize actual3 = square3.getSquareSize();
		assertThat(actual3, is(expected3));
	}

	@Test
	public final void testIsConcave() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		boolean expected1 = false;
		boolean actual1 = square.isConcave();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testContainsInSquareArea() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		boolean expected1 = true;
		boolean actual1 = square.containsInSquare(square.getVertices()[0].x, square.getVertices()[0].y);
		assertThat(actual1, is(expected1));

		boolean expected2 = true;
		boolean actual2 = square.containsInSquare(square.getVertices()[0].x, (square.getVertices()[1].y + square.getVertices()[2].y) / 2);
		assertThat(actual2, is(expected2));

		boolean expected3 = false;
		boolean actual3 = square.containsInSquare(square.getVertices()[0].x, Math.nextAfter(square.getVertices()[0].y, Float.NEGATIVE_INFINITY));
		assertThat(actual3, is(expected3));
	}

	@Test
	public final void testToString() {
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		boolean expected1 = true;
		boolean actual1 = square.toString().contains(Square2dType.GRASSY_SQUARE1.getName());
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testNotifyObservers() {
		System.out.println(Gdx.gl);
		SimpleSquare2d square = Square2dType.GRASSY_SQUARE1.create();
		SquareObserver observer1 = mock(SquareObserver.class);
		SquareObserver observer2 = mock(SquareObserver.class);
		SquareObserver observer3 = mock(SquareObserver.class);

		Square2dEvent event = new Square2dEvent();
		square.addSquareObserver(observer1);
		square.addSquareObserver(observer3);
		square.notifyObservers(event);
		square.addSquareObserver(observer2);
		square.notifyObservers(event);

		verify(observer1, times(2)).notify(event);
		verify(observer2, times(1)).notify(event);
		verify(observer3, times(2)).notify(event);

		square.removeSquareObserver(observer1);
		square.notifyObservers(event);

		verify(observer1, times(2)).notify(event);
		verify(observer2, times(2)).notify(event);
		verify(observer3, times(3)).notify(event);
	}

}