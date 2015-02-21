package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * @author goshi 2015/02/15
 */
public class CombinedSquare2d extends Square2d {
	private Array<Square2d> squares;
	private Array<Vertex> vertices;
	private ObjectMap<Vertex, CombineTarget> combinePoints;
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
		this.combinePoints = new ObjectMap<>();
		this.leftEndX = base.getLeftEndX();
		this.rightEndX = base.getRightEndX();
		this.buttomEndY = base.getButtomEndY();
		this.topEndY = base.getTopEndY();
	}

	@Override
	public Array<Vertex> getVertices() {
		return new Array<>(this.vertices);
	}

	/**
	 * @return combinePoints
	 */
	public ObjectMap<Vertex, CombineTarget> getCombinePoints() {
		return this.combinePoints;
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
		final Array<Vertex> targetSquareVertices = targetSquare.getVertices();
		if (!targetSquareVertices.contains(targetsCombineVertex, true)) {
			return false;
		}
		if (this.isNotCombinableWith(thisCombineVertex, targetSquare, targetsCombineVertex)) {
			return false;
		}
		CombineTarget combineTarget = new CombineTarget(targetSquare, targetsCombineVertex);
		this.combinePoints.put(thisCombineVertex, combineTarget);

		this.insertVertices(thisCombineVertex, targetsCombineVertex, targetSquareVertices);
		this.normalizeVertices();
		this.addSquare(targetSquare, thisCombineVertex.x - targetsCombineVertex.x, thisCombineVertex.y - targetsCombineVertex.y);
		recalculateBorder();
		return true;
	}

	private void insertVertices(Vertex thisCombineVertex, Vertex targetsCombineVertex, final Array<Vertex> targetSquareVertices) {
		final int verteciesInsertStartIndex = this.vertices.indexOf(thisCombineVertex, true) + 1;
		for (int i = 0; i < targetSquareVertices.size; i++) {
			final int insertTargetSquareVertexIndex = (targetSquareVertices.indexOf(targetsCombineVertex, true) + 1 + i) % targetSquareVertices.size;
			final float insertVertexX = thisCombineVertex.x + (targetSquareVertices.get(insertTargetSquareVertexIndex).x - targetsCombineVertex.x);
			final float insertVertexY = thisCombineVertex.y + (targetSquareVertices.get(insertTargetSquareVertexIndex).y - targetsCombineVertex.y);
			Vertex insertVertex = new Vertex(insertVertexX, insertVertexY);
			this.vertices.insert(verteciesInsertStartIndex + i, insertVertex);
		}
	}

	private void recalculateBorder() {
		this.recalculateLeftEndX();
		this.recalculateRightEndX();
		this.recalculateButtomEndY();
		this.recalculateTopEndY();
	}

	private boolean isNotCombinableWith(Vertex thisCombineVertex, Square2d targetSquare, Vertex targetsCombineVertex) {
		// may be not perfect
		final Vertex[] thisPolygonVertices = this.vertices.<Vertex> toArray(Vertex.class);
		final Vertex[] afterCombineTargetPolygonVertices = new Vertex[targetSquare.getVertices().size];
		int sameVertexCounter = 0;
		for (int i = 0; i < afterCombineTargetPolygonVertices.length; i++) {
			final float x = thisCombineVertex.x + (targetSquare.getVertices().get(i).x - targetsCombineVertex.x);
			final float y = thisCombineVertex.y + (targetSquare.getVertices().get(i).y - targetsCombineVertex.y);
			afterCombineTargetPolygonVertices[i] = new Vertex(x, y);
			for (int j = 0; j < thisPolygonVertices.length; j++) {
				if (canBeRegardedAsSameVertex(afterCombineTargetPolygonVertices[i], thisPolygonVertices[j])) {
					afterCombineTargetPolygonVertices[i] = new Vertex(thisPolygonVertices[j]);
					sameVertexCounter++;
					if (sameVertexCounter == Math.min(thisPolygonVertices.length, afterCombineTargetPolygonVertices.length)) {
						return largerPolygonContainsSmallerPolygonCentroid(thisPolygonVertices, afterCombineTargetPolygonVertices);
					}
					break;
				}
			}
		}

		if (existsInvalidContainedVertex(thisPolygonVertices, afterCombineTargetPolygonVertices)) {
			return true;
		}
		if (existsInvalidContainedVertex(afterCombineTargetPolygonVertices, thisPolygonVertices)) {
			return true;
		}
		if (existsInvalidIntersection(thisPolygonVertices, afterCombineTargetPolygonVertices)) {
			return true;
		}
		return false;
	}

	private static boolean largerPolygonContainsSmallerPolygonCentroid(final Vertex[] thisPolygonVertices, final Vertex[] afterCombineTargetPolygonVertices) {
		if (thisPolygonVertices.length == afterCombineTargetPolygonVertices.length) {
			return true;
		}
		final Vertex[] smallerVertices, largetVertices;
		if (thisPolygonVertices.length < afterCombineTargetPolygonVertices.length) {
			smallerVertices = thisPolygonVertices;
			largetVertices = afterCombineTargetPolygonVertices;
		} else {
			smallerVertices = afterCombineTargetPolygonVertices;
			largetVertices = thisPolygonVertices;
		}
		final float[] smallerVerticesArray = toFloatArray(smallerVertices);
		final Vector2 smallerVerticesPolygonCentroid = new Vector2();
		GeometryUtils.polygonCentroid(smallerVerticesArray, 0, smallerVerticesArray.length, smallerVerticesPolygonCentroid);
		return createPolygon(largetVertices).contains(smallerVerticesPolygonCentroid.x, smallerVerticesPolygonCentroid.y);
	}

	private static boolean existsInvalidContainedVertex(Vertex[] polygonVertices, Vertex[] checkVertices) {
		Polygon polygon = createPolygon(polygonVertices);
		for (int i = 0; i < checkVertices.length; i++) {
			Vertex checkVertex = checkVertices[i];
			if (polygon.contains(checkVertex.x, checkVertex.y) == false) {
				continue;
			}
			if (existsSufficientlyCloseEdge(polygonVertices, checkVertex) == false) {
				return true;
			}
		}
		return false;
	}

	private static boolean existsSufficientlyCloseEdge(Vertex[] baseVertices, Vertex checkVertex) {
		for (int i = 0; i < baseVertices.length; i++) {
			final Vertex vertex1 = baseVertices[i];
			final Vertex vertex2 = baseVertices[(i + 1) % baseVertices.length];
			float distance = Intersector.distanceSegmentPoint(vertex1.x, vertex1.y, vertex2.x, vertex2.y, checkVertex.x, checkVertex.y);
			if (isSufficientlyCloseDistance(distance)) {
				return true;
			}
		}
		return false;
	}

	private static boolean existsInvalidIntersection(Vertex[] polygonVertices1, Vertex[] polygonVertices2) {
		for (int i = 0; i < polygonVertices1.length; i++) {
			Vertex v1 = polygonVertices1[i];
			Vertex v2 = polygonVertices1[(i + 1) % polygonVertices1.length];
			for (int j = 0; j < polygonVertices2.length; j++) {
				Vertex v3 = polygonVertices2[j];
				Vertex v4 = polygonVertices2[(j + 1) % polygonVertices2.length];
				Vector2 intersection = new Vector2();
				if (Intersector.intersectSegments(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y, intersection)) {
					Vertex intersectionVertex = new Vertex(intersection.x, intersection.y);
					if (!existsSufficientlyCloseVertex(polygonVertices1, intersectionVertex)) {
						return true;
					}
					if (!existsSufficientlyCloseVertex(polygonVertices2, intersectionVertex)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean existsSufficientlyCloseVertex(Vertex[] vertices, Vertex checkVertex) {
		for (int i = 0; i < vertices.length; i++) {
			if (canBeRegardedAsSameVertex(vertices[i], checkVertex)) {
				return true;
			}
		}
		return false;
	}
	
	private static float[] toFloatArray(Vertex[] target){
		float[] result = new float[target.length * 2];
		int i = 0;
		for(Vertex vertex : target){
			result[i++] = vertex.x;
			result[i++] = vertex.y;
		}
		return result;
	}

	private void addSquare(Square2d square, float x, float y) {
		square.setPosition(x, y);
		for (Square2dObject object : square.objects) {
			square.removeSquareObject(object, false);
			this.addSquareObject(object, x + object.getX(), y + object.getY(), false);
		}
		this.squares.add(square);
		this.addActorForce(square);
		this.requestDrawOrderUpdate();
	}

	/**
	 * @param enable
	 */
	public void setDrawEdge(boolean enable) {
		this.drawEdge = enable;
	}

	private void normalizeVertices() {
		Vertex[] verticesArray = this.getVertices().toArray(Vertex.class);
		Vertex[][] commonVertex = getCommonVertex(verticesArray);
		for (int i = 0; i < verticesArray.length; i++) {
			if (commonVertex[i].length == 0 || !this.vertices.contains(verticesArray[i], true)) {
				continue;
			}
			Vertex[] removeVertices = new Vertex[commonVertex[i].length + 1];
			removeVertices[0] = verticesArray[i];
			System.arraycopy(commonVertex[i], 0, removeVertices, 1, commonVertex[i].length);
			if (this.isRemovableVertices(removeVertices)) {
				this.vertices.removeValue(verticesArray[i], true);
				for (int j = 0; j < commonVertex[i].length; j++) {
					this.vertices.removeValue(commonVertex[i][j], true);
				}
			}
		}
	}

	private static Vertex[][] getCommonVertex(Vertex[] checkVertices) {
		Vertex[][] result = new Vertex[checkVertices.length][];
		for (int i = 0; i < checkVertices.length; i++) {
			Vertex checkVertex = checkVertices[i];
			Array<Vertex> commonVertices = new Array<>();
			for (int j = 0; j < checkVertices.length; j++) {
				Vertex compareVertex = checkVertices[j];
				if (checkVertex == compareVertex) {
					continue;
				}

				if (canBeRegardedAsSameVertex(checkVertex, compareVertex)) {
					commonVertices.add(compareVertex);
				}
				result[i] = commonVertices.toArray(Vertex.class);
			}
		}
		return result;
	}

	private static boolean canBeRegardedAsSameVertex(Vertex checkVertex, Vertex compareVertex) {
		return isSufficientlyCloseDistance(checkVertex.calculateR(compareVertex));
	}

	private static boolean isSufficientlyCloseDistance(float a) {
		final float regardAsSufficientlyCloseThreshold = 5;
		return Math.abs(a) < regardAsSufficientlyCloseThreshold;
	}

	private boolean isRemovableVertices(Vertex... removeVertices) {
		float beforeArea = this.getVertexPolygon().area();
		float afterArea = this.getVertexPolygonExcept(removeVertices).area();
		float relativeError = Math.abs(afterArea - beforeArea) / beforeArea;
		final float permissibleRelativeError = 1 / 100f;
		return relativeError < permissibleRelativeError;
	}

	private Polygon getVertexPolygon() {
		return this.getVertexPolygonExcept();
	}

	private Polygon getVertexPolygonExcept(Vertex... exceptVertices) {
		return createPolygon(this.vertices.<Vertex> toArray(Vertex.class), exceptVertices);
	}

	private static Polygon createPolygon(Vertex[] vertices, Vertex... exceptVertices) {
		float[] points = new float[vertices.length * 2 - exceptVertices.length * 2];
		int i = 0;
		for (Vertex vertex : vertices) {
			if (contains(exceptVertices, vertex)) {
				continue;
			}
			points[i++] = vertex.x;
			points[i++] = vertex.y;
		}
		return new Polygon(points);
	}

	private static boolean contains(Object[] objects, Object findObject) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == findObject) {
				return true;
			}
		}
		return false;
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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.vertices.get(0));
		for (int i = 1; i < this.vertices.size; i++) {
			sb.append("-").append(this.vertices.get(i)); //$NON-NLS-1$
		}
		return sb.toString();
	}

	/**
	 * @author goshi 2015/02/16
	 */
	public static class CombineTarget {
		/**
		 * combine target square
		 */
		public final Square2d targetSquare;
		/**
		 * combine target vertex of square
		 */
		public final Vertex targetVertex;

		/**
		 * @param targetSquare
		 * @param targetVertex
		 */
		public CombineTarget(Square2d targetSquare, Vertex targetVertex) {
			this.targetSquare = targetSquare;
			this.targetVertex = targetVertex;
		}
	}
}
