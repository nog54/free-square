package org.nognog.freeSquare;

import java.util.Date;

import org.nognog.freeSquare.model.Nameable;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.persist.PersistItems;
import org.nognog.freeSquare.model.persist.PersistManager;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.model.square.SquareObserver;
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
import org.nognog.freeSquare.square2d.event.UpdateObjectEvent;
import org.nognog.freeSquare.square2d.item.Square2dItem;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.LifeObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;
import org.nognog.freeSquare.square2d.ui.ColorUtils;
import org.nognog.freeSquare.square2d.ui.ItemList;
import org.nognog.freeSquare.square2d.ui.Menu;
import org.nognog.freeSquare.square2d.ui.ModePresenter;
import org.nognog.freeSquare.square2d.ui.MultiLevelFlickButtonController;
import org.nognog.freeSquare.square2d.ui.PlayersItemList;
import org.nognog.freeSquare.square2d.ui.PlayersLifeList;
import org.nognog.freeSquare.square2d.ui.PlayersSquareList;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter implements SquareObserver {
	private Stage stage;
	private Square2d square;

	private int logicalCameraWidth;
	private int logicalCameraHeight;

	private boolean isLockingCameraZoom;
	private boolean isLockingCameraMove;

	private boolean isSeparateSquareMode;

	private Array<CameraObserver> cameraObservers;

	private BitmapFont font;
	private PlayLog playlog;
	private Player player;
	private Date lastRun;
	private Menu menu;
	private PlayersItemList playersItemList;
	private PlayersLifeList playersLifeList;
	private PlayersSquareList playersSquareList;
	private ItemList itemList;
	private ModePresenter modePresenter;

	@Override
	public void create() {
		this.cameraObservers = new Array<>();
		this.logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		this.logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		final float timeFromLastRun = setupPersistItems();
		this.stage = new Stage(new FitViewport(this.logicalCameraWidth, this.logicalCameraHeight));
		this.setSquare(null);
		this.actLongTime(timeFromLastRun);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new FreeSquareGestureDetector(this));
		multiplexer.addProcessor(this.stage);
		Gdx.input.setInputProcessor(multiplexer);
		final int fontSize = this.logicalCameraWidth / 18;
		this.font = FontUtil.createMPlusFont(fontSize);
		this.initializeWidgets();

	}

	private void initializeWidgets() {
		this.menu = new Menu(this.font, this.logicalCameraWidth / 4);
		this.menu.addFlickButtonController(new MultiLevelFlickButtonController.MultiLevelFlickButtonInputListener() {
			@Override
			public void up() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayerItemList();
			}

			@Override
			public void right() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayersSquareList();
			}

			@Override
			public void left() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayersLifeList();
			}

			@Override
			public void down() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showItemList();
			}

		}, Messages.getString("item"), "リスト", Messages.getString("square"), Messages.getString("life")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		this.menu.addFlickButtonController(new MultiLevelFlickButtonController.MultiLevelFlickButtonInputListener() {
			@Override
			public void up() {
				FreeSquare.this.setSquareWithAction(null);
				FreeSquare.this.showSquareOnly();
			}

			@Override
			public void right() {
				FreeSquare.this.showSquareOnly();
			}

			@Override
			public void left() {
				FreeSquare.this.showSquareOnly();
			}

			@Override
			public void down() {
				FreeSquare.this.showSquareOnly();
				FreeSquare.this.setSeparateSquareMode(true);
			}

		}, Messages.getString("clear"), Messages.getString("separate"), "/", "/", ColorUtils.peterRiver, ColorUtils.belizeHole); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		this.playersItemList = new PlayersItemList(this.stage.getCamera(), this.player, this.font) {
			private Actor pannedObject;
			private PossessedItem<?> panndedItem;

			@Override
			protected void selectedItemPanned(PossessedItem<?> pannedItem, float x, float y, float deltaX, float deltaY) {
				if (pannedItem == null) {
					return;
				}
				if (this.pannedObject == null) {
					Item<?, ?> item = pannedItem.getItem();
					if (item instanceof Square2dObjectItem && FreeSquare.this.getSquare() != null) {
						Square2dObject pannedSquareObject = ((Square2dObjectItem) item).createSquare2dObject();
						this.pannedObject = pannedSquareObject;
						this.panndedItem = pannedItem;
						this.addSquare2dObjectTemporary(pannedSquareObject, x, y, item);
						FreeSquare.this.showSquareOnly();
					} else if (item instanceof Square2dItem) {
						Square2d pannedSquare = ((Square2dItem) item).createSquare2d();
						this.pannedObject = pannedSquare;
						this.panndedItem = pannedItem;
						this.addSquare2dTemporary(pannedSquare, x, y, item);
						FreeSquare.this.showSquareOnly();
					}
				} else {
					if (!this.addedObjectisBeingEaten()) {
						this.pannedObject.moveBy(deltaX, deltaY);
						if (FreeSquare.this.getSquare() != null) {
							FreeSquare.this.getSquare().notify(new UpdateObjectEvent());
						}
					}
				}
			}

			private void addSquare2dObjectTemporary(Square2dObject pannedSquareObject, float x, float y, Item<?, ?> item) {
				pannedSquareObject.setEnabledAction(false);
				Vector2 squareCoodinateXY = FreeSquare.this.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
				FreeSquare.this.getSquare().addSquareObject(pannedSquareObject, squareCoodinateXY.x, squareCoodinateXY.y, false);
			}

			private void addSquare2dTemporary(Square2d pannedSquare, float x, float y, Item<?, ?> item) {
				Vector2 squareCoodinateXY = this.getWidget().localToStageCoordinates(new Vector2(x, y));
				pannedSquare.setPosition(squareCoodinateXY.x - pannedSquare.getWidth() / 2, squareCoodinateXY.y - pannedSquare.getHeight() / 2);
				if (FreeSquare.this.getSquare() != null) {
					FreeSquare.this.getSquare().addActorForce(pannedSquare);
				} else {
					FreeSquare.this.getStage().addActor(pannedSquare);
				}
			}

			private boolean addedObjectisBeingEaten() {
				return (this.pannedObject instanceof EatableObject) && ((EatableObject) this.pannedObject).isBeingEaten();
			}

			@Override
			protected void touchUp(float x, float y) {
				if (this.pannedObject == null || this.panndedItem == null) {
					this.pannedObject = null;
					this.panndedItem = null;
					return;
				}
				if (this.pannedObject instanceof Square2dObject) {
					Square2dObject addObject = (Square2dObject) this.pannedObject;
					if (FreeSquare.this.putSquareObject(addObject)) {
						this.getPlayer().takeOutItem(Square2dObjectItem.getInstance(addObject.getType()));
					}
				} else if (this.pannedObject instanceof Square2d) {
					Square2d addSquare = (Square2d) this.pannedObject;
					if (FreeSquare.this.putSquare2d(addSquare)) {
						FreeSquare.this.getPlayer().takeOutItem(this.panndedItem.getItem());
					}
				}
				this.getList().setSelectedIndex(-1);
				FreeSquare.this.showPlayerItemList();
				this.pannedObject = null;
				this.panndedItem = null;
			}
		};

		this.playersLifeList = new PlayersLifeList(this.stage.getCamera(), this.player, this.font) {
			private LifeObject addObject;

			@Override
			protected void selectedItemLongPressed(Life longPressedItem, float x, float y) {
				FreeSquare.this.inputName(longPressedItem, Messages.getString("lifeNameInput")); //$NON-NLS-1$
			}

			@Override
			protected void selectedItemPanned(Life pannedItem, float x, float y, float deltaX, float deltaY) {
				if (pannedItem == null) {
					return;
				}
				if (this.addObject == null) {
					this.addObject = LifeObject.create(pannedItem);
					this.addObject.setEnabledAction(false);
					Vector2 squareCoodinateXY = FreeSquare.this.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
					FreeSquare.this.getSquare().addSquareObject(this.addObject, squareCoodinateXY.x, squareCoodinateXY.y, false);
					FreeSquare.this.showSquareOnly();
				} else {
					this.addObject.moveBy(deltaX, deltaY);
				}
			}

			@Override
			protected void touchUp(float x, float y) {
				if (this.addObject != null) {
					if (this.addObject.isValid()) {
						FreeSquare.this.getPlayer().removeLife(this.addObject.getLife());
						this.addObject.setEnabledAction(true);
						Square2dEvent event = new AddObjectEvent(this.addObject);
						event.addExceptObserver(this.addObject);
						FreeSquare.this.getSquare().notifyObservers(event);
					} else {
						FreeSquare.this.getSquare().removeSquareObject(this.addObject);
					}
					FreeSquare.this.showPlayersLifeList();
					this.addObject = null;
				}
			}

		};

		this.playersSquareList = new PlayersSquareList(this.stage.getCamera(), this.player, this.font) {
			private Square2d addSquare;

			@Override
			protected Square<?>[] getShowListItemsFromPlayer(Player setupPlayer) {
				Array<Square<?>> playersSquares = new Array<>(super.getShowListItemsFromPlayer(setupPlayer));
				playersSquares.removeValue(FreeSquare.this.getSquare(), true);
				return playersSquares.toArray(Square.class);
			}

			@Override
			protected void selectedItemLongPressed(Square<?> longPressedItem, float x, float y) {
				FreeSquare.this.inputName(longPressedItem, Messages.getString("squareNameInput")); //$NON-NLS-1$
			}

			@Override
			protected void selectedItemPanned(Square<?> pannedItem, float x, float y, float deltaX, float deltaY) {
				if (FreeSquare.this.getSquare() == null || pannedItem == null || !(pannedItem instanceof Square2d)) {
					return;
				}
				if (this.addSquare == null) {
					this.addSquare = (Square2d) pannedItem;
					Vector2 squareCoodinateXY = this.getWidget().localToStageCoordinates(new Vector2(x, y));
					this.addSquare.setPosition(squareCoodinateXY.x - this.addSquare.getWidth() / 2, squareCoodinateXY.y - this.addSquare.getHeight() / 2);
					FreeSquare.this.getSquare().addActorForce(this.addSquare);
					FreeSquare.this.showSquareOnly();
					this.addSquare.toFront();
				} else {
					this.addSquare.moveBy(deltaX, deltaY);
					FreeSquare.this.getSquare().notify(new UpdateObjectEvent());
				}
			}

			@Override
			protected void selectedItemTapped(Square<?> tappedItem, int count) {
				final boolean isDoubleTapped = (count == 2);
				if (isDoubleTapped && tappedItem instanceof Square2d) {
					FreeSquare.this.setSquareWithAction((Square2d) tappedItem);
					FreeSquare.this.showSquareOnly();
				}
			}

			@Override
			protected void touchUp(float x, float y) {
				if (this.addSquare == null) {
					return;
				}
				if (FreeSquare.this.getSquare() != null) {
					FreeSquare.this.getSquare().removeActorForce(this.addSquare);
				} else {
					FreeSquare.this.getStage().getRoot().removeActor(this.addSquare);
				}
				final boolean existsBaseSquare = FreeSquare.this.getSquare() != null;
				final boolean successPut = FreeSquare.this.putSquare2d(this.addSquare);
				final boolean executedCombine = existsBaseSquare && successPut;
				if (executedCombine) {
					this.getPlayer().removeSquare(this.addSquare);
				}
				FreeSquare.this.showPlayersSquareList();
				this.addSquare = null;
			}

		};

		this.itemList = new ItemList(this.stage.getCamera(), FreeSquare.getAllItems(), this.font) {
			@Override
			protected void selectedItemTapped(Item<?, ?> tappedItem) {
				FreeSquare.this.getPlayer().putItem(tappedItem);
			}
		};

		this.modePresenter = new ModePresenter(this.stage.getCamera(), this.font) {
			@Override
			public void tapped() {
				FreeSquare.this.showSquareOnly();
			}
		};
	}

	protected void convertThisSquareToCombineSquare2d() {
		if (this.square instanceof CombineSquare2d || this.square == null) {
			return;
		}

		if (!(this.square instanceof SimpleSquare2d)) {
			throw new IllegalArgumentException("convert failure : not support no-SimpleSquare2d yet"); //$NON-NLS-1$
		}
		Square2d convertTarget = this.square;
		final float cameraX = this.getStage().getCamera().position.x;
		final float cameraY = this.getStage().getCamera().position.y;
		this.setSquare(null);
		CombineSquare2d combineSquare = new CombineSquare2d(convertTarget);
		this.player.replaceSquare(convertTarget, combineSquare);
		this.setSquare(combineSquare);
		this.getStage().getCamera().position.x = cameraX;
		this.getStage().getCamera().position.y = cameraY;
	}

	/**
	 * @return stage
	 */
	public Stage getStage() {
		return this.stage;
	}

	/**
	 * @return square
	 */
	public Square2d getSquare() {
		return this.square;
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
		this.stage.addAction(Square2dActions.changeSquare(this, setSquare, direction));
	}

	/**
	 * @param square
	 */
	public void setSquare(Square2d square) {
		if (this.square != null) {
			this.enableSquare();
			this.square.removeSquareObserver(this);
			this.square.remove();
			this.square.setDrawEdge(false);
		}
		this.square = square;
		if (this.square != null) {
			this.square.setPosition(0, 0);
			this.stage.addActor(square);
			this.square.addSquareObserver(this);
			this.square.setDrawEdge(true);
			if (this.square instanceof CombineSquare2d) {
				((CombineSquare2d) this.square).setHighlightSeparatableSquare(this.isSeparateSquareMode);
			}
		}
		this.player.notifyPlayerObservers();
		this.stage.getCamera().position.x = (this.getCameraRangeLowerLeft().x + this.getCameraRangeUpperRight().x) / 2;
		this.stage.getCamera().position.y = (this.getCameraRangeLowerLeft().y + this.getCameraRangeUpperRight().y) / 2;
	}

	protected boolean putSquare2d(Square2d putSquare) {
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
		final Vector2[] stageCoordinatesBaseSquareVertices = FreeSquare.this.getSquare().getStageCoordinatesVertices();
		for (Vertex addSquareVertex : combineSquare.getVertices()) {
			final Vector2 stageCoordinatesAddSquareVertex = combineSquare.localToStageCoordinates(new Vector2(addSquareVertex.x, addSquareVertex.y));
			for (int i = 0; i < stageCoordinatesBaseSquareVertices.length; i++) {
				final Vector2 stageCoordinatesBaseSquareVertex = stageCoordinatesBaseSquareVertices[i];
				final float r = Square2dUtils.toVertex(stageCoordinatesAddSquareVertex).calculateR(Square2dUtils.toVertex(stageCoordinatesBaseSquareVertex));
				if (CombineSquare2dUtils.regardAsSufficientlyCloseThreshold * 2 > r) {
					return baseSquare.combine(baseSquare.getVertices()[i], combineSquare, addSquareVertex);
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
				this.player.addSquare(containsSeparateSquareSquare);
			}
		}
	}

	protected boolean putSquareObject(Square2dObject putObject) {
		if (putObject.isLandingOnSquare()) {
			putObject.setEnabledAction(true);
			Square2dEvent event = new AddObjectEvent(putObject);
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
	 * @return true if locking camera zoom
	 */
	public boolean isLockingCameraZoom() {
		return this.isLockingCameraZoom;
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
	 * @return true if locking camera move
	 */
	public boolean isLockingCameraMove() {
		return this.isLockingCameraMove;
	}

	/**
	 * @return lower-left point of camera range
	 */
	private Vector2 getCameraRangeLowerLeft() {
		if (this.square == null) {
			return new Vector2(0, 0);
		}
		return new Vector2(this.square.getLeftEndX(), this.square.getBottomEndY());
	}

	/**
	 * @return upper-right point of camera range
	 */
	private Vector2 getCameraRangeUpperRight() {
		if (this.square == null) {
			return new Vector2(0, 0);
		}
		return new Vector2(this.square.getRightEndX(), this.square.getTopEndY());
	}

	/**
	 * 
	 */
	public void adjustCameraPositionIfRangeOver() {
		OrthographicCamera camera = (OrthographicCamera) this.getStage().getCamera();
		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		float minCameraPositionX = this.getCameraRangeLowerLeft().x + effectiveViewportWidth / 2f;
		float maxCameraPositionX = this.getCameraRangeUpperRight().x - effectiveViewportWidth / 2f;
		float minCameraPositionY = this.getCameraRangeLowerLeft().y + effectiveViewportHeight / 2f;
		float maxCameraPositionY = this.getCameraRangeUpperRight().y - effectiveViewportHeight / 2f;

		camera.position.x = MathUtils.clamp(camera.position.x, minCameraPositionX, maxCameraPositionX);
		camera.position.y = MathUtils.clamp(camera.position.y, minCameraPositionY, maxCameraPositionY);
	}

	void showSquareOnly() {
		this.hideMenu();
		this.hidePlayerItemList();
		this.hidePlayersLifeList();
		this.hidePlayersSquareList();
		this.hideItemList();
		this.hideModePresenter();
		this.enableSquare();
	}

	private void enableSquare() {
		if (this.square == null) {
			return;
		}
		this.square.getColor().a = 1;
		this.square.setTouchable(Touchable.enabled);
		this.isLockingCameraMove = false;
		this.isLockingCameraZoom = false;
		this.setSeparateSquareMode(false);
	}

	private void disableSquare() {
		if (this.square == null) {
			return;
		}
		this.stage.cancelTouchFocus(this.square);
		this.square.getColor().a = 0.75f;
		this.square.setTouchable(Touchable.disabled);
	}

	void showMenu(float x, float y) {
		this.menu.setPosition(x, y);
		final OrthographicCamera camera = (OrthographicCamera) this.stage.getCamera();
		final float cameraViewingWidth = camera.zoom * camera.viewportWidth;
		final float goldenRatio = Settings.getGoldenRatio();
		this.menu.setScale(cameraViewingWidth / (this.menu.getButtonWidthHeight() * 3) * (goldenRatio / (1 + goldenRatio)));
		this.menu.showFirstLevel();
		this.show(this.menu);
	}

	void hideMenu() {
		this.stage.getRoot().removeActor(this.menu);
		this.removeCameraObserver(this.menu);
	}

	private void show(Actor actor) {
		this.stage.getRoot().addActor(actor);
		if (actor instanceof CameraObserver) {
			this.addCameraObserver((CameraObserver) actor);
			((CameraObserver) actor).updateCamera(this.stage.getCamera());
		}
		this.stage.cancelTouchFocus();
		this.disableSquare();
	}

	private void hide(Actor actor) {
		this.stage.getRoot().removeActor(actor);
		if (actor instanceof CameraObserver) {
			this.removeCameraObserver((CameraObserver) actor);
		}
	}

	void showPlayerItemList() {
		this.show(this.playersItemList);
	}

	void hidePlayerItemList() {
		this.hide(this.playersItemList);
	}

	void showPlayersLifeList() {
		this.show(this.playersLifeList);
	}

	void hidePlayersLifeList() {
		this.hide(this.playersLifeList);
	}

	void showPlayersSquareList() {
		this.show(this.playersSquareList);
	}

	void hidePlayersSquareList() {
		this.hide(this.playersSquareList);
	}

	void showItemList() {
		this.show(this.itemList);
	}

	void hideItemList() {
		this.hide(this.itemList);
	}

	void showModePresenter(String text) {
		this.stage.getRoot().addActor(this.modePresenter);
		this.addCameraObserver(this.modePresenter);
		this.modePresenter.setText(text);
		this.modePresenter.updateCamera(this.stage.getCamera());
	}

	void hideModePresenter() {
		this.stage.getRoot().removeActor(this.modePresenter);
		this.removeCameraObserver(this.modePresenter);
	}

	protected void inputName(final Nameable nameable, String title) {
		Gdx.input.getTextInput(new TextInputListener() {
			@Override
			public void input(String text) {
				final BitmapFont listFont = FreeSquare.this.getPlayersLifeList().getFont();
				final float maxTextDrawWidth = FreeSquare.this.getPlayersLifeList().getWidth() - 2 * FreeSquare.this.getPlayersLifeList().getList().getItemHeight();
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
				// nothing
			}
		}, title, nameable.getName());
	}

	@Override
	public void render() {
		this.stage.act();
		this.drawStage();
	}

	private void actLongTime(float longDelta) {
		float ramainActTime = longDelta;
		while (ramainActTime > 0) {
			float delta = 0.5f;
			ramainActTime -= delta;
			if (ramainActTime < 0) {
				delta += ramainActTime;
			}
			this.stage.act(delta);
		}
	}

	private void drawStage() {
		Gdx.gl.glClearColor(0.2f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
	}

	@Override
	public void pause() {
		PersistItems.PLAYER.save(this.player);
		LastPlay.update();
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void resume() {
		Date lastPauseDate = LastPlay.getLastPlayDate();
		Date now = new Date();
		this.actLongTime((now.getTime() - lastPauseDate.getTime()) / 1000f);
		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void dispose() {
		try {
			if (this.font != null) {
				this.font.dispose();
			}
			this.stage.dispose();
			Square2dObjectType.Manager.dispose();
		} catch (Throwable t) {
			Gdx.app.error(this.getClass().getName(), "error occured in dispose", t); //$NON-NLS-1$
			throw t;
		}
		System.out.println(PersistManager.getUseJson().prettyPrint(this.player));
	}

	private float setupPersistItems() {
		this.playlog = PersistItems.PLAY_LOG.load();
		if (this.playlog == null) {
			System.out.println("new playlog"); //$NON-NLS-1$
			this.playlog = PlayLog.create();
			PersistItems.PLAY_LOG.save(this.playlog);
		}
		this.player = PersistItems.PLAYER.load();
		// if (this.player == null) {
		//			System.out.println("new player"); //$NON-NLS-1$
		//			this.player = new Player("goshi"); //$NON-NLS-1$
		// PersistItems.PLAYER.save(this.player);
		// }
		this.lastRun = LastPlay.getLastPlayDate();
		if (this.lastRun == null) {
			System.out.println("new last Run"); //$NON-NLS-1$
			this.lastRun = new Date();
			return 0;
		}
		Date now = new Date();
		float diffSecond = (now.getTime() - this.lastRun.getTime()) / 1000f;
		return diffSecond;
	}

	private static Item<?, ?>[] getAllItems() {
		Item<?, ?>[] allSquare2dObjectItems = Square2dObjectItem.getAllItems();
		Item<?, ?>[] allSquareItem = Square2dItem.getAllItems();
		Item<?, ?>[] allItems = new Item<?, ?>[allSquare2dObjectItems.length + allSquareItem.length];
		System.arraycopy(allSquare2dObjectItems, 0, allItems, 0, allSquare2dObjectItems.length);
		System.arraycopy(allSquareItem, 0, allItems, allSquare2dObjectItems.length, allSquareItem.length);
		return allItems;
	}

	/**
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
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

	@Override
	public void notify(SquareEvent event) {
		if (event instanceof CollectObjectRequestEvent) {
			Square2dObject collectRequestedObject = ((CollectObjectRequestEvent) event).getCollectRequestedObject();
			if (collectRequestedObject instanceof LifeObject) {
				this.player.addLife(((LifeObject) collectRequestedObject).getLife());
			} else {
				this.player.putItem(Square2dObjectItem.getInstance(collectRequestedObject.getType()));
			}
			this.square.removeSquareObject(collectRequestedObject);
		}

		if (event instanceof RenameRequestEvent) {
			Nameable renameRequestedObject = ((RenameRequestEvent) event).getRenameRequestedObject();
			final String title;
			if (renameRequestedObject instanceof Life) {
				title = Messages.getString("lifeNameInput"); //$NON-NLS-1$
			} else if (renameRequestedObject instanceof Square) {
				title = Messages.getString("squareNameInput"); //$NON-NLS-1$
			} else {
				title = ""; //$NON-NLS-1$
			}
			this.inputName(renameRequestedObject, title);
		}
	}
}
