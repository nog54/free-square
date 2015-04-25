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

package org.nognog.freeSquare;

import java.util.Date;

import org.nognog.freeSquare.activity.FreeSquareActivity;
import org.nognog.freeSquare.activity.FreeSquareActivityFactory;
import org.nognog.freeSquare.model.Nameable;
import org.nognog.freeSquare.model.persist.PersistItems;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectTypeManager;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter {
	private Stage stage;
	private FreeSquareActivity currentActivity;
	private FreeSquareActivityFactory activityFactory;

	private InputMultiplexer multiplexer;

	private int logicalCameraWidth;
	private int logicalCameraHeight;

	private Array<CameraObserver> cameraObservers;

	private BitmapFont font;
	private PlayLog playlog;
	private Player player;
	private Date lastRun;

	@Override
	public void create() {
		this.cameraObservers = new Array<>();
		this.logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		this.logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();
		this.font = FontUtil.createMPlusFont(Settings.getFontSize());
		this.stage = new Stage(new FitViewport(this.logicalCameraWidth, this.logicalCameraHeight));
		final float timeFromLastRun = setupPersistItems();
		this.actLongTime(timeFromLastRun);

		this.multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(this.multiplexer);
		this.player = null;
		this.activityFactory = new FreeSquareActivityFactory(this);
		if (this.player != null) {
			this.setActivity(this.activityFactory.getMainActivity());
		} else {
			this.setActivity(this.activityFactory.getInitializeActivity());
		}
	}

	/**
	 * @param activity
	 */
	public void setActivity(FreeSquareActivity activity) {
		if (this.currentActivity != null) {
			this.removeCameraObserver(this.currentActivity);
			this.currentActivity.pause();
		}
		this.multiplexer.clear();
		this.stage.getRoot().removeActor(this.currentActivity);

		this.stage.getRoot().addActor(activity);
		final InputProcessor inputProcesser = activity.getInputProcesser();
		if (inputProcesser != null) {
			this.multiplexer.addProcessor(inputProcesser);
		}
		this.multiplexer.addProcessor(this.stage);
		this.currentActivity = activity;
		this.addCameraObserver(this.currentActivity);
		this.currentActivity.resume();
	}

	/**
	 * @return stage
	 */
	public Stage getStage() {
		return this.stage;
	}

	/**
	 * @return font
	 */
	public BitmapFont getFont() {
		return this.font;
	}

	/**
	 * @return stage camera
	 */
	public Camera getCamera() {
		return this.stage.getCamera();
	}

	/**
	 * @return the logicalCameraWidth
	 */
	public int getLogicalCameraWidth() {
		return this.logicalCameraWidth;
	}

	/**
	 * @return the logicalCameraHeight
	 */
	public int getLogicalCameraHeight() {
		return this.logicalCameraHeight;
	}

	/**
	 * @param player
	 * @return square that first appear in array
	 */
	public static Square2d getHeadSquare2d(Player player) {
		for (Square<?> square : player.getSquares()) {
			if (square instanceof Square2d) {
				return (Square2d) square;
			}
		}
		return null;
	}

	/**
	 * @param nameable
	 * @param title
	 */
	public void inputName(final Nameable nameable, String title) {
		this.inputName(nameable, title, ""); //$NON-NLS-1$
	}

	/**
	 * @param nameable
	 * @param title
	 * @param hint
	 */
	public void inputName(final Nameable nameable, String title, String hint) {
		this.inputName(nameable, title, hint, Settings.getObjectNameMaxTextDrawWidth());
	}

	/**
	 * @param nameable
	 * @param title
	 * @param hint
	 * @param maxTextDrawWidth
	 */
	public void inputName(final Nameable nameable, String title, String hint, final float maxTextDrawWidth) {
		Gdx.input.getTextInput(new TextInputListener() {
			@Override
			public void input(String text) {
				final BitmapFont listFont = FreeSquare.this.getFont();
				String inputText = text;
				for (int i = inputText.length(); i > 0; i--) {
					inputText = inputText.substring(0, i);
					final float inputTextDrawWidth = listFont.getBounds(inputText).width;
					if (inputTextDrawWidth <= maxTextDrawWidth) {
						break;
					}
				}
				for (char c : inputText.toCharArray()) {
					final String checkCharSequence = String.valueOf(c);
					if (!FontUtil.ALL_AVAILABLE_CHARACTERS.contains(checkCharSequence)) {
						inputText = inputText.replace(checkCharSequence, ""); //$NON-NLS-1$
					}
				}
				if (inputText.equals("")) { //$NON-NLS-1$
					return;
				}
				nameable.setName(inputText);
			}

			@Override
			public void canceled() {
				//
			}
		}, title, nameable.getName(), hint);
	}

	@Override
	public void render() {
		this.act(Gdx.graphics.getDeltaTime());
		this.drawStage();
	}

	private void act(float delta) {
		this.stage.act(delta);
		if (this.player != null) {
			for (Square<?> playerSquare : this.player.getSquares()) {
				if (playerSquare instanceof Square2d) {
					((Square2d) playerSquare).act(delta);
				}
			}
		}
	}

	private void actLongTime(float longDelta) {
		float ramainActTime = longDelta;
		while (ramainActTime > 0) {
			float delta = 0.5f;
			ramainActTime -= delta;
			if (ramainActTime < 0) {
				delta += ramainActTime;
			}
			this.act(delta);
		}
	}

	private void drawStage() {
		Gdx.gl.glClearColor(0.2f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
	}

	@Override
	public void pause() {
		if (this.player != null) {
			PersistItems.PLAYER.save(this.player);
			LastPlay.update();
		}
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void resume() {
		if (this.player != null) {
			Date lastPauseDate = LastPlay.getLastPlayDate();
			Date now = new Date();
			this.actLongTime((now.getTime() - lastPauseDate.getTime()) / 1000f);
		}
		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void dispose() {
		try {
			if (this.font != null) {
				this.font.dispose();
			}
			this.stage.dispose();
			Square2dObjectTypeManager.disposeAll();
			Square2d.dispose();
		} catch (Throwable t) {
			Gdx.app.error(this.getClass().getName(), "error occured in dispose", t); //$NON-NLS-1$
			throw t;
		}
	}

	private float setupPersistItems() {
		this.playlog = PersistItems.PLAY_LOG.load();
		if (this.playlog == null) {
			System.out.println("new playlog"); //$NON-NLS-1$
			this.playlog = PlayLog.create();
			PersistItems.PLAY_LOG.save(this.playlog);
		}
		this.player = PersistItems.PLAYER.load();
		this.lastRun = LastPlay.getLastPlayDate();
		if (this.lastRun == null) {
			System.out.println("new last Run"); //$NON-NLS-1$
			this.lastRun = new Date();
			return 0;
		}
		final Date now = new Date();
		float diffSecond = (now.getTime() - this.lastRun.getTime()) / 1000f;
		return diffSecond;
	}

	/**
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * create player
	 */
	public void createPlayer() {
		if(this.player != null){
			throw new RuntimeException("player should be null if create new player."); //$NON-NLS-1$
		}
		this.player = new Player();
		this.inputName(this.player, "プレイヤー名入力"); //$NON-NLS-1$
	}

	/**
	 * @return activity factory
	 */
	public FreeSquareActivityFactory getActivityFactory() {
		return this.activityFactory;
	}

	/**
	 * @param observer
	 */
	public void addCameraObserver(CameraObserver observer) {
		if (!this.cameraObservers.contains(observer, true)) {
			this.cameraObservers.add(observer);
		}
	}

	/**
	 * @param observer
	 */
	public void removeCameraObserver(CameraObserver observer) {
		this.cameraObservers.removeValue(observer, true);
	}

	/**
	 * notify all camera observers
	 */
	public void notifyCameraObservers() {
		for (int i = 0; i < this.cameraObservers.size; i++) {
			this.cameraObservers.get(i).updateCamera(this.stage.getCamera());
		}
	}

	/**
	 * exit application
	 */
	@SuppressWarnings("static-method")
	public void exit() {
		Gdx.app.exit();
	}
}
