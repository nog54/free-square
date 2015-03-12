package org.nognog.freeSquare.model.square;

import org.nognog.freeSquare.square2d.Square2dEvent;

/**
 * @author goshi
 * 2015/01/12
 */
public interface SquareObserver {
	/**
	 * update square
	 * @param event 
	 */
	void notify(Square2dEvent event);
}