package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.object.Square2dObject;

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
	 * @param combineVertex
	 * @param targetSquare
	 * @param targetVertex
	 * @return true if success
	 */
	public boolean combine(Vertex combineVertex, Square2d targetSquare, Vertex targetVertex) {
		if (!this.vertices.contains(combineVertex, true)) {
			return false;
		}
		final Array<Vertex> targetSquareVertices = targetSquare.getVertices();
		if (!targetSquareVertices.contains(targetVertex, true)) {
			return false;
		}
		CombineTarget combineTarget = new CombineTarget(targetSquare, targetVertex);
		this.combinePoints.put(combineVertex, combineTarget);

		final int verteciesInsertStartIndex = this.vertices.indexOf(combineVertex, true) + 1;
		for (int i = 0; i < targetSquareVertices.size; i++) {
			final int insertTargetSquareVertexIndex = (targetSquareVertices.indexOf(targetVertex, true) + 1 + i) % targetSquareVertices.size;
			final float insertVertexX = combineVertex.x + (targetSquareVertices.get(insertTargetSquareVertexIndex).x - targetVertex.x);
			final float insertVertexY = combineVertex.y + (targetSquareVertices.get(insertTargetSquareVertexIndex).y - targetVertex.y);
			Vertex insertVertex = new Vertex(insertVertexX, insertVertexY);
			this.vertices.insert(verteciesInsertStartIndex + i, insertVertex);
		}

		this.addSquare(targetSquare, combineVertex.x - targetVertex.x, combineVertex.y - targetVertex.y);

		this.recalculateLeftEndX();
		this.recalculateRightEndX();
		this.recalculateButtomEndY();
		this.recalculateTopEndY();
		return true;
	}

	private void addSquare(Square2d square, float x, float y) {
		square.setPosition(x, y);
		for (Square2dObject object : square.objects) {
			square.removeSquareObject(object, false);
			this.addSquareObject(object, x + object.getX(), y + object.getY(), false);
		}
		this.squares.add(square);
		this.addActorForce(square);
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
