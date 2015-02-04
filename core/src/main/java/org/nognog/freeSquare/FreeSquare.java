package org.nognog.freeSquare;

import java.util.Date;

import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.item.Square2dObjectItem;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.family.ShibaInu;
import org.nognog.freeSquare.model.persist.PersistItem;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;
import org.nognog.freeSquare.ui.FlickButtonController.FlickInputListener;
import org.nognog.freeSquare.ui.ItemList;
import org.nognog.freeSquare.ui.Menu;
import org.nognog.freeSquare.ui.PlayerItemList;
import org.nognog.freeSquare.ui.square2d.EatableObject;
import org.nognog.freeSquare.ui.square2d.Square2d;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;
import org.nognog.freeSquare.ui.square2d.Square2dObject;
import org.nognog.freeSquare.ui.square2d.events.AddObjectEvent;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;
import org.nognog.freeSquare.ui.square2d.squares.Square2dType;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter {
	private Stage stage;
	private Square2d square;

	private Vector2 cameraRangeLowerLeft;
	private Vector2 cameraRangeUpperRight;
	private boolean isLockingCameraZoom;
	private boolean isLockingCameraMove;

	private EatableObject eatenObject;

	private Array<CameraObserver> cameraObservers;

	private BitmapFont font;
	private PlayLog playlog;
	private Player player;
	private Life life;
	private Date lastRun;
	private Menu menu;
	private PlayerItemList playerItemList;
	private ItemList itemList;

	@Override
	public void create() {
		setupPersistItems();
		this.cameraObservers = new Array<>();
		final int logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		final int logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		this.square = Square2dType.GRASSY_SQUARE1.create();
		this.square.setX(-this.square.getWidth() / 2);
		// for (Square2dObjectType object : Square2dObjectType.values()) {
		// for (int i = 0; i < 1; i++) {
		// this.square.addSquareObject(object.create(), false);
		// }
		// }

		this.eatenObject = new EatableObject(Square2dObjectType.EatableObjectType.MINT_TOFU);
		this.square.addSquareObject(this.eatenObject);

		this.stage = new Stage(new FitViewport(logicalCameraWidth, logicalCameraHeight));
		this.stage.addActor(this.square);
		this.stage.getCamera().position.x -= this.stage.getCamera().viewportWidth / 2;

		this.cameraRangeLowerLeft = new Vector2(this.square.getX(), this.square.getY());
		this.cameraRangeUpperRight = new Vector2(this.square.getX() + this.square.getWidth(), this.square.getY() + Math.max(this.square.getHeight(), logicalCameraHeight));

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new FreeSquareGestureDetector(this));
		multiplexer.addProcessor(this.stage);
		Gdx.input.setInputProcessor(multiplexer);
		this.font = FontUtil.createMPlusFont(logicalCameraWidth / 24);
		this.menu = new Menu(this.font, logicalCameraWidth / 6, new FlickInputListener() {
			@Override
			public void up() {
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
				FreeSquare.this.hideMenu();
				FreeSquare.this.showItemList();
			}

			@Override
			public void center() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.showPlayerItemList();
			}
		});

		this.playerItemList = new PlayerItemList(this.stage.getCamera(), this.player, this.font) {
			private Square2dObject addedObject;

			@Override
			protected void selectedItemPanned(PossessedItem<?> pannedItem, float x, float y, float deltaX, float deltaY) {
				if (pannedItem == null) {
					return;
				}
				if (this.addedObject == null) {
					Item<?, ?> item = pannedItem.getItem();
					if (item instanceof Square2dObjectItem) {
						this.addedObject = ((Square2dObjectItem) item).createSquare2dObject();
						this.addedObject.setEnabledAction(false);
						if (FreeSquare.this.getPlayer().getItemBox().getItemQuantity(item) > 0) {
							Vector2 squareCoodinateXY = FreeSquare.this.getSquare().stageToLocalCoordinates(this.getWidget().localToStageCoordinates(new Vector2(x, y)));
							FreeSquare.this.getSquare().addSquareObject(this.addedObject, squareCoodinateXY.x, squareCoodinateXY.y, false);
							FreeSquare.this.showSquareOnly();
						}
					}
				} else {
					if (!this.addedObjectisBeingEaten()) {
						this.addedObject.moveBy(deltaX, deltaY);
					}
				}
			}

			private boolean addedObjectisBeingEaten() {
				return (this.addedObject instanceof EatableObject) && ((EatableObject) this.addedObject).isBeingEaten();
			}

			@Override
			protected void touchUp(float x, float y) {
				if (this.addedObject != null) {
					if (this.addedObject.isLandingOnSquare()) {
						FreeSquare.this.getPlayer().takeOutItem(Square2dObjectItem.getInstance(this.addedObject.getType()));
						this.addedObject.setEnabledAction(true);
						Square2dEvent event = new AddObjectEvent(this.addedObject);
						event.addExceptObserver(this.addedObject);
						FreeSquare.this.getSquare().notifyObservers(event);
					} else {
						FreeSquare.this.getSquare().removeSquareObject(this.addedObject);
					}
					FreeSquare.this.showPlayerItemList();
					this.addedObject = null;
					this.getColor().a = 1f;
				}
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
		return this.cameraRangeLowerLeft;
	}

	/**
	 * @return upper-right point of camera range
	 */
	public Vector2 getCameraRangeUpperRight() {
		return this.cameraRangeUpperRight;
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
		final float currentCameraZoom = ((OrthographicCamera) this.stage.getCamera()).zoom;
		this.menu.setScale(currentCameraZoom);
		this.stage.getRoot().addActor(this.menu);
		this.addCameraObserver(this.menu);
		this.menu.updateCamera(this.stage.getCamera());
		this.disableSquare();
	}

	void hideMenu() {
		this.stage.getRoot().removeActor(this.menu);
		this.removeCameraObserver(this.menu);
	}

	void showPlayerItemList() {
		this.stage.getRoot().addActor(this.playerItemList);
		this.addCameraObserver(this.playerItemList);
		this.playerItemList.updateCamera(this.stage.getCamera());
		this.disableSquare();
	}

	void hidePlayerItemList() {
		this.stage.getRoot().removeActor(this.playerItemList);
		this.removeCameraObserver(this.playerItemList);
	}

	void showItemList() {
		this.stage.getRoot().addActor(this.itemList);
		this.addCameraObserver(this.itemList);
		this.itemList.updateCamera(this.stage.getCamera());
		this.disableSquare();
	}

	void hideItemList() {
		this.stage.getRoot().removeActor(this.itemList);
		this.removeCameraObserver(this.itemList);
	}

	@Override
	public void render() {
		this.stage.act();
		this.drawStage();
	}

	private void drawStage() {
		Gdx.gl.glClearColor(0.2f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
	}

	@Override
	public void pause() {
		PersistItem.PLAYER.save(this.player);
		LastPlay.update();
	}

	@Override
	public void dispose() {
		try {
			if (this.font != null) {
				this.font.dispose();
			}
			this.stage.dispose();
			Square2dType.dispose();
			Square2dObjectType.EatableObjectType.dispose();
			Square2dObjectType.LifeObjectType.dispose();
		} catch (Throwable t) {
			Gdx.app.error(this.getClass().getName(), "error occured in dispose", t); //$NON-NLS-1$
			throw t;
		}
	}

	private void setupPersistItems() {
		this.playlog = PersistItem.PLAY_LOG.load();
		if (this.playlog == null) {
			System.out.println("new playlog"); //$NON-NLS-1$
			this.playlog = PlayLog.create();
			PersistItem.PLAY_LOG.save(this.playlog);
		}
		this.player = PersistItem.PLAYER.load();
		if (this.player == null) {
			System.out.println("new player"); //$NON-NLS-1$
			this.player = new Player("goshi"); //$NON-NLS-1$
			PersistItem.PLAYER.save(this.player);
		}
		this.life = PersistItem.LIFE1.load();
		if (this.life == null) {
			System.out.println("new life"); //$NON-NLS-1$
			this.life = new Life(new ShibaInu());
		}
		this.life.getStatus().addCuriosity(1);
		this.lastRun = LastPlay.getLastPlayDate();
		if (this.lastRun == null) {
			System.out.println("new last Run"); //$NON-NLS-1$
			this.lastRun = new Date();
		}
		PersistItem.LIFE1.save(this.life);
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

}
