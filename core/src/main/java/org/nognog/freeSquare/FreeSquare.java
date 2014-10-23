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
import org.nognog.freeSquare.square2d.Square2D.FourCorner;
import org.nognog.freeSquare.square2d.objects.Riki;
import org.nognog.freeSquare.square2d.squares.GrassySquare1;
import org.nognog.freeSquare.util.font.FontUtil;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @author goshi 2014/10/23
 */
public class FreeSquare extends ApplicationAdapter {

	private float w;
	private float h;
	private static final float MAX_ZOOM = 1f;
	private static final float MIN_ZOOM = 0.5f;
	int logicalWidth;
	int logicalHeight;
	float aspectRatio;
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
	int repeatCount = 0;
	int pauseCount = 0;
	int resumeCount = 0;

	@Override
	public void create() {
		Gdx.graphics.setContinuousRendering(false);
		this.pool = Executors.newCachedThreadPool();
		this.shapeRenderer = new ShapeRenderer();
		
		this.w = Gdx.graphics.getWidth();
		this.h = Gdx.graphics.getHeight();
		this.aspectRatio = this.h / this.w;
		this.logicalWidth = 1024;
		this.logicalHeight = (int) (this.logicalWidth * this.aspectRatio);
		this.stage = new Stage(new FitViewport(this.logicalWidth, this.logicalHeight));
		this.stage.setDebugAll(true);
		this.square = new GrassySquare1(this.logicalWidth);
		this.square.addAndStartObject(new Riki());
		
		this.stage.addActor(this.square);

		setupPersistItems();
		final int fontSize = this.logicalHeight / 36;
		this.font = FontUtil.createMPlusFont(fontSize);

		this.now = new Date().toString();
		this.t = new RepeatRunThread(new Runnable() {
			@Override
			public void run() {
				String nowString = LastPlay.update().toString();
				if (!FreeSquare.this.now.equals(nowString)) {
					FreeSquare.this.now = LastPlay.update().toString();
					Gdx.graphics.requestRendering();
				}
				FreeSquare.this.repeatCount++;
			}
		});
		this.future = this.pool.submit(this.t);
		String startString = "start:" + this.start; //$NON-NLS-1$
		String nowString = "now:" + this.now; //$NON-NLS-1$
		String lastString = "last:" + this.lastRun; //$NON-NLS-1$

		this.labelStyle = new Label.LabelStyle();
		this.labelStyle.font = this.font;
		Label startText = new Label(startString, this.labelStyle);
		Label nowText = new Label(nowString, this.labelStyle) {
			@Override
			public void act(float delta) {
				super.act(delta);
				OrthographicCamera camera = (OrthographicCamera) FreeSquare.this.stage.getCamera();
				this.setText("start:" + camera.position); //$NON-NLS-1$
			}
		};
		Label lastText = new Label(lastString, this.labelStyle);
		startText.setPosition(this.logicalWidth / 16f, this.logicalHeight / 8f * 3f);
		nowText.setPosition(this.logicalWidth / 16f, this.logicalHeight / 8f * 2f);
		lastText.setPosition(this.logicalWidth / 16f, this.logicalHeight / 8f * 1f);
		this.stage.addActor(startText);
		this.stage.addActor(nowText);
		this.stage.addActor(lastText);

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

				camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f,
						FreeSquare.this.logicalWidth - effectiveViewportWidth / 2f);
				camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f,
						FreeSquare.this.logicalHeight - effectiveViewportHeight / 2f);
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
		this.stage.act();
		this.stage.draw();
		FourCorner corners = this.square.getCorners();

		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		this.shapeRenderer.setColor(0, 0, 1, 1);
		this.shapeRenderer.line(corners.near.x, corners.near.y, corners.right.x, corners.right.y);
		this.shapeRenderer.line(corners.near.x, corners.near.y, corners.left.x, corners.left.y);
		this.shapeRenderer.line(corners.far.x, corners.far.y, corners.right.x, corners.right.y);
		this.shapeRenderer.line(corners.far.x, corners.far.y, corners.left.x, corners.left.y);
		this.shapeRenderer.end();
	}

	private void writeWorldCoordinate() {
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		this.shapeRenderer.setProjectionMatrix(this.stage.getCamera().combined);
		final int grid = 75;
		final int largerAxisSize = Math.max(this.logicalWidth, this.logicalHeight);
		for (int i = 0; i < largerAxisSize / 75 + 1; i++) {
			final int nextLine = i * grid;
			if (nextLine == 0) {
				this.shapeRenderer.setColor(1, 1, 1, 1);
			} else {
				this.shapeRenderer.setColor(1, 0, 0, 1);
			}

			this.shapeRenderer.line(0, nextLine, this.logicalWidth, nextLine);
			if (nextLine == 0) {
				this.shapeRenderer.setColor(1, 1, 1, 1);
			} else {
				this.shapeRenderer.setColor(0, 1, 0, 1);
			}

			this.shapeRenderer.line(nextLine, 0, nextLine, this.logicalHeight);
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
		this.pauseCount++;
	}

	@Override
	public void resume() {
		this.future = this.pool.submit(this.t);
		this.resumeCount++;
	}

	@Override
	public void resize(int width, int height) {
		Gdx.graphics.requestRendering();
	}

	@Override
	public void dispose() {
		System.out.println("end up: " + LastPlay.update()); //$NON-NLS-1$
		this.font.dispose();
		this.pool.shutdownNow();
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
