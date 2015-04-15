package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

/**
 * @author goshi 2015/02/02
 */
public class RemoveObjectEvent extends Square2dEvent {
	private final Square2dObject removedObject;

	/**
	 * 
	 */
	public RemoveObjectEvent() {
		this(null, null);
	}

	/**
	 * @param removedObject
	 */
	public RemoveObjectEvent(Square2dObject removedObject) {
		this(removedObject, null);
	}

	/**
	 * @param target
	 */
	public RemoveObjectEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param removedObject
	 * @param target
	 */
	public RemoveObjectEvent(Square2dObject removedObject, SquareEventListener target) {
		super(target);
		this.removedObject = removedObject;
	}

	/**
	 * @return the removedObject
	 */
	public Square2dObject getRemovedObject() {
		return this.removedObject;
	}

}
