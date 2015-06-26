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

import net.dermetfan.gdx.scenes.scene2d.ui.FileChooser;

import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.FreeSquare.InputTextListener;
import org.nognog.freeSquare.Messages;
import org.nognog.freeSquare.activity.FreeSquareActivity;
import org.nognog.freeSquare.activity.main.ui.PlayersItemList;
import org.nognog.freeSquare.activity.main.ui.PlayersLifeList;
import org.nognog.freeSquare.activity.main.ui.PlayersSquareList;
import org.nognog.freeSquare.model.Nameable;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.square2d.CombineSquare2d;
import org.nognog.freeSquare.square2d.CombineSquare2dUtils;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActionUtlls;
import org.nognog.freeSquare.square2d.event.CollectObjectRequestEvent;
import org.nognog.freeSquare.square2d.event.RenameRequestEvent;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectType;
import org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary;
import org.nognog.freeSquare.square2d.object.types.life.FlyingLifeObject;
import org.nognog.freeSquare.square2d.object.types.life.LandingLifeObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectType;
import org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary;
import org.nognog.freeSquare.square2d.object.types.other.OtherObjectTypeManager;
import org.nognog.freeSquare.ui.ItemList;
import org.nognog.freeSquare.ui.Menu;
import org.nognog.freeSquare.ui.ModePresenter;
import org.nognog.gdx.util.camera.Camera;
import org.nognog.gdx.util.camera.CameraObserver;
import org.nognog.gdx.util.camera.ObservableOrthographicCamera;
import org.nognog.gdx.util.ui.CameraFitFileChooser;
import org.nognog.gdx.util.ui.CameraFitSimpleDialog;
import org.nognog.gdx.util.ui.ColorUtils;
import org.nognog.gdx.util.ui.MultiLevelFlickButtonController;
import org.nognog.gdx.util.ui.SimpleDialog.SimpleDialogListener;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/04/13
 */
public class MainActivity extends FreeSquareActivity {
	static final float cameraDeceleration = 2000;
	static final int ratioOfMenuButtonWidthToCameraWidth = 4;

	private Player player;
	private Square2d square;

	private MainActivityInputProcessor inputProcessor;

	private boolean isSeparateSquareMode;

	private Menu menu;
	private PlayersItemList playersItemList;
	private PlayersLifeList playersLifeList;
	private PlayersSquareList playersSquareList;
	private ItemList itemList;
	private ModePresenter modePresenter;
	private CameraFitFileChooser fileChooser;
	private CameraFitSimpleDialog dialog;

	private final Array<CameraObserver> childCameraObserver;

	/**
	 * @param freeSquare
	 */
	public MainActivity(FreeSquare freeSquare) {
		super(freeSquare);
		this.childCameraObserver = new Array<>();
		this.initializeWidgets();
		this.inputProcessor = new MainActivityInputProcessor(this);
		this.setPlayer(freeSquare.getPlayer());
		this.hideAllUIsExceptForSquare();
	}

