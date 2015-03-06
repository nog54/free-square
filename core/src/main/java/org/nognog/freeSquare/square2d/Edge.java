package org.nognog.freeSquare.square2d;

/**
 * @author goshi 2015/03/06
 */
public class Edge {
	/**
	 * v1
	 */
	public final Vertex v1;
	/**
	 * v2
	 */
	public final Vertex v2;

	/**
	 * @param v1
	 * @param v2
	 */
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Edge) {
			Edge edgeObj = (Edge) obj;
			return (this.v1 == edgeObj.v1 && this.v2 == edgeObj.v2) || (this.v1 == edgeObj.v2 && this.v2 == edgeObj.v1);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
