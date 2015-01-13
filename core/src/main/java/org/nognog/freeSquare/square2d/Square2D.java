package org.nognog.freeSquare.square2d;

import static org.nognog.freeSquare.square2d.Square2D.Vertex.vertex;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.nognog.freeSquare.square.Square;
import org.nognog.freeSquare.square.SquareObserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author goshi 2014/12/10
 */
public class Square2D extends Group implements Square<SquareObject2D> {
	/** vertex1 of Square */
	public final Vertex vertex1;
	/** vertex2 of Square */
	public final Vertex vertex2;
	/** vertex3 of Square */
	public final Vertex vertex3;
	/** vertex4 of Square */
	public final Vertex vertex4;
	private final boolean isConcave;
	private final Square2DSize size;
	private Image squareImage;

	private Array<SquareObserver> observers;
	private Array<SquareObject2D> objects;

	private ExecutorService pool = Executors.newCachedThreadPool();
	private Future<?> future;

	private boolean isRequestedDrawOrderUpdate = false;
	private boolean isDisposed = false;

	private static final Comparator<Actor> actorComparator = new Comparator<Actor>() {

		@Override
		public int compare(Actor actor1, Actor actor2) {
			if (actor1.getY() < actor2.getY()) {
				return 1;
			} else if (actor1.getY() > actor2.getY()) {
				return -1;
			}
			return 0;
		}

	};

	/**
	 * @param size
	 * @param baseVertex1
	 * @param baseVertex2
	 * @param baseVertex3
	 * @param baseVertex4
	 * @param texture
	 */
	public Square2D(Square2DSize size, Vertex baseVertex1, Vertex baseVertex2, Vertex baseVertex3, Vertex baseVertex4, Texture texture) {
		this.size = size;
		final float width = size.getWidth();
		final float scale = width / texture.getWidth();
		final float squareImagePositionOffsetY = width / 12;
		this.vertex1 = vertex(baseVertex1.x * scale, baseVertex1.y * scale + squareImagePositionOffsetY);
		this.vertex2 = vertex(baseVertex2.x * scale, baseVertex2.y * scale + squareImagePositionOffsetY);
		this.vertex3 = vertex(baseVertex3.x * scale, baseVertex3.y * scale + squareImagePositionOffsetY);
		this.vertex4 = vertex(baseVertex4.x * scale, baseVertex4.y * scale + squareImagePositionOffsetY);
		if (this.isInvalidVertex()) {
			throw new RuntimeException("Square corners are invalid."); //$NON-NLS-1$
		}
		if (Intersector.intersectSegments(this.vertex1.x, this.vertex1.y, this.vertex3.x, this.vertex3.y, this.vertex2.x, this.vertex2.y, this.vertex4.x, this.vertex4.y, null)) {
			this.isConcave = false;
		} else {
			this.isConcave = true;
		}

		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.squareImage = new Image(texture);
		this.squareImage.setWidth(width);
		this.squareImage.setHeight(this.squareImage.getHeight() * scale);
		this.squareImage.setY(squareImagePositionOffsetY);
		this.squareImage.setName("squareImage"); //$NON-NLS-1$
		this.addActor(this.squareImage);
		this.observers = new Array<>();
		this.objects = new Array<>();

		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (Square2D.this.containsInSquareArea(x, y) || this.isTouchingSquareObject(x, y)) {
					return false;
				}
				event.stop();
				return false;
			}

