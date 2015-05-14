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

package org.nognog.freeSquare.activity;

import net.dermetfan.gdx.scenes.scene2d.ui.FileChooser;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.FreeSquare;
import org.nognog.freeSquare.Messages;
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
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.Square2dUtils;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.action.Square2dActions;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.CollectObjectRequestEvent;
import org.nognog.freeSquare.square2d.event.RenameRequestEvent;
import org.nognog.freeSquare.square2d.item.Square2dItem;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.FlyingLifeObject;
import org.nognog.freeSquare.square2d.object.LandingLifeObject;
import org.nognog.freeSquare.square2d.object.LifeObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;
import org.nognog.freeSquare.square2d.object.types.life.ExternalLifeObjectTypeDictionary;
import org.nognog.freeSquare.square2d.object.types.life.LifeObjectTypeManager;
import org.nognog.freeSquare.square2d.object.types.other.ExternalOtherObjectTypeDictionary;
import org.nognog.freeSquare.square2d.object.types.other.OtherObjectTypeManager;
import org.nognog.freeSquare.square2d.ui.ColorUtils;
import org.nognog.freeSquare.square2d.ui.FreeSquareFileChooser;
import org.nognog.freeSquare.square2d.ui.FreeSquareSimpleDialog;
import org.nognog.freeSquare.square2d.ui.ItemList;
import org.nognog.freeSquare.square2d.ui.Menu;
import org.nognog.freeSquare.square2d.ui.ModePresenter;
import org.nognog.freeSquare.square2d.ui.MultiLevelFlickButtonController;
import org.nognog.freeSquare.square2d.ui.PlayersItemList;
import org.nognog.freeSquare.square2d.ui.PlayersLifeList;
import org.nognog.freeSquare.square2d.ui.PlayersSquareList;
import org.nognog.freeSquare.square2d.ui.SimpleDialog.SimpleDialogListener;
import org.nognog.freeSquare.util.square2d.AllSquare2dObjectTypeManager;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

	private static final int ratioOfMenuButtonWidthToCameraWidth = 4;

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
	private FreeSquareFileChooser fileChooser;
	private FreeSquareSimpleDialog dialog;

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
	}

	private void initializeWidgets() {
		final FreeSquare freeSquare = this.getFreeSquare();
		final BitmapFont font = freeSquare.getFont();
		this.menu = new Menu(font, freeSquare.getLogicalCameraWidth() / ratioOfMenuButtonWidthToCameraWidth);
		this.menu.addFlickButtonController(new MultiLevelFlickButtonController.MultiLevelFlickButtonInputListener() {
			@Override
			public void up() {
				MainActivity.this.hideMenu();
				MainActivity.this.showPlayerItemList();
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
				if (MainActivity.this.getSquare() != null) {
					MainActivity.this.showFileChooser();
				}
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

		this.itemList = new ItemList(freeSquare.getCamera(), MainActivity.getAllItems(), font) {
			@Override
			protected void selectedItemTapped(Item<?, ?> tappedItem) {
				freeSquare.getPlayer().putItem(tappedItem);
			}
		};

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
				MainActivity.this.showDialog("Please select object type.", "Decoration", "Life", new SimpleDialogListener() { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

							@Override
							public void leftButtonClicked() {
								Square2dObjectType<?> newType = OtherObjectTypeManager.getInstance().createExternalOtherObjectType("noname", file.path()); //$NON-NLS-1$
								MainActivity.this.saveDictionary(OtherObjectTypeManager.getInstance().getDictionary());
								Square2dObject newObject = newType.create();
								if (MainActivity.this.getSquare() != null) {
									MainActivity.this.getSquare().addSquareObject(newObject);
								}
								MainActivity.this.showSquareOnly();
							}

							@Override
							public void rightButtonClicked() {
								MainActivity.this.showDialog("Please select Landing or Flying", "Landing", "Flying", new SimpleDialogListener() { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

											@Override
											public void leftButtonClicked() {
												this.addNewLifeObject(LandingLifeObject.class);
											}

											@Override
											public void rightButtonClicked() {
												this.addNewLifeObject(FlyingLifeObject.class);
											}

											private <T extends LifeObject> void addNewLifeObject(Class<T> newLifeObjectClass) {
												final LifeObjectTypeManager lifeObjectManager = LifeObjectTypeManager.getInstance();
												Square2dObjectType<?> newType = lifeObjectManager.createAndRegisterExternalLifeObjectType("noname", file.path(), 100, 5, newLifeObjectClass); //$NON-NLS-1$
												MainActivity.this.saveDictionary(lifeObjectManager.getDictionary());
												Square2dObject newObject = newType.create();
												if (MainActivity.this.getSquare() != null) {
													MainActivity.this.getSquare().addSquareObject(newObject);
												}
												MainActivity.this.showSquareOnly();
											}
										});
							}
						});

			}

			@Override
			public void cancel() {
				MainActivity.this.showSquareOnly();
			}
		};
		this.fileChooser = new FreeSquareFileChooser(freeSquare.getCamera(), font, listener);
		this.dialog = new FreeSquareSimpleDialog(freeSquare);
	}

	private static Item<?, ?>[] getAllItems() {
		final Item<?, ?>[] allSquare2dObjectItems = Square2dObjectItem.toSquare2dObjectItem(AllSquare2dObjectTypeManager.getAllTypes());
		final Item<?, ?>[] allSquareItems = Square2dItem.getAllItems();
		final Item<?, ?>[] allItems = new Item<?, ?>[allSquare2dObjectItems.length + allSquareItems.length];
		System.arraycopy(allSquare2dObjectItems, 0, allItems, 0, allSquare2dObjectItems.length);
		System.arraycopy(allSquareItems, 0, allItems, allSquare2dObjectItems.length, allSquareItems.length);
		return allItems;
	}

	/**
	 * @return the playerItemList
	 */
	public PlayersItemList getPlayerItemList() {
		return this.playersItemList;
	}

	/**
	 * @return the playersLifeList
	 */
	public PlayersLifeList getPlayersLifeList() {
		return this.playersLifeList;
	}

	/**
	 * @return the playersSquareList
	 */
	public PlayersSquareList getPlayersSquareList() {
		return this.playersSquareList;
	}

	/**
	 * @return the itemList
	 */
	public ItemList getItemList() {
		return this.itemList;
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
			} else {
				this.hideModePresenter();
			}
		}
	}

	/**
	 * 
	 */
	public void convertThisSquareToCombineSquare2d() {
		if (this.square instanceof CombineSquare2d || this.square == null) {
			return;
		}

		if (!(this.square instanceof SimpleSquare2d)) {
			throw new IllegalArgumentException("convert failure : not support no-SimpleSquare2d yet"); //$NON-NLS-1$
		}
		final Square2d convertTarget = this.square;
		final float cameraX = this.getFreeSquare().getCamera().position.x;
		final float cameraY = this.getFreeSquare().getCamera().position.y;
		this.setSquare(null);
		final CombineSquare2d combineSquare = new CombineSquare2d(convertTarget);
		combineSquare.startCreateSimpleTextureAsyncIfNotStart();
		combineSquare.startSetupSeparatableSquaresAsyncIfNotStart();
		this.player.replaceSquare(convertTarget, combineSquare);
		this.setSquare(combineSquare);
		this.getFreeSquare().getCamera().position.x = cameraX;
		this.getFreeSquare().getCamera().position.y = cameraY;
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
		this.addAction(Square2dActions.changeSquare(this, setSquare, direction));
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
		this.getFreeSquare().getCamera().position.x = (this.getVisibleRangeLowerLeft().x + this.getVisibleRangeUpperRight().x) / 2;
		this.getFreeSquare().getCamera().position.y = (this.getVisibleRangeLowerLeft().y + this.getVisibleRangeUpperRight().y) / 2;
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
		final boolean isSuccessCombine = this.combineSquare(putSquare);
		if (isSuccessCombine) {
			this.getPlayer().notifyPlayerObservers();
		}
		return isSuccessCombine;
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
					final boolean isSuccess = baseSquare.combine(baseSquare.getVertices()[i], combineSquare, addSquareVertex);
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
	 * @param putObject
	 * @return true if success
	 */
	public boolean putSquareObject(Square2dObject putObject) {
		if (putObject.isLandingOnSquare()) {
			putObject.setEnabledAction(true);
			final Square2dEvent event = new AddObjectEvent(putObject);
			event.addExceptObserver(putObject);
			this.getSquare().notifyObservers(event);
			return true;
		}
		if (this.getSquare() != null) {
			this.getSquare().removeSquareObject(putObject);
		}
		return false;
	}

	/**
	 * show square
	 */
	public void showSquareOnly() {
		this.hideAll();
		this.enableSquare();
	}

	/**
	 * hide all
	 */
	public void hideAll() {
		this.hideMenu();
		this.hidePlayerItemList();
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

	/**
	 * @return true if square is enabled.
	 */
	public boolean isEnablingSquare() {
		if (this.square == null) {
			return false;
		}
		return this.square.getTouchable() == Touchable.enabled;
	}

	private void show(Actor actor) {
		if (!this.getChildren().contains(actor, true)) {
			this.addActor(actor);
		}
		actor.toFront();
		if (actor instanceof CameraObserver) {
			this.addChildCameraObserver((CameraObserver) actor);
			((CameraObserver) actor).updateCamera(this.getFreeSquare().getCamera());
		}
		this.getFreeSquare().getStage().cancelTouchFocus();
		this.disableSquare();
	}

	private void hide(Actor actor) {
		this.removeActor(actor);
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
	public void showPlayerItemList() {
		this.show(this.playersItemList);
	}

	/**
	 * hide player item list
	 */
	public void hidePlayerItemList() {
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
		this.modePresenter.updateCamera(this.getFreeSquare().getCamera());
		this.square.getColor().a = 1;
		this.square.setTouchable(Touchable.enabled);
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
	 * 
	 */
	public void adjustCameraZoomAndPositionIfRangeOver() {
		if (this.square == null) {
			return;
		}
		final float minZoom = this.getMinZoom();
		final float maxZoom = this.getMaxZoom();
		final OrthographicCamera camera = (OrthographicCamera) this.getFreeSquare().getCamera();
		camera.zoom = MathUtils.clamp(camera.zoom, minZoom, maxZoom);
		final float minViewableWidth = camera.viewportWidth * minZoom;
		final float minViewableHeight = camera.viewportHeight * minZoom;

		float squareWidth = this.square.getWidth();
		float squareHeight = this.square.getHeight();
		if (minViewableWidth > squareWidth + minViewableWidth / 2f || minViewableHeight > squareHeight + minViewableHeight / 2f) {
			camera.position.x = this.square.getX() + squareWidth / 2;
			camera.position.y = this.square.getY() + squareHeight / 2;
			return;
		}
		final float viewingWidth = camera.viewportWidth * camera.zoom;
		final float viewingHeight = camera.viewportHeight * camera.zoom;

		final float minCameraPositionX = this.getVisibleRangeLowerLeft().x + viewingWidth / 2f;
		final float maxCameraPositionX = this.getVisibleRangeUpperRight().x - viewingWidth / 2f;
		final float minCameraPositionY = this.getVisibleRangeLowerLeft().y + viewingHeight / 2f;
		final float maxCameraPositionY = this.getVisibleRangeUpperRight().y - viewingHeight / 2f;
		camera.position.x = MathUtils.clamp(camera.position.x, minCameraPositionX, maxCameraPositionX);
		camera.position.y = MathUtils.clamp(camera.position.y, minCameraPositionY, maxCameraPositionY);

	}

	/**
	 * @return count of showing UIs
	 */
	public int getViewableUICount() {
		int result = 0;
		for (Actor child : this.getChildren()) {
			if (child.getColor().a != 0) {
				result++;
			}
		}
		return result;
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
		final OrthographicCamera camera = (OrthographicCamera) this.getFreeSquare().getCamera();
		final float fitSquareWidthZoom = this.square.getWidth() / camera.viewportWidth;
		final float fitSquareHeightZoom = this.square.getHeight() / camera.viewportHeight;
		final float maxZoom = Math.max(1, Math.max(fitSquareWidthZoom, fitSquareHeightZoom));
		return maxZoom;
	}
	
	protected void saveDictionary(ExternalLifeObjectTypeDictionary dictionary){
		this.getFreeSquare().saveDictionary(dictionary);
		this.itemList.updateItems(MainActivity.getAllItems());
	}
	protected void saveDictionary(ExternalOtherObjectTypeDictionary dictionary){
		this.getFreeSquare().saveDictionary(dictionary);
		this.itemList.updateItems(MainActivity.getAllItems());
	}

	@Override
	public void notify(SquareEvent event) {
		if (event instanceof CollectObjectRequestEvent) {
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
			this.getFreeSquare().inputName(renameRequestedObject, title, ""); //$NON-NLS-1$
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
			this.childCameraObserver.get(i).updateCamera(this.getFreeSquare().getCamera());
		}
	}

	@Override
	public void resume() {
		// Nothing to do
	}

	@Override
	public void pause() {
		this.hideAll();
	}

	@Override
	public void act(float delta) {
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
}
