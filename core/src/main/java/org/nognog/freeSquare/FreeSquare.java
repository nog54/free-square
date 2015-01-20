package org.nognog.freeSquare;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.family.ShibaInu;
import org.nognog.freeSquare.model.persist.PersistItem;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.PlayLog;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.ui.FlickButtonController;
import org.nognog.freeSquare.ui.FlickButtonController.FlickInputListener;
import org.nognog.freeSquare.ui.PlayerItemList;
import org.nognog.freeSquare.ui.SquareObserver;
import org.nognog.freeSquare.ui.square2d.Square2d;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;
import org.nognog.freeSquare.ui.square2d.squares.Square2dType;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter implements SquareObserver {
	private Stage stage;
	private Square2d square;

	private Vector2 cameraRangeLowerLeft;
	private Vector2 cameraRangeUpperRight;
	private boolean isLockingCameraZoom;
	private boolean isLockingCameraMove;

	private BitmapFont font;
	private PlayLog playlog;
	private Player player;
	private Life life;
	private Date lastRun;
	private ShapeRenderer shapeRenderer;
	private FlickButtonController menu;
	private PlayerItemList itemList;

	private ExecutorService pool;
	private Future<?> future;

	@Override
	public void create() {
		Gdx.graphics.setContinuousRendering(false);
		setupPersistItems();
		System.out.println(this.player.getItemBox());
		this.shapeRenderer = new ShapeRenderer();
		final int logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		final int logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		this.square = Square2dType.GRASSY_SQUARE1.create();
		this.square.addSquareObserver(this);
		this.square.setX(-this.square.getWidth() / 2);
		for (Square2dObjectType object : Square2dObjectType.values()) {
			for (int i = 0; i < 2; i++) {
				this.square.addSquareObject(object.create());
			}
		}

		//this.player.takeOutItem(Square2dObjectItem.getInstance(Square2dObjectType.GOLD_SESAME_TOFU));

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
		this.menu = new FlickButtonController(this.font, logicalCameraWidth / 6, new FlickInputListener() {
			@Override
			public void up() {
				FreeSquare.this.showSquare();
			}

			@Override
			public void right() {
				FreeSquare.this.showSquare();
			}

			@Override
			public void left() {
				FreeSquare.this.showSquare();
			}

			@Override
			public void down() {
				FreeSquare.this.showSquare();
			}

			@Override
			public void center() {
				FreeSquare.this.showItemList();
			}
		});

		this.itemList = new PlayerItemList(this.player, this.font);
		this.itemList.setWidth(logicalCameraWidth / 2);
		this.itemList.setHeight(logicalCameraHeight / 2);

		this.pool = Executors.newCachedThreadPool();
		this.future = this.pool.submit(new Runnable() {
			FreeSquare target = FreeSquare.this;
			private long lastActTime = TimeUtils.millis();

			@Override
			public void run() {

				while (true) {
					final long currentTime = TimeUtils.millis();
					final float delta = (currentTime - this.lastActTime) / 1000f;
					try {
						this.target.actStage(delta);
					} catch (NullPointerException e) {
						// NullPointerException may occurs when remove actor.
					}
					this.lastActTime = currentTime;
					if (Thread.currentThread().isInterrupted()) {
						break;
					}
				}

			}

		});
		this.update();

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

	void showSquare() {
		for (Actor child : this.stage.getRoot().getChildren()) {
			if (child != this.square) {
				this.stage.getRoot().removeActor(child);
			}
		}
		this.square.getColor().a = 1;
		this.square.setTouchable(Touchable.enabled);
		this.isLockingCameraMove = false;
		this.isLockingCameraZoom = false;
	}

	void showMenu(float x, float y) {
		this.showSquare();
		this.menu.setPosition(x, y);
		final float currentCameraZoom = ((OrthographicCamera) this.stage.getCamera()).zoom;
		this.menu.setScale(currentCameraZoom);
		this.stage.getRoot().addActor(this.menu);
		this.disableSquare();
		this.isLockingCameraZoom = true;
	}

	void showItemList() {
		this.showSquare();
		resetItemListPositionAndScale();
		this.stage.getRoot().addActor(this.itemList);
		this.disableSquare();
	}

	private void disableSquare() {
		this.stage.cancelTouchFocus(this.square);
		this.square.getColor().a = 0.75f;
		this.square.setTouchable(Touchable.disabled);
	}

	void resetItemListPositionAndScale() {
		final float currentCameraZoom = ((OrthographicCamera) this.stage.getCamera()).zoom;
		final float newX = this.stage.getCamera().position.x;
		final float newY = this.stage.getCamera().position.y - currentCameraZoom * this.itemList.getHeight();
		this.itemList.setPosition(newX, newY);
		this.itemList.setScale(currentCameraZoom);
	}

	@Override
	public void render() {
		this.drawStage();
	}

	synchronized void drawStage() {
		Gdx.gl.glClearColor(0.2f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// writeWorldCoordinate();
		this.stage.draw();
	}

	synchronized void actStage(float delta) {
		synchronized (Gdx.input) {
			this.stage.act(delta);
		}
	}

	@SuppressWarnings("unused")
	private void writeWorldCoordinate() {
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		this.shapeRenderer.setProjectionMatrix(this.stage.getCamera().combined);
		this.shapeRenderer.setColor(1, 0, 0, 1);
		this.shapeRenderer.line(-2048, 0, 2048, 0);
		this.shapeRenderer.setColor(1, 0, 0, 1);
		this.shapeRenderer.line(0, -2048, 0, 2048);
		this.shapeRenderer.end();
	}

	@Override
	public void pause() {
		Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void resume() {
		Gdx.graphics.setContinuousRendering(true);
	}

	@Override
	public void dispose() {
		try {
			this.future.cancel(true);
			LastPlay.update();
			PersistItem.PLAYER.save(this.player);
			if (this.font != null) {
				this.font.dispose();
			}
			this.stage.dispose();
			Square2dType.dispose();
			Square2dObjectType.dispose();
			this.shapeRenderer.dispose();
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

	@Override
	public void update() {
		Gdx.graphics.requestRendering();
	}

}
