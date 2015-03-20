package org.nognog.freeSquare.model.square;


/**
 * @author goshi
 * 2015/01/12
 */
public interface SquareObserver {
	/**
	 * update square
	 * @param event 
	 */
	void notify(SquareEvent event);
}
