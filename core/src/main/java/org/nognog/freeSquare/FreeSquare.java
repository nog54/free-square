package org.nognog.freeSquare;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.family.ShibaInu;
import org.nognog.freeSquare.model.persist.PersistItem;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square2d.Square2D;
import org.nognog.freeSquare.square2d.Square2DSize;
import org.nognog.freeSquare.square2d.objects.Riki;
import org.nognog.freeSquare.square2d.squares.GrassySquare1;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter {

	private static final float MAX_ZOOM = 1f;
	private static final float MIN_ZOOM = 0.5f;
	Vector2 cameraRangeLowerLeft;
	Vector2 cameraRangeUpperRight;
	Stage stage;
	Square2D square;
	ExecutorService pool;
	BitmapFont font;
	Label.LabelStyle labelStyle;
	Player player;
	Life life;
	Date start;
	Date lastRun;
	String now;
	RepeatRunThread t;
	ShapeRenderer shapeRenderer;
	Future<?> future;

	@Override
	public void create() {
		if (Gdx.app.getType() != ApplicationType.iOS) {
			Gdx.graphics.setContinuousRendering(false);
		}
		this.pool = Executors.newCachedThreadPool();
		this.shapeRenderer = new ShapeRenderer();
		final int logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		final int logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		this.stage = new Stage(new FitViewport(logicalCameraWidth, logicalCameraHeight));
		this.square = new GrassySquare1(Square2DSize.MEDIUM);
		this.square.addAndRunObject(new Riki());
		this.square.addAndRunObject(new Riki());
		this.square.addAndRunObject(new Riki());
		this.stage.addActor(this.square);

		this.cameraRangeLowerLeft = new Vector2(0, 0);
		this.cameraRangeUpperRight = new Vector2(logicalCameraWidth, logicalCameraHeight);

		setupPersistItems();

		GestureListener gestureListener = new BasicGestureListener() {
			private float initialScale = 1;

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				this.initialScale = ((OrthographicCamera) (FreeSquare.this.stage.getCamera())).zoom;
				return false;
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				float currentZoom = ((OrthographicCamera) (FreeSquare.this.stage.getCamera())).zoom;
				FreeSquare.this.stage.getCamera().translate(-deltaX * currentZoom, deltaY * currentZoom, 0);
				moveCameraPositionIfWorldOver((OrthographicCamera) (FreeSquare.this.stage.getCamera()));
				return false;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				float ratio = initialDistance / distance;
				float nextZoom = MathUtils.clamp(this.initialScale * ratio, MIN_ZOOM, MAX_ZOOM);
				((OrthographicCamera) (FreeSquare.this.stage.getCamera())).zoom = nextZoom;
				moveCameraPositionIfWorldOver((OrthographicCamera) (FreeSquare.this.stage.getCamera()));
				return false;
			}

			private void moveCameraPositionIfWorldOver(OrthographicCamera camera) {
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
		Gdx.input.setInputProcessor(new GestureDetector(gestureListener));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.4f, 0.4f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		writeWorldCoordinate();
		this.stage.draw();
		// this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		// this.shapeRenderer.setColor(0, 0, 1, 1);
		// this.shapeRenderer.line(this.square.vertex1.x, this.square.vertex1.y,
		// this.square.vertex2.x,
		// this.square.vertex2.y);
		// this.shapeRenderer.line(this.square.vertex2.x, this.square.vertex2.y,
		// this.square.vertex3.x,
		// this.square.vertex3.y);
		// this.shapeRenderer.line(this.square.vertex3.x, this.square.vertex3.y,
		// this.square.vertex4.x,
		// this.square.vertex4.y);
		// this.shapeRenderer.line(this.square.vertex1.x, this.square.vertex1.y,
		// this.square.vertex4.x,
		// this.square.vertex4.y);
		// this.shapeRenderer.end();
	}

	private void writeWorldCoordinate() {
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		this.shapeRenderer.setProjectionMatrix(this.stage.getCamera().combined);
		final int grid = 75;
		final int largerAxisSize = Math.max(Settings.getDefaultLogicalCameraWidth(),
				Settings.getDefaultLogicalCameraHeight());
		for (int i = 0; i < largerAxisSize / grid + 1; i++) {
			final int nextLine = i * grid;
			if (nextLine == 0) {
				this.shapeRenderer.setColor(1, 1, 1, 1);
			} else {
				this.shapeRenderer.setColor(1, 0, 0, 1);
			}

			this.shapeRenderer.line(0, nextLine, Settings.getDefaultLogicalCameraWidth(), nextLine);
			if (nextLine == 0) {
				this.shapeRenderer.setColor(1, 1, 1, 1);
			} else {
				this.shapeRenderer.setColor(0, 1, 0, 1);
			}

			this.shapeRenderer.line(nextLine, 0, nextLine, Settings.getDefaultLogicalCameraHeight());
		}
		this.shapeRenderer.end();

	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.stage.getCamera().translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.stage.getCamera().translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.stage.getCamera().translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.stage.getCamera().translate(0, 3, 0);
		}
		this.stage.getCamera().update();
	}

	@Override
	public void pause() {
		if (this.future != null) {
			this.future.cancel(true);
		}
	}

	@Override
	public void resume() {
		if (this.pool != null) {
			this.future = this.pool.submit(this.t);
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.graphics.requestRendering();
	}

	@Override
	public void dispose() {
		System.out.println("end up: " + LastPlay.update()); //$NON-NLS-1$
		if (this.font != null) {
			this.font.dispose();
		}
		if (this.pool != null) {
			this.pool.shutdownNow();
		}
		this.stage.dispose();
		this.square.dispose();
		this.shapeRenderer.dispose();
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
}
