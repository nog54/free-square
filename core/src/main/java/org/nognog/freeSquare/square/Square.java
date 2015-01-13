package org.nognog.freeSquare.square;

/**
 * @author goshi 2014/12/17
 * @param <T>
 */
public interface Square<T extends SquareObject<?>> {

	/**
	 * @param object
	 */
	void addSquareObject(T object);

	/**
	 * @return square objects
	 */
	Iterable<T> getObjects();

	/**
	 * @param observer
	 * 
	 */
	void addSquareObserver(SquareObserver observer);

	/**
	 * @param observer
	 */
	void removeSquareObserver(SquareObserver observer);
	
	/**
	 * 
	 */
	void notifyObservers();

}
