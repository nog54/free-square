package org.nognog.freeSquare.square2d;

import java.util.Comparator;

import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.RemoveObjectEvent;
import org.nognog.freeSquare.square2d.event.UpdateObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/15
 */
public abstract class Square2d extends Group implements Square<Square2dObject> {

	protected Array<SquareObserver> observers = new Array<>();
	protected Array<Square2dObject> objects = new Array<>();

	private boolean isRequestedDrawOrderUpdate = false;

	protected static final Comparator<Actor> actorComparator = new Comparator<Actor>() {

		@Override
		public int compare(Actor actor1, Actor actor2) {
			boolean actor1IsSquareObject = actor1 instanceof Square2dObject;
			boolean actor2IsSquareObject = actor2 instanceof Square2dObject;
			if (actor1IsSquareObject && !actor2IsSquareObject) {
				return 1;
			}
			if (!actor1IsSquareObject && actor2IsSquareObject) {
				return -1;
			}

			if (actor1 instanceof Square2d && actor2 instanceof Square2d) {
				final Square2d square1 = (Square2d) actor1;
				final Square2d square2 = (Square2d) actor2;
				final float square1BottomVertexY = square1.getStageCoordinate().y + square1.getMostBottomVertex().y;
				final float square2ButtomVertexY = square2.getStageCoordinate().y + square2.getMostBottomVertex().y;
				if (square1BottomVertexY < square2ButtomVertexY) {
					return 1;
				} else if (square1BottomVertexY > square2ButtomVertexY) {
					return -1;
				}
				return 0;
			}
			final float squareObject1Y = actor1.getY() - actor1.getOriginY();
			final float squareObject2Y = actor2.getY() - actor2.getOriginY();
			if (squareObject1Y < squareObject2Y) {
				return 1;
			} else if (squareObject1Y > squareObject2Y) {
				return -1;
			}
			return 0;
		}
	};

