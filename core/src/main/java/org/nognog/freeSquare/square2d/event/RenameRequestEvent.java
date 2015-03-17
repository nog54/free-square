package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.Nameable;
import org.nognog.freeSquare.square2d.Square2dEvent;

/**
 * @author goshi 2015/03/13
 */
public class RenameRequestEvent extends Square2dEvent {
	private final Nameable renameRequestedObject;

	/**
	 * @param renameRequestObject
	 */
	public RenameRequestEvent(Nameable renameRequestObject) {
		this.renameRequestedObject = renameRequestObject;
	}

	/**
	 * @return the renameRequestObject
	 */
	public Nameable getRenameRequestedObject() {
		return this.renameRequestedObject;
	}
}
