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

import com.badlogic.gdx.math.MathUtils;

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

	/**
	 * @return theta (0 <= theta <= pi)
	 */
	public float getTheta() {
		if (this.v1.y < this.v2.y) {
			return calcTheta(this.v2, this.v1);
		}
		return calcTheta(this.v1, this.v2);
	}

	private static float calcTheta(Vertex v1, Vertex v2) {
		return Math.abs(MathUtils.atan2(v1.y - v2.y, v1.x - v2.x));
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