	/**
	 * @param vertex
	 * @return true if contains vertex.
	 */
	public boolean contains(Vertex vertex) {
		return Square2dUtils.contains(this.getVertices(), vertex);
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is contained in Square.
	 */
	public boolean containsPosition(float x, float y) {
		if (this.isVertexPosition(x, y)) {
			return true;
		}
		final Polygon p = Square2dUtils.createPolygon(this.getVertices());
		return p.contains(x, y);
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is vertex position.
	 */
	public boolean isVertexPosition(float x, float y) {
		for (Vertex vertex : this.getVertices()) {
			if (vertex.equals(x, y)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final float getWidth() {
		return this.getRightEndX() - this.getLeftEndX();
	}

	@Override
	public final float getHeight() {
		return this.getTopEndY() - this.getButtomEndY();
	}

	/**
	 * @return square vertices.
	 */
	public abstract Vertex[] getVertices();

	/**
	 * @return x of left end
	 */
	public abstract float getLeftEndX();

	/**
	 * @return x of right end
	 */
	public abstract float getRightEndX();

	/**
	 * @return y of buttom end
	 */
	public abstract float getButtomEndY();

	/**
	 * @return y of top end
	 */
	public abstract float getTopEndY();

	/**
	 * @return most right vertex
	 */
	public Vertex getMostRightVertex() {
		Vertex[] vertices = this.getVertices();
		Vertex mostRightVertex = vertices[0];
		for (int i = 1; i < vertices.length; i++) {
			if (mostRightVertex.x < vertices[i].x) {
				mostRightVertex = vertices[i];
			}
		}
		return mostRightVertex;
	}

	/**
	 * @return most left vertex
	 */
	public Vertex getMostLeftVertex() {
		Vertex[] vertices = this.getVertices();
		Vertex mostLeftVertex = vertices[0];
		for (int i = 1; i < vertices.length; i++) {
			if (mostLeftVertex.x > vertices[i].x) {
				mostLeftVertex = vertices[i];
			}
		}
		return mostLeftVertex;
	}

	/**
	 * @return most top vertex
	 */
	public Vertex getMostTopVertex() {
		Vertex[] vertices = this.getVertices();
		Vertex mostTopVertex = vertices[0];
		for (int i = 1; i < vertices.length; i++) {
			if (mostTopVertex.y < vertices[i].y) {
				mostTopVertex = vertices[i];
			}
		}
		return mostTopVertex;
	}

	/**
	 * @return most bottom vertex
	 */
	public Vertex getMostBottomVertex() {
		Vertex[] vertices = this.getVertices();
		Vertex mostButtomVertex = vertices[0];
		for (int i = 1; i < vertices.length; i++) {
			if (mostButtomVertex.y > vertices[i].y) {
				mostButtomVertex = vertices[i];
			}
		}
		return mostButtomVertex;
	}

	protected Vector2 getStageCoordinate() {
		return this.localToStageCoordinates(new Vector2(0, 0));
	}

	/**
	 * @return stage coordinates vertices
	 */
	public Vector2[] getStageCoordinatesVertices() {
		Vertex[] vertices = this.getVertices();
		Vector2[] result = new Vector2[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			result[i] = this.localToStageCoordinates(new Vector2(vertices[i].x, vertices[i].y));
		}
		return result;
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
		super.addActor(actor);
	}

	/**
	 * @param actor
	 */
	public void addActorForce(Actor actor) {
		super.addActor(actor);
	}

	/**
	 * @param actor
	 */
	public void removeActorForce(Actor actor) {
		super.removeActor(actor);
	}

	@Override
	public boolean removeActor(Actor actor) {
		if (actor instanceof Square2dObject) {
			return this.removeSquareObject((Square2dObject) actor);
		}
		return super.removeActor(actor);
	}

	@Override
	public Iterable<Square2dObject> getObjects() {
		return new Array<>(this.objects);
	}

	/**
	 * search all over the stage
	 * 
	 * @return all landing object
	 */
	public Array<Square2dObject> getAllLandingSquareObjectsOnStage() {
		Array<Square2dObject> allSquare2dObject = getAllSquare2dObject(this.getStage().getRoot());
		Array<Square2dObject> result = new Array<>();
		for (Square2dObject object : allSquare2dObject) {
			Vector2 stageCoordinateObjectPosition = object.getParent().localToStageCoordinates(new Vector2(object.getX(), object.getY()));
			Vector2 thisCoordinateObjectPosition = this.stageToLocalCoordinates(stageCoordinateObjectPosition);
			if (this.containsPosition(thisCoordinateObjectPosition.x, thisCoordinateObjectPosition.y)) {
				result.add(object);
			}
		}
		return result;
	}

	private static Array<Square2dObject> getAllSquare2dObject(Actor actor) {
		Array<Square2dObject> result = new Array<>();
		if (actor instanceof Square2dObject) {
			result.add((Square2dObject) actor);
		} else if (actor instanceof Group) {
			for (Actor child : ((Group) actor).getChildren()) {
				result.addAll(getAllSquare2dObject(child));
			}
		}
		return result;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (this.isRequestedDrawOrderUpdate) {
			this.getChildren().sort(actorComparator);
			this.isRequestedDrawOrderUpdate = false;
		}
		super.draw(batch, parentAlpha);
	}

	@Override
	public void addSquareObserver(SquareObserver observer) {
		if (this.observers.contains(observer, true)) {
			return;
		}
		this.observers.add(observer);
	}

	/**
	 * @param addObservers
	 */
	public void addSquareObservers(SquareObserver... addObservers) {
		for (SquareObserver observer : addObservers) {
			this.addSquareObserver(observer);
		}
	}

	@Override
	public void removeSquareObserver(SquareObserver observer) {
		this.observers.removeValue(observer, true);
	}

	@Override
	public void notify(Square2dEvent event) {
		if (event instanceof UpdateObjectEvent) {
			this.requestDrawOrderUpdate();
		}
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

	protected void requestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = true;
	}

	/**
	 * @return vertices string
	 */
	public String toVerticesString() {
		StringBuilder sb = new StringBuilder();
		Vertex[] vertices = this.getVertices();
		sb.append(vertices[0]);
		for (int i = 1; i < vertices.length; i++) {
			sb.append("-").append(vertices[i]); //$NON-NLS-1$
		}
		return sb.toString();
	}

}
