package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

/**
 * @author goshi 2015/02/02
 */
public class CollectObjectRequestEvent extends Square2dEvent {
	private final Square2dObject collectRequestedObject;

	/**
	 * 
	 */
	public CollectObjectRequestEvent() {
		this(null, null);
	}

	/**
	 * @param collectRequestedObject
	 */
	public CollectObjectRequestEvent(Square2dObject collectRequestedObject) {
		this(collectRequestedObject, null);
	}

	/**
	 * @param target
	 */
	public CollectObjectRequestEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param collectRequestedObject
	 * @param target
	 */
	public CollectObjectRequestEvent(Square2dObject collectRequestedObject, SquareEventListener target) {
		super(target);
		this.collectRequestedObject = collectRequestedObject;
	}

	/**
	 * @return the collectRequestedObject
	 */
	public Square2dObject getCollectRequestedObject() {
		return this.collectRequestedObject;
	}

}