	private void initializeWidgets() {
		final FreeSquare freeSquare = this.getFreeSquare();
		final BitmapFont font = freeSquare.getFont();
		this.menu = new Menu(font, freeSquare.getLogicalCameraWidth() / ratioOfMenuButtonWidthToCameraWidth);
		this.menu.addFlickButtonController(new MultiLevelFlickButtonController.MultiLevelFlickButtonInputListener() {
			@Override
			public void up() {
				MainActivity.this.hideMenu();
				MainActivity.this.showPlayersItemList();
			}

			@Override
			public void right() {
				MainActivity.this.hideMenu();
				MainActivity.this.showPlayersSquareList();
			}

			@Override
			public void left() {
				MainActivity.this.hideMenu();
				MainActivity.this.showPlayersLifeList();
			}

			@Override
			public void down() {
				MainActivity.this.hideMenu();
				MainActivity.this.showItemList();
			}

		}, Messages.getString("item"), "リスト", Messages.getString("square"), Messages.getString("life"), ColorUtils.emerald, ColorUtils.nephritis); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.menu.addFlickButtonController(new MultiLevelFlickButtonController.MultiLevelFlickButtonInputListener() {
			@Override
			public void up() {
				MainActivity.this.setSquareWithAction(null);
				MainActivity.this.showSquareOnly();
			}

			@Override
			public void right() {
				MainActivity.this.showSquareOnly();
				MainActivity.this.showFileChooser();
			}

			@Override
			public void left() {
				MainActivity.this.showSquareOnly();
				final ExternalLifeObjectTypeDictionary externalLifeObjectTypeDictionary = LifeObjectTypeManager.getInstance().getDictionary();
				externalLifeObjectTypeDictionary.clear();
				final ExternalOtherObjectTypeDictionary externalOtherObjectTypeDictionary = OtherObjectTypeManager.getInstance().getDictionary();
				externalOtherObjectTypeDictionary.clear();
				MainActivity.this.saveDictionary(externalLifeObjectTypeDictionary);
				MainActivity.this.saveDictionary(externalOtherObjectTypeDictionary);
			}

			@Override
			public void down() {
				MainActivity.this.showSquareOnly();
				MainActivity.this.setSeparateSquareMode(true);
			}

		}, Messages.getString("clear"), Messages.getString("separate"), "file", "/", ColorUtils.nephritis, ColorUtils.emerald); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		this.playersItemList = new PlayersItemList(this, freeSquare.getPlayer(), font);
		this.playersLifeList = new PlayersLifeList(this, freeSquare.getPlayer(), font);
		this.playersSquareList = new PlayersSquareList(this, freeSquare.getPlayer(), font);

		this.itemList = new ItemList(freeSquare.getCamera(), font) {
			@Override
			protected void selectedItemTapped(Item<?, ?> tappedItem) {
				freeSquare.getPlayer().putItem(tappedItem);
			}
		};
		LifeObjectTypeManager.getInstance().getDictionary().addDictionaryObserver(this.itemList);
		OtherObjectTypeManager.getInstance().getDictionary().addDictionaryObserver(this.itemList);

		this.modePresenter = new ModePresenter(freeSquare.getCamera(), font) {
			@Override
			public void tapped() {
				MainActivity.this.showSquareOnly();
			}
		};

		final FileChooser.Listener listener = new FileChooser.Listener() {
			@Override
			public void choose(Array<FileHandle> files) {
				MainActivity.this.showSquareOnly();
			}

			@Override
			public void choose(final FileHandle file) {
				MainActivity.this.showSquareOnly();
				final SimpleDialogListener createNewExternalTypeDialogListener = new SimpleDialogListener() {
					@Override
					public void leftButtonClicked() {
						final OtherObjectTypeManager otherObjectTypeManager = OtherObjectTypeManager.getInstance();
						final ExternalOtherObjectType newType = otherObjectTypeManager.createExternalOtherObjectType(file.path());
						MainActivity.this.showSquareOnly();
						MainActivity.this.inputName(newType, "Please input decoration name", new FreeSquare.InputTextListener() { //$NON-NLS-1$
									@Override
									public void afterInputName() {
										otherObjectTypeManager.register(newType);
										MainActivity.this.saveDictionary(otherObjectTypeManager.getDictionary());
									}
								});
					}

					@Override
					public void rightButtonClicked() {
						final SimpleDialogListener createExternelLifeObjectTypeDialogLister = new SimpleDialogListener() {
							@Override
							public void leftButtonClicked() {
								this.addNewLifeObject(LandingLifeObject.class);
							}

							@Override
							public void rightButtonClicked() {
								this.addNewLifeObject(FlyingLifeObject.class);
							}

							private <T extends LifeObject> void addNewLifeObject(Class<T> newLifeObjectClass) {
								final LifeObjectTypeManager lifeObjectTypeManager = LifeObjectTypeManager.getInstance();
								final ExternalLifeObjectType newType = lifeObjectTypeManager.createExternalLifeObjectType(file.path(), 100, 5, newLifeObjectClass);
								MainActivity.this.showSquareOnly();
								MainActivity.this.inputName(newType, "Please input name", new FreeSquare.InputTextListener() { //$NON-NLS-1$
											@Override
											public void afterInputName() {
												lifeObjectTypeManager.register(newType);
												MainActivity.this.saveDictionary(lifeObjectTypeManager.getDictionary());
											}
										});
							}
						};
						MainActivity.this.showDialog("Please select Landing or Flying", "Landing", "Flying", createExternelLifeObjectTypeDialogLister); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
				};
				MainActivity.this.showDialog("Please select object type.", "Decoration", "Life", createNewExternalTypeDialogListener); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			}

			@Override
			public void cancel() {
				MainActivity.this.showSquareOnly();
			}
		};
		this.fileChooser = new CameraFitFileChooser(freeSquare.getCamera(), font, listener);
		this.dialog = new CameraFitSimpleDialog(freeSquare.getCamera(), font);
	}

	/**
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
		if (this.playersItemList != null) {
			this.playersItemList.setPlayer(player);
		}
		if (this.playersSquareList != null) {
			this.playersSquareList.setPlayer(player);
		}
		if (this.playersLifeList != null) {
			this.playersLifeList.setPlayer(player);
		}
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * @return the square
	 */
	public Square2d getSquare() {
		return this.square;
	}

	/**
	 * @return true if separate mode
	 */
	public boolean isSeparateSquareMode() {
		return this.isSeparateSquareMode;
	}

	/**
	 * @param enable
	 */
	public void setSeparateSquareMode(boolean enable) {
		this.isSeparateSquareMode = enable;
		if (this.square instanceof CombineSquare2d) {
			((CombineSquare2d) this.square).setHighlightSeparatableSquare(enable);
			if (enable) {
				this.showModePresenter(Messages.getString("separate", "mode")); //$NON-NLS-1$ //$NON-NLS-2$
				this.square.getColor().a = 1;
				this.square.setTouchable(Touchable.enabled);
			} else {
				this.hideModePresenter();
			}
		}
	}

	/**
	 * convert square to combineSquare2d
	 */
	public void convertThisSquareToCombineSquare2d() {
		if (!(this.square instanceof SimpleSquare2d)) {
			throw new IllegalStateException("convert failure : not support no-SimpleSquare2d yet"); //$NON-NLS-1$
		}
		final Square2d convertTarget = this.square;
		final float cameraX = this.getCamera().getX();
		final float cameraY = this.getCamera().getY();
		this.setSquare(null);
		final CombineSquare2d combineSquare = new CombineSquare2d(convertTarget);
		combineSquare.startCreateSimpleTextureAsyncIfNotStart();
		combineSquare.startSetupSeparatableSquaresAsyncIfNotStart();
		this.player.replaceSquare(convertTarget, combineSquare);
		this.setSquare(combineSquare);
		this.getCamera().setX(cameraX, false);
		this.getCamera().setY(cameraY, false);
	}

	/**
	 * @param setSquare
	 * @param direction
	 */
	public void setSquareWithAction(Square2d setSquare) {
		this.setSquareWithAction(setSquare, Direction.LEFT);
	}

	/**
	 * @param setSquare
	 * @param direction
	 */
	public void setSquareWithAction(Square2d setSquare, Direction direction) {
		this.addAction(Square2dActionUtlls.changeSquare(this, setSquare, direction));
	}

	/**
	 * @param square
	 */
	public void setSquare(Square2d square) {
		if (this.square != null) {
			this.enableSquare();
			this.square.removeSquareObserver(this);
			this.square.remove();
		}
		this.square = square;
		if (this.square != null) {
			this.square.setPosition(0, 0);
			this.addActor(this.square);
			this.square.addSquareObserver(this);
			if (this.square instanceof CombineSquare2d) {
				((CombineSquare2d) this.square).setHighlightSeparatableSquare(this.isSeparateSquareMode);
			}
		}
		this.player.notifyPlayerObservers();
		this.getCamera().setX((this.getVisibleRangeLowerLeft().x + this.getVisibleRangeUpperRight().x) / 2, false);
		this.getCamera().setY((this.getVisibleRangeLowerLeft().y + this.getVisibleRangeUpperRight().y) / 2, false);
	}

	/**
	 * @param putSquare
	 * @return true if success
	 */
	public boolean putSquare2d(Square2d putSquare) {
		if (this.getSquare() == null) {
			this.getPlayer().addSquare(putSquare);
			this.setSquare(putSquare);
			return true;
		}
		this.getSquare().removeActorForce(putSquare);
		return this.combineSquare(putSquare);
	}

	private boolean combineSquare(Square2d combineSquare) {
		if (!(this.getSquare() instanceof CombineSquare2d)) {
			this.convertThisSquareToCombineSquare2d();
		}
		final CombineSquare2d baseSquare = (CombineSquare2d) this.getSquare();
		final Vector2[] stageCoordinatesBaseSquareVertices = this.getSquare().getStageCoordinatesVertices();
		for (Vertex addSquareVertex : combineSquare.getVertices()) {
			final Vector2 stageCoordinatesAddSquareVertex = combineSquare.localToStageCoordinates(new Vector2(addSquareVertex.x, addSquareVertex.y));
			for (int i = 0; i < stageCoordinatesBaseSquareVertices.length; i++) {
				final Vector2 stageCoordinatesBaseSquareVertex = stageCoordinatesBaseSquareVertices[i];
				final float r = Square2dUtils.toVertex(stageCoordinatesAddSquareVertex).calculateR(Square2dUtils.toVertex(stageCoordinatesBaseSquareVertex));
				if (CombineSquare2dUtils.regardAsSufficientlyCloseThreshold * 2 > r) {
					final boolean isSuccess = baseSquare.combineWith(baseSquare.getVertices()[i], combineSquare, addSquareVertex);
					if (isSuccess) {
						baseSquare.startCreateSimpleTextureAsyncIfNotStart();
						baseSquare.startSetupSeparatableSquaresAsyncIfNotStart();
					}
					return isSuccess;
				}
			}
		}
		return false;
	}

	/**
	 * @param separateSquare
	 */
	public void separateSquare(Square2d separateSquare) {
		if (this.square instanceof CombineSquare2d) {
			Square2d containsSeparateSquareSquare = ((CombineSquare2d) this.square).getSquareThatContains(separateSquare);
			final boolean isSuccessSeparate = ((CombineSquare2d) this.square).separate(containsSeparateSquareSquare);
			if (isSuccessSeparate) {
				((CombineSquare2d) this.square).startCreateSimpleTextureAsyncIfNotStart();
				((CombineSquare2d) this.square).startSetupSeparatableSquaresAsyncIfNotStart();
				this.player.addSquare(containsSeparateSquareSquare);
			}
		}
	}

	/**
	 * show square
	 */
	public void showSquareOnly() {
		this.hideAllUIsExceptForSquare();
		this.enableSquare();
		this.setSeparateSquareMode(false);
	}

	/**
	 * hide all actor except for square
	 */
	private void hideAllUIsExceptForSquare() {
		this.hideMenu();
		this.hidePlayersItemList();
		this.hidePlayersLifeList();
		this.hidePlayersSquareList();
		this.hideItemList();
		this.hideModePresenter();
		this.hideFileChooser();
		this.hideDialog();
	}

	private void enableSquare() {
		if (this.square == null) {
			return;
		}
		this.square.getColor().a = 1;
		this.square.setTouchable(Touchable.enabled);
	}

	private void disableSquare() {
		if (this.square == null) {
			return;
		}
		this.getFreeSquare().getStage().cancelTouchFocus(this.square);
		this.square.getColor().a = 0.75f;
		this.square.setTouchable(Touchable.disabled);
	}

	private void show(Actor actor) {
		if (!this.getChildren().contains(actor, true)) {
			this.addActor(actor);
		}
		actor.setVisible(true);
		actor.toFront();
		if (actor instanceof CameraObserver) {
			this.addChildCameraObserver((CameraObserver) actor);
			((CameraObserver) actor).updateCamera(this.getCamera());
		}
		this.getFreeSquare().getStage().cancelTouchFocus();
		this.disableSquare();
	}

	private void hide(Actor actor) {
		actor.setVisible(false);
		if (actor instanceof CameraObserver) {
			this.removeChildCameraObserver((CameraObserver) actor);
		}
	}

	/**
	 * show menu
	 * 
	 * @param x
	 * @param y
	 */
	public void showMenu(float x, float y) {
		this.menu.setPosition(x, y);
		this.menu.showFirstLevel();
		this.show(this.menu);
	}

	/**
	 * hide menu
	 */
	public void hideMenu() {
		this.hide(this.menu);
	}

	/**
	 * show player item list.
	 */
	public void showPlayersItemList() {
		this.show(this.playersItemList);
	}

	/**
	 * hide player item list
	 */
	public void hidePlayersItemList() {
		this.hide(this.playersItemList);
	}

	/**
	 * show player life list
	 */
	public void showPlayersLifeList() {
		this.show(this.playersLifeList);
	}

	/**
	 * show player life list
	 */
	public void hidePlayersLifeList() {
		this.hide(this.playersLifeList);
	}

	/**
	 * show plauer square list
	 */
	public void showPlayersSquareList() {
		this.show(this.playersSquareList);
	}

	/**
	 * show plyaer square list
	 */
	public void hidePlayersSquareList() {
		this.hide(this.playersSquareList);
	}

	/**
	 * show item list
	 */
	public void showItemList() {
		this.show(this.itemList);
	}

	/**
	 * hide item list
	 */
	public void hideItemList() {
		this.hide(this.itemList);
	}

	/**
	 * show mode presenter
	 * 
	 * @param text
	 */
	public void showModePresenter(String text) {
		this.show(this.modePresenter);
		this.modePresenter.setText(text);
		this.modePresenter.updateCamera(this.getCamera());
	}

	/**
	 * hide mode presenter
	 */
	public void hideModePresenter() {
		this.hide(this.modePresenter);
	}

	/**
	 * show file chooser
	 */
	public void showFileChooser() {
		this.show(this.fileChooser);
	}

	/**
	 * hide file chooser
	 */
	public void hideFileChooser() {
		this.hide(this.fileChooser);
	}

	/**
	 * show file chooser
	 * 
	 * @param text
	 * @param leftButtonText
	 * @param rightButtonText
	 * @param listener
	 */
	public void showDialog(String text, String leftButtonText, String rightButtonText, SimpleDialogListener listener) {
		this.dialog.setText(text);
		this.dialog.setLeftButtonText(leftButtonText);
		this.dialog.setRightButtonText(rightButtonText);
		this.dialog.setListener(listener);
		this.dialog.setDebug(true);
		this.show(this.dialog);
	}

	/**
	 * hide file chooser
	 */
	public void hideDialog() {
		this.hide(this.dialog);
	}

	@Override
	public InputProcessor getInputProcesser() {
		return this.inputProcessor;
	}

	/**
	 * @return lower-left point of camera range
	 */
	private Vector2 getVisibleRangeLowerLeft() {
		if (this.square == null) {
			return new Vector2(0, 0);
		}
		return new Vector2(this.square.getLeftEndX() - this.square.getWidth(), this.square.getBottomEndY() - this.square.getHeight());
	}

	/**
	 * @return upper-right point of camera range
	 */
	private Vector2 getVisibleRangeUpperRight() {
		if (this.square == null) {
			return new Vector2(0, 0);
		}
		return new Vector2(this.square.getRightEndX() + this.square.getWidth(), this.square.getTopEndY() + this.square.getHeight());
	}

	/**
	 * @param notifyIfChanged
	 * 
	 */
	public void adjustCameraZoomAndPositionIfRangeOver(boolean notifyIfChanged) {
		if (notifyIfChanged == false) {
			this.adjustCameraZoomAndPositionIfRangeOver();
			return;
		}
		final ObservableOrthographicCamera camera = this.getCamera();
		final float oldZoom = camera.getZoom();
		final float oldX = camera.getX();
		final float oldY = camera.getX();

		this.adjustCameraZoomAndPositionIfRangeOver();

		final float newZoom = camera.getZoom();
		final float newX = camera.getX();
		final float newY = camera.getX();
		if (oldZoom != newZoom || oldX != newX || oldY != newY) {
			camera.notifyCameraObservers();
		}
	}

	private void adjustCameraZoomAndPositionIfRangeOver() {
		if (this.square == null) {
			return;
		}
		final float minZoom = this.getMinZoom();
		final float maxZoom = this.getMaxZoom();
		final ObservableOrthographicCamera camera = this.getCamera();
		camera.setZoom(MathUtils.clamp(camera.getZoom(), minZoom, maxZoom), false);
		final float minViewableWidth = camera.getViewportWidth() * minZoom;
		final float minViewableHeight = camera.getViewportHeight() * minZoom;

		float squareWidth = this.square.getWidth();
		float squareHeight = this.square.getHeight();
		if (minViewableWidth > squareWidth + minViewableWidth / 2f || minViewableHeight > squareHeight + minViewableHeight / 2f) {
			camera.setX(this.square.getX() + squareWidth / 2, false);
			camera.setY(this.square.getY() + squareHeight / 2, false);
			return;
		}
		final float viewingWidth = camera.getViewportWidth() * camera.getZoom();
		final float viewingHeight = camera.getViewportHeight() * camera.getZoom();
		final float minCameraPositionX = this.getVisibleRangeLowerLeft().x + viewingWidth / 2f;
		final float maxCameraPositionX = this.getVisibleRangeUpperRight().x - viewingWidth / 2f;
		final float minCameraPositionY = this.getVisibleRangeLowerLeft().y + viewingHeight / 2f;
		final float maxCameraPositionY = this.getVisibleRangeUpperRight().y - viewingHeight / 2f;
		camera.setX(MathUtils.clamp(camera.getX(), minCameraPositionX, maxCameraPositionX), false);
		camera.setY(MathUtils.clamp(camera.getY(), minCameraPositionY, maxCameraPositionY), false);
	}

	/**
	 * @return count of showing UIs
	 */
	public int getVisibleActorCount() {
		int result = 0;
		for (Actor child : this.getChildren()) {
			if (child.isVisible()) {
				result++;
			}
		}
		return result;
	}

	/**
	 * @return camera
	 */
	public ObservableOrthographicCamera getCamera() {
		return this.getFreeSquare().getCamera();
	}

	/**
	 * @return min zoom
	 */
	@SuppressWarnings("static-method")
	public float getMinZoom() {
		return 1;
	}

	/**
	 * @return max zoom
	 */
	public float getMaxZoom() {
		if (this.square == null) {
			return 1f;
		}
		final Camera camera = this.getCamera();
		final float fitSquareWidthZoom = this.square.getWidth() / camera.getViewportWidth();
		final float fitSquareHeightZoom = this.square.getHeight() / camera.getViewportHeight();
		final float maxZoom = Math.max(1, Math.max(fitSquareWidthZoom, fitSquareHeightZoom));
		return maxZoom;
	}

	@Override
	public void handleEvent(SquareEvent event) {
		if (event instanceof CollectObjectRequestEvent && this.square != null) {
			final Square2dObject collectRequestedObject = ((CollectObjectRequestEvent) event).getCollectRequestedObject();
			if (collectRequestedObject instanceof LifeObject) {
				this.player.addLife(((LifeObject) collectRequestedObject).getLife());
			} else {
				this.player.putItem(Square2dObjectItem.getInstance(collectRequestedObject.getType()));
			}
			this.square.removeSquareObject(collectRequestedObject);
		}

		if (event instanceof RenameRequestEvent) {
			final Nameable renameRequestedObject = ((RenameRequestEvent) event).getRenameRequestedObject();
			final String title;
			if (renameRequestedObject instanceof Life) {
				title = Messages.getString("lifeNameInput"); //$NON-NLS-1$
			} else if (renameRequestedObject instanceof Square) {
				title = Messages.getString("squareNameInput"); //$NON-NLS-1$
			} else {
				title = ""; //$NON-NLS-1$
			}
			this.getFreeSquare().inputName(renameRequestedObject, title);
		}
	}

	private void addChildCameraObserver(CameraObserver observer) {
		if (observer == null || this.childCameraObserver.contains(observer, true)) {
			return;
		}

		this.childCameraObserver.add(observer);
	}

	private void removeChildCameraObserver(CameraObserver observer) {
		this.childCameraObserver.removeValue(observer, true);
	}

	@Override
	public void updateCamera(Camera camera) {
		for (int i = 0; i < this.childCameraObserver.size; i++) {
			this.childCameraObserver.get(i).updateCamera(this.getCamera());
		}
	}

	@Override
	public void resume() {
		// Nothing to do
	}

	@Override
	public void pause() {
		this.hideAllUIsExceptForSquare();
	}

	@Override
	public void act(float delta) {
		this.actExceptForSquare(delta);
	}

	private void actExceptForSquare(float delta) {
		// Player has this.square. So this.square is acted with Player
		final int oldSquareIndex = this.getChildren().indexOf(this.square, true);
		final Square2d temporaryRemovedSquare = this.square;
		if (oldSquareIndex != -1) {
			this.getChildren().removeValue(this.square, true);
		}
		super.act(delta);
		if (oldSquareIndex != -1 && this.square == temporaryRemovedSquare) {
			this.getChildren().insert(oldSquareIndex, this.square);
		}
	}

	/**
	 * @param externalOtherObjectTypeDictionary
	 */
	public void saveDictionary(ExternalOtherObjectTypeDictionary externalOtherObjectTypeDictionary) {
		this.getFreeSquare().saveDictionary(externalOtherObjectTypeDictionary);
	}

	/**
	 * @param externalLifeObjectTypeDictionary
	 */
	public void saveDictionary(ExternalLifeObjectTypeDictionary externalLifeObjectTypeDictionary) {
		this.getFreeSquare().saveDictionary(externalLifeObjectTypeDictionary);
	}

	/**
	 * @param nameable
	 * @param string
	 */
	public void inputName(Nameable nameable, String string) {
		this.getFreeSquare().inputName(nameable, string);
	}

	/**
	 * @param nameable
	 * @param string
	 * @param inputTextListener
	 */
	public void inputName(Nameable nameable, String string, InputTextListener inputTextListener) {
		this.getFreeSquare().inputName(nameable, string, inputTextListener);
	}

}
