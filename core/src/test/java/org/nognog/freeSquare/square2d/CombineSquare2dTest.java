package org.nognog.freeSquare.square2d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.CombineSquare2d.CombinePoint;
import org.nognog.freeSquare.square2d.squares.Square2dType;

import com.badlogic.gdx.utils.ObjectMap;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class CombineSquare2dTest {

	@Test
	public final void testGetVertices() {

	}

	@Test
	public final void testGetLeftEndX() {

	}

	@Test
	public final void testGetRightEndX() {

	}

	@Test
	public final void testGetButtomEndY() {

	}

	@Test
	public final void testGetTopEndY() {

	}

	@Test
	public final void testCombine() {
		{
			SimpleSquare2d base = Square2dType.GRASSY_SQUARE1.create();
			CombineSquare2d combineSquare = new CombineSquare2d(base);
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1.create();
			boolean actual1 = combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertices()[2]);
			boolean actual2 = combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertices()[0]);
			boolean actual3 = combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertices()[1]);
			boolean expected1 = false;
			boolean expected2 = false;
			boolean expected3 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
			assertThat(actual3, is(expected3));
			for (Vertex appendSquareVertex : appendSquare1.getVertices()) {
				boolean actual4 = combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquareVertex);
				boolean expected4 = false;
				assertThat(actual4, is(expected4));
			}
			CombinePoint[] combinePoints = combineSquare.getCombinePoints();
			int actual5 = combinePoints.length;
			int expected5 = 6;
			assertThat(actual5, is(expected5));

			int size1Counter = 0, size2Counter = 0;
			int containsBaseVertex1Counter = 0, containsBaseVertex2Counter = 0, containsBaseVertex3Counter = 0, containsBaseVertex4Counter = 0;
			int containsAppendSquareVertex1Counter = 0, containsAppendSquareVertex2Counter = 0, containsAppendSquareVertex3Counter = 0, containsAppendSquareVertex4Counter = 0;
			for (CombinePoint combinePoint : combinePoints) {
				if (combinePoint.combinedVertices.size == 1) {
					size1Counter++;
					assertThat(combineSquare.contains(combinePoint.actualVertex), is(true));
				}
				if (combinePoint.combinedVertices.size == 2) {
					size2Counter++;
					assertThat(combinePoint.getVertexOf(base), is(notNullValue()));
					assertThat(combinePoint.getVertexOf(appendSquare1), is(notNullValue()));
					assertThat(combineSquare.contains(combinePoint.actualVertex), is(false));
				}
				if (combinePoint.getVertexOf(base) == base.getVertex1()) {
					containsBaseVertex1Counter++;
				}
				if (combinePoint.getVertexOf(base) == base.getVertex2()) {
					containsBaseVertex2Counter++;
				}
				if (combinePoint.getVertexOf(base) == base.getVertex3()) {
					containsBaseVertex3Counter++;
				}
				if (combinePoint.getVertexOf(base) == base.getVertex4()) {
					containsBaseVertex4Counter++;
				}
				if (combinePoint.getVertexOf(appendSquare1) == appendSquare1.getVertex1()) {
					containsAppendSquareVertex1Counter++;
				}
				if (combinePoint.getVertexOf(appendSquare1) == appendSquare1.getVertex2()) {
					containsAppendSquareVertex2Counter++;
				}
				if (combinePoint.getVertexOf(appendSquare1) == appendSquare1.getVertex3()) {
					containsAppendSquareVertex3Counter++;
				}
				if (combinePoint.getVertexOf(appendSquare1) == appendSquare1.getVertex4()) {
					containsAppendSquareVertex4Counter++;
				}
			}
			assertThat(size1Counter, is(4));
			assertThat(size2Counter, is(2));
			assertThat(containsBaseVertex1Counter, is(1));
			assertThat(containsBaseVertex2Counter, is(1));
			assertThat(containsBaseVertex3Counter, is(1));
			assertThat(containsBaseVertex4Counter, is(1));
			assertThat(containsAppendSquareVertex1Counter, is(1));
			assertThat(containsAppendSquareVertex2Counter, is(1));
			assertThat(containsAppendSquareVertex3Counter, is(1));
			assertThat(containsAppendSquareVertex4Counter, is(1));
		}
		{
			SimpleSquare2d base = Square2dType.GRASSY_SQUARE1.create();
			CombineSquare2d combineSquare = new CombineSquare2d(base);
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			boolean actual1 = combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex2());
			boolean actual2 = combineSquare.combine(combineSquare.getVertices()[combineSquare.getVertices().length - 1], appendSquare2, appendSquare2.getVertex1());
			boolean expected1 = true;
			boolean expected2 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
			int actual3 = combineSquare.getVertices().length;
			int expected3 = 8;
			assertThat(actual3, is(expected3));
		}
	}

	@Test
	public final void testSeparate() {
		{
			SimpleSquare2d base = Square2dType.GRASSY_SQUARE1.create();
			CombineSquare2d combineSquare = new CombineSquare2d(base);
			Vertex[] baseVertices = combineSquare.getVertices();
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			boolean actual1 = combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex2());
			boolean actual2 = combineSquare.combine(combineSquare.getVertices()[combineSquare.getVertices().length - 1], appendSquare2, appendSquare2.getVertex1());
			boolean expected1 = true;
			boolean expected2 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));

			boolean actual3 = combineSquare.separate(base);
			boolean expected3 = false;
			assertThat(actual3, is(expected3));

			boolean actual4 = combineSquare.separate(appendSquare1);
			boolean actual5 = combineSquare.separate(appendSquare2);
			boolean expected4 = true;
			boolean expected5 = true;
			assertThat(actual4, is(expected4));
			assertThat(actual5, is(expected5));

			Vertex[] afterSeparateVertices = combineSquare.getVertices();
			assertThat(afterSeparateVertices.length, is(baseVertices.length));
			for (int i = 0; i < afterSeparateVertices.length; i++) {
				for (int j = 0; j < baseVertices.length; j++) {
					if (afterSeparateVertices[i] == baseVertices[j]) {
						break;
					}
					if (j == baseVertices.length - 1) {
						fail();
					}
				}
			}
		}
		{
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex2());
			Vertex[] expectedVertices = combineSquare.getVertices();
			CombinePoint[] expectedCombinePoints = combineSquare.getCombinePoints();
			for (int i = 0; i < combineSquare.getVertices().length; i++) {
				for (int j = 0; j < appendSquare2.getVertices().length; j++) {
					combineSquare.combine(combineSquare.getVertices()[i], appendSquare2, appendSquare2.getVertices()[j]);
					combineSquare.separate(appendSquare2);
					Vertex[] actualVertices = combineSquare.getVertices();
					CombinePoint[] actualCombinePoints = combineSquare.getCombinePoints();

					assertThat(actualVertices.length, is(expectedVertices.length));
					for (Vertex actualVertex : actualVertices) {
						assertThat(Square2dUtils.contains(expectedVertices, actualVertex), is(true));
					}
					assertThat(actualCombinePoints.length, is(expectedCombinePoints.length));
					for (CombinePoint actualCombinePoint : actualCombinePoints) {
						assertThat(Square2dUtils.contains(expectedCombinePoints, actualCombinePoint), is(true));
					}
				}
			}
		}
		{
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1_LARGE.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			combineSquare.combine(combineSquare.getVertices()[3], appendSquare1, appendSquare1.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare2, appendSquare2.getVertex2());

			boolean actual1 = combineSquare.separate(appendSquare1);
			boolean actual2 = combineSquare.separate(appendSquare2);
			boolean expected1 = true;
			boolean expected2 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
		}
		{
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1_LARGE.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			combineSquare.combine(combineSquare.getVertices()[3], appendSquare1, appendSquare1.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare2, appendSquare2.getVertex2());

			boolean actual1 = combineSquare.separate(appendSquare2);
			boolean actual2 = combineSquare.separate(appendSquare1);
			boolean expected1 = true;
			boolean expected2 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
		}
		{
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1_LARGE.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare1, appendSquare1.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[5], appendSquare2, appendSquare2.getVertex1());

			boolean actual1 = combineSquare.separate(appendSquare1);
			boolean actual2 = combineSquare.separate(appendSquare2);
			boolean expected1 = true;
			boolean expected2 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
		}
		{
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1_LARGE.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare1, appendSquare1.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[5], appendSquare2, appendSquare2.getVertex1());

			boolean actual1 = combineSquare.separate(appendSquare2);
			boolean actual2 = combineSquare.separate(appendSquare1);
			boolean expected1 = true;
			boolean expected2 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
		}
	}
}
