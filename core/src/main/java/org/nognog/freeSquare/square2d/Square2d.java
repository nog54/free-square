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

	private static final Comparator<Actor> actorComparator = new Comparator<Actor>() {
		
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
	public boolean containsInSquare(float x, float y) {
		if (this.isPointOfVertex(x, y)) {
			return true;
		}
		Vertex[] vertices = this.getVertices();
		float[] floatVertices = new float[vertices.length * 2];
		int i = 0;
		for (Vertex vertex : vertices) {
			floatVertices[i++] = vertex.x;
			floatVertices[i++] = vertex.y;
		}

		Polygon p = new Polygon(floatVertices);
		return p.contains(x, y);
	}

	private boolean isPointOfVertex(float x, float y) {
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
		throw new RuntimeException("Non-sqaure2d-object is passed to AbstractSquare2d$addActor."); //$NON-NLS-1$
	}

	protected void addActorForce(Actor actor) {
		super.addActor(actor);
	}

	protected void removeActorForce(Actor actor) {
		super.removeActor(actor);
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

	protected void requestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = true;
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
