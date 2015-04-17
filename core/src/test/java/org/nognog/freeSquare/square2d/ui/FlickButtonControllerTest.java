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

package org.nognog.freeSquare.square2d.ui;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.freeSquare.GdxTestRunner;
import org.nognog.freeSquare.square2d.ui.FlickButtonController;
import org.nognog.freeSquare.square2d.ui.FlickButtonController.FlickButtonInputListener;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

@SuppressWarnings({ "boxing", "javadoc" })
@RunWith(GdxTestRunner.class)
public class FlickButtonControllerTest {

	private BitmapFont font;
	private float buttonWidthHeight;
	private FlickButtonInputListener listener;
	int centerCounter, rightCounter, upCounter, leftCounter, downCounter;
	private String centerMessage = "center"; //$NON-NLS-1$
	private String rightMessage = "right"; //$NON-NLS-1$
	private String upMessage = "up"; //$NON-NLS-1$
	private String leftMessage = "left"; //$NON-NLS-1$
	private String downMessage = "down"; //$NON-NLS-1$

	/**
	 * 
	 */
	@Before
	public void setup() {
		this.font = FontUtil.createMPlusFont(42);
		this.buttonWidthHeight = 64;
		this.listener = mock(FlickButtonInputListener.class);
		doThrow(new RuntimeException(this.centerMessage)).when(this.listener).center();
		doThrow(new RuntimeException(this.rightMessage)).when(this.listener).right();
		doThrow(new RuntimeException(this.upMessage)).when(this.listener).up();
		doThrow(new RuntimeException(this.leftMessage)).when(this.listener).left();
		doThrow(new RuntimeException(this.downMessage)).when(this.listener).down();
	}

	@Test
	public final void testSelectCenterButton() {
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.listener);
		controller.centerButton.setChecked(true);
		boolean expected1 = true;
		boolean actual1 = controller.centerButton.isChecked();
		assertThat(actual1, is(expected1));
		try {
			controller.selectCenterButton();
			fail();
		} catch (RuntimeException e) {
			if (!(e.getMessage() == this.centerMessage)) {
				fail();
			}
		}
		boolean expected2 = false;
		boolean actual2 = controller.centerButton.isChecked();
		assertThat(actual2, is(expected2));
	}

	@Test
	public final void testSelectUpButton() {
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.listener);
		controller.upButton.setChecked(true);
		boolean expected1 = true;
		boolean actual1 = controller.upButton.isChecked();
		assertThat(actual1, is(expected1));
		try {
			controller.selectUpButton();
			fail();
		} catch (RuntimeException e) {
			if (!(e.getMessage() == this.upMessage)) {
				fail();
			}
		}
		boolean expected2 = false;
		boolean actual2 = controller.upButton.isChecked();
		assertThat(actual2, is(expected2));
	}

	@Test
	public final void testSelectDownButton() {
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.listener);
		controller.downButton.setChecked(true);
		try {
			controller.selectDownButton();
			fail();
		} catch (RuntimeException e) {
			if (!(e.getMessage() == this.downMessage)) {
				fail();
			}
		}
		boolean expected1 = false;
		boolean actual1 = controller.downButton.isChecked();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testSelectRightButton() {
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.listener);
		controller.rightButton.setChecked(true);
		try {
			controller.selectRightButton();
			fail();
		} catch (RuntimeException e) {
			if (!(e.getMessage() == this.rightMessage)) {
				fail();
			}
		}
		boolean expected1 = false;
		boolean actual1 = controller.rightButton.isChecked();
		assertThat(actual1, is(expected1));
	}

	@Test
	public final void testSelectLeftButton() {
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.listener);
		controller.leftButton.setChecked(true);
		try {
			controller.selectLeftButton();
			fail();
		} catch (RuntimeException e) {
			if (!(e.getMessage() == this.leftMessage)) {
				fail();
			}
		}
		boolean expected1 = false;
		boolean actual1 = controller.leftButton.isChecked();
		assertThat(actual1, is(expected1));
	}

}
