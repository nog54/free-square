package org.nognog.freeSquare.square2d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.squares.Square2dType;

import com.badlogic.gdx.utils.Json;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class CombineSquare2dTest {
	
	@Test
	public final void testGetBorder() {
		SimpleSquare2d base = Square2dType.GRASSY_SQUARE1.create();
		CombineSquare2d combineSquare = new CombineSquare2d(base);
		float actual1 = combineSquare.getLeftEndX();
		float actual2 = combineSquare.getRightEndX();
		float actual3 = combineSquare.getButtomEndY();
		float actual4 = combineSquare.getTopEndY();
		float expected1 = 0f;
		float expected2 = base.getSquareImage().getWidth();
		float expected3 = 0f;
		float expected4 = base.getSquareImage().getHeight();
		assertThat(actual1, is(expected1));
		assertThat(actual2, is(expected2));
		assertThat(actual3, is(expected3));
		assertThat(actual4, is(expected4));

		SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1.create();
		Vertex appendVertex1 = combineSquare.getVertices()[1];
		combineSquare.combine(appendVertex1, appendSquare1, appendSquare1.getVertex1());
		float actual5 = combineSquare.getLeftEndX();
		float actual6 = combineSquare.getRightEndX();
		float actual7 = combineSquare.getButtomEndY();
		float actual8 = combineSquare.getTopEndY();
		float expected5 = 0f;
		float expected6 = appendSquare1.getSquareImage().getWidth() + appendVertex1.x - appendSquare1.getVertex1().x;
		float expected7 = 0f;
		float expected8 = appendSquare1.getSquareImage().getHeight() + appendVertex1.y - appendSquare1.getVertex1().y;
		assertThat(actual5, is(expected5));
		assertThat(actual6, is(expected6));
		assertThat(actual7, is(expected7));
		assertThat(actual8, is(expected8));

		SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1.create();
		Vertex appendVertex2 = combineSquare.getVertices()[0];
		combineSquare.combine(combineSquare.getVertices()[0], appendSquare2, appendSquare2.getVertex2());
		float actual9 = combineSquare.getLeftEndX();
		float actual10 = combineSquare.getRightEndX();
		float actual11 = combineSquare.getButtomEndY();
		float actual12 = combineSquare.getTopEndY();
		float expected9 = appendVertex2.x - appendSquare2.getVertex2().x;
		float expected10 = expected6;
		float expected11 = appendVertex2.y - appendSquare2.getVertex2().y;
		float expected12 = expected8;

		assertThat(actual9, is(expected9));
		assertThat(actual9, is(appendSquare2.getX()));
		assertThat(actual10, is(expected10));
		assertThat(actual11, is(expected11));
		assertThat(actual12, is(expected12));

		final int combineToLeftCount = 5;
		for (int i = 0; i < combineToLeftCount; i++) {
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1.create();
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare3, appendSquare3.getVertex2());
		}
		float actual13 = combineSquare.getLeftEndX();
		float actual14 = combineSquare.getRightEndX();
		float actual15 = combineSquare.getButtomEndY();
		float actual16 = combineSquare.getTopEndY();
		float expected13 = expected9 * (combineToLeftCount + 1);
		float expected14 = expected10;
		float expected15 = expected11 * (combineToLeftCount + 1);
		float expected16 = expected12;
		assertThat(actual13, is(expected13));
		assertThat(actual14, is(expected14));
		assertThat(actual15, is(expected15));
		assertThat(actual16, is(expected16));
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
		{ // separate to base only
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
		{ // separate last combine square
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
		{ // separate square that contains cancave point 1
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
		{ // separate square that contains cancave point 2
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
		{ // separate square that contains cancave point 3
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
		{ // separate square that contains cancave point 4
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
		{ // separate sandwiched square 1
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1.create();
			combineSquare.combine(combineSquare.getVertices()[3], appendSquare1, appendSquare1.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[3], appendSquare2, appendSquare2.getVertex1());
			boolean actual1 = combineSquare.separate(appendSquare1);
			boolean expected1 = false;
			assertThat(actual1, is(expected1));
		}
		{ // separate sandwiched square 2
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1_LARGE.create());
			Vertex[] firstVertices = combineSquare.getVertices();
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 1時の方向
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 2時の方向
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 3時の方向
			SimpleSquare2d appendSquare4 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 4時の方向
			SimpleSquare2d appendSquare5 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 5時の方向
			SimpleSquare2d appendSquare6 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 6時の方向
			SimpleSquare2d appendSquare7 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 7時の方向
			SimpleSquare2d appendSquare8 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 8時の方向
			SimpleSquare2d appendSquare9 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 9時の方向
			SimpleSquare2d appendSquare10 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 10時の方向
			SimpleSquare2d appendSquare11 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 11時の方向
			SimpleSquare2d appendSquare12 = Square2dType.GRASSY_SQUARE1_SMALL.create(); // 12時の方向
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare1, appendSquare1.getVertex4());
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare2, appendSquare2.getVertex4());
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare3, appendSquare3.getVertex3());
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare4, appendSquare4.getVertex3());
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare5, appendSquare5.getVertex3());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare6, appendSquare6.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare7, appendSquare7.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare8, appendSquare8.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare9, appendSquare9.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare10, appendSquare10.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare11, appendSquare11.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare12, appendSquare12.getVertex1());

			boolean actual1 = combineSquare.separate(appendSquare1);
			boolean actual2 = combineSquare.separate(appendSquare2);
			boolean actual3 = combineSquare.separate(appendSquare4);
			boolean actual4 = combineSquare.separate(appendSquare5);
			boolean actual5 = combineSquare.separate(appendSquare7);
			boolean actual6 = combineSquare.separate(appendSquare8);
			boolean actual7 = combineSquare.separate(appendSquare10);
			boolean actual8 = combineSquare.separate(appendSquare11);

			boolean actual9 = combineSquare.separate(appendSquare12);
			boolean actual10 = combineSquare.separate(appendSquare3);
			boolean actual11 = combineSquare.separate(appendSquare6);
			boolean actual12 = combineSquare.separate(appendSquare9);

			boolean actual13 = combineSquare.separate(appendSquare4);
			boolean actual14 = combineSquare.separate(appendSquare7);
			boolean actual15 = combineSquare.separate(appendSquare10);
			boolean actual16 = combineSquare.separate(appendSquare11);

			assertThat(actual1, is(true));
			assertThat(actual2, is(true));
			assertThat(actual3, is(false));
			assertThat(actual4, is(true));
			assertThat(actual5, is(false));
			assertThat(actual6, is(true));
			assertThat(actual7, is(false));
			assertThat(actual8, is(false));
			assertThat(actual9, is(true));
			assertThat(actual10, is(true));
			assertThat(actual11, is(true));
			assertThat(actual12, is(true));
			assertThat(actual13, is(true));
			assertThat(actual14, is(true));
			assertThat(actual15, is(true));
			assertThat(actual16, is(true));

			Vertex[] afterVertices = combineSquare.getVertices();
			assertThat(afterVertices.length, is(firstVertices.length));
			for (Vertex afterVertex : afterVertices) {
				assertThat(Square2dUtils.contains(firstVertices, afterVertex), is(true));
			}
		}
		{ // separate sandwiched square 3
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare4 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare5 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare6 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare7 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare8 = Square2dType.GRASSY_SQUARE1_SMALL.create();
			SimpleSquare2d appendSquare9 = Square2dType.GRASSY_SQUARE1_SMALL.create();

			combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex4());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare2, appendSquare2.getVertex4());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare3, appendSquare3.getVertex4());
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare4, appendSquare4.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[1], appendSquare5, appendSquare5.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare6, appendSquare6.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[2], appendSquare7, appendSquare7.getVertex2());
			combineSquare.combine(combineSquare.getVertices()[5], appendSquare8, appendSquare8.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[3], appendSquare9, appendSquare9.getVertex3());
			boolean actual1 = combineSquare.separate(appendSquare8);
			boolean actual2 = combineSquare.separate(appendSquare4);
			boolean actual3 = combineSquare.separate(appendSquare8);
			assertThat(actual1, is(false));
			assertThat(actual2, is(true));
			assertThat(actual3, is(true));
			assertThat(combineSquare.getVertices().length, is(10));
		}
		{ // separate sandwiched square 4 (has single vertex)
			CombineSquare2d combineSquare = new CombineSquare2d(Square2dType.GRASSY_SQUARE1_SMALL.create());
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1.create();
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_SMALL.create();

			combineSquare.combine(combineSquare.getVertices()[1], appendSquare1, appendSquare1.getVertex1());
			combineSquare.combine(combineSquare.getVertices()[0], appendSquare2, appendSquare2.getVertex2());

			Vertex[] beforeVertices = combineSquare.getVertices();
			combineSquare.combine(combineSquare.getVertices()[7], appendSquare3, appendSquare3.getVertex1());
			boolean actual1 = combineSquare.separate(appendSquare3);
			Vertex[] afterVertices = combineSquare.getVertices();

			// TODO improve to true.
			// assertThat(actual1, is(true));
			// assertThat(afterVertices.length, is(beforeVertices.length));
			// for (Vertex afterSeparateVertex : afterVertices) {
			// assertThat(Square2dUtils.contains(beforeVertices,
			// afterSeparateVertex), is(true));
			// }
		}
	}
}
