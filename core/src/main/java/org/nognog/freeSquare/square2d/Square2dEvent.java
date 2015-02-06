package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square2d.ui.SquareObserver;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/25
 */
public class Square2dEvent {

	private final SquareObserver targetObserver;
	private Array<SquareObserver> exceptObservers;

	/**
	 * 
	 */
	public Square2dEvent() {
		this(null);
	}

	/**
	 * @param targetObserver
	 */
	public Square2dEvent(SquareObserver targetObserver) {
		this.targetObserver = targetObserver;
		this.exceptObservers = new Array<>();
	}

	/**
	 * @return object that related this event
	 */
	public SquareObserver getTargetObserver() {
		return this.targetObserver;
	}

	/**
	 * @param observer
	 */
	public void addExceptObserver(SquareObserver observer) {
		if (this.targetObserver != null) {
			throw new RuntimeException("target observer should be null when addExceptObserver is called."); //$NON-NLS-1$
		}
		if (!this.exceptObservers.contains(observer, true)) {
			this.exceptObservers.add(observer);
		}
	}

	/**
	 * @return except observers.
	 */
	public Array<SquareObserver> getExceptObservers() {
		return this.exceptObservers;
	}
}