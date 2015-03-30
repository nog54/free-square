package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

/**
 * @author goshi 2015/02/02
 */
public class UpdateSquareObjectEvent extends Square2dEvent {
	private final Square2dObject updatedSquare;

	/**
	 * 
	 */
	public UpdateSquareObjectEvent() {
		this(null, null);
	}

	/**
	 * @param updatedSquare
	 */
	public UpdateSquareObjectEvent(Square2dObject updatedSquare) {
		this(updatedSquare, null);
	}

	/**
	 * @param target
	 */
	public UpdateSquareObjectEvent(SquareObserver target) {
		this(null, target);
	}

	/**
	 * @param updatedSquare
	 * @param target
	 */
	public UpdateSquareObjectEvent(Square2dObject updatedSquare, SquareObserver target) {
		super(target);
		this.updatedSquare = updatedSquare;
	}

	/**
	 * @return the updatedObject
	 */
	public Square2dObject getUpdatedObject() {
		return this.updatedSquare;
	}

}
