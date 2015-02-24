package org.nognog.freeSquare.square2d.event;

import org.nognog.freeSquare.model.square.SquareObserver;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.EatableObject;
import org.nognog.freeSquare.square2d.object.Square2dObject;

/**
 * @author goshi 2015/02/02
 */
public class EatObjectEvent extends Square2dEvent {
	private final Square2dObject eater;
	private final EatableObject eatenObject;
	private final int amount;

	/**
	 * @param eater
	 * @param eatenObject
	 * @param amount 
	 * 
	 */
	public EatObjectEvent(Square2dObject eater, EatableObject eatenObject, int amount) {
		this(eater, eatenObject, amount, null);
	}

	/**
	 * @param eater
	 * @param eatenObject
	 * @param amount 
	 * @param target
	 */
	public EatObjectEvent(Square2dObject eater, EatableObject eatenObject, int amount, SquareObserver target) {
		super(target);
		this.eater = eater;
		this.eatenObject = eatenObject;
		this.amount = amount;
	}

	/**
	 * @return the eater
	 */
	public Square2dObject getEater() {
		return this.eater;
	}

	/**
	 * @return the eatenObject
	 */
	public EatableObject getEatenObject() {
		return this.eatenObject;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return this.amount;
	}
}
