package org.nognog.freeSquare.square2d;

import java.util.Comparator;

import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.RemoveObjectEvent;
import org.nognog.freeSquare.square2d.event.UpdateObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.squares.Square2dType;
import org.nognog.freeSquare.square2d.ui.SquareObserver;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2014/12/10
 */
public class SimpleSquare2d extends Group implements Square2d, Json.Serializable {

	private Square2dType type;

	private Image squareImage;

	private Array<SquareObserver> observers;
	private Array<Square2dObject> objects;

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

	private SimpleSquare2d() {
		// used by json.
		this.observers = new Array<>();
		this.objects = new Array<>();
		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (SimpleSquare2d.this.containsInSquare(x, y) || this.isTouchingSquareObject(x, y)) {
					return false;
				}
				event.stop();
				return false;
			}

			private boolean isTouchingSquareObject(float x, float y) {
				Actor touchedActor = SimpleSquare2d.this.hit(x, y, true);
				return touchedActor != SimpleSquare2d.this.getSquareImage();
			}
		});
	}

	/**
	 * @param type
	 */
	public SimpleSquare2d(Square2dType type) {
		this();
		setupType(type);
	}

	protected void setupType(Square2dType type) {
		if (type == null) {
			return;
		}
		if (this.type != null) {
			throw new RuntimeException("type is already setted"); //$NON-NLS-1$
		}
		this.type = type;
		this.squareImage = createSquareImage(type);
		super.addActor(this.squareImage);
	}

	private static Image createSquareImage(Square2dType type) {
		Image image = new Image(type.getTexture());
		image.setWidth(type.getSize().getWidth());
		image.setHeight(image.getHeight() * (type.getSize().getWidth() / type.getTexture().getWidth()));
		image.setY(type.getSquarePositionOffsetY());
		image.setName(type.getName());
		return image;
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
	public float getImageWidth() {
		return this.getWidth();
	}

	@Override
	public float getImageHeight() {
		return this.getHeight();
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
		if (notifyObserver) {
			this.notifyObservers(new AddObjectEvent(object));
		}
		this.addSquareObserver(object);
		this.objects.add(object);
	}

	/**
	 * not notify observer
	 * 
	 * @param object
	 */
	@Override
	public boolean removeSquareObject(Square2dObject object) {
		this.objects.removeValue(object, true);
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
		this.removeSquareObserver(object);
		this.objects.removeValue(object, true);
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
	private void requestDrawOrderUpdate() {
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

	@Override
	public boolean containsInSquare(float x, float y) {
		if (this.isVertexPoint(x, y)) {
			return true;
		}
		Vertex vertex1 = this.type.vertex1;
		Vertex vertex2 = this.type.vertex2;
		Vertex vertex3 = this.type.vertex3;
		Vertex vertex4 = this.type.vertex4;
		float[] vertices = { vertex1.x, vertex1.y, vertex2.x, vertex2.y, vertex3.x, vertex3.y, vertex4.x, vertex4.y };
		Polygon p = new Polygon(vertices);
		return p.contains(x, y);
	}

	@Override
	public Vertex[] getVertices() {
		return new Vertex[] { this.type.vertex1, this.type.vertex2, this.type.vertex3, this.type.vertex4 };
	}
	
	/**
	 * @return vertex1
	 */
	public Vertex getVertex1(){
		return this.type.vertex1;
	}
	
	/**
	 * @return vertex1
	 */
	public Vertex getVertex2(){
		return this.type.vertex2;
	}
	
	/**
	 * @return vertex1
	 */
	public Vertex getVertex3(){
		return this.type.vertex3;
	}
	
	/**
	 * @return vertex1
	 */
	public Vertex getVertex4(){
		return this.type.vertex4;
	}

	private boolean isVertexPoint(float x, float y) {
		return this.type.vertex1.equals(x, y) || this.type.vertex2.equals(x, y) || this.type.vertex3.equals(x, y) || this.type.vertex4.equals(x, y);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.type.getName());
		sb.append(this.type.vertex1).append("-").append(this.type.vertex2).append("-").append(this.type.vertex3).append("-") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.append(this.type.vertex4);
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

	@Override
	public void notify(Square2dEvent event) {
		if (event instanceof UpdateObjectEvent) {
			this.requestDrawOrderUpdate();
		}
	}

	@Override
	public void write(Json json) {
		json.writeField(this, "type"); //$NON-NLS-1$
		json.writeField(this, "objects"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		Square2dType readType = json.readValue(Square2dType.class, jsonData.get("type")); //$NON-NLS-1$
		this.setupType(readType);
		Array<Square2dObject> readObjects = json.readValue(Array.class, jsonData.get("objects")); //$NON-NLS-1$
		for (Square2dObject object : readObjects) {
			this.addSquareObject(object, object.getX(), object.getY(), false);
		}
	}
}
