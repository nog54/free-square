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

package org.nognog.freeSquare.square2d.exception;

import org.nognog.freeSquare.square2d.Square2d;

/**
 * @author goshi 2015/03/24
 */
public class CombineSquare2dReadFailureException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Square2d[] containedSquares;

	/**
	 * @param containedSquares
	 */
	public CombineSquare2dReadFailureException(Square2d[] containedSquares) {
		this.containedSquares = containedSquares;
	}

	/**
	 * @return the squares
	 */
	public Square2d[] getContainedSquares() {
		return this.containedSquares;
	}

}
