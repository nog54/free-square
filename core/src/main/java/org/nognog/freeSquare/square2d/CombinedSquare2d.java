package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/15
 */
public class CombinedSquare2d extends Square2d {
	private Square2d base;
	private Array<Square2d> squares;
	private Array<Vertex> vertices;
	private Array<CombinePoint> combinePoints;
	private float leftEndX, rightEndX;
	private float buttomEndY, topEndY;

	private transient Array<Square2d> separatableSquares;

	private boolean drawEdge;

	/**
	 * @param base
	 */
	public CombinedSquare2d(Square2d base) {
		this.base = base;
		this.squares = new Array<>();
		this.addSquare(base, 0, 0);
		this.vertices = new Array<>();
		for (Vertex baseVertex : base.getVertices()) {
			this.vertices.add(new Vertex(baseVertex));
		}
		this.combinePoints = new Array<>();
		this.leftEndX = base.getLeftEndX();
		this.rightEndX = base.getRightEndX();
		this.buttomEndY = base.getButtomEndY();
		this.topEndY = base.getTopEndY();
	}

	@Override
	public Vertex[] getVertices() {
		return this.vertices.<Vertex> toArray(Vertex.class);
	}

	/**
	 * @return combine points
	 */
	public CombinePoint[] getCombinePoints() {
		return this.combinePoints.<CombinePoint> toArray(CombinePoint.class);
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
		if (!this.isCombinableWith(thisCombineVertex, targetSquare, targetsCombineVertex)) {
			return false;
		}
		Array<CombinePoint> combinedPoints = this.combineVertices(thisCombineVertex, targetSquare, targetsCombineVertex);
		this.addCombinePoint(combinedPoints.<CombinePoint> toArray(CombinePoint.class));
		this.normalizeVertices();
		this.addSquare(targetSquare, thisCombineVertex.x - targetsCombineVertex.x, thisCombineVertex.y - targetsCombineVertex.y);
		this.separatableSquares = null;
		return true;
	}

	private boolean isCombinableWith(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetsCombineVertex) {
		if (!this.contains(thisCombineVertex)) {
			return false;
		}
		if (this.contains(targetSquare) || targetSquare == this) {
			return false;
		}
		if (!targetSquare.contains(targetsCombineVertex)) {
			return false;
		}
		if (!this.isValidEvenIfCombinedWith(thisCombineVertex, targetSquare, targetsCombineVertex)) {
			return false;
		}
		return true;
	}

	private Array<CombinePoint> combineVertices(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetCombineVertex) {
		return combineVertices(this, this.vertices, thisCombineVertex, targetSquare, targetSquare.getVertices(), targetCombineVertex);
	}

	private static Array<CombinePoint> combineVertices(Square2d destSquare, Array<Vertex> dest, Vertex destCombineVertex, Square2d targetSquare, Vertex[] target, Vertex targetCombineVertex) {
		return combineVertices(destSquare, dest, destCombineVertex, targetSquare, new Array<>(target), targetCombineVertex);
	}

	private static Array<CombinePoint> combineVertices(Square2d destSquare, Array<Vertex> dest, Vertex destCombineVertex, Square2d targetSquare, Array<Vertex> target, Vertex targetCombineVertex) {
		Array<CombinePoint> newCombinePoints = new Array<>();
		Vertex[] beforeDestVertices = dest.<Vertex> toArray(Vertex.class);
		Vertex[] beforeTargetVertices = target.<Vertex> toArray(Vertex.class);
		final int verteciesInsertStartIndex = dest.indexOf(destCombineVertex, true) + 1;
		for (int i = 0; i < beforeTargetVertices.length; i++) {
			final int insertTargetSquareVertexIndex = (target.indexOf(targetCombineVertex, true) + 1 + i) % target.size;
			Vertex insertVertex = createAfterCombineTargetVertex(beforeTargetVertices[insertTargetSquareVertexIndex], destCombineVertex, targetCombineVertex);
			for (int j = 0; j < beforeDestVertices.length; j++) {
				if (CombinedSquare2dUtils.canBeRegardedAsSameVertex(beforeDestVertices[j], insertVertex)) {
					insertVertex = new Vertex(beforeDestVertices[j]);
					newCombinePoints.add(new CombinePoint(destSquare, beforeDestVertices[j], targetSquare, beforeTargetVertices[insertTargetSquareVertexIndex], insertVertex));
					break;
				}
			}
			dest.insert(verteciesInsertStartIndex + i, insertVertex);
		}
		return newCombinePoints;
	}

	private void recalculateBorder() {
		this.recalculateLeftEndX();
		this.recalculateRightEndX();
		this.recalculateButtomEndY();
		this.recalculateTopEndY();
	}

