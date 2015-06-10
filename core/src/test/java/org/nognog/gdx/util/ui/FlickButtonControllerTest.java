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

package org.nognog.gdx.util.ui;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.GdxTestRunner;
import org.nognog.freeSquare.util.font.FontUtil;
import org.nognog.gdx.util.ui.FlickButtonController;
import org.nognog.gdx.util.ui.FlickButtonController.FlickButtonInputListener;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

@SuppressWarnings({ "boxing", "javadoc" })
@RunWith(GdxTestRunner.class)
public class FlickButtonControllerTest {

	@Mocked
	FlickButtonInputListener mockListener;

	private BitmapFont font;
	private float buttonWidthHeight;
	int centerCounter, rightCounter, upCounter, leftCounter, downCounter;
	private String centerMessage = "center"; //$NON-NLS-1$
	private String rightMessage = "right"; //$NON-NLS-1$
	private String upMessage = "up"; //$NON-NLS-1$
	private String leftMessage = "left"; //$NON-NLS-1$
	private String downMessage = "down"; //$NON-NLS-1$

	/**
	 * 
	 */
	@SuppressWarnings({ "unused", "synthetic-access" })
	@Before
	public void setup() {
		this.font = FontUtil.createMPlusFont(42);
		this.buttonWidthHeight = 64;
		new NonStrictExpectations() {
			{
				FlickButtonControllerTest.this.mockListener.center();
				result = new RuntimeException(FlickButtonControllerTest.this.centerMessage);
				FlickButtonControllerTest.this.mockListener.up();
				result = new RuntimeException(FlickButtonControllerTest.this.upMessage);
				FlickButtonControllerTest.this.mockListener.left();
				result = new RuntimeException(FlickButtonControllerTest.this.leftMessage);
				FlickButtonControllerTest.this.mockListener.down();
				result = new RuntimeException(FlickButtonControllerTest.this.downMessage);
				FlickButtonControllerTest.this.mockListener.right();
				result = new RuntimeException(FlickButtonControllerTest.this.rightMessage);
			}
		};
	}

	@Test
	public final void testSelectCenterButton() {
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.mockListener);
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
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.mockListener);
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
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.mockListener);
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
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.mockListener);
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
		FlickButtonController controller = new FlickButtonController(this.font, this.buttonWidthHeight, this.mockListener);
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