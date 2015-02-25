package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/15
 */
public class CombinedSquare2d extends Square2d {
	private Array<Square2d> squares;
	private Array<Vertex> vertices;
	private Array<CombinePoint> combinePoints;
	private float leftEndX, rightEndX;
	private float buttomEndY, topEndY;

	private boolean drawEdge;

	/**
	 * @param base
	 */
	public CombinedSquare2d(Square2d base) {
		this.squares = new Array<>();
		this.addSquare(base, 0, 0);
		this.vertices = new Array<>();
		this.vertices.addAll(base.getVertices());
		this.combinePoints = new Array<>();
		this.leftEndX = base.getLeftEndX();
		this.rightEndX = base.getRightEndX();
		this.buttomEndY = base.getButtomEndY();
		this.topEndY = base.getTopEndY();
	}

	@Override
	public Array<Vertex> getVertices() {
		return new Array<>(this.vertices);
	}

	private void addCombinePoint(CombinePoint... points) {
		for (CombinePoint combinePoint : points) {
			if (this.combinePoints.contains(combinePoint, false)) {
				return;
			}
			this.combinePoints.add(combinePoint);
		}
	}

	@Override
	public float getLeftEndX() {
		return this.leftEndX;
	}

	@Override
	public float getRightEndX() {
		return this.rightEndX;
	}

	@Override
	public float getButtomEndY() {
		return this.buttomEndY;
	}

	@Override
	public float getTopEndY() {
		return this.topEndY;
	}

