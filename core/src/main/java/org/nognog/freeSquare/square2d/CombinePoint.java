package org.nognog.freeSquare.square2d;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/15
 */
@SuppressWarnings("javadoc")
public class CombinePoint {
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

	static class CombinedVertex {
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
