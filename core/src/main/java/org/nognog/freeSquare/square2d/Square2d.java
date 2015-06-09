/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d;

import java.util.Arrays;
import java.util.Comparator;

import org.nognog.freeSquare.model.SimpleDrawable;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.square2d.event.AddObjectEvent;
import org.nognog.freeSquare.square2d.event.RemoveObjectEvent;
import org.nognog.freeSquare.square2d.event.UpdateSquareObjectEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/15
 */
public abstract class Square2d extends Group implements Square<Square2dObject>, SimpleDrawable {

	protected Array<Square2dObject> objects = new Array<>();
	protected Array<SquareEventListener> observers = new Array<>();

	private boolean drawEdge;

	private boolean isRequestedDrawOrderUpdate = false;

	// cache
	private transient Vector2 stageCoordinatesPosition;
	private transient Vector2[] stageCoordinatesVertices;

	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
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
				final Vector2 square1StageCoordinatePosition = square1.getStageCoordinates();
				final float square1BottomVertexY = square1StageCoordinatePosition.y + square1.getMostBottomVertex().y;
				final float square1TopVertexY = square1StageCoordinatePosition.y + square1.getMostTopVertex().y;
				final float square1MiddleY = (square1BottomVertexY + square1TopVertexY) / 2;

				final Vector2 square2StageCooridnatePosition = square2.getStageCoordinates();
				final float square2BottomVertexY = square2StageCooridnatePosition.y + square2.getMostBottomVertex().y;
				final float square2TopVertexY = square2StageCooridnatePosition.y + square2.getMostTopVertex().y;
				final float square2MiddleY = (square2BottomVertexY + square2TopVertexY) / 2;
				if (square1MiddleY < square2MiddleY) {
					return 1;
				} else if (square1MiddleY > square2MiddleY) {
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
	public boolean hasVertex(Vertex vertex) {
		return Square2dUtils.contains(this.getVertices(), vertex);
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is in square. (true even if (x, y) is on square
	 *         edge)
	 */
	public boolean contains(float x, float y) {
		final Polygon p = Square2dUtils.createPolygon(this.getVertices());
		final boolean contains = p.contains(x, y);
		if (contains) {
			return contains;
		}
		return this.isOnEdgePoint(x, y);
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is vertex point.
	 */
	public boolean isOnVertexPoint(float x, float y) {
		for (Vertex vertex : this.getVertices()) {
			if (vertex.equals(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if (x, y) is on square edge
	 */
	public boolean isOnEdgePoint(float x, float y) {
		Vertex[] vertices = this.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			final Vertex v1 = vertices[i];
			final Vertex v2 = vertices[(i + 1) % vertices.length];
			final Vertex leftVertex, rightVertex;
			if (v1.x < v2.x) {
				leftVertex = v1;
				rightVertex = v2;
			} else {
				leftVertex = v2;
				rightVertex = v1;
			}
			if (rightVertex.x < x || leftVertex.x > x) {
				continue;
			}
			final double a = ((double) rightVertex.y - leftVertex.y) / ((double) rightVertex.x - leftVertex.x);
			final double b = leftVertex.y - a * leftVertex.x;

			final float compareY = (float) (a * x + b);
			final float ulp = Math.ulp(compareY);

			if (compareY - ulp <= y && y <= compareY + ulp) {
				return true;
			}

		}
		return false;
	}

	/**
	 * @return x of center point
	 */
	public float getCenterX() {
		return (this.getRightEndX() + this.getLeftEndX()) / 2;
	}

	/**
	 * @return y of center point
	 */
	public float getCenterY() {
		return (this.getTopEndY() + this.getBottomEndY()) / 2;
	}

	@Override
	public final float getWidth() {
		return this.getRightEndX() - this.getLeftEndX();
	}

	@Override
	public final float getHeight() {
		return this.getTopEndY() - this.getBottomEndY();
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
	public abstract float getBottomEndY();

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
		final Vertex[] vertices = this.getVertices();
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
		final Vertex[] vertices = this.getVertices();
		Vertex mostBottomVertex = vertices[0];
		for (int i = 1; i < vertices.length; i++) {
			if (mostBottomVertex.y > vertices[i].y) {
				mostBottomVertex = vertices[i];
			}
		}
		return mostBottomVertex;
	}

	@Override
	protected void positionChanged() {
		super.positionChanged();
		this.stageCoordinatesPosition = null;
		this.stageCoordinatesVertices = null;
	}

	@Override
	protected void childrenChanged() {
		super.childrenChanged();
		this.stageCoordinatesPosition = null;
		this.stageCoordinatesVertices = null;
	}

	private void updateStageCoordinates() {
		this.stageCoordinatesPosition = this.localToStageCoordinates(new Vector2(0, 0));
		this.stageCoordinatesVertices = this.createStageCoordinateVertices();
	}

	private Vector2[] createStageCoordinateVertices() {
		Vertex[] vertices = this.getVertices();
		Vector2[] result = new Vector2[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			result[i] = this.localToStageCoordinates(new Vector2(vertices[i].x, vertices[i].y));
		}
		return result;
	}

	/**
	 * @return stage coorinate position
	 */
	public Vector2 getStageCoordinates() {
		if (this.stageCoordinatesPosition == null) {
			this.updateStageCoordinates();
		}
		return this.stageCoordinatesPosition;
	}

	/**
	 * @return stage coordinates vertices
	 */
	public Vector2[] getStageCoordinatesVertices() {
		if (this.stageCoordinatesVertices == null) {
			this.updateStageCoordinates();
		}
		return this.stageCoordinatesVertices;
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
	public Square2dObject[] getObjects() {
		return this.objects.<Square2dObject> toArray(Square2dObject.class);
	}

	/**
	 * @return sorted object array
	 */
	public Square2dObject[] getSortedObjects() {
		final Square2dObject[] result = this.getObjects();
		Arrays.sort(result, actorComparator);
		return result;
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
			if (this.contains(thisCoordinateObjectPosition.x, thisCoordinateObjectPosition.y)) {
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

		if (this.drawEdge) {
			this.drawEdge(batch);
		}
	}

	/**
	 * @param enable
	 */
	public void setDrawEdge(boolean enable) {
		this.drawEdge = enable;
	}

	/**
	 * @return true if draw edge
	 */
	public boolean isDrawEdge() {
		return this.drawEdge;
	}

	protected void drawEdge(Batch batch) {
		batch.end();
		synchronized (shapeRenderer) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

			final Vector2[] verticesPosition = this.getStageCoordinatesVertices();
			for (int i = 0; i < verticesPosition.length; i++) {
				Vector2 vertex1 = verticesPosition[i];
				Vector2 vertex2 = verticesPosition[(i + 1) % verticesPosition.length];
				shapeRenderer.line(vertex1.x, vertex1.y, vertex2.x, vertex2.y);
			}
			shapeRenderer.end();
		}
		batch.begin();
	}

	@Override
	public void addSquareObserver(SquareEventListener observer) {
		if (this.observers.contains(observer, true)) {
			return;
		}
		this.observers.add(observer);
	}

	/**
	 * @param addObservers
	 */
	public void addSquareObservers(SquareEventListener... addObservers) {
		for (SquareEventListener observer : addObservers) {
			this.addSquareObserver(observer);
		}
	}

	@Override
	public void removeSquareObserver(SquareEventListener observer) {
		this.observers.removeValue(observer, true);
	}

	@Override
	public void notify(SquareEvent event) {
		if (event instanceof UpdateSquareObjectEvent) {
			this.requestDrawOrderUpdate();
		}
	}

	@Override
	public void notifyObservers(SquareEvent event) {
		if (!(event instanceof Square2dEvent)) {
			return;
		}
		Square2dEvent square2dEvent = (Square2dEvent) event;
		SquareEventListener notifyTarget = square2dEvent.getTargetObserver();
		if (notifyTarget != null && this.observers.contains(notifyTarget, true)) {
			notifyTarget.notify(square2dEvent);
			return;
		}
		for (int i = 0; i < this.observers.size; i++) {
			if (square2dEvent.getExceptObservers().contains(this.observers.get(i), true)) {
				continue;
			}
			this.observers.get(i).notify(square2dEvent);
		}
	}

	protected void requestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = true;
	}

	protected void cancelRequestDrawOrderUpdate() {
		this.isRequestedDrawOrderUpdate = false;
	}

	protected boolean isRequestedDrawOrderUpdate() {
		return this.isRequestedDrawOrderUpdate;
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

	/**
	 * dispose
	 */
	public static void dispose() {
		shapeRenderer.dispose();
	}

}
