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

package org.nognog.freeSquare.activity.main;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import mockit.Cascading;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nognog.GdxTestRunner;
import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.FreeSquare.InputTextListener;
import org.nognog.freeSquare.activity.main.ui.PlayersItemList;
import org.nognog.freeSquare.activity.main.ui.PlayersLifeList;
import org.nognog.freeSquare.activity.main.ui.PlayersSquareList;
import org.nognog.freeSquare.model.Nameable;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.square2d.CombineSquare2d;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.action.ChangeSquareAction;
import org.nognog.freeSquare.square2d.event.CollectObjectRequestEvent;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;
import org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary;
import org.nognog.freeSquare.ui.ItemList;
import org.nognog.freeSquare.ui.Menu;
import org.nognog.freeSquare.ui.ModePresenter;
import org.nognog.gdx.util.camera.Camera;
import org.nognog.gdx.util.ui.CameraFitFileChooser;
import org.nognog.gdx.util.ui.CameraFitSimpleDialog;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author goshi 2015/06/12
 */
@RunWith(GdxTestRunner.class)
@SuppressWarnings({ "unused", "boxing", "static-method" })
public class MainActivityTest {

	@Cascading
	FreeSquare freeSquare;

	@Mocked
	Square2d square;

	private MainActivity createActivity() {
		final BitmapFont defaultFont = new BitmapFont();
		new NonStrictExpectations() {
			{
				MainActivityTest.this.freeSquare.getFont();
				result = defaultFont;
			}
		};
		final MainActivity activity = new MainActivity(this.freeSquare);
		return activity;
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#act(float)}.
	 * 
	 * @param image
	 */
	@Test
	public final void testAct(@Injectable final Image image) {
		final MainActivity activity = this.createActivity();
		activity.setSquare(this.square);
		activity.addActor(image);
		final float delta = 0.5f;
		activity.act(delta);

		new Verifications() {
			{
				MainActivityTest.this.square.act(delta);
				times = 0;
				image.act(delta);
				times = 1;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getInputProcesser()}
	 * .
	 */
	@Test
	public final void testGetInputProcesser() {
		final MainActivity activity = this.createActivity();
		final InputProcessor processor = activity.getInputProcesser();
		final boolean expected = true;
		final boolean actual = processor instanceof MainActivityInputProcessor;
		assertThat(actual, is(expected));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#resume()}.
	 */
	@Test
	public final void testResume() {
		final MainActivity activity = this.createActivity();
		try {
			activity.resume();
		} catch (Throwable t) {
			fail();
		}
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#pause()}.
	 */
	@Test
	public final void testPause() {
		final MainActivity activity = this.createActivity();
		new Expectations(activity) {
			{
				invoke(activity, "hideAllUIsExceptForSquare"); //$NON-NLS-1$
				times = 1;
			}
		};
		activity.pause();
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getPlayer()}.
	 */
	@Test
	public final void testGetPlayer() {
		final MainActivity activity = this.createActivity();
		assertThat(activity.getPlayer(), is(this.freeSquare.getPlayer()));
		activity.setPlayer(null);
		assertThat(activity.getPlayer(), is(nullValue()));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setPlayer()}.
	 */
	@Test
	public final void testSetPlayer1() {
		// setPlayer1,2,3をひとつにまとめれるならまとめたい。
		// まとめたらPlayersItemList,PlayersLifeList,PlayersSquareListが
		// 同じ親クラスからか失敗した。
		final MainActivity activity = this.createActivity();
		final PlayersItemList itemList = Deencapsulation.getField(activity, PlayersItemList.class);
		new Expectations(itemList) {
			{
				itemList.setPlayer(null);
				times = 1;
				itemList.setPlayer(MainActivityTest.this.freeSquare.getPlayer());
			}
		};
		activity.setPlayer(null);
		activity.setPlayer(this.freeSquare.getPlayer());
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setPlayer()}.
	 */
	@Test
	public final void testSetPlayer2() {
		final MainActivity activity = this.createActivity();
		final PlayersLifeList lifeList = Deencapsulation.getField(activity, PlayersLifeList.class);
		new Expectations(lifeList) {
			{
				lifeList.setPlayer(null);
				times = 1;
				lifeList.setPlayer(MainActivityTest.this.freeSquare.getPlayer());
			}
		};
		activity.setPlayer(null);
		activity.setPlayer(this.freeSquare.getPlayer());
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setPlayer()}.
	 */
	@Test
	public final void testSetPlayer3() {
		final MainActivity activity = this.createActivity();
		final PlayersSquareList squareList = Deencapsulation.getField(activity, PlayersSquareList.class);
		new Expectations(squareList) {
			{
				squareList.setPlayer(null);
				times = 1;
				squareList.setPlayer(MainActivityTest.this.freeSquare.getPlayer());
			}
		};
		activity.setPlayer(null);
		activity.setPlayer(this.freeSquare.getPlayer());
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getSquare()}.
	 */
	@Test
	public final void testGetSquare() {
		final MainActivity activity = this.createActivity();
		assertThat(activity.getSquare(), is(nullValue()));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#isSeparateSquareMode()}
	 * .
	 */
	@Test
	public final void testIsSeparateSquareMode() {
		final MainActivity activity = this.createActivity();
		assertThat(activity.isSeparateSquareMode(), is(false));
		activity.setSeparateSquareMode(true);
		assertThat(activity.isSeparateSquareMode(), is(true));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setSeparateSquareMode(boolean)}
	 * .
	 * 
	 * @param simpleSquare2d
	 * @param combineSquare2d
	 */
	@Test
	public final void testSetSeparateSquareMode(@Mocked final SimpleSquare2d simpleSquare2d, @Mocked final CombineSquare2d combineSquare2d) {
		final MainActivity activity = this.createActivity();
		new NonStrictExpectations() {
			{
				simpleSquare2d.getColor();
				result = new Color();
				combineSquare2d.getColor();
				result = new Color();
			}
		};
		assertThat(activity.isSeparateSquareMode(), is(false));

		activity.setSeparateSquareMode(true);
		assertThat(activity.isSeparateSquareMode(), is(true));
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 0);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 0);

		activity.setSeparateSquareMode(false);
		assertThat(activity.isSeparateSquareMode(), is(false));
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 0);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 0);

		activity.setSquare(simpleSquare2d);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 0);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 0);

		activity.setSeparateSquareMode(true);
		assertThat(activity.isSeparateSquareMode(), is(true));
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 0);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 0);

		activity.setSeparateSquareMode(false);
		assertThat(activity.isSeparateSquareMode(), is(false));
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 0);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 0);

		activity.setSquare(combineSquare2d);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 1);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 0);

		activity.setSeparateSquareMode(true);
		assertThat(activity.isSeparateSquareMode(), is(true));
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 1);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 1);

		activity.setSeparateSquareMode(false);
		assertThat(activity.isSeparateSquareMode(), is(false));
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, false, 2);
		this.verificationSetHighlightSeparatableSquare(combineSquare2d, true, 1);
	}

	private void verificationSetHighlightSeparatableSquare(final CombineSquare2d verifySquare, final boolean enable, final int expectTimes) {
		new Verifications() {
			{
				verifySquare.setHighlightSeparatableSquare(enable);
				times = expectTimes;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#convertThisSquareToCombineSquare2d()}
	 * .
	 * 
	 * @param simpleSquare2d
	 * @param combineSquare2d
	 */
	@Test
	public final void testConvertThisSquareToCombineSquare2d(@Mocked final SimpleSquare2d simpleSquare2d, @Mocked final CombineSquare2d combineSquare2d) {
		new NonStrictExpectations() {
			{
				simpleSquare2d.getColor();
				result = new Color();
				combineSquare2d.getColor();
				result = new Color();
			}
		};
		final MainActivity activity = this.createActivity();
		try {
			activity.convertThisSquareToCombineSquare2d();
			fail();
		} catch (IllegalStateException e) {
			// pass
		}

		activity.setSquare(combineSquare2d);
		try {
			activity.convertThisSquareToCombineSquare2d();
			fail();
		} catch (IllegalStateException e) {
			// pass
		}

		activity.setSquare(simpleSquare2d);
		activity.convertThisSquareToCombineSquare2d();
		final Square2d afterSquare = activity.getSquare();
		assertThat(simpleSquare2d != afterSquare, is(true));
		new Verifications() {
			{
				activity.getPlayer().replaceSquare(simpleSquare2d, afterSquare);
				times = 1;
			}
		};

	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setSquareWithAction(org.nognog.freeSquare.square2d.Square2d)}
	 * .
	 * 
	 * @param square2d
	 */
	@Test
	public final void testSetSquareWithActionSquare2d(@Mocked final Square2d square2d) {
		final MainActivity activity = this.createActivity();
		new Expectations(activity) {
			{
				activity.setSquareWithAction(square2d, Direction.LEFT);
				times = 1;
			}
		};
		activity.setSquareWithAction(square2d);
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setSquareWithAction(org.nognog.freeSquare.square2d.Square2d, org.nognog.freeSquare.square2d.Direction)}
	 * .
	 * 
	 * @param square2d
	 */
	@Test
	public final void testSetSquareWithActionSquare2dDirection(@Mocked final Square2d square2d) {
		final MainActivity activity = this.createActivity();
		for (Direction direction : Direction.values()) {
			activity.setSquareWithAction(square2d, direction);
			assertThat(activity.getActions().size, is(1));
			final Action action = activity.getActions().get(0);
			assertThat(action instanceof ChangeSquareAction, is(true));
			assertThat(((ChangeSquareAction) action).getActivity(), is(activity));
			assertThat(((ChangeSquareAction) action).getDirection(), is(direction));
			assertThat(((ChangeSquareAction) action).getSquare(), is(square2d));
			activity.removeAction(action);
		}
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#setSquare(org.nognog.freeSquare.square2d.Square2d)}
	 * .
	 * 
	 * @param simpleSquare2d
	 * @param combineSquare2d
	 */
	@Test
	public final void testSetSquare(@Mocked final SimpleSquare2d simpleSquare2d, @Mocked final CombineSquare2d combineSquare2d) {
		new NonStrictExpectations() {
			{
				simpleSquare2d.getColor();
				result = new Color();
				combineSquare2d.getColor();
				result = new Color();
			}
		};
		final MainActivity activity = this.createActivity();

		activity.setSquare(simpleSquare2d);
		new Verifications() {
			{
				simpleSquare2d.setPosition(0, 0);
				times = 1;
				simpleSquare2d.addSquareObserver(activity);
				times = 1;
				simpleSquare2d.removeSquareObserver(activity);
				times = 0;
				simpleSquare2d.remove();
				times = 0;
				activity.getPlayer().notifyPlayerObservers();
				times = 1;
			}
		};
		assertThat(activity.getChildren().contains(simpleSquare2d, true), is(true));
		assertThat(activity.getChildren().contains(combineSquare2d, true), is(false));

		activity.setSquare(null);
		new Verifications() {
			{
				simpleSquare2d.setPosition(0, 0);
				times = 1;
				simpleSquare2d.addSquareObserver(activity);
				times = 1;
				simpleSquare2d.removeSquareObserver(activity);
				times = 1;
				simpleSquare2d.remove();
				times = 1;
				activity.getPlayer().notifyPlayerObservers();
				times = 2;
			}
		};

		activity.setSquare(combineSquare2d);
		new Verifications() {
			{
				activity.getPlayer().notifyPlayerObservers();
				times = 3;
			}
		};
		assertThat(activity.getChildren().contains(combineSquare2d, true), is(true));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#putSquare2d(org.nognog.freeSquare.square2d.Square2d)}
	 * .
	 * 
	 * @param simpleSquare2d
	 * @param combineSquare2d
	 */
	@Test
	public final void testPutSquare2d(@Mocked final SimpleSquare2d simpleSquare2d, @Mocked final CombineSquare2d combineSquare2d) {
		new NonStrictExpectations() {
			{
				simpleSquare2d.getColor();
				result = new Color();
				combineSquare2d.getColor();
				result = new Color();
			}
		};
		final MainActivity activity = this.createActivity();
		activity.putSquare2d(simpleSquare2d);
		assertThat(activity.getSquare(), is((Square2d) simpleSquare2d));
		new Verifications() {
			{
				activity.getPlayer().addSquare(simpleSquare2d);
				times = 1;
			}
		};

		final boolean result = activity.putSquare2d(combineSquare2d);
		final Square2d afterSquare = activity.getSquare();
		assertThat(afterSquare, is(not((Square2d) simpleSquare2d)));
		assertThat(afterSquare, is(not((Square2d) combineSquare2d)));
		assertThat(afterSquare instanceof CombineSquare2d, is(true));
		new Verifications() {
			{
				new CombineSquare2d(simpleSquare2d);
				times = 1;
			}
		};
		assertThat(result, is(false));

		// TODO Add test case to success pattern of combine method
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#separateSquare(org.nognog.freeSquare.square2d.Square2d)}
	 * .
	 * 
	 * @param simpleSquare2d
	 * @param combineSquare2d
	 */
	@Test
	public final void testSeparateSquare(@Mocked final SimpleSquare2d simpleSquare2d, @Mocked final CombineSquare2d combineSquare2d) {
		new NonStrictExpectations() {
			{
				simpleSquare2d.getColor();
				result = new Color();
				combineSquare2d.getColor();
				result = new Color();
			}
		};
		final MainActivity activity = this.createActivity();
		activity.separateSquare(simpleSquare2d);
		new Verifications() {
			{
				combineSquare2d.separate((Square2d) any);
				times = 0;
			}
		};

		activity.setSquare(simpleSquare2d);
		activity.separateSquare(combineSquare2d);
		new Verifications() {
			{
				combineSquare2d.separate((Square2d) any);
				times = 0;
			}
		};

		activity.setSquare(combineSquare2d);
		activity.separateSquare(simpleSquare2d);
		new Verifications() {
			{
				combineSquare2d.separate((Square2d) any);
				times = 1;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showSquareOnly()}
	 * .
	 */
	@Test
	public final void testShowSquareOnly() {
		final MainActivity activity = this.createActivity();
		activity.showSquareOnly();
		final boolean actual1 = this.verifyThatActorInActivityIsNotVisibleExceptForSquare(activity);
		final boolean expected1 = true;
		assertThat(actual1, is(expected1));

		activity.showDialog("", "", "", null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final boolean actual2 = this.verifyThatActorInActivityIsNotVisibleExceptForSquare(activity);
		final boolean expected2 = false;
		assertThat(actual2, is(expected2));

		activity.showSquareOnly();
		final boolean actual3 = this.verifyThatActorInActivityIsNotVisibleExceptForSquare(activity);
		final boolean expected3 = true;
		assertThat(actual3, is(expected3));

		for (Actor actor : activity.getChildren()) {
			actor.setVisible(true);
		}

		final boolean actual4 = this.verifyThatActorInActivityIsNotVisibleExceptForSquare(activity);
		final boolean expected4 = false;
		assertThat(actual4, is(expected4));

		activity.showSquareOnly();
		final boolean actual5 = this.verifyThatActorInActivityIsNotVisibleExceptForSquare(activity);
		final boolean expected5 = true;
		assertThat(actual5, is(expected5));

	}

	private boolean verifyThatActorInActivityIsNotVisibleExceptForSquare(final MainActivity activity) {
		for (Actor actor : activity.getChildren()) {
			if (actor instanceof Square2d) {
				if (!actor.isVisible()) {
					return false;
				}
			} else {
				if (actor.isVisible()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showMenu(float, float)}
	 * .
	 */
	@Test
	public final void testShowHideMenu() {
		final MainActivity activity = this.createActivity();
		final Menu menu = Deencapsulation.getField(activity, Menu.class);
		assertThat(menu.isVisible(), is(false));

		final float x = 1.2f, y = -100;
		activity.showMenu(x, y);
		assertThat(menu.isVisible(), is(true));
		assertThat(menu.getX(), is(x));
		assertThat(menu.getY(), is(y));

		activity.hideMenu();
		assertThat(menu.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showPlayerItemList()}
	 * .
	 */
	@Test
	public final void testShowHidePlayersItemList() {
		final MainActivity activity = this.createActivity();
		final PlayersItemList itemList = Deencapsulation.getField(activity, PlayersItemList.class);
		assertThat(itemList.isVisible(), is(false));

		activity.showPlayersItemList();
		assertThat(itemList.isVisible(), is(true));

		activity.hidePlayersItemList();
		assertThat(itemList.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showPlayersLifeList()}
	 * .
	 */
	@Test
	public final void testShowHidePlayersLifeList() {
		final MainActivity activity = this.createActivity();
		final PlayersLifeList lifeList = Deencapsulation.getField(activity, PlayersLifeList.class);
		assertThat(lifeList.isVisible(), is(false));

		activity.showPlayersLifeList();
		assertThat(lifeList.isVisible(), is(true));

		activity.hidePlayersLifeList();
		assertThat(lifeList.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showPlayersSquareList()}
	 * .
	 */
	@Test
	public final void testShowHidePlayersSquareList() {
		final MainActivity activity = this.createActivity();
		final PlayersSquareList squareList = Deencapsulation.getField(activity, PlayersSquareList.class);
		assertThat(squareList.isVisible(), is(false));

		activity.showPlayersSquareList();
		assertThat(squareList.isVisible(), is(true));

		activity.hidePlayersSquareList();
		assertThat(squareList.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showItemList()}.
	 */
	@Test
	public final void testShowHideItemList() {
		final MainActivity activity = this.createActivity();
		final ItemList itemList = Deencapsulation.getField(activity, ItemList.class);
		assertThat(itemList.isVisible(), is(false));

		activity.showItemList();
		assertThat(itemList.isVisible(), is(true));

		activity.hideItemList();
		assertThat(itemList.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showModePresenter(java.lang.String)}
	 * .
	 */
	@Test
	public final void testShowHideModePresenter() {
		final String modeText = "test!"; //$NON-NLS-1$
		final MainActivity activity = this.createActivity();
		final ModePresenter modePresenter = Deencapsulation.getField(activity, ModePresenter.class);
		assertThat(modePresenter.isVisible(), is(false));
		new Expectations(modePresenter) {
			{
				modePresenter.setText(modeText);
				times = 1;
			}
		};

		activity.showModePresenter(modeText);
		assertThat(modePresenter.isVisible(), is(true));

		activity.hideModePresenter();
		assertThat(modePresenter.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showFileChooser()}
	 * .
	 */
	@Test
	public final void testShowHideFileChooser() {
		final String modeText = "test!"; //$NON-NLS-1$
		final MainActivity activity = this.createActivity();
		final CameraFitFileChooser fileChooser = Deencapsulation.getField(activity, CameraFitFileChooser.class);
		assertThat(fileChooser.isVisible(), is(false));

		activity.showFileChooser();
		assertThat(fileChooser.isVisible(), is(true));

		activity.hideFileChooser();
		assertThat(fileChooser.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#showDialog(java.lang.String, java.lang.String, java.lang.String, org.nognog.gdx.util.ui.SimpleDialog.SimpleDialogListener)}
	 * .
	 */
	@Test
	public final void testShowHideDialog() {
		final String text = "testext"; //$NON-NLS-1$
		final MainActivity activity = this.createActivity();
		final CameraFitSimpleDialog dialog = Deencapsulation.getField(activity, CameraFitSimpleDialog.class);
		assertThat(dialog.isVisible(), is(false));
		new Expectations(dialog) {
			{
				dialog.setText(text);
				dialog.setLeftButtonText(text);
				dialog.setRightButtonText(text);
				dialog.setListener(null);
			}
		};

		activity.showDialog(text, text, text, null);
		assertThat(dialog.isVisible(), is(true));

		activity.hideDialog();
		assertThat(dialog.isVisible(), is(false));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#adjustCameraZoomAndPositionIfRangeOver(boolean)}
	 * .
	 */
	@Test
	public final void testAdjustCameraZoomAndPositionIfRangeOver() {
		final MainActivity activity = this.createActivity();
		activity.adjustCameraZoomAndPositionIfRangeOver(false);
		// TODO
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getVisibleActorCount()}
	 * .
	 * 
	 * @param square2d
	 */
	@Test
	public final void testGetVisibleActorCount(@Mocked final Square2d square2d) {
		new NonStrictExpectations() {
			{
				square2d.isVisible();
				result = true;
				square2d.getColor();
				result = new Color();
			}
		};
		final MainActivity activity = this.createActivity();
		final int expected1 = 0;
		final int actual1 = activity.getVisibleActorCount();
		assertThat(actual1, is(expected1));

		activity.setSquare(square2d);

		final int expected2 = 1;
		final int actual2 = activity.getVisibleActorCount();
		assertThat(actual2, is(expected2));

		activity.showMenu(0, 0);
		activity.showItemList();

		final int expected3 = 3;
		final int actual3 = activity.getVisibleActorCount();
		assertThat(actual3, is(expected3));

		activity.showSquareOnly();

		final int expected4 = 1;
		final int actual4 = activity.getVisibleActorCount();
		assertThat(actual4, is(expected4));

	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getCamera()}.
	 */
	@Test
	public final void testGetCamera() {
		final MainActivity activity = this.createActivity();
		assertThat(activity.getCamera(), is(this.freeSquare.getCamera()));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getMinZoom()}.
	 */
	@Test
	public final void testGetMinZoom() {
		final MainActivity activity = this.createActivity();
		final float minZoom = activity.getMinZoom();
		final float maxZoom = activity.getMaxZoom();
		final boolean actual = minZoom > 0 && minZoom <= maxZoom;
		final boolean expected = true;
		assertThat(actual, is(expected));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#getMaxZoom()}.
	 */
	@Test
	public final void testGetMaxZoom() {
		final MainActivity activity = this.createActivity();
		final float minZoom = activity.getMinZoom();
		final float maxZoom = activity.getMaxZoom();
		final boolean actual = maxZoom > 0 && maxZoom >= minZoom;
		final boolean expected = true;
		assertThat(actual, is(expected));
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#notify(org.nognog.freeSquare.model.square.SquareEvent)}
	 * .
	 * 
	 * @param square2d
	 * @param square2dObject
	 * @param lifeObject
	 */
	@Test
	public final void testNotifySquareEvent(@Cascading final Square2d square2d, @Mocked final Square2dObject square2dObject, @Mocked final LifeObject lifeObject) {
		final Life lifeOfLifeObject = new Life(null, null, null);
		final MainActivity activity = this.createActivity();
		new NonStrictExpectations(Square2dObjectItem.class) {
			{
				Square2dObjectItem.getInstance(square2dObject.getType());
				result = null;
			}
		};
		new NonStrictExpectations() {
			{
				lifeObject.getLife();
				result = lifeOfLifeObject;
			}
		};
		activity.notify(new CollectObjectRequestEvent(square2dObject));
		new Verifications() {
			{
				activity.getPlayer().putItem((Item<?, ?>) any);
				times = 0;
			}
		};

		activity.setSquare(square2d);

		activity.notify(new CollectObjectRequestEvent(square2dObject));
		new Verifications() {
			{
				activity.getPlayer().putItem((Item<?, ?>) any);
				times = 1;
				Square2dObjectItem.getInstance(square2dObject.getType());
				times = 1;
			}
		};
		activity.notify(new CollectObjectRequestEvent(lifeObject));
		new Verifications() {
			{
				activity.getPlayer().addLife(lifeOfLifeObject);
				times = 1;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#updateCamera(org.nognog.gdx.util.camera.Camera)}
	 * .
	 * 
	 * @param camera
	 */
	@Test
	public final void testUpdateCamera(@Injectable final Camera camera) {
		final MainActivity activity = this.createActivity();
		final ItemList list = Deencapsulation.getField(activity, ItemList.class);
		new Expectations(list) {
			{
				list.updateCamera((Camera) any);
				times = 1;
			}
		};

		activity.showItemList();
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#saveDictionary(org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary)}
	 * .
	 * 
	 * @param dictionary
	 */
	@Test
	public final void testSaveDictionaryExternalOtherObjectTypeDictionary(@Injectable final ExternalOtherObjectTypeDictionary dictionary) {
		final MainActivity activity = this.createActivity();
		activity.saveDictionary(dictionary);
		new Verifications() {
			{
				MainActivityTest.this.freeSquare.saveDictionary(dictionary);
				times = 1;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#saveDictionary(org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary)}
	 * .
	 * 
	 * @param dictionary
	 */
	@Test
	public final void testSaveDictionaryExternalLifeObjectTypeDictionary(@Injectable final ExternalLifeObjectTypeDictionary dictionary) {
		final MainActivity activity = this.createActivity();
		activity.saveDictionary(dictionary);
		new Verifications() {
			{
				MainActivityTest.this.freeSquare.saveDictionary(dictionary);
				times = 1;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#inputName(org.nognog.freeSquare.model.Nameable, java.lang.String)}
	 * .
	 * 
	 * @param nameable
	 */
	@Test
	public final void testInputNameNameableString(@Mocked final Nameable nameable) {
		final String text = ""; //$NON-NLS-1$
		final MainActivity activity = this.createActivity();
		activity.inputName(nameable, text);

		new Verifications() {
			{
				MainActivityTest.this.freeSquare.inputName(nameable, text);
				times = 1;
			}
		};
	}

	/**
	 * Test method for
	 * {@link org.nognog.freeSquare.activity.main.MainActivity#inputName(org.nognog.freeSquare.model.Nameable, java.lang.String, org.nognog.freeSquare.FreeSquare.InputTextListener)}
	 * .
	 * 
	 * @param nameable
	 * @param listener
	 */
	@Test
	public final void testInputNameNameableStringInputTextListener(@Mocked final Nameable nameable, @Mocked final InputTextListener listener) {
		final String text = ""; //$NON-NLS-1$
		final MainActivity activity = this.createActivity();
		activity.inputName(nameable, text, listener);

		new Verifications() {
			{
				MainActivityTest.this.freeSquare.inputName(nameable, text, listener);
				times = 1;
			}
		};
	}

}
