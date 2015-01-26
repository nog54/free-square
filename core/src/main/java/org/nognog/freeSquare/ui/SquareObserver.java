package org.nognog.freeSquare.ui;

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
