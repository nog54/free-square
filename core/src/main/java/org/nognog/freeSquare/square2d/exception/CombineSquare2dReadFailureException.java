package org.nognog.freeSquare.square2d.exception;

import org.nognog.freeSquare.square2d.Square2d;

/**
 * @author goshi 2015/03/24
 */
public class CombineSquare2dReadFailureException extends RuntimeException{
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
