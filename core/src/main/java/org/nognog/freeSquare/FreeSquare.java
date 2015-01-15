package org.nognog.freeSquare;

import java.util.Date;

import org.nognog.freeSquare.FlickButtonController.FlickInputListener;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.family.ShibaInu;
import org.nognog.freeSquare.model.persist.PersistItem;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square.SquareObserver;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dSize;
import org.nognog.freeSquare.square2d.objects.Square2dObjectKind;
import org.nognog.freeSquare.square2d.squares.GrassySquare1;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter implements SquareObserver {

	private static final float MAX_ZOOM = 1f;
	private static final float MIN_ZOOM = 0.5f;
	private boolean isShowingMenu;

	private boolean isRequestedToRedraw;
	Vector2 cameraRangeLowerLeft;
	Vector2 cameraRangeUpperRight;
	Stage mainStage;
	Square2d square;
	BitmapFont font;
	Player player;
	Life life;
	Date start;
	Date lastRun;
	String now;
	ShapeRenderer shapeRenderer;
	FlickButtonController menu;

	@Override
	public void create() {
		this.shapeRenderer = new ShapeRenderer();
		final int logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		final int logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		this.square = new GrassySquare1(Square2dSize.MEDIUM);
		this.square.addSquareObserver(this);
		this.square.setX(-this.square.getWidth() / 2);

		for (Square2dObjectKind object : Square2dObjectKind.values()) {
			this.square.addSquareObject(object.create());
		}

		this.mainStage = new Stage(new FitViewport(logicalCameraWidth, logicalCameraHeight));
		this.mainStage.addActor(this.square);
		this.mainStage.getCamera().position.x -= this.mainStage.getCamera().viewportWidth / 2;

		this.cameraRangeLowerLeft = new Vector2(this.square.getX(), this.square.getY());
		this.cameraRangeUpperRight = new Vector2(this.square.getX() + this.square.getWidth(), this.square.getY() + Math.max(this.square.getHeight(), logicalCameraHeight));
		setupPersistItems();
		GestureListener gestureListener = new GestureDetector.GestureAdapter() {

			private float initialScale = 1;

			private Actor lastTouchDownActor;

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				Vector2 worldPosition = FreeSquare.this.mainStage.getViewport().unproject(new Vector2(x, y));
				this.lastTouchDownActor = FreeSquare.this.mainStage.hit(worldPosition.x, worldPosition.y, true);
				OrthographicCamera camera = (OrthographicCamera) FreeSquare.this.mainStage.getCamera();
				this.initialScale = camera.zoom;
				return false;
			}

			private boolean isLastTouchSquareObjectOrMenu() {
				return !(this.lastTouchDownActor == FreeSquare.this.square.getSquareImage() || this.lastTouchDownActor == null);
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (this.isLastTouchSquareObjectOrMenu()) {
					return false;
				}
				OrthographicCamera camera = (OrthographicCamera) FreeSquare.this.mainStage.getCamera();
				float currentZoom = camera.zoom;
				camera.translate(-deltaX * currentZoom, deltaY * currentZoom, 0);
				this.adjustCameraPositionIfRangeOver();
				return true;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				if (FreeSquare.this.square.getTouchable() == Touchable.disabled) {
					return false;
				}
				if (this.isLastTouchSquareObjectOrMenu()) {
					return false;
				}
				if (FreeSquare.this.isShowingMenu()) {
					return false;
				}
				float ratio = initialDistance / distance;
				float nextZoom = MathUtils.clamp(this.initialScale * ratio, MIN_ZOOM, MAX_ZOOM);
				OrthographicCamera camera = (OrthographicCamera) FreeSquare.this.mainStage.getCamera();
				camera.zoom = nextZoom;
				this.adjustCameraPositionIfRangeOver();
				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				if (this.isLastTouchSquareObjectOrMenu()) {
					return false;
				}
				if (this.tapsSquare(x, y)) {
					return false;
				}

				if (FreeSquare.this.isShowingMenu()) {
					FreeSquare.this.hideMenu();
					return true;
				}
				Vector2 menuPosition = FreeSquare.this.mainStage.screenToStageCoordinates(new Vector2(x, y));
				FreeSquare.this.showMenu(menuPosition.x, menuPosition.y);
				return true;
			}

			private boolean tapsSquare(float x, float y) {
				if (FreeSquare.this.square.getTouchable() == Touchable.disabled) {
					return false;
				}
				Vector2 squareCoordinateTapPosition = FreeSquare.this.square.screenToLocalCoordinates(new Vector2(x, y));
				return FreeSquare.this.square.containsInSquareArea(squareCoordinateTapPosition.x, squareCoordinateTapPosition.y);
			}

			private void adjustCameraPositionIfRangeOver() {
				OrthographicCamera camera = (OrthographicCamera) FreeSquare.this.mainStage.getCamera();
				float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
				float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

				float minCameraPositionX = FreeSquare.this.cameraRangeLowerLeft.x + effectiveViewportWidth / 2f;
				float maxCameraPositionX = FreeSquare.this.cameraRangeUpperRight.x - effectiveViewportWidth / 2f;
				float minCameraPositionY = FreeSquare.this.cameraRangeLowerLeft.y + effectiveViewportHeight / 2f;
				float maxCameraPositionY = FreeSquare.this.cameraRangeUpperRight.y - effectiveViewportHeight / 2f;

				camera.position.x = MathUtils.clamp(camera.position.x, minCameraPositionX, maxCameraPositionX);
				camera.position.y = MathUtils.clamp(camera.position.y, minCameraPositionY, maxCameraPositionY);
			}

		};
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(gestureListener));
		multiplexer.addProcessor(this.mainStage);
		Gdx.input.setInputProcessor(multiplexer);
		this.font = FontUtil.createMPlusFont(logicalCameraWidth / 32);
		this.menu = new FlickButtonController(this.font, logicalCameraWidth / 6, new FlickInputListener() {

			@Override
			public void up() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.mainStage.getCamera().translate(0, 10, 0);
			}

			@Override
			public void right() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.mainStage.getCamera().translate(10, 0, 0);
			}

			@Override
			public void left() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.mainStage.getCamera().translate(-10, 0, 0);
			}

			@Override
			public void down() {
				FreeSquare.this.hideMenu();
				FreeSquare.this.mainStage.getCamera().translate(0, -10, 0);
			}

			@Override
			public void center() {
				FreeSquare.this.hideMenu();
				((OrthographicCamera) FreeSquare.this.mainStage.getCamera()).zoom -= 0.2;
			}
		});
	}

	/**
	 * @return true if menu is showing
	 */
	public boolean isShowingMenu() {
		return this.isShowingMenu;
	}

	void showMenu(float x, float y) {
		this.mainStage.getRoot().removeActor(this.menu);
		this.menu.setPosition(x, y);
		final float currentCameraZoom = ((OrthographicCamera) this.mainStage.getCamera()).zoom;
		this.menu.setScale(currentCameraZoom);
		this.mainStage.getRoot().addActor(FreeSquare.this.menu);
		this.square.getColor().a = 0.75f;
		this.square.setTouchable(Touchable.disabled);
		this.isShowingMenu = true;
	}

	void hideMenu() {
		this.mainStage.getRoot().removeActor(this.menu);
		this.square.getColor().a = 1;
		this.square.setTouchable(Touchable.enabled);
		this.isShowingMenu = false;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.4f, 0.4f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		writeWorldCoordinate();
		try {
			this.mainStage.act();
			if (this.isRequestedToRedraw) {
				this.mainStage.draw();
				this.isRequestedToRedraw = false;
			}
		} catch (Throwable t) {
			Gdx.app.error(this.getClass().getName(), "error occured in draw", t); //$NON-NLS-1$
			throw t;
		}
	}

	private void writeWorldCoordinate() {
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		this.shapeRenderer.setProjectionMatrix(this.mainStage.getCamera().combined);
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
			System.out.println("end up: " + LastPlay.update()); //$NON-NLS-1$
			if (this.font != null) {
				this.font.dispose();
			}
			this.mainStage.dispose();
			this.square.dispose();
			this.shapeRenderer.dispose();
		} catch (Throwable t) {
			Gdx.app.error(this.getClass().getName(), "error occured in dispose", t); //$NON-NLS-1$
			throw t;
		}
	}

	private void setupPersistItems() {
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
		this.start = LastPlay.update();

		PersistItem.LIFE1.save(this.life);
	}

	@Override
	public void update() {
		this.isRequestedToRedraw = true;
	}

}
