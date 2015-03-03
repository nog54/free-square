package org.nognog.freeSquare;

import java.util.Date;

import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.persist.PersistItems;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.CombineSquare2d;
import org.nognog.freeSquare.square2d.CombineSquare2dUtils;
import org.nognog.freeSquare.square2d.SimpleSquare2d;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.Vertex;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.CollectObjectRequestEvent;
import org.nognog.freeSquare.square2d.item.Square2dObjectItem;
import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.LifeObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;
import org.nognog.freeSquare.square2d.squares.Square2dType;
import org.nognog.freeSquare.square2d.ui.FlickButtonController.FlickInputListener;
import org.nognog.freeSquare.square2d.ui.ItemList;
import org.nognog.freeSquare.square2d.ui.Menu;
import org.nognog.freeSquare.square2d.ui.PlayerItemList;
import org.nognog.freeSquare.square2d.ui.PlayersLifeList;
import org.nognog.freeSquare.square2d.ui.PlayersSquareList;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

	private Array<CameraObserver> cameraObservers;

	private BitmapFont font;
	private PlayLog playlog;
	private Player player;
	private Date lastRun;
	private Menu menu;
	private PlayerItemList playerItemList;
	private PlayersLifeList playersLifeList;
	private PlayersSquareList playersSquareList;
	private ItemList itemList;

	@Override
	public void create() {
		this.cameraObservers = new Array<>();
		this.logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		this.logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		final float timeFromLastRun = setupPersistItems();
		if (this.player.getSquares().size == 0) {
			this.square = Square2dType.GRASSY_SQUARE1.create();
		} else {
			this.square = (SimpleSquare2d) this.player.getSquares().get(0);
		}
		this.square.setX(-this.square.getWidth() / 2);
		this.square.addSquareObserver(this);

		this.player.addSquare(this.square);
		this.player.addSquare(Square2dType.GRASSY_SQUARE1_SMALL.create());
		this.stage = new Stage(new FitViewport(this.logicalCameraWidth, this.logicalCameraHeight));
		CombineSquare2d combineSquare = new CombineSquare2d(this.square);
		SimpleSquare2d appendSquare1 = Square2dType.GRASSY_SQUARE1_LARGE.create();
		SimpleSquare2d appendSquare2 = Square2dType.GRASSY_SQUARE1_LARGE.create();
		SimpleSquare2d appendSquare3 = Square2dType.GRASSY_SQUARE1_SMALL.create();
		SimpleSquare2d appendSquare4 = Square2dType.GRASSY_SQUARE1_SMALL.create();
		combineSquare.combine(combineSquare.getVertices()[0], appendSquare1, appendSquare1.getVertex2());
		combineSquare.combine(combineSquare.getVertices()[5], appendSquare2, appendSquare2.getVertices()[0]);
		combineSquare.separate(appendSquare2);
		
		combineSquare.combine(combineSquare.getVertices()[1], appendSquare3, appendSquare3.getVertices()[1]);
		combineSquare.combine(combineSquare.getVertices()[4], appendSquare4, appendSquare4.getVertices()[0]);
		//combineSquare.combine(combineSquare.getVertices()[2], appendSquare3, appendSquare3.getVertices()[0]);
		//combineSquare.combine(combineSquare.getVertices()[2], appendSquare4, appendSquare4.getVertices()[0]);
		//combineSquare.separate(appendSquare3);
		combineSquare.setDrawEdge(true);
		this.stage.addActor(combineSquare);
		this.square = combineSquare;
		this.actLongTime(timeFromLastRun);
		this.stage.getCamera().position.x = (this.getCameraRangeLowerLeft().x + this.getCameraRangeUpperRight().x) / 2;
		this.stage.getCamera().position.y = (this.getCameraRangeLowerLeft().y + this.getCameraRangeUpperRight().y) / 2;

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new FreeSquareGestureDetector(this));
		multiplexer.addProcessor(this.stage);
		Gdx.input.setInputProcessor(multiplexer);
		this.font = FontUtil.createMPlusFont(this.logicalCameraWidth / 24);
		this.initializeWidgets();

	}

	private void initializeWidgets() {
		this.menu = new Menu(this.font, this.logicalCameraWidth / 6, new FlickInputListener() {
			@Override
			public void up() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayersSquareList();
			}

			@Override
			public void right() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayersLifeList();
			}

			@Override
			public void left() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayerItemList();
			}

			@Override
			public void down() {
				FreeSquare.this.showSquareOnly();
			}

			@Override
			public void center() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showItemList();
			}
		});

		this.playerItemList = new PlayerItemList(this.stage.getCamera(), this.player, this.font) {
			private Square2dObject addObject;

			@Override
			protected void selectedItemPanned(PossessedItem<?> pannedItem, float x, float y, float deltaX, float deltaY) {
				if (pannedItem == null) {
					return;
				}
				if (this.addObject == null) {
					Item<?, ?> item = pannedItem.getItem();
					if (item instanceof Square2dObjectItem) {
						this.addObject = ((Square2dObjectItem) item).createSquare2dObject();
						this.addObject.setEnabledAction(false);
						if (FreeSquare.this.getPlayer().getItemBox().getItemQuantity(item) > 0) {
							Vector2 squareCoodinateXY = FreeSquare.this.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
							FreeSquare.this.getSquare().addSquareObject(this.addObject, squareCoodinateXY.x, squareCoodinateXY.y, false);
							FreeSquare.this.showSquareOnly();
						}
					}
				} else {
					if (!this.addedObjectisBeingEaten()) {
						this.addObject.moveBy(deltaX, deltaY);
					}
				}
			}

			private boolean addedObjectisBeingEaten() {
				return (this.addObject instanceof EatableObject) && ((EatableObject) this.addObject).isBeingEaten();
			}

			@Override
			protected void touchUp(float x, float y) {
				if (this.addObject != null) {
					if (this.addObject.isLandingOnSquare()) {
						FreeSquare.this.getPlayer().takeOutItem(Square2dObjectItem.getInstance(this.addObject.getType()));
						this.addObject.setEnabledAction(true);
						Square2dEvent event = new AddObjectEvent(this.addObject);
						event.addExceptObserver(this.addObject);
						FreeSquare.this.getSquare().notifyObservers(event);
					} else {
						FreeSquare.this.getSquare().removeSquareObject(this.addObject);
					}
					FreeSquare.this.showPlayerItemList();
					this.addObject = null;
				}
			}
		};

		this.playersLifeList = new PlayersLifeList(this.stage.getCamera(), this.player, this.font) {
			private LifeObject addObject;

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
			protected void selectedItemPanned(Square<?> pannedItem, float x, float y, float deltaX, float deltaY) {
				if (pannedItem == null) {
					return;
				}
				if (this.addSquare == null) {
					if (pannedItem instanceof Square2d) {
						this.addSquare = (Square2d) pannedItem;
						final float previousX = this.addSquare.getX();
						final float previousY = this.addSquare.getY();
						Vector2 squareCoodinateXY = FreeSquare.this.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
						this.addSquare.setPosition(squareCoodinateXY.x - this.addSquare.getWidth() / 2, squareCoodinateXY.y - this.addSquare.getHeight() / 2);
						try {
							FreeSquare.this.getStage().getRoot().addActor(this.addSquare);
						} catch (IllegalArgumentException e) {
							this.addSquare.setPosition(previousX, previousY);
							this.addSquare = null;
							return;
						}
						this.addSquare.toBack();
						FreeSquare.this.showSquareOnly();
					}
				} else {
					this.addSquare.moveBy(deltaX, deltaY);
				}
			}

			@Override
			protected void touchUp(float x, float y) {
				if (this.addSquare == null) {
					return;
				}
				FreeSquare.this.getStage().getRoot().removeActor(this.addSquare);
				FreeSquare.this.showPlayersSquareList();
				if (!(FreeSquare.this.getSquare() instanceof CombineSquare2d)) {
					this.addSquare = null;
					return;
				}
				final Vector2[] stageCoordinatesBaseSquareVertices = FreeSquare.this.getSquare().getStageCoordinatesVertices();
				for (Vertex addSquareVertex : this.addSquare.getVertices()) {
					final Vector2 stageCoordinatesAddSquareVertex = this.addSquare.localToStageCoordinates(new Vector2(addSquareVertex.x, addSquareVertex.y));
					for (int i = 0; i < stageCoordinatesBaseSquareVertices.length; i++) {
						final Vector2 stageCoordinatesBaseSquareVertex = stageCoordinatesBaseSquareVertices[i];
						if (CombineSquare2dUtils.canBeRegardedAsSameVertex(stageCoordinatesAddSquareVertex, stageCoordinatesBaseSquareVertex)) {
							final CombineSquare2d baseSquare = (CombineSquare2d) FreeSquare.this.getSquare();
							baseSquare.combine(baseSquare.getVertices()[i], this.addSquare, addSquareVertex);
							this.addSquare = null;
							return;
						}
					}
				}
				this.addSquare = null;
			}

		};

		this.itemList = new ItemList(this.stage.getCamera(), Square2dObjectItem.getAllItems(), this.font) {
			@Override
			protected void selectedItemTapped(Item<?, ?> tappedItem) {
				FreeSquare.this.getPlayer().putItem(tappedItem);
			}
		};
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
	 * @return true if locking camera zoom
	 */
	public boolean isLockingCameraZoom() {
		return this.isLockingCameraZoom;
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
	public Vector2 getCameraRangeLowerLeft() {
		return new Vector2(this.square.getLeftEndX(), this.square.getButtomEndY());
	}

	/**
	 * @return upper-right point of camera range
	 */
	public Vector2 getCameraRangeUpperRight() {
		return new Vector2(this.square.getRightEndX(), this.square.getTopEndY());
	}

	/**
	 * @return true if showing square
	 */
	public boolean isShowingSquare() {
		return this.square.getTouchable() == Touchable.enabled;
	}

	void showSquareOnly() {
		this.hideMenu();
		this.hidePlayerItemList();
		this.hidePlayersLifeList();
		this.hidePlayersSquareList();
		this.hideItemList();
		this.enableSquare();
	}

	private void enableSquare() {
		this.square.getColor().a = 1;
		this.square.setTouchable(Touchable.enabled);
		this.isLockingCameraMove = false;
		this.isLockingCameraZoom = false;
	}

	private void disableSquare() {
		this.stage.cancelTouchFocus(this.square);
		this.square.getColor().a = 0.75f;
		this.square.setTouchable(Touchable.disabled);
	}

	void showMenu(float x, float y) {
		this.menu.setPosition(x, y);
		this.menu.setScale(((OrthographicCamera) this.stage.getCamera()).zoom);
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
		this.show(this.playerItemList);
	}

	void hidePlayerItemList() {
		this.hide(this.playerItemList);
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
		Gdx.gl.glClearColor(0.2f, 1f, 1f, 1);
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
	}

	private float setupPersistItems() {
		this.playlog = PersistItems.PLAY_LOG.load();
		if (this.playlog == null) {
			System.out.println("new playlog"); //$NON-NLS-1$
			this.playlog = PlayLog.create();
			PersistItems.PLAY_LOG.save(this.playlog);
		}
		this.player = PersistItems.PLAYER.load();
		if (this.player == null) {
			System.out.println("new player"); //$NON-NLS-1$
			this.player = new Player("goshi"); //$NON-NLS-1$
			PersistItems.PLAYER.save(this.player);
		}
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
	public void notify(Square2dEvent event) {
		if (event instanceof CollectObjectRequestEvent) {
			Square2dObject collectRequestedObject = ((CollectObjectRequestEvent) event).getCollectRequestedObject();
			if (collectRequestedObject instanceof LifeObject) {
				this.player.addLife(((LifeObject) collectRequestedObject).getLife());
			} else {
				this.player.putItem(Square2dObjectItem.getInstance(collectRequestedObject.getType()));
			}
			this.square.removeSquareObject(collectRequestedObject);
		}
	}

}
