package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;
import org.nognog.freeSquare.square2d.ui.SquareObserver;

/**
 * @author goshi 2015/02/02
 */
public class UpdateObjectEvent extends Square2dEvent {
	private final Square2dObject updatedSquare;

	/**
	 * 
	 */
	public UpdateObjectEvent() {
		this(null, null);
	}

	/**
	 * @param updatedSquare
	 */
	public UpdateObjectEvent(Square2dObject updatedSquare) {
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
	public UpdateObjectEvent(Square2dObject updatedSquare, SquareObserver target) {
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
