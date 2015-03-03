package org.nognog.freeSquare.square2d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.CombineSquare2d.CombinePoint;
import org.nognog.freeSquare.square2d.squares.Square2dType;

import com.badlogic.gdx.utils.Array;

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
			int expected5 = 2;
			assertThat(actual5, is(expected5));

			CombinePoint actual6 = combinePoints[0];
			CombinePoint actual7 = combinePoints[1];
			CombinePoint expected6 = new CombinePoint(combineSquare, combinePoints[0].vertex1, appendSquare1, appendSquare1.getVertex3(), combinePoints[0].actualVertex);
			CombinePoint expected7 = new CombinePoint(combineSquare, combinePoints[1].vertex1, appendSquare1, appendSquare1.getVertex2(), combinePoints[1].actualVertex);

			assertThat(actual6, is(expected6));
			assertThat(actual7, is(expected7));
			assertThat(combineSquare.contains(combinePoints[0].actualVertex), is(false));
			assertThat(combineSquare.contains(combinePoints[1].actualVertex), is(false));
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
					boolean isCombined = combineSquare.combine(combineSquare.getVertices()[i], appendSquare2, appendSquare2.getVertices()[j]);
					boolean isSeparated = combineSquare.separate(appendSquare2);
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
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare4 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[5], appendSquare2, appendSquare2.getVertices()[0]);
			combineSquare.separate(appendSquare2);
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare3, appendSquare3.getVertices()[1]);
			combineSquare.combine(combineSquare.getVertices()[4], appendSquare4, appendSquare4.getVertices()[0]);
			
			boolean actual1 = combineSquare.separate(appendSquare4);
			boolean actual2 = combineSquare.separate(appendSquare3);
//			boolean expected1 = true;
//			boolean expected2 = true;
//			assertThat(actual1, is(expected1));
//			assertThat(actual2, is(expected2));
		}
		{
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare4 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[5], appendSquare2, appendSquare2.getVertices()[0]);
			combineSquare.separate(appendSquare2);
			combineSquare.combine(combineSquare.getVertices()[2],appendSquare3, appendSquare3.getVertices()[0]);
			combineSquare.combine(combineSquare.getVertices()[2],appendSquare4, appendSquare4.getVertices()[0]);
			
			boolean actual1 = combineSquare.separate(appendSquare4);
			boolean actual2 = combineSquare.separate(appendSquare3);
//			boolean expected1 = true;
//			boolean expected2 = true;
//			assertThat(actual1, is(expected1));
//			assertThat(actual2, is(expected2));
		}
	}
}
