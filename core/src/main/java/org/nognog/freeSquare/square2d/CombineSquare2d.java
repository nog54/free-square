package org.nognog.freeSquare.square2d;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.nognog.freeSquare.model.persist.PersistManager;
import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.CombineInfo.ReconstructCombineInfo;
import org.nognog.freeSquare.square2d.CombinePoint.CombinedVertex;
import org.nognog.freeSquare.square2d.event.UpdateSquareEvent;
import org.nognog.freeSquare.square2d.object.LandObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi 2015/02/15
 */
public class CombineSquare2d extends Square2d {
	static {
		CombineSquare2d.CombineSquare2dSetUpper.setup();
	}

	private Square2d base;
	private Array<Square2d> squares;
	private Array<Vertex> vertices;
	private ObjectMap<Vertex, CombinePoint> combinePoints;
	private float leftEndX, rightEndX;
	private float buttomEndY, topEndY;

	private CombineInfo combineInfo;

	private transient Array<Square2d> separatableIfNotExistsObjectSquares;
	private transient Array<SimpleSquare2d> allChildSimpleSquare2d;
	private transient Texture simpleTexture;

	private boolean drawEdge;

	private final ExecutorService createSimpleTextureBaseThreadPool = Executors.newFixedThreadPool(1);
	private Future<Pixmap> simpleTextureBaseFuture;

	/**
	 * @param base
	 */
	public CombineSquare2d(Square2d base) {
		this.base = base;
		this.squares = new Array<>();
		this.setPosition(base.getX(), base.getY());
		this.addSquare(base, 0, 0);
		this.vertices = new Array<>();
		this.combinePoints = new ObjectMap<>();
		for (Vertex baseVertex : base.getVertices()) {
			final Vertex vertex = new Vertex(baseVertex);
			this.vertices.add(vertex);
			this.combinePoints.put(vertex, new CombinePoint(vertex, base, baseVertex));
		}
		this.setName(base.getName());
		this.combineInfo = new CombineInfo();
		this.calculateBorder();
		for (Square2dObject object : base.getObjects()) {
			this.addSquareObject(object, object.getX(), object.getY(), false);
		}
		this.setupSeparatableSquaresList();
	}

	@Override
	public Vertex[] getVertices() {
		return this.vertices.<Vertex> toArray(Vertex.class);
	}

	/**
	 * @return squares
	 */
	public Square2d[] getSquares() {
		return this.squares.<Square2d> toArray(Square2d.class);
	}
	
	@Override
	protected void positionChanged() {
		super.positionChanged();
		this.calculateBorder();
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
	public float getBottomEndY() {
		return this.buttomEndY;
	}

	@Override
	public float getTopEndY() {
		return this.topEndY;
	}

	private void recalculateLeftEndX() {
		float childSquareMaxMinEndX = Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (childSquareMaxMinEndX > square.getLeftEndX()) {
				childSquareMaxMinEndX = square.getLeftEndX();
			}
		}
		this.leftEndX = childSquareMaxMinEndX;
	}

