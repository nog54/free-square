package org.nognog.freeSquare;

import java.util.Date;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.life.family.ShibaInu;
import org.nognog.freeSquare.model.persist.PersistItem;
import org.nognog.freeSquare.model.player.LastPlay;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square2d.Square2D;
import org.nognog.freeSquare.square2d.Square2DSize;
import org.nognog.freeSquare.square2d.SquareObject2D;
import org.nognog.freeSquare.square2d.objects.Riki;
import org.nognog.freeSquare.square2d.squares.GrassySquare1;
import org.nognog.freeSquare.util.font.FontUtil;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.SnapshotArray;
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
	BitmapFont font;
	Label.LabelStyle labelStyle;
	Player player;
	Life life;
	Date start;
	Date lastRun;
	String now;
	ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		if (Gdx.app.getType() != ApplicationType.iOS) {
			Gdx.graphics.setContinuousRendering(false);
		}
		this.shapeRenderer = new ShapeRenderer();
		final int logicalCameraWidth = Settings.getDefaultLogicalCameraWidth();
		final int logicalCameraHeight = Settings.getDefaultLogicalCameraHeight();

		System.out.println(logicalCameraWidth);
		System.out.println(logicalCameraHeight);
		this.square = new GrassySquare1(Square2DSize.SMALL);
		this.square.setX(-this.square.getWidth() / 2);
		for (int i = 0; i < 3; i++) {
			this.square.addAndRunObject(new Riki());
		}

		this.stage = new Stage(new FitViewport(logicalCameraWidth, logicalCameraHeight));
		this.stage.addActor(this.square);
		this.stage.getCamera().position.x -= this.stage.getCamera().viewportWidth / 2;

		this.cameraRangeLowerLeft = new Vector2(this.square.getX(), this.square.getY());
		this.cameraRangeUpperRight = new Vector2(this.square.getX() + this.square.getWidth(), this.square.getY()
				+ Math.max(this.square.getHeight(), logicalCameraHeight));
		setupPersistItems();

		GestureListener gestureListener = new GestureDetector.GestureAdapter() {

			private float initialScale = 1;
			private Stage targetStage = FreeSquare.this.stage;
			private Square2D targetSquare = FreeSquare.this.square;
			private OrthographicCamera correspondingCamera = (OrthographicCamera) FreeSquare.this.stage.getCamera();

			private Actor lastTouchDownActor;

			@Override
			public boolean touchDown(float x, float y, int pointer, int button) {
				Vector2 worldPosition = this.targetStage.getViewport().unproject(new Vector2(x, y));
				this.lastTouchDownActor = this.targetStage.hit(worldPosition.x, worldPosition.y, true);
				this.initialScale = this.correspondingCamera.zoom;
				return false;
			}

			private boolean isLastTouchSquareObject() {
				return !(this.lastTouchDownActor == this.targetSquare.getSquareImage() || this.lastTouchDownActor == null);
			}

			@Override
			public boolean pan(float x, float y, float deltaX, float deltaY) {
				if (this.isLastTouchSquareObject()) {
					return false;
				}
				float currentZoom = this.correspondingCamera.zoom;
				this.correspondingCamera.translate(-deltaX * currentZoom, deltaY * currentZoom, 0);
				this.adjustCameraPositionIfRangeOver();
				return true;
			}

			@Override
			public boolean zoom(float initialDistance, float distance) {
				if (this.isLastTouchSquareObject()) {
					return false;
				}
				float ratio = initialDistance / distance;
				float nextZoom = MathUtils.clamp(this.initialScale * ratio, MIN_ZOOM, MAX_ZOOM);
				this.correspondingCamera.zoom = nextZoom;
				this.adjustCameraPositionIfRangeOver();
				return true;
			}

			@Override
			public boolean tap(float x, float y, int count, int button) {
				if (this.isLastTouchSquareObject()) {
					return false;
				}
				if (this.tapsSquare(x, y)) {
					return false;
				}
				return true;
			}

			private boolean tapsSquare(float x, float y) {
				Vector2 squareCoordinateTapPosition = this.targetSquare.screenToLocalCoordinates(new Vector2(x, y));
				return this.targetSquare.containsInSquareArea(squareCoordinateTapPosition.x, squareCoordinateTapPosition.y);
			}

			private void adjustCameraPositionIfRangeOver() {
				float effectiveViewportWidth = this.correspondingCamera.viewportWidth * this.correspondingCamera.zoom;
				float effectiveViewportHeight = this.correspondingCamera.viewportHeight * this.correspondingCamera.zoom;

				float minCameraPositionX = FreeSquare.this.cameraRangeLowerLeft.x + effectiveViewportWidth / 2f;
				float maxCameraPositionX = FreeSquare.this.cameraRangeUpperRight.x - effectiveViewportWidth / 2f;
				float minCameraPositionY = FreeSquare.this.cameraRangeLowerLeft.y + effectiveViewportHeight / 2f;
				float maxCameraPositionY = FreeSquare.this.cameraRangeUpperRight.y - effectiveViewportHeight / 2f;

				this.correspondingCamera.position.x = MathUtils.clamp(this.correspondingCamera.position.x,
						minCameraPositionX, maxCameraPositionX);
				this.correspondingCamera.position.y = MathUtils.clamp(this.correspondingCamera.position.y,
						minCameraPositionY, maxCameraPositionY);
			}

		};
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new GestureDetector(gestureListener));
		multiplexer.addProcessor(this.stage);
		Gdx.input.setInputProcessor(multiplexer);
		this.font = FontUtil.createMPlusFont(16);
		this.batch = new SpriteBatch();
		// this.stage.setDebugAll(true);
		this.stage.getRoot().setDebug(true);
		System.out.println(this.stage.getRoot().getX());
	}

	private SpriteBatch batch;

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.4f, 0.4f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		writeWorldCoordinate();
		// drawVertexLine(this.shapeRenderer, this.square);
		this.stage.draw();

		OrthographicCamera camera = (OrthographicCamera) FreeSquare.this.stage.getCamera();
		String cameraPosition = "(" + camera.position.x + "," + camera.position.y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String cameraViewPort = "(" + camera.viewportWidth + "," + camera.viewportHeight + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.batch.begin();
		this.font.draw(this.batch, cameraPosition, 0, this.font.getLineHeight());
		this.font.draw(this.batch, this.cameraRangeLowerLeft.toString(), 0, this.font.getLineHeight() * 2);
		this.font.draw(this.batch, this.cameraRangeUpperRight.toString(), 0, this.font.getLineHeight() * 3);
		this.font.draw(this.batch, cameraViewPort, 0, this.font.getLineHeight() * 4);
		this.font.draw(this.batch, this.lastRun.toString(), 0, this.font.getLineHeight() * 5);
		this.batch.end();

	}

	@SuppressWarnings("unused")
	private static void drawVertexLine(ShapeRenderer renderer, Square2D square2) {
		renderer.begin(ShapeType.Line);
		renderer.setColor(0, 0, 1, 1);
		renderer.line(square2.vertex1.x, square2.vertex1.y, square2.vertex2.x, square2.vertex2.y);
		renderer.line(square2.vertex2.x, square2.vertex2.y, square2.vertex3.x, square2.vertex3.y);
		renderer.line(square2.vertex3.x, square2.vertex3.y, square2.vertex4.x, square2.vertex4.y);
		renderer.line(square2.vertex4.x, square2.vertex4.y, square2.vertex1.x, square2.vertex1.y);
		renderer.end();
	}

	private void writeWorldCoordinate() {
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		this.shapeRenderer.setProjectionMatrix(this.stage.getCamera().combined);
		this.shapeRenderer.setColor(1, 0, 0, 1);
		this.shapeRenderer.line(-2048, 0, 2048, 0);
		this.shapeRenderer.setColor(1, 0, 0, 1);
		this.shapeRenderer.line(0, -2048, 0, 2048);
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

	private SnapshotArray<SquareObject2D> performedObjectWhenPause;

	@Override
	public void pause() {
		super.pause();
		this.performedObjectWhenPause = new SnapshotArray<>();
		for (SquareObject2D object : this.square.getObjects()) {
			if (object.isPerformingIndependentAction()) {
				object.haltIndependentAction();
				this.performedObjectWhenPause.add(object);
			}

		}
	}

	@Override
	public void resume() {
		super.resume();
		if (this.performedObjectWhenPause == null) {
			return;
		}
		for (SquareObject2D object : this.performedObjectWhenPause) {
			object.startIndependentAction();
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
