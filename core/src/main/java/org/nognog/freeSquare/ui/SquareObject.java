package org.nognog.freeSquare.ui;

/**
 * @author goshi 2014/12/17
 * @param <T> 
 */
public interface SquareObject<T extends Square<?>> {

	/**
	 * This method should only be involed via {@link Square#addSquareObject(SquareObject)}
	 * @param square
	 */
	void setSquare(T square);

	/**
	 * @return square
	 */
	T getSquare();

}
