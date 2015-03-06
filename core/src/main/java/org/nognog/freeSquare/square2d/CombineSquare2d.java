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
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi 2015/02/15
 */
public class CombineSquare2d extends Square2d {
	private Square2d base;
	private Array<Square2d> squares;
	private Array<Vertex> vertices;
	private ObjectMap<Vertex, CombinePoint> combinePoints;
	private float leftEndX, rightEndX;
	private float buttomEndY, topEndY;

	private transient Array<Square2d> separatableSquares;

	private boolean drawEdge;

	/**
	 * @param base
	 */
	public CombineSquare2d(Square2d base) {
		this.base = base;
		this.squares = new Array<>();
		this.addSquare(base, 0, 0);
		this.vertices = new Array<>();
		this.combinePoints = new ObjectMap<>();
		for (Vertex baseVertex : base.getVertices()) {
			final Vertex vertex = new Vertex(baseVertex);
			this.vertices.add(vertex);
			this.combinePoints.put(vertex, new CombinePoint(vertex, base, baseVertex));
		}
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
		return this.combinePoints.values().toArray().<CombinePoint> toArray(CombinePoint.class);
	}

	private void mergeCombinePoints(CombinePoint[] mergeCombinePoints) {
		for (CombinePoint mergeCombinePoint : mergeCombinePoints) {
			this.mergeCombinePoint(mergeCombinePoint);
		}
	}

	private void mergeCombinePoint(CombinePoint mergeCombinePoint) {
		for (Vertex alreadyExistActualVertex : this.combinePoints.keys()) {
			if (CombineSquare2dUtils.canBeRegardedAsSameVertex(mergeCombinePoint.actualVertex, alreadyExistActualVertex)) {
				CombinePoint alreadyExistsSameCombinePoint = this.combinePoints.get(alreadyExistActualVertex);
				alreadyExistsSameCombinePoint.addCombinedVertices(mergeCombinePoint.combinedVertices.<CombinedVertex> toArray(CombinedVertex.class));
				final int mergeCombinePointActualVertexIndex = this.vertices.indexOf(mergeCombinePoint.actualVertex, true);
				if (mergeCombinePointActualVertexIndex != -1) {
					this.vertices.set(mergeCombinePointActualVertexIndex, alreadyExistsSameCombinePoint.actualVertex);
				}
				return;
			}
		}
		this.combinePoints.put(mergeCombinePoint.actualVertex, mergeCombinePoint);
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
		this.normalizeVertices();
		this.mergeCombinePoints(combinedPoints.<CombinePoint> toArray(CombinePoint.class));
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
		return combineVertices(this.vertices, thisCombineVertex, targetSquare, targetCombineVertex);
	}

	private static Array<CombinePoint> combineVertices(Array<Vertex> dest, Vertex destCombineVertex, Square2d targetSquare, Vertex targetCombineVertex) {
		return combineVertices(dest, destCombineVertex, targetSquare, new Array<>(targetSquare.getVertices()), targetCombineVertex);
	}