	private void recalculateRightEndX() {
		float childSquareMaxRightEndX = -Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (childSquareMaxRightEndX < square.getRightEndX()) {
				childSquareMaxRightEndX = square.getRightEndX();
			}
		}
		this.rightEndX = childSquareMaxRightEndX;
	}

	private void recalculateButtomEndY() {
		float childSquareMinButtomEndY = Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (childSquareMinButtomEndY > square.getBottomEndY()) {
				childSquareMinButtomEndY = square.getBottomEndY();
			}
		}
		this.buttomEndY = childSquareMinButtomEndY;
	}

	private void recalculateTopEndY() {
		float childSquareMaxTopEndY = -Float.MAX_VALUE;
		for (Square2d square : this.squares) {
			if (childSquareMaxTopEndY < square.getTopEndY()) {
				childSquareMaxTopEndY = square.getTopEndY();
			}
		}
		this.topEndY = childSquareMaxTopEndY;
	}

	@Override
	public Texture getSimpleTexture() {
		if (this.simpleTexture == null) {
			if (this.simpleTextureBaseFuture != null && this.simpleTextureBaseFuture.isDone()) {
				try {
					this.simpleTexture = new Texture(this.simpleTextureBaseFuture.get());
					this.simpleTextureBaseFuture = null;
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return this.simpleTexture;
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
		this.combineInfo.addCombineInfo(this, thisCombineVertex, targetSquare, targetsCombineVertex);
		Array<CombinePoint> combinedPoints = this.combineVertices(thisCombineVertex, targetSquare, targetsCombineVertex);
		this.normalizeVertices();
		this.mergeCombinePoints(combinedPoints.<CombinePoint> toArray(CombinePoint.class));
		this.addSquare(targetSquare, thisCombineVertex.x - targetsCombineVertex.x, thisCombineVertex.y - targetsCombineVertex.y);
		this.setupSeparatableSquaresList();
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

	private void calculateBorder() {
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
		final float squarePositionDiffX = x - this.getX();
		final float squarePositionDiffY = y - this.getY();
		square.setPosition(x, y);
		this.squares.add(square);
		this.addActorForce(square);
		for (Square2dObject object : square.getObjects()) {
			square.removeSquareObject(object, false);
			this.addSquareObject(object, object.getX() + squarePositionDiffX, object.getY() + squarePositionDiffY, false);
		}
		this.addSquareObservers(square.observers.<SquareObserver> toArray(SquareObserver.class));
		this.calculateBorder();
		this.requestDrawOrderUpdate();
		this.resetAllSimpleSquare2d();
	}

	private boolean removeSquare(Square2d square) {
		if (this.squares.removeValue(square, true) == false) {
			return false;
		}
		this.removeActorForce(square);
		this.requestDrawOrderUpdate();
		this.calculateBorder();
		this.resetAllSimpleSquare2d();
		return true;
	}

	@Override
	public void addActorForce(Actor actor) {
		super.addActorForce(actor);
		if (actor instanceof Square2d) {
			this.resetAllSimpleSquare2d();
		}
	}

	@Override
	public void removeActorForce(Actor actor) {
		super.removeActorForce(actor);
		if (actor instanceof Square2d) {
			this.resetAllSimpleSquare2d();
		}
	}

	private void resetAllSimpleSquare2d() {
		this.allChildSimpleSquare2d = new Array<>();
		for (Actor child : this.getChildren()) {
			if (child instanceof SimpleSquare2d) {
				this.allChildSimpleSquare2d.add((SimpleSquare2d) child);
			} else if (child instanceof CombineSquare2d) {
				this.allChildSimpleSquare2d.addAll(((CombineSquare2d) child).allChildSimpleSquare2d);
			}
		}
		if (this.simpleTexture != null) {
			this.simpleTexture.dispose();
		}
		if (this.simpleTextureBaseFuture != null && !this.simpleTextureBaseFuture.isDone()) {
			this.simpleTextureBaseFuture.cancel(true);
		}
		this.simpleTexture = null;
		this.simpleTextureBaseFuture = null;

		this.startCreateSimpleTextureBaseThreadIfNotStart();
	}

	private void startCreateSimpleTextureBaseThreadIfNotStart() {
		if (this.simpleTextureBaseFuture != null && !this.simpleTextureBaseFuture.isDone()) {
			return;
		}

		this.simpleTextureBaseFuture = this.createSimpleTextureBaseThreadPool.submit(new Callable<Pixmap>() {
			@Override
			public Pixmap call() {
				final CombineSquare2d target = CombineSquare2d.this;

				final Pixmap pixmap = new Pixmap((int) (target.getWidth()), (int) (target.getHeight()), Pixmap.Format.RGBA8888);
				SimpleSquare2d[] simpleSquares = getOrderedSimpleSquare2dArray();
				for (SimpleSquare2d square : simpleSquares) {
					if (Thread.currentThread().isInterrupted()) {
						pixmap.dispose();
						return null;
					}

					final TextureData textureData = square.getSquare2dType().getTexture().getTextureData();
					if (!textureData.isPrepared()) {
						textureData.prepare();
					}
					final Pixmap squarePixmap = textureData.consumePixmap();

					final Vector2 stageCoordinateSquarePosition = square.getStageCoordinate();
					final int x = (int) (stageCoordinateSquarePosition.x - target.getLeftEndX());
					final int y = pixmap.getHeight() - (int) (stageCoordinateSquarePosition.y - target.getBottomEndY() + square.getHeight());

					pixmap.drawPixmap(squarePixmap, 0, 0, squarePixmap.getWidth(), squarePixmap.getHeight(), x, y, (int) square.getWidth(), (int) square.getHeight());
				}
				return pixmap;
			}

		});
	}

	SimpleSquare2d[] getOrderedSimpleSquare2dArray() {
		final SimpleSquare2d[] simpleSquares = this.allChildSimpleSquare2d.<SimpleSquare2d> toArray(SimpleSquare2d.class);
		Arrays.sort(simpleSquares, actorComparator);
		return simpleSquares;
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
		if (this.getLandingSquareObjectsOn(separateTarget).size != 0) {
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
		this.combineInfo.removeCombineInfo(separateTarget);
		this.setupSeparatableSquaresList();
		this.notifyObservers(new UpdateSquareEvent(this));
		return true;
	}

	private Array<Square2dObject> getLandingSquareObjectsOn(Square2d square) {
		if (!this.squares.contains(square, true)) {
			throw new IllegalArgumentException();
		}
		final Array<Square2dObject> result = new Array<>();
		for (Square2dObject object : this.objects) {
			if (!(object instanceof LandObject)) {
				continue;
			}
			if (square.containsPosition(object.getX() - square.getX(), object.getY() - square.getY())) {
				result.add(object);
			}
		}
		return result;
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
		if (this.validateRemoveCombinePoints(separateTarget) == false) {
			return false;
		}
		Vertex[] singleVertices = getSingleCombineVertices(separateTargetCombinePoints);
		if (singleVertices.length > 0) {
			return this.trySeparateProjectingSquare(separateTarget, singleVertices);

		}
		return this.trySeparateBuriedSquare(separateTarget);
	}

	private boolean validateRemoveCombinePoints(Square2d separateTarget) {
		if (this.squares.contains(separateTarget, true) && this.squares.size == 2) {
			return true;
		}
		for (Square2d validateSquare : this.squares) {
			if (validateSquare == separateTarget) {
				continue;
			}
			final Vertex[] validateSquareActualVertices = this.getOrderedActualVertices(validateSquare).<Vertex> toArray(Vertex.class);
			final Array<Square2d> compareSquares = new Array<>(this.squares);
			compareSquares.removeValue(validateSquare, true);
			compareSquares.removeValue(separateTarget, true);
			boolean combinedWithOtherSquare = false;
			int closeToOtherSquareVertexCount = 0;
			for (CombinePoint combinePoint : this.combinePoints.values().toArray()) {
				if (combinePoint.contains(validateSquare)) {
					final int afterRemoveCombineVerticesCount = (combinePoint.contains(separateTarget)) ? combinePoint.combinedVertices.size - 1 : combinePoint.combinedVertices.size;
					if (afterRemoveCombineVerticesCount != 1) {
						combinedWithOtherSquare = true;
						closeToOtherSquareVertexCount++;
					} else {
						for (Square2d compareSquare : compareSquares) {
							final Vertex[] compareSquareActualVertices = this.getOrderedActualVertices(compareSquare).<Vertex> toArray(Vertex.class);
							if (CombineSquare2dUtils.getNearestSufficientlyCloseEdge(combinePoint.actualVertex, compareSquareActualVertices) != null) {
								closeToOtherSquareVertexCount++;
							}
						}
					}
				} else {
					if (combinePoint.contains(separateTarget) && combinePoint.combinedVertices.size == 1) {
						continue;
					}
					if (CombineSquare2dUtils.getNearestSufficientlyCloseEdge(combinePoint.actualVertex, validateSquareActualVertices) != null) {
						closeToOtherSquareVertexCount++;
					}
				}
			}
			if (!combinedWithOtherSquare) {
				return false;
			}
			if (closeToOtherSquareVertexCount < 2) {
				return false;
			}
		}
		return true;
	}

	private boolean trySeparateProjectingSquare(Square2d separateTarget, Vertex[] singleVertices) {
		final CombinePoint[] separateTargetCombinePoints = this.getCombinePointOf(separateTarget);
		Array<CombinePoint> recessedCombinePoints = new Array<>();
		for (CombinePoint combinePoint : separateTargetCombinePoints) {
			if (this.contains(combinePoint.actualVertex)) {
				continue;
			}
			final Edge onlineEdge = this.getNearestSufficientlyCloseEdgeOf(combinePoint.actualVertex);
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
		Array<Vertex> orderedSeparateTargetActualVertices = getOrderedActualVertices(separateTarget);
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
			final int nextInsertVertexIndex = (orderedSeparateTargetActualVertices.indexOf(onlineSeparateTargetVertices.get(0), true) + i * addDirection + orderedSeparateTargetActualVertices.size)
					% orderedSeparateTargetActualVertices.size;
			Vertex nextInsertVertex = orderedSeparateTargetActualVertices.get(nextInsertVertexIndex);
			this.vertices.insert(insertIndex + i, nextInsertVertex);
		}
		if (this.isTwist()) {
			this.vertices.removeAll(orderedSeparateTargetActualVertices, true);

			for (int i = 0; i < orderedSeparateTargetActualVertices.size; i++) {
				final int nextInsertVertexIndex = (orderedSeparateTargetActualVertices.indexOf(onlineSeparateTargetVertices.get(onlineSeparateTargetVertices.size - 1), true) + i * (-addDirection) + orderedSeparateTargetActualVertices.size)
						% orderedSeparateTargetActualVertices.size;
				final Vertex nextInsertVertex = orderedSeparateTargetActualVertices.get(nextInsertVertexIndex);
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
		if (onlineSeparateTargetVertices.size == 1) {
			return 1;
		}
		final int validateIndex = (orderedSeparateTargetActualVertices.indexOf(onlineSeparateTargetVertices.get(0), true) + 1) % orderedSeparateTargetActualVertices.size;
		if ((orderedSeparateTargetActualVertices.get(validateIndex) == onlineSeparateTargetVertices.get(1))) {
			return -1;
		}
		return 1;
	}

	private int getInsertIndexOfBuriedSquareVertices(Array<Vertex> orderedSeparateTargetActualVertices, Array<Vertex> onlineSeparateTargetVertices) {
		final boolean targetIsHalfBuried = onlineSeparateTargetVertices.size == 1 || onlineSeparateTargetVertices.size == 2;
		if (!targetIsHalfBuried) {
			return -1;
		}

		final Edge onlineEdge;
		if (onlineSeparateTargetVertices.size == 2) {
			onlineEdge = this.getSameOnlineEdge(onlineSeparateTargetVertices.get(0), onlineSeparateTargetVertices.get(1));
		} else {
			onlineEdge = this.getNearestSufficientlyCloseEdgeOf(onlineSeparateTargetVertices.get(0));
		}
		if (onlineEdge == null) {
			return -1;
		}
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

	private Array<Vertex> getOrderedActualVertices(Square2d square) {
		final CombinePoint[] squareCombinePoints = this.getCombinePointOf(square);
		final Array<Vertex> result = new Array<>();
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

	private Edge getNearestSufficientlyCloseEdgeOf(Vertex vertex) {
		return CombineSquare2dUtils.getNearestSufficientlyCloseEdge(vertex, this.getVertices());
	}

	private Edge getSameOnlineEdge(Vertex vertex1, Vertex vertex2) {
		final Vertex[] searchVertices = this.getVertices();
		final Edge[] vertex1OnliseEdges = CombineSquare2dUtils.getOnlineEdge(vertex1, searchVertices);
		final Edge[] vertex2OnliseEdges = CombineSquare2dUtils.getOnlineEdge(vertex2, searchVertices);
		for (Edge vertex1OnlineEdge : vertex1OnliseEdges) {
			for (Edge vertex2OnlineEdge : vertex2OnliseEdges) {
				if (vertex1OnlineEdge.equals(vertex2OnlineEdge)) {
					return vertex1OnlineEdge;
				}
			}
		}
		return null;
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

	private CombinePoint[] getCombinePointOf(Square2d square) {
		Array<CombinePoint> result = new Array<>();
		for (CombinePoint combinePoint : this.combinePoints.values()) {
			if (combinePoint.contains(square)) {
				result.add(combinePoint);
			}
		}
		return result.<CombinePoint> toArray(CombinePoint.class);
	}

	private boolean isTwist() {
		return Square2dUtils.isTwist(this);
	}

	/**
	 * @param square
	 * @return true if separatable square.
	 */
	public boolean isSeparatable(Square2d square) {
		if (!this.contains(square)) {
			return false;
		}
		if (this.separatableIfNotExistsObjectSquares != null) {
			return this.separatableIfNotExistsObjectSquares.contains(square, true);
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
		return this.separatableIfNotExistsObjectSquares.<Square2d> toArray(Square2d.class);
	}

	private void setupSeparatableSquaresList() {
		this.separatableIfNotExistsObjectSquares = null;
		final Array<Square2d> result = new Array<>();
		for (Square2d combinedSquare : this.squares.<Square2d> toArray(Square2d.class)) {
			if (this.isSeparatable(combinedSquare)) {
				result.add(combinedSquare);
			}
		}
		this.separatableIfNotExistsObjectSquares = result;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (this.getParent() != this.getStage().getRoot()) {
			return;
		}
		final boolean oldVisible = this.isVisible();
		this.setVisible(false);
		final float alpha = parentAlpha * this.getColor().a * this.getColor().a;
		super.draw(batch, alpha);
		this.setVisible(oldVisible);

		this.drawChildSimpleSquares(batch, alpha);
		this.drawSquare2dObjects(batch, alpha);

		if (this.drawEdge) {
			this.drawEdge(batch);
		}
	}

	private void drawChildSimpleSquares(Batch batch, float parentAlpha) {
		SimpleSquare2d[] simpleSquares = getOrderedSimpleSquare2dArray();
		for (SimpleSquare2d square : simpleSquares) {
			final float oldX = square.getX();
			final float oldY = square.getY();
			final Vector2 stageCoordinateSquarePosition = square.getStageCoordinate();
			square.setX(stageCoordinateSquarePosition.x);
			square.setY(stageCoordinateSquarePosition.y);
			square.draw(batch, parentAlpha);
			square.setX(oldX);
			square.setY(oldY);
		}
	}

	private void drawSquare2dObjects(Batch batch, float parentAlpha) {
		for (Actor actor : this.getChildren()) {
			if (actor instanceof Square2dObject) {
				actor.draw(batch, parentAlpha);
			}
		}
	}

	private void drawEdge(Batch batch) {
		batch.end();
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		Vector2[] stageCoordinateVertices = this.getStageCoordinatesVertices();
		for (int i = 0; i < stageCoordinateVertices.length; i++) {
			Vector2 vertex1 = stageCoordinateVertices[i];
			Vector2 vertex2 = stageCoordinateVertices[(i + 1) % stageCoordinateVertices.length];
			shapeRenderer.line(vertex1.x, vertex1.y, vertex2.x, vertex2.y);
		}
		shapeRenderer.end();
		shapeRenderer.dispose();
		batch.begin();
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
		// return this.toVerticesString();
		return this.getName();
	}

	private static class CombineSquare2dSetUpper {
		static void setup() {
			Json json = PersistManager.getUseJson();
			json.setSerializer(CombineSquare2d.class, new Json.Serializer<CombineSquare2d>() {

				@Override
				@SuppressWarnings({ "hiding", "rawtypes", "synthetic-access" })
				public void write(Json json, CombineSquare2d object, Class knownType) {
					json.writeObjectStart();
					json.writeType(CombineSquare2d.class);
					json.writeValue("squares", object.squares); //$NON-NLS-1$
					json.writeValue("reconstructCombineInfo", object.combineInfo.toReconstructCombineInfo()); //$NON-NLS-1$
					json.writeValue("objects", object.objects); //$NON-NLS-1$
					json.writeValue("name", object.getName()); //$NON-NLS-1$
					json.writeObjectEnd();
				}

				@Override
				@SuppressWarnings({ "hiding", "rawtypes", "unchecked" })
				public CombineSquare2d read(Json json, JsonValue jsonData, Class type) {
					final Array<Square2d> combinedSquares = json.readValue("squares", Array.class, Square2d.class, jsonData); //$NON-NLS-1$
					final ReconstructCombineInfo reconstructConbineInfo = json.readValue("reconstructCombineInfo", ReconstructCombineInfo.class, jsonData); //$NON-NLS-1$
					if (combinedSquares.size < 1) {
						return null;
					}
					final Array<Vertex> combineVertices1 = reconstructConbineInfo.getVertices1();
					final Array<Vertex> combineVertices2 = reconstructConbineInfo.getVertices2();
					if (combineVertices1.size != combineVertices2.size) {
						return null;
					}
					if (combineVertices1.size != (combinedSquares.size - 1)) {
						return null;
					}
					final CombineSquare2d baseSquare = new CombineSquare2d(combinedSquares.get(0));
					combinedSquares.removeIndex(0);
					try {
						this.appendSquares(baseSquare, combinedSquares, combineVertices1, combineVertices2);
					} catch (IllegalStateException e) {
						return null;
					}
					Array<Square2dObject> readObjects = json.readValue(Array.class, jsonData.get("objects")); //$NON-NLS-1$
					for (Square2dObject object : readObjects) {
						baseSquare.addSquareObject(object, object.getX(), object.getY(), false);
					}
					String name = json.readValue("name", String.class, jsonData); //$NON-NLS-1$
					baseSquare.setName(name);
					return baseSquare;
				}

				@SuppressWarnings("synthetic-access")
				private void appendSquares(final CombineSquare2d baseSquare, final Array<Square2d> combineSquares, final Array<Vertex> combineVertices1, final Array<Vertex> combineVertices2) {
					for (int i = 0; i < combineSquares.size; i++) {
						final Square2d beCombineSquare = combineSquares.get(i);
						final Vertex combineVertex1 = CombineSquare2dUtils.getSameValueVertex(combineVertices1.get(i), baseSquare.getVertices());
						final Vertex combineVertex2 = CombineSquare2dUtils.getSameValueVertex(combineVertices2.get(i), beCombineSquare.getVertices());
						final boolean isCombineSuccess = baseSquare.combine(combineVertex1, beCombineSquare, combineVertex2);
						if (isCombineSuccess) {
							combineSquares.removeValue(beCombineSquare, true);
							combineVertices1.removeIndex(i);
							combineVertices2.removeIndex(i);
							i--;
						} else {
							break;
						}
					}
					while (combineSquares.size != 0) {
						boolean isCombined = false;
						for (int i = 0; i < combineSquares.size; i++) {
							final Square2d beCombinedSquare = combineSquares.get(i);
							for (Vertex beCombinedSquareVertex : beCombinedSquare.getVertices()) {
								final Vertex afterBeCombinedSquareVertex = createAfterCombineTargetVertex(beCombinedSquareVertex, combineVertices1.get(i), combineVertices2.get(i));
								final Vertex sufficientlyCloseVertex = CombineSquare2dUtils.getSufficientlyCloseVertex(afterBeCombinedSquareVertex, baseSquare.getVertices());
								final boolean isCombineSuccess = baseSquare.combine(sufficientlyCloseVertex, beCombinedSquare, beCombinedSquareVertex);
								if (isCombineSuccess) {
									combineSquares.removeValue(beCombinedSquare, true);
									combineVertices1.removeIndex(i);
									combineVertices2.removeIndex(i);
									i--;
									isCombined = true;
									break;
								}
							}
						}
						if (isCombined == false) {
							throw new IllegalStateException();
						}
					}
				}
			});
		}
	}

}
