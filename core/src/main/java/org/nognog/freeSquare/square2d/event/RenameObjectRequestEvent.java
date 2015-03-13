package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.LifeObject;

/**
 * @author goshi 2015/03/13
 */
public class RenameObjectRequestEvent extends Square2dEvent {
	private final LifeObject renameRequestObject;

	/**
	 * @param renameRequestObject
	 */
	public RenameObjectRequestEvent(LifeObject renameRequestObject) {
		this.renameRequestObject = renameRequestObject;
	}

	/**
	 * @return the renameRequestObject
	 */
	public LifeObject getRenameRequestObject() {
		return this.renameRequestObject;
	}
}