	private void recalculateLeftEndX() {
		float result = Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			final float squareLeftEndX = square.getLeftEndX();
			if (result > squareLeftEndX) {
				result = squareLeftEndX;
			}
		}
		this.leftEndX = result;
	}

	private void recalculateRightEndX() {
		float result = -Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (result < square.getRightEndX()) {
				result = square.getRightEndX();
			}
		}
		this.rightEndX = result;
	}

	private void recalculateButtomEndY() {
		float result = Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (result > square.getButtomEndY()) {
				result = square.getButtomEndY();
			}
		}
		this.buttomEndY = result;
	}

	private void recalculateTopEndY() {
		float result = -Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (result < square.getTopEndY()) {
				result = square.getTopEndY();
			}
		}
		this.topEndY = result;
	}

	/**
	 * @param thisCombineVertex
	 * @param targetSquare
	 * @param targetsCombineVertex
	 * @return true if success
	 */
	public boolean combine(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetsCombineVertex) {
		if (!this.vertices.contains(thisCombineVertex, true)) {
			return false;
		}
		if (this.contains(targetSquare)) {
			return false;
		}
		final Array<Vertex> targetSquareVertices = targetSquare.getVertices();
		if (!targetSquareVertices.contains(targetsCombineVertex, true)) {
			return false;
		}
		if (!this.isCombinableWith(thisCombineVertex, targetSquare, targetsCombineVertex)) {
			return false;
		}
		Array<CombinePoint> combinedPoints = this.combineVertices(thisCombineVertex, targetSquareVertices, targetsCombineVertex);
		this.addCombinePoint(combinedPoints.<CombinePoint> toArray(CombinePoint.class));
		this.normalizeVertices();
		this.addSquare(targetSquare, thisCombineVertex.x - targetsCombineVertex.x, thisCombineVertex.y - targetsCombineVertex.y);
		recalculateBorder();
		return true;
	}

	private Array<CombinePoint> combineVertices(Vertex thisCombineVertex, Array<Vertex> targetSquareVertices, Vertex targetsCombineVertex) {
		return combineVertices(this.vertices, thisCombineVertex, targetSquareVertices, targetsCombineVertex);
	}

	private static Array<CombinePoint> combineVertices(Array<Vertex> dest, Vertex destCombineVertex, Array<Vertex> target, Vertex targetCombineVertex) {
		Array<CombinePoint> combinePoints = new Array<>();
		Vertex[] destVertices = dest.<Vertex> toArray(Vertex.class);
		Vertex[] targetVertices = target.<Vertex> toArray(Vertex.class);
		final int verteciesInsertStartIndex = dest.indexOf(destCombineVertex, true) + 1;
		for (int i = 0; i < targetVertices.length; i++) {
			final int insertTargetSquareVertexIndex = (target.indexOf(targetCombineVertex, true) + 1 + i) % target.size;
			final float insertVertexX = destCombineVertex.x + (targetVertices[insertTargetSquareVertexIndex].x - targetCombineVertex.x);
			final float insertVertexY = destCombineVertex.y + (targetVertices[insertTargetSquareVertexIndex].y - targetCombineVertex.y);
			Vertex insertVertex = new Vertex(insertVertexX, insertVertexY);
			for (int j = 0; j < destVertices.length; j++) {
				if (CombinedSquare2dUtils.canBeRegardedAsSameVertex(destVertices[j], insertVertex)) {
					combinePoints.add(new CombinePoint(destVertices[j], targetVertices[insertTargetSquareVertexIndex]));
					insertVertex = new Vertex(destVertices[j]);
					break;
				}
			}
			dest.insert(verteciesInsertStartIndex + i, insertVertex);
		}
		return combinePoints;
	}

	private void recalculateBorder() {
		this.recalculateLeftEndX();
		this.recalculateRightEndX();
		this.recalculateButtomEndY();
		this.recalculateTopEndY();
	}

	private boolean isCombinableWith(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetCombineVertex) {
		final Vertex[] thisPolygonVertices = this.vertices.<Vertex> toArray(Vertex.class);
		final Vertex[] afterCombineTargetPolygonVertices = this.createAfterCombineTargetVertices(thisCombineVertex, targetSquare, targetCombineVertex);
		if (afterCombineTargetPolygonVertices == null) {
			return false;
		}

		Array<Vertex> simulatedAfterCombineVertices = new Array<>();
		simulatedAfterCombineVertices.addAll(this.getVertices());
		combineVertices(simulatedAfterCombineVertices, thisCombineVertex, targetSquare.getVertices(), targetCombineVertex);
		normalizeVertices(simulatedAfterCombineVertices);
		if (Square2dUtils.isTwist(simulatedAfterCombineVertices.<Vertex> toArray(Vertex.class))) {
			 return false;
		}
		// if (CombinedSquare2dUtils.countAdjacentPoint(thisPolygonVertices,
		// afterCombineTargetPolygonVertices) <= 1) {
		// return false;
		// }
		//
		// if
		// (CombinedSquare2dUtils.existsInvalidContainedVertex(thisPolygonVertices,
		// afterCombineTargetPolygonVertices)) {
		// return false;
		// }
		// if
		// (CombinedSquare2dUtils.existsInvalidContainedVertex(afterCombineTargetPolygonVertices,
		// thisPolygonVertices)) {
		// return false;
		// }
		return true;
	}

	private Vertex[] createAfterCombineTargetVertices(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetCombineVertex) {
		final Vertex[] thisPolygonVertices = this.vertices.<Vertex> toArray(Vertex.class);
		final Vertex[] result = new Vertex[targetSquare.getVertices().size];
		int sameVertexCounter = 0;
		for (int i = 0; i < result.length; i++) {
			final float x = thisCombineVertex.x + (targetSquare.getVertices().get(i).x - targetCombineVertex.x);
			final float y = thisCombineVertex.y + (targetSquare.getVertices().get(i).y - targetCombineVertex.y);
			result[i] = new Vertex(x, y);
			for (int j = 0; j < thisPolygonVertices.length; j++) {
				if (CombinedSquare2dUtils.canBeRegardedAsSameVertex(result[i], thisPolygonVertices[j])) {
					result[i] = new Vertex(thisPolygonVertices[j]);
					sameVertexCounter++;
					if (sameVertexCounter == Math.min(thisPolygonVertices.length, result.length)) {
						if (largerVertexPolygonContainsSmallerVertexPolygonCentroid(thisPolygonVertices, result)) {
							return null;
						}
					}
					break;
				}
			}
		}
		return result;
	}

	private static boolean largerVertexPolygonContainsSmallerVertexPolygonCentroid(final Vertex[] vertices1, final Vertex[] vertices2) {
		if (vertices1.length == vertices2.length) {
			return true;
		}
		final Vertex[] smallerVerticesPolygon, largetVerticesPolygon;
		if (vertices1.length < vertices2.length) {
			smallerVerticesPolygon = vertices1;
			largetVerticesPolygon = vertices2;
		} else {
			smallerVerticesPolygon = vertices2;
			largetVerticesPolygon = vertices1;
		}
		final float[] smallerVerticesArray = Square2dUtils.toFloatArray(smallerVerticesPolygon);
		final Vector2 smallerVerticesPolygonCentroid = new Vector2();
		GeometryUtils.polygonCentroid(smallerVerticesArray, 0, smallerVerticesArray.length, smallerVerticesPolygonCentroid);
		return Square2dUtils.createPolygon(largetVerticesPolygon).contains(smallerVerticesPolygonCentroid.x, smallerVerticesPolygonCentroid.y);
	}

	private void addSquare(Square2d square, float x, float y) {
		square.setPosition(x, y);
		for (Square2dObject object : square.objects) {
			square.removeSquareObject(object, false);
			this.addSquareObject(object, x + object.getX(), y + object.getY(), false);
		}
		this.squares.add(square);
		this.addActorForce(square);
		this.addSquareObservers(square.observers.<SquareObserver> toArray(SquareObserver.class));
		this.requestDrawOrderUpdate();
	}

	/**
	 * @param enable
	 */
	public void setDrawEdge(boolean enable) {
		this.drawEdge = enable;
	}

	private void normalizeVertices() {
		normalizeVertices(this.vertices);
	}

	private static void normalizeVertices(Array<Vertex> vertices) {
		Vertex[] verticesArray = vertices.toArray(Vertex.class);
		Vertex[][] commonVertex = CombinedSquare2dUtils.getCommonVertex(verticesArray);
		for (int i = 0; i < verticesArray.length; i++) {
			if (commonVertex[i].length == 0 || !vertices.contains(verticesArray[i], true)) {
				continue;
			}
			Vertex[] removeCandidateVertices = new Vertex[commonVertex[i].length + 1];
			removeCandidateVertices[0] = verticesArray[i];
			System.arraycopy(commonVertex[i], 0, removeCandidateVertices, 1, commonVertex[i].length);
			if (hasSameAreaEvenIfRemoveVertices(verticesArray, removeCandidateVertices)) {
				for (int j = 0; j < removeCandidateVertices.length; j++) {
					vertices.removeValue(removeCandidateVertices[j], true);
				}
			}
		}
		verticesArray = vertices.toArray(Vertex.class);
		for (Vertex vertex : verticesArray) {
			if (hasSameAreaEvenIfRemoveVertices(verticesArray, vertex)) {
				vertices.removeValue(vertex, true);
			}
		}
	}

	private boolean hasSameAreaEvenIfRemoveVertices(Vertex... removeVertices) {
		return hasSameAreaEvenIfRemoveVertices(this.vertices.<Vertex> toArray(Vertex.class), removeVertices);
	}

	private static boolean hasSameAreaEvenIfRemoveVertices(Vertex[] vertices, Vertex... removeVertices) {
		float beforeArea = Math.abs(Square2dUtils.createPolygon(vertices).area());
		float afterArea = Math.abs(Square2dUtils.createPolygon(vertices, removeVertices).area());
		float relativeError = Math.abs(afterArea - beforeArea) / beforeArea;
		final float permissibleRelativeError = 1 / 100f;
		return relativeError < permissibleRelativeError;
	}

	private Polygon getVertexPolygon() {
		return this.getVertexPolygonExcept();
	}

	private Polygon getVertexPolygonExcept(Vertex... exceptVertices) {
		return Square2dUtils.createPolygon(this.vertices.<Vertex> toArray(Vertex.class), exceptVertices);
	}

	/**
	 * @param square
	 * @return true if contains argument square
	 */
	public boolean contains(Square2d square) {
		if (this.squares.contains(square, true)) {
			return true;
		}
		return false;
	}

	public boolean separate(Square2d separateTarget) {
		if (!this.contains(separateTarget)) {
			return false;
		}
		this.squares.removeValue(separateTarget, true);
		// this
		return true;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (this.drawEdge) {
			batch.end();
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
			shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			for (int i = 0; i < this.vertices.size; i++) {
				Vertex vertex1 = this.vertices.get(i);
				Vertex vertex2 = this.vertices.get((i + 1) % this.vertices.size);
				shapeRenderer.line(vertex1.x, vertex1.y, vertex2.x, vertex2.y);
			}
			shapeRenderer.end();
			shapeRenderer.dispose();
			batch.begin();
		}
	}

	@Override
	public boolean removeActor(Actor actor) {
		if (actor instanceof Square2dObject) {
			return this.removeSquareObject((Square2dObject) actor);
		}
		if (actor instanceof Square2d) {
			if (this.separate((Square2d) actor)) {
				return true;
			}
		}
		throw new RuntimeException("Invalid argument is passed to CombinedSquare$removeActor."); //$NON-NLS-1$
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.vertices.get(0));
		for (int i = 1; i < this.vertices.size; i++) {
			sb.append("-").append(this.vertices.get(i)); //$NON-NLS-1$
		}
		return sb.toString();
	}

	private static class CombinePoint {
		public final Vertex vertex1;
		public final Vertex vertex2;

		CombinePoint(Vertex vertex1, Vertex vertex2) {
			this.vertex1 = vertex1;
			this.vertex2 = vertex2;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CombinePoint) {
				if (this.vertex1 == ((CombinePoint) obj).vertex1 && this.vertex2 == ((CombinePoint) obj).vertex2) {
					return true;
				}
				if (this.vertex1 == ((CombinePoint) obj).vertex2 && this.vertex2 == ((CombinePoint) obj).vertex2) {
					return true;
				}
				return false;
			}
			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return sb.append("[").append(this.vertex1).append(", ").append(this.vertex2).append("]").toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}
}