			private boolean isTouchingSquareObject(float x, float y) {
				Actor touchedActor = Square2D.this.hit(x, y, true);
				return touchedActor != Square2D.this.getSquareImage();
			}
		});
	}

	@Override
	public float getWidth() {
		return this.squareImage.getWidth();
	}

	@Override
	public float getHeight() {
		return this.squareImage.getHeight();
	}

	private boolean isInvalidVertex() {
		if (Intersector.intersectSegments(this.vertex1.x, this.vertex1.y, this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y, null)) {
			return true;
		}
		if (Intersector.intersectSegments(this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y, this.vertex1.x, this.vertex1.y, null)) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
		if (this.isRequestedDrawOrderUpdate) {
			this.getChildren().sort(actorComparator);
			moveSquareImageToBack();
			this.isRequestedDrawOrderUpdate = false;
		}
		super.draw(batch, parentAlpha);
	}

	private void moveSquareImageToBack() {
		if (this.squareImage.getZIndex() != 0) {
			this.squareImage.setZIndex(0);
		}
	}

	/**
	 * @param object
	 */
	@Override
	public void addObject(SquareObject2D object) {
		this.objects.add(object);
		this.addActor(object);
		object.setSquare(this);
	}

	/**
	 * @param object
	 */
	public void addAndRunObject(SquareObject2D object) {
		this.addObject(object);
		object.startIndependentAction();
	}

	@Override
	public Iterable<SquareObject2D> getObjects() {
		return this.objects;
	}

	/**
	 * request a change the order in which actors are drawed
	 */
	public void requestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = true;
	}

	/**
	 * @return size
	 */
	public Square2DSize getSquareSize() {
		return this.size;
	}

	/**
	 * @return pool
	 */
	public ExecutorService getPool() {
		return this.pool;
	}

	/**
	 * @return square image
	 */
	public Image getSquareImage() {
		return this.squareImage;
	}

	/**
	 * @return true if square is concave
	 */
	public boolean isConcave() {
		return this.isConcave;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is contained in square of vertex1, vertex2,
	 *         vertex3 and vertex4
	 */
	public boolean containsInSquareArea(float x, float y) {
		float[] vertices = { this.vertex1.x, this.vertex1.y, this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y };
		Polygon p = new Polygon(vertices);
		return p.contains(x, y);
	}

	/**
	 * dispose
	 */
	public void dispose() {
		this.pool.shutdownNow();
		((TextureRegionDrawable) (this.squareImage.getDrawable())).getRegion().getTexture().dispose();
		for (SquareObject2D object : this.objects) {
			if (!object.isDisposed()) {
				object.dispose();
			}
		}
		this.isDisposed = true;
	}

	/**
	 * @return whether this is disposed
	 */
	public boolean isDisposed() {
		return this.isDisposed;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.vertex1).append("-").append(this.vertex2).append("-").append(this.vertex3).append("-") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.append(this.vertex4);
		return sb.toString();
	}

	@Override
	public void addAction(Action action) {
		super.addAction(action);
		if (this.future == null || this.future.isDone()) {
			this.future = this.getPool().submit(new Runnable() {
				private static final long interval = 10;
				private long previousActionTime = TimeUtils.millis();

				@Override
				public void run() {
					this.actUntilAllActionFinished();
				}

				private void actUntilAllActionFinished() {
					while (Square2D.this.getActions().size != 0) {
						final long currentTime = TimeUtils.millis();
						final float delta = (currentTime - this.previousActionTime) / 1000f;
						Square2D.this.getSquareImage().act(delta);
						this.previousActionTime = currentTime;
						if (Thread.currentThread().isInterrupted()) {
							break;
						}
						try {
							Thread.sleep(interval);
						} catch (InterruptedException e) {
							break;
						}
					}
				}

			});
		}
	}

	/**
	 * Immutable vector
	 * 
	 * @author goshi 2014/12/17
	 */
	public static class Vertex {
		/** x-axis point */
		public final float x;
		/** y-axis point */
		public final float y;

		/**
		 * @param x
		 * @param y
		 */
		public Vertex(float x, float y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * @param x1
		 * @param y1
		 * @return sqrt((this.x - x) ^ 2 + (this.y - y) ^ 2 )
		 */
		public float calculateR(float x1, float y1) {
			final float diffX = this.x - x1;
			final float diffY = this.y - y1;
			return (float) Math.sqrt(diffX * diffX + diffY * diffY);
		}

		/**
		 * factory method
		 * 
		 * @param x
		 * @param y
		 * @return new instance
		 */
		public static Vertex vertex(float x, float y) {
			return new Vertex(x, y);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return sb.append("(").append(this.x).append(",").append(this.y).append(")").toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	@Override
	public void addSquareObserver(SquareObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeSquareObserver(SquareObserver observer) {
		this.observers.removeValue(observer, true);
	}

	@Override
	public void notifyObservers() {
		for (int i = 0; i < this.observers.size; i++) {
			this.observers.get(i).update();
		}
	}
}