	private static Array<CombinePoint> combineVertices(Array<Vertex> dest, Vertex destCombineVertex, Square2d targetSquare, Array<Vertex> target, Vertex targetCombineVertex) {
		Array<CombinePoint> newCombinePoints = new Array<>();
		Vertex[] beforeDestVertices = dest.<Vertex> toArray(Vertex.class);
		Vertex[] beforeTargetVertices = target.<Vertex> toArray(Vertex.class);
		final int verteciesInsertStartIndex = dest.indexOf(destCombineVertex, true) + 1;
		for (int i = 0; i < beforeTargetVertices.length; i++) {
			final int insertTargetSquareVertexIndex = (target.indexOf(targetCombineVertex, true) + 1 + i) % target.size;
			Vertex insertVertex = createAfterCombineTargetVertex(beforeTargetVertices[insertTargetSquareVertexIndex], destCombineVertex, targetCombineVertex);
			final float r = CombineSquare2dUtils.getDistanceToNearestEdge(insertVertex, beforeDestVertices);
			if (CombineSquare2dUtils.isSufficientlyCloseDistance(r)) {
				final Vertex sufficientlyCloseVertex = CombineSquare2dUtils.getSufficientlyCloseVertex(insertVertex, beforeDestVertices);
				if (sufficientlyCloseVertex != null) {
					insertVertex = new Vertex(sufficientlyCloseVertex);
				}
			}
			newCombinePoints.add(new CombinePoint(insertVertex, targetSquare, beforeTargetVertices[insertTargetSquareVertexIndex]));
			dest.insert(verteciesInsertStartIndex + i, insertVertex);
		}
		CombineSquare2dUtils.normalizeVertices(dest);
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
		combineVertices(simulatedAfterCombineVertices, thisCombineVertex, targetSquare, targetCombineVertex);
		CombineSquare2dUtils.normalizeVertices(simulatedAfterCombineVertices);
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
				if (CombineSquare2dUtils.canBeRegardedAsSameVertex(result[i], thisPolygonVertices[j])) {
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
		CombineSquare2dUtils.normalizeVertices(this.vertices);
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
		this.removeNoLongerRequiredCombinePoints(separateTarget);
		this.removeSquare(separateTarget);
		this.separatableSquares = null;
		return true;
	}

	private void removeNoLongerRequiredCombinePoints(Square2d separateTarget) {
		final CombinePoint[] separateTargetCombinePoints = this.getCombinePointOf(separateTarget);
		for (CombinePoint separateTargetCombinePoint : separateTargetCombinePoints) {
			separateTargetCombinePoint.removeCombinedVertex(separateTarget);
			if (separateTargetCombinePoint.combinedVertices.size == 0) {
				this.combinePoints.remove(separateTargetCombinePoint.actualVertex);
			}
		}
	}

	private boolean trySeparate(Square2d separateTarget) {
		if (separateTarget == this.base) {
			return false;
		}
		final CombinePoint[] separateTargetCombinePoints = this.getCombinePointOf(separateTarget);
		if (separateTargetCombinePoints.length == 0) {
			return false;
		}
		Vertex[] singleVertices = getSingleCombineVertices(separateTargetCombinePoints);
		if (singleVertices.length > 0) {
			return this.trySeparateProjectingSquare(separateTarget, singleVertices);

		}
		return this.trySeparateBuriedSquare(separateTarget);
	}

	private boolean trySeparateProjectingSquare(Square2d separateTarget, Vertex[] singleVertices) {
		final CombinePoint[] separateTargetCombinePoints = this.getCombinePointOf(separateTarget);
		Array<CombinePoint> recessedCombinePoints = new Array<>();
		for (CombinePoint combinePoint : separateTargetCombinePoints) {
			if (this.contains(combinePoint.actualVertex)) {
				continue;
			}
			final Edge onlineEdge = this.getOnlineEdgeOf(combinePoint.actualVertex);
			if (onlineEdge == null) {
				recessedCombinePoints.add(combinePoint);
			} else {
				final int onlineEdgeLargerIndex, onlineEdgeSmallerIndex;
				if (this.vertices.indexOf(onlineEdge.v1, true) < this.vertices.indexOf(onlineEdge.v2, true)) {
					onlineEdgeSmallerIndex = this.vertices.indexOf(onlineEdge.v1, true);
					onlineEdgeLargerIndex = this.vertices.indexOf(onlineEdge.v2, true);
				} else {
					onlineEdgeSmallerIndex = this.vertices.indexOf(onlineEdge.v2, true);
					onlineEdgeLargerIndex = this.vertices.indexOf(onlineEdge.v1, true);
				}
				final int insertVertex = (onlineEdgeLargerIndex - onlineEdgeSmallerIndex == 1) ? onlineEdgeLargerIndex : 0;
				this.vertices.insert(insertVertex, combinePoint.actualVertex);
			}
		}
		if (recessedCombinePoints.size >= 2) {
			// TODO unsupported yet.
			return false;
		}
		for (CombinePoint recessCombinePoint : recessedCombinePoints) {
			Vertex recessCombinePointVertex = recessCombinePoint.actualVertex;
			this.vertices.insert(this.vertices.indexOf(singleVertices[0], true), recessCombinePointVertex);
		}
		for (Vertex singleVertex : singleVertices) {
			this.vertices.removeValue(singleVertex, true);
		}
		this.normalizeVertices();
		if (this.isTwist()) {
			return false;
		}
		return true;
	}

	private boolean trySeparateBuriedSquare(Square2d separateTarget) {
		Array<Vertex> orderedSeparateTargetActualVertices = createOrderedActualVertices(separateTarget);
		if (orderedSeparateTargetActualVertices == null) {
			return false;
		}
		Array<Vertex> onlineSeparateTargetVertices = this.getOnlineVertices(orderedSeparateTargetActualVertices);
		final int insertIndex = this.getInsertIndexOfBuriedSquareVertices(orderedSeparateTargetActualVertices, onlineSeparateTargetVertices);
		if (insertIndex == -1) {
			return false;
		}
		final int addDirection = deciedAddDirection(orderedSeparateTargetActualVertices, onlineSeparateTargetVertices);
		for (int i = 0; i < orderedSeparateTargetActualVertices.size; i++) {
			int nextInsertVertexIndex = (orderedSeparateTargetActualVertices.indexOf(onlineSeparateTargetVertices.get(0), true) + i * addDirection + orderedSeparateTargetActualVertices.size)
					% orderedSeparateTargetActualVertices.size;
			Vertex nextInsertVertex = orderedSeparateTargetActualVertices.get(nextInsertVertexIndex);
			this.vertices.insert(insertIndex + i, nextInsertVertex);
		}
		if (this.isTwist()) {
			this.vertices.removeAll(orderedSeparateTargetActualVertices, true);
			for (int i = 0; i < orderedSeparateTargetActualVertices.size; i++) {
				int nextInsertVertexIndex = (orderedSeparateTargetActualVertices.indexOf(onlineSeparateTargetVertices.get(1), true) + i * (-addDirection) + orderedSeparateTargetActualVertices.size)
						% orderedSeparateTargetActualVertices.size;
				Vertex nextInsertVertex = orderedSeparateTargetActualVertices.get(nextInsertVertexIndex);
				this.vertices.insert(insertIndex + i, nextInsertVertex);
			}
		}
		this.normalizeVertices();
		if (this.isTwist()) {
			return false;
		}
		return true;
	}

	private static int deciedAddDirection(Array<Vertex> orderedSeparateTargetActualVertices, Array<Vertex> onlineSeparateTargetVertices) {
		final int validateIndex = (orderedSeparateTargetActualVertices.indexOf(onlineSeparateTargetVertices.get(0), true) + 1) % orderedSeparateTargetActualVertices.size;
		if ((orderedSeparateTargetActualVertices.get(validateIndex) == onlineSeparateTargetVertices.get(1))) {
			return -1;
		}
		return 1;
	}

	private int getInsertIndexOfBuriedSquareVertices(Array<Vertex> orderedSeparateTargetActualVertices, Array<Vertex> onlineSeparateTargetVertices) {
		final boolean targetIsHalfBuried = onlineSeparateTargetVertices.size == 2;
		if (!targetIsHalfBuried) {
			return -1;
		}
		if (!this.getOnlineEdgeOf(onlineSeparateTargetVertices.get(0)).equals(this.getOnlineEdgeOf(onlineSeparateTargetVertices.get(1)))) {
			return -1;
		}
		final Edge onlineEdge = this.getOnlineEdgeOf(onlineSeparateTargetVertices.get(0));
		final int onlineEdgeLargerIndex, onlineEdgeSmallerIndex;
		if (this.vertices.indexOf(onlineEdge.v1, true) < this.vertices.indexOf(onlineEdge.v2, true)) {
			onlineEdgeSmallerIndex = this.vertices.indexOf(onlineEdge.v1, true);
			onlineEdgeLargerIndex = this.vertices.indexOf(onlineEdge.v2, true);
		} else {
			onlineEdgeSmallerIndex = this.vertices.indexOf(onlineEdge.v2, true);
			onlineEdgeLargerIndex = this.vertices.indexOf(onlineEdge.v1, true);
		}
		final int indexDiff = onlineEdgeLargerIndex - onlineEdgeSmallerIndex;
		if (indexDiff != 1 && indexDiff != this.vertices.size - 1) {
			return -1;
		}
		if (indexDiff == 1) {
			return onlineEdgeLargerIndex;
		}
		return onlineEdgeSmallerIndex;
	}

	private Array<Vertex> getOnlineVertices(Array<Vertex> checkVertices) {
		final Array<Vertex> result = new Array<>();
		final Vertex[] searchVertices = this.getVertices();
		for (Vertex vertex : checkVertices) {
			if (CombineSquare2dUtils.canBeRegardedAsOnline(vertex, searchVertices)) {
				result.add(vertex);
			}
		}
		return result;
	}

	private Array<Vertex> createOrderedActualVertices(Square2d square) {
		final CombinePoint[] squareCombinePoints = this.getCombinePointOf(square);
		Array<Vertex> result = new Array<>();
		for (Vertex squareVertex : square.getVertices()) {
			for (CombinePoint combinePoint : squareCombinePoints) {
				if (combinePoint.getVertexOf(square) == squareVertex) {
					result.add(combinePoint.actualVertex);
					break;
				}
			}
		}
		if (result.size != square.getVertices().length) {
			return null;
		}
		return result;
	}

	private Edge getOnlineEdgeOf(Vertex vertex) {
		return CombineSquare2dUtils.getOnlineEdge(vertex, this.getVertices());
	}

	private static Vertex[] getSingleCombineVertices(final CombinePoint[] separateTargetCombinePoints) {
		Array<Vertex> result = new Array<>();
		for (CombinePoint combinePoint : separateTargetCombinePoints) {
			if (combinePoint.combinedVertices.size == 1) {
				result.add(combinePoint.actualVertex);
			}
		}
		return result.<Vertex> toArray(Vertex.class);
	}

	private boolean isTwist() {
		return Square2dUtils.isTwist(this);
	}

	private CombinePoint[] getCombinePointOf(Square2d square) {
		Array<CombinePoint> result = new Array<>();
		for (CombinePoint combinePoint : this.combinePoints.values()) {
			if (combinePoint.isCombine(square)) {
				result.add(combinePoint);
			}
		}
		return result.<CombinePoint> toArray(CombinePoint.class);
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
			if (!this.contains((Square2d) actor)) {
				return false;
			}
			boolean isSuccessSeparate = this.separate((Square2d) actor);
			if (!isSuccessSeparate) {
				throw new IllegalArgumentException(actor.getName() + "is not separable."); //$NON-NLS-1$
			}
			return true;
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
		public final Array<CombinedVertex> combinedVertices;

		CombinePoint(Vertex actualVertex, Square2d square, Vertex vertex) {
			this.actualVertex = actualVertex;
			this.combinedVertices = new Array<>();
			this.combinedVertices.add(new CombinedVertex(square, vertex));
		}

		public boolean isCombine(Square2d square) {
			return this.getVertexOf(square) != null;
		}

		public Vertex getVertexOf(Square2d square) {
			for (CombinedVertex combinedVertex : this.combinedVertices) {
				if (combinedVertex.square == square) {
					return combinedVertex.vertex;
				}
			}
			return null;
		}

		public void addCombinedVertex(Square2d square, Vertex vertex) {
			this.addCombinedVertex(new CombinedVertex(square, vertex));
		}

		public void addCombinedVertex(CombinedVertex combinedVertex) {
			if (this.getVertexOf(combinedVertex.square) != null) {
				throw new IllegalArgumentException();
			}
			this.combinedVertices.add(combinedVertex);
		}

		public void addCombinedVertices(CombinedVertex[] addCombinedVertices) {
			for (CombinedVertex addCombinedVertex : addCombinedVertices) {
				this.addCombinedVertex(addCombinedVertex);
			}
		}

		public void removeCombinedVertex(Square2d square) {
			for (CombinedVertex combinedVertex : this.combinedVertices) {
				if (combinedVertex.square == square) {
					this.combinedVertices.removeValue(combinedVertex, true);
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return sb.append(this.actualVertex).append(" | ").append(this.combinedVertices).toString(); //$NON-NLS-1$
		}
	}

	private static class CombinedVertex {
		public final Square2d square;
		public final Vertex vertex;

		CombinedVertex(Square2d square, Vertex vertex) {
			if (!square.contains(vertex)) {
				throw new IllegalArgumentException();
			}
			this.square = square;
			this.vertex = vertex;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			return sb.append(this.square).append(" ").append(this.vertex).toString(); //$NON-NLS-1$
		}
	}
}
