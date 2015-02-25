package org.nognog.freeSquare.square2d;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.squares.Square2dType;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

@SuppressWarnings("all")
@RunWith(GdxTestRunner.class)
public class CombinedSquare2dTest {

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
	public final void testRemoveActorActor() {

	}

	@Test
	public final void testCombine() {
		{
			SimpleSquare2d base = Square2dType.GRASSY_SQUARE1.create();
			base.setX(-base.getWidth() / 2);
			CombinedSquare2d combineSquare = new CombinedSquare2d(base);
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1.create();
			boolean actual1 = combineSquare.combine(combineSquare.getVertices().get(0), appendSquare1, appendSquare1.getVertices().get(2));
			boolean actual2 = combineSquare.combine(combineSquare.getVertices().get(0), appendSquare1, appendSquare1.getVertices().get(0));
			boolean actual3 = combineSquare.combine(combineSquare.getVertices().get(0), appendSquare1, appendSquare1.getVertices().get(1));
			boolean expected1 = false;
			boolean expected2 = false;
			boolean expected3 = true;
			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
			assertThat(actual3, is(expected3));
			for(Vertex appendSquareVertex : appendSquare1.getVertices()){
				boolean actual4 = combineSquare.combine(combineSquare.getVertices().get(0), appendSquare1, appendSquareVertex);
				boolean expected4 = false;
				assertThat(actual4, is(expected4));
			}
		}
		{
			SimpleSquare2d base = Square2dType.GRASSY_SQUARE1.create();
			base.setX(-base.getWidth() / 2);
			CombinedSquare2d combineSquare = new CombinedSquare2d(base);
			SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1.create();
			SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_LARGE.create();
			boolean actual1 = combineSquare.combine(combineSquare.getVertices().get(0), appendSquare1, appendSquare1.getVertices().get(2));
			boolean actual2 = combineSquare.combine(combineSquare.getVertices().get(0), appendSquare2, appendSquare2.getVertex2());
			boolean actual3 = combineSquare.combine(combineSquare.getVertices().get(combineSquare.getVertices().size - 1), appendSquare3, appendSquare3.getVertex2());
			boolean expected1 = false;
			boolean expected2 = true;
			boolean expected3 = false;

			assertThat(actual1, is(expected1));
			assertThat(actual2, is(expected2));
			assertThat(actual3, is(expected3));
			
		}
	}

	@Test
	public final void testContains() {

	}

	@Test
	public final void testSeparate() {

	}

}
