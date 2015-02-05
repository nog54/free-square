package org.nognog.freeSquare.ui.square2d.event;

import org.nognog.freeSquare.ui.SquareObserver;
import org.nognog.freeSquare.ui.square2d.Square2d;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;

/**
 * @author goshi 2015/02/02
 */
public class UpdateObjectEvent extends Square2dEvent {
	private final Square2d updatedSquare;

	/**
	 * 
	 */
	public UpdateObjectEvent() {
		this(null, null);
	}

	/**
	 * @param updatedSquare
	 */
	public UpdateObjectEvent(Square2d updatedSquare) {
		this(updatedSquare, null);
	}

	/**
	 * @param target
	 */
	public UpdateObjectEvent(SquareObserver target) {
		this(null, target);
	}

	/**
	 * @param updatedSquare
	 * @param target
	 */
	public UpdateObjectEvent(Square2d updatedSquare, SquareObserver target) {
		super(target);
		this.updatedSquare = updatedSquare;
	}

	/**
	 * @return the updatedObject
	 */
	public Square2d getUpdatedObject() {
		return this.updatedSquare;
	}

}
