package org.nognog.freeSquare.ui.square2d.events;

import org.nognog.freeSquare.ui.SquareObserver;
import org.nognog.freeSquare.ui.square2d.Square2dEvent;
import org.nognog.freeSquare.ui.square2d.Square2dObject;

/**
 * @author goshi 2015/02/02
 */
public class AddObjectEvent extends Square2dEvent {
	private final Square2dObject addedObject;

	/**
	 * 
	 */
	public AddObjectEvent() {
		this(null, null);
	}

	/**
	 * @param addedObject
	 */
	public AddObjectEvent(Square2dObject addedObject) {
		this(addedObject, null);
	}

	/**
	 * @param target
	 */
	public AddObjectEvent(SquareObserver target) {
		this(null, target);
	}

	/**
	 * @param addedObject
	 * @param target
	 */
	public AddObjectEvent(Square2dObject addedObject, SquareObserver target) {
		super(target);
		this.addedObject = addedObject;
	}

	/**
	 * @return the addedObject
	 */
	public Square2dObject getAddedObject() {
		return this.addedObject;
	}

}
