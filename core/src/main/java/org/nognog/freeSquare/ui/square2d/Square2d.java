package org.nognog.freeSquare.ui.square2d;

import java.util.Comparator;

import org.nognog.freeSquare.ui.Square;
import org.nognog.freeSquare.ui.SquareObserver;
import org.nognog.freeSquare.ui.square2d.events.AddObjectEvent;
import org.nognog.freeSquare.ui.square2d.events.RemoveObjectEvent;
import org.nognog.freeSquare.ui.square2d.squares.Square2dType;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2014/12/10
 */
public class Square2d extends Group implements Square<Square2dObject> {

	private final Square2dType type;

	/** vertex 1 */
	public final Vertex vertex1;
	/** vertex 2 */
	public final Vertex vertex2;
	/** vertex 3 */
	public final Vertex vertex3;
	/** vertex 4 */
	public final Vertex vertex4;

	private final Image squareImage;

	private final Array<SquareObserver> observers;
	private final Array<Square2dObject> objects;

	private boolean isRequestedDrawOrderUpdate = false;

	private static final Comparator<Actor> actorComparator = new Comparator<Actor>() {

		@Override
		public int compare(Actor actor1, Actor actor2) {
			float actor1Y = actor1.getY() - actor1.getOriginY();
			float actor2Y = actor2.getY() - actor2.getOriginY();
			if (actor1Y < actor2Y) {
				return 1;
			} else if (actor1Y > actor2Y) {
				return -1;
			}
			return 0;
		}

	};

	/**
	 * @param type
	 */
	public Square2d(Square2dType type) {
		this.type = type;
		this.vertex1 = type.vertex1;
		this.vertex2 = type.vertex2;
		this.vertex3 = type.vertex3;
		this.vertex4 = type.vertex4;
		this.squareImage = type.createSquareImage();
		super.addActor(this.squareImage);
		this.observers = new Array<>();
		this.objects = new Array<>();

		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (Square2d.this.containsInSquareArea(x, y) || this.isTouchingSquareObject(x, y)) {
					return false;
				}
				event.stop();
				return false;
			}

			private boolean isTouchingSquareObject(float x, float y) {
				Actor touchedActor = Square2d.this.hit(x, y, true);
				return touchedActor != Square2d.this.getSquareImage();
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

	@Override
	public void draw(Batch batch, float parentAlpha) {
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
	public void addSquareObject(Square2dObject object) {
		this.addSquareObject(object, true);
	}

	/**
	 * @param object
	 * @param notifyObserver
	 */
	public void addSquareObject(Square2dObject object, boolean notifyObserver) {
		Vector2 randomPoint = Square2dUtils.getRandomPointOn(this);
		this.addSquareObject(object, randomPoint.x, randomPoint.y, notifyObserver);
	}

	/**
	 * @param object
	 * @param x
	 * @param y
	 * @param notifyObserver
	 */
	public void addSquareObject(Square2dObject object, float x, float y) {
		this.addSquareObject(object, x, y, true);
	}

	/**
	 * @param object
	 * @param x
	 * @param y
	 * @param notifyObserver
	 */
	public void addSquareObject(Square2dObject object, float x, float y, boolean notifyObserver) {
		super.addActor(object);
		object.setSquare(this);
		object.setPosition(x, y);
		this.objects.add(object);
		if (notifyObserver) {
			this.notifyObservers(new AddObjectEvent(object));
		}
		this.addSquareObserver(object);
	}

	/**
	 * not notify observer
	 * 
	 * @param object
	 */
	@Override
	public boolean removeSquareObject(Square2dObject object) {
		return this.removeSquareObject(object, true);
	}

	/**
	 * @param object
	 * @param notifyObserver
	 * @return true if object is removed
	 */
	public boolean removeSquareObject(Square2dObject object, boolean notifyObserver) {
		if (!this.getChildren().contains(object, true)) {
			return false;
		}
		if (notifyObserver) {
			this.notifyObservers(new RemoveObjectEvent(object, object));
		}
		super.removeActor(object);
		object.setSquare(null);
		object.setPosition(0, 0);
		this.objects.removeValue(object, true);
		this.removeSquareObserver(object);
		if (notifyObserver) {
			this.notifyObservers(new RemoveObjectEvent(object));
		}
		return true;

	}

	@Override
	public void addActor(Actor actor) {
		if (actor instanceof Square2dObject) {
			this.addSquareObject((Square2dObject) actor);
			return;
		}
		throw new RuntimeException("Non-sqaure2d-object is passed to square2d$addActor."); //$NON-NLS-1$
	}

	@Override
	public boolean removeActor(Actor actor) {
		if (actor instanceof Square2dObject) {
			return this.removeSquareObject((Square2dObject) actor);
		}
		throw new RuntimeException("Non-sqaure2d-object is passed to square2d$removeActor."); //$NON-NLS-1$
	}

	@Override
	public Iterable<Square2dObject> getObjects() {
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
	public Square2dSize getSquareSize() {
		return this.type.getSize();
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
		return this.type.isConcave();
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is contained in square of vertex1, vertex2,
	 *         vertex3 and vertex4
	 */
	public boolean containsInSquareArea(float x, float y) {
		if (this.isVertexPoint(x, y)) {
			return true;
		}
		float[] vertices = { this.vertex1.x, this.vertex1.y, this.vertex2.x, this.vertex2.y, this.vertex3.x, this.vertex3.y, this.vertex4.x, this.vertex4.y };
		Polygon p = new Polygon(vertices);
		return p.contains(x, y);
	}

	private boolean isVertexPoint(float x, float y) {
		return this.vertex1.equals(x, y) || this.vertex2.equals(x, y) || this.vertex3.equals(x, y) || this.vertex4.equals(x, y);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.type.getName());
		sb.append(this.vertex1).append("-").append(this.vertex2).append("-").append(this.vertex3).append("-") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.append(this.vertex4);
		return sb.toString();
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
		 * @param x
		 * @param y
		 * @return true if same x,y
		 */
		@SuppressWarnings("hiding")
		public boolean equals(float x, float y) {
			return x == this.x && y == this.y;
		}

		/**
		 * @param vertex
		 * @return true if same value
		 */
		public boolean equals(Vertex vertex) {
			return this.equals(vertex.x, vertex.y);
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
	public void notifyObservers(Square2dEvent event) {
		SquareObserver notifyTarget = event.getTargetObserver();
		if (notifyTarget != null && this.observers.contains(notifyTarget, true)) {
			notifyTarget.notify(event);
			return;
		}
		for (int i = 0; i < this.observers.size; i++) {
			if (event.getExceptObservers().contains(this.observers.get(i), true)) {
				continue;
			}
			this.observers.get(i).notify(event);
		}
	}
}
