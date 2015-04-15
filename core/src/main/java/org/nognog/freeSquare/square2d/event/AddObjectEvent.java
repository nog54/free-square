package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

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
	public AddObjectEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param addedObject
	 * @param target
	 */
	public AddObjectEvent(Square2dObject addedObject, SquareEventListener target) {
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
