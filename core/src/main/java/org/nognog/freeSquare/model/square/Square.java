package org.nognog.freeSquare.model.square;

import org.nognog.freeSquare.model.Nameable;

/**
 * @author goshi 2014/12/17
 * @param <T>
 */
public interface Square<T extends SquareObject<?>> extends Nameable{
	
	/**
	 * @param object
	 */
	void addSquareObject(T object);

	/**
	 * @param object
	 * @return true if object is removed
	 */
	boolean removeSquareObject(T object);

	/**
	 * @return square objects
	 */
	T[] getObjects();

	/**
	 * @param observer
	 * 
	 */
	void addSquareObserver(SquareEventListener observer);

	/**
	 * @param observer
	 */
	void removeSquareObserver(SquareEventListener observer);

	/**
	 * @param event
	 */
	void notifyObservers(SquareEvent event);

	/**
	 * @param event
	 */
	void notify(SquareEvent event);

}
