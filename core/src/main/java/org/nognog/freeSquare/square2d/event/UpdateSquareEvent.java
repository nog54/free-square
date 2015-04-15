package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dEvent;

/**
 * @author goshi 2015/02/02
 */
public class UpdateSquareEvent extends Square2dEvent {
	private final Square2d updatedSquare;

	/**
	 * 
	 */
	public UpdateSquareEvent() {
		this(null, null);
	}

	/**
	 * @param updatedSquare
	 */
	public UpdateSquareEvent(Square2d updatedSquare) {
		this(updatedSquare, null);
	}

	/**
	 * @param target
	 */
	public UpdateSquareEvent(SquareEventListener target) {
		this(null, target);
	}

	/**
	 * @param updatedSquare
	 * @param target
	 */
	public UpdateSquareEvent(Square2d updatedSquare, SquareEventListener target) {
		super(target);
		this.updatedSquare = updatedSquare;
	}

	/**
	 * @return the updatedObject
	 */
	public Square2d getUpdatedSquare() {
		return this.updatedSquare;
	}

}
