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

import org.nognog.freeSquare.square2d.CombinePoint.CombinedVertex;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/03/10
 */
public class CombineInfo {
	private Array<CombinedVertex> combineVertices1;
	private Array<CombinedVertex> combineVertices2;

	/**
	 * 
	 */
	public CombineInfo() {
		this.combineVertices1 = new Array<>();
		this.combineVertices2 = new Array<>();
	}

	/**
	 * @param combineSquare1
	 * @param combineVertex1
	 * @param combineSquare2
	 * @param combineVertex2
	 */
	public void addCombineInfo(CombineSquare2d combineSquare1, Vertex combineVertex1, Square2d combineSquare2, Vertex combineVertex2) {
		this.addCombineInfo(new CombinedVertex(combineSquare1, combineVertex1), new CombinedVertex(combineSquare2, combineVertex2));
	}

	/**
	 * @param combinedVertex1
	 * @param combinedVertex2
	 */
	public void addCombineInfo(CombinedVertex combinedVertex1, CombinedVertex combinedVertex2) {
		this.combineVertices1.add(combinedVertex1);
		this.combineVertices2.add(combinedVertex2);
	}

	/**
	 * @param square
	 */
	public void removeCombineInfo(Square2d square) {
		for (CombinedVertex combinedVertex1 : this.combineVertices1) {
			if (combinedVertex1.square == square) {
				final int removeIndex = this.combineVertices1.indexOf(combinedVertex1, true);
				this.combineVertices1.removeIndex(removeIndex);
				this.combineVertices2.removeIndex(removeIndex);
			}
		}
		for (CombinedVertex combinedVertex2 : this.combineVertices2) {
			if (combinedVertex2.square == square) {
				final int removeIndex = this.combineVertices2.indexOf(combinedVertex2, true);
				this.combineVertices1.removeIndex(removeIndex);
				this.combineVertices2.removeIndex(removeIndex);
			}
		}
	}

	/**
	 * @return convert to infomation to reconstruct
	 */
	public ReconstructCombineInfo toReconstructCombineInfo() {
		Vertex[] vertices1 = new Vertex[this.combineVertices1.size];
		Vertex[] vertices2 = new Vertex[this.combineVertices2.size];
		if (vertices1.length != vertices2.length) {
			return null;
		}
		for (int i = 0; i < vertices1.length; i++) {
			vertices1[i] = this.combineVertices1.get(i).vertex;
			vertices2[i] = this.combineVertices2.get(i).vertex;
		}

		return new ReconstructCombineInfo(vertices1, vertices2);
	}

	static class ReconstructCombineInfo {
		private Vertex[] vertices1;
		private Vertex[] vertices2;

		ReconstructCombineInfo() {

		}

		ReconstructCombineInfo(Vertex[] vertex1, Vertex[] vertex2) {
			this.vertices1 = vertex1;
			this.vertices2 = vertex2;
		}

		Array<Vertex> getVertices1() {
			return new Array<>(this.vertices1);
		}

		Array<Vertex> getVertices2() {
			return new Array<>(this.vertices2);
		}
	}
}