	private boolean isValidEvenIfCombinedWith(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetCombineVertex) {
		final Vertex[] afterCombineTargetPolygonVertices = this.createAfterCombineTargetVertices(thisCombineVertex, targetSquare, targetCombineVertex);
		if (afterCombineTargetPolygonVertices == null) {
			return false;
		}

		Array<Vertex> simulatedAfterCombineVertices = new Array<>();
		simulatedAfterCombineVertices.addAll(this.getVertices());
		combineVertices(this, simulatedAfterCombineVertices, thisCombineVertex, targetSquare, targetSquare.getVertices(), targetCombineVertex);
		CombinedSquare2dUtils.normalizeVertices(simulatedAfterCombineVertices);
		if (Square2dUtils.isTwist(simulatedAfterCombineVertices.<Vertex> toArray(Vertex.class))) {
			return false;
		}
		return true;
	}

	private Vertex[] createAfterCombineTargetVertices(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetCombineVertex) {
		final Vertex[] thisPolygonVertices = this.vertices.<Vertex> toArray(Vertex.class);
		final Vertex[] result = new Vertex[targetSquare.getVertices().length];
		int sameVertexCounter = 0;
		for (int i = 0; i < result.length; i++) {
			result[i] = createAfterCombineTargetVertex(targetSquare.getVertices()[i], thisCombineVertex, targetCombineVertex);
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

	private static Vertex createAfterCombineTargetVertex(Vertex baseVertex, Vertex beCombinedVertex, Vertex combineVertex) {
		final float x = beCombinedVertex.x + (baseVertex.x - combineVertex.x);
		final float y = beCombinedVertex.y + (baseVertex.y - combineVertex.y);
		return new Vertex(x, y);
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
		this.recalculateBorder();
		this.requestDrawOrderUpdate();
	}

	private boolean removeSquare(Square2d square) {
		if (this.squares.removeValue(square, true) == false) {
			return false;
		}
		if (square == this.base) {
			this.base = null;
		}
		this.removeActorForce(square);
		this.requestDrawOrderUpdate();
		this.recalculateBorder();
		return true;
	}

	/**
	 * @param enable
	 */
	public void setDrawEdge(boolean enable) {
		this.drawEdge = enable;
	}

	private void normalizeVertices() {
		CombinedSquare2dUtils.normalizeVertices(this.vertices);
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

	/**
	 * @param separateTarget
	 * @return true if success.
	 */
	public boolean separate(Square2d separateTarget) {
		if (!this.contains(separateTarget)) {
			return false;
		}
		final Vertex[] rollbackVertices = this.vertices.<Vertex> toArray(Vertex.class);
		final boolean isSuccessSeparate = this.trySeparate(separateTarget);
		if (!isSuccessSeparate) {
			this.vertices.clear();
			this.vertices.addAll(rollbackVertices);
			return false;
		}
		final CombinePoint[] separateTargetCombinePoints = this.getCombinePointOf(separateTarget);
		for (CombinePoint separateTargetCombinePoint : separateTargetCombinePoints) {
			this.combinePoints.removeValue(separateTargetCombinePoint, true);
		}
		this.removeSquare(separateTarget);
		this.separatableSquares = null;
		return true;
	}

	private boolean trySeparate(Square2d separateTarget) {
		if (separateTarget == this.base) {
			return false;
		}
		final CombinePoint[] separateTargetCombinePoints = this.getCombinePointOf(separateTarget);
		if (separateTargetCombinePoints.length == 0) {
			return false;
		}
		Array<Vertex> addedCombinePointVertices = new Array<>();
		for (CombinePoint combinePoint : separateTargetCombinePoints) {
			try {
				Vertex addedCombinePointVertex = this.addCombinePointVertexIfRemoved(combinePoint);
				if (addedCombinePointVertex != null) {
					addedCombinePointVertices.add(addedCombinePointVertex);
				}
			} catch (IllegalStateException e) {
				return false;
			}
		}
		Vertex targetCombineVertex = separateTargetCombinePoints[0].getVertexOf(separateTarget);
		Vertex thisCombineVertex = separateTargetCombinePoints[0].getOtherOneOf(targetCombineVertex);
		Vertex[] afterCombineVertices = new Vertex[separateTarget.getVertices().length];
		for (int i = 0; i < afterCombineVertices.length; i++) {
			afterCombineVertices[i] = createAfterCombineTargetVertex(separateTarget.getVertices()[i], thisCombineVertex, targetCombineVertex);
		}
		this.removeVerticesExcept(afterCombineVertices, addedCombinePointVertices.<Vertex> toArray(Vertex.class));
		this.normalizeVertices();
		if (this.isTwist()) {
			return false;
		}
		return true;
	}

	/**
	 * @param square
	 * @return true if separatable square.
	 */
	public boolean isSeparatable(Square2d square) {
		if (!this.contains(square)) {
			return false;
		}
		if (this.separatableSquares != null) {
			return this.separatableSquares.contains(square, true);
		}
		final Vertex[] rollbackVertices = this.vertices.<Vertex> toArray(Vertex.class);
		final boolean isSuccessSeparate = this.trySeparate(square);
		this.vertices.clear();
		this.vertices.addAll(rollbackVertices);
		return isSuccessSeparate;
	}

	/**
	 * @return separatable squares.
	 */
	public Square2d[] getSeparatableSquares() {
		if (this.separatableSquares != null) {
			return this.separatableSquares.<Square2d> toArray(Square2d.class);
		}
		Array<Square2d> result = new Array<>();
		for (Square2d combinedSquare : this.squares) {
			if (this.isSeparatable(combinedSquare)) {
				result.add(combinedSquare);
			}
		}
		this.separatableSquares = result;
		return this.separatableSquares.<Square2d> toArray(Square2d.class);
	}

	private boolean isTwist() {
		return Square2dUtils.isTwist(this);
	}

	private CombinePoint[] getCombinePointOf(Square2d square) {
		Array<CombinePoint> result = new Array<>();
		for (CombinePoint combinePoint : this.combinePoints) {
			for (Vertex vertex : square.getVertices()) {
				if (combinePoint.contains(square, vertex)) {
					result.add(combinePoint);
					break;
				}
			}
		}
		return result.<CombinePoint> toArray(CombinePoint.class);
	}

	private Vertex addCombinePointVertexIfRemoved(CombinePoint combinePoint) {
		Vertex combinePointActualVertex = combinePoint.actualVertex;
		if (!this.contains(combinePointActualVertex)) {
			Vertex insertVertex = combinePoint.getVertexOf(this);
			final int insertIndex = CombinedSquare2dUtils.getUnchangeSquareAreaIndexEvenIfInsert(this.getVertices(), insertVertex);
			if (insertIndex == -1) {
				throw new IllegalStateException();
			}
			this.vertices.insert(insertIndex, insertVertex);
			return combinePointActualVertex;
		}
		return null;
	}

	private void removeVerticesExcept(Vertex[] removeVertices, Vertex[] exceptVertices) {
		for (Vertex removeVertex : removeVertices) {
			if (CombinedSquare2dUtils.existsSufficientlyCloseVertex(removeVertex, exceptVertices)) {
				continue;
			}
			for (Vertex thisVertex : this.vertices) {
				if (CombinedSquare2dUtils.canBeRegardedAsSameVertex(thisVertex, removeVertex)) {
					this.vertices.removeValue(thisVertex, true);
				}
			}
		}
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
			return this.removeSquare((Square2d) actor);
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

	/**
	 * @author goshi 2015/02/15
	 */
	@SuppressWarnings("javadoc")
	public static class CombinePoint {
		public final Vertex actualVertex;

		public final Square2d square1;
		public final Vertex vertex1;

		public final Square2d square2;
		public final Vertex vertex2;

		CombinePoint(Square2d square1, Vertex vertex1, Square2d square2, Vertex vertex2, Vertex actualVertex) {
			if (square1 == square2) {
				throw new RuntimeException("square1 must be different from square2"); //$NON-NLS-1$
			}
			this.square1 = square1;
			this.vertex1 = vertex1;
			this.square2 = square2;
			this.vertex2 = vertex2;
			this.actualVertex = actualVertex;
		}

		public boolean contains(Square2d square, Vertex checkVertex) {
			if (square == null || checkVertex == null) {
				return false;
			}
			return this.getVertexOf(square) == checkVertex;
		}

		public Vertex getOtherOneOf(Vertex vertex) {
			if (vertex == this.vertex1) {
				return this.vertex2;
			}
			if (vertex == this.vertex2) {
				return this.vertex1;
			}
			return null;
		}

		public Vertex getVertexOf(Square2d square) {
			if (square == this.square1) {
				return this.vertex1;
			}
			if (square == this.square2) {
				return this.vertex2;
			}
			return null;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CombinePoint) {
				CombinePoint compareObj = (CombinePoint) obj;
				if (this.actualVertex != compareObj.actualVertex) {
					return false;
				}
				if ((this.square1 == compareObj.square1 && this.vertex1 == compareObj.vertex1) && (this.square2 == compareObj.square2 && this.vertex2 == compareObj.vertex2)) {
					return true;
				}
				if ((this.square1 == compareObj.square2 && this.vertex1 == compareObj.vertex2) && (this.square2 == compareObj.square1 && this.vertex2 == compareObj.vertex1)) {
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
