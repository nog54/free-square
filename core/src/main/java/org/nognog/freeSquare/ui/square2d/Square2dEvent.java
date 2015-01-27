package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.ui.SquareObserver;

import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/01/25
 */
public class Square2dEvent {

	private final EventType eventType;
	private final Square2dObject relatedObject;
	private final SquareObserver targetObserver;
	private Array<SquareObserver> exceptObservers;

	private static final Square2dEvent simpleSquareUpdateEvent = new Square2dEvent();

	/**
	 * 
	 */
	private Square2dEvent() {
		this(EventType.UPDATE_SQUARE);
	}

	/**
	 * @param eventType
	 */
	public Square2dEvent(EventType eventType) {
		this(eventType, null);
	}

	/**
	 * @param eventType
	 * @param relatedObject
	 */
	public Square2dEvent(EventType eventType, Square2dObject relatedObject) {
		this(eventType, relatedObject, null);
	}

	/**
	 * @param eventType
	 * @param relatedObject
	 * @param targetObject
	 */
	public Square2dEvent(EventType eventType, Square2dObject relatedObject, SquareObserver targetObject) {
		this.eventType = eventType;
		this.relatedObject = relatedObject;
		this.targetObserver = targetObject;
		this.exceptObservers = new Array<>();
	}

	/**
	 * @return event type
	 */
	public EventType getEventType() {
		return this.eventType;
	}

	/**
	 * @return object that related this event
	 */
	public Square2dObject getRelatedObject() {
		return this.relatedObject;
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
		this.exceptObservers.add(observer);
	}
	
	/**
	 * @return except observers.
	 */
	public Array<SquareObserver> getExceptObservers(){
		return this.exceptObservers;
	}

	/**
	 * @param event
	 * @return true if all field is same.
	 */
	public boolean equals(Square2dEvent event) {
		return (this.eventType == event.eventType) && (this.relatedObject == event.relatedObject) && (this.targetObserver == event.targetObserver);
	}

	/**
	 * @return simple event
	 */
	public static Square2dEvent getSimpleSquareUpdateEvent() {
		return simpleSquareUpdateEvent;
	}

	/**
	 * @author goshi 2015/01/25
	 */
	@SuppressWarnings("javadoc")
	public static enum EventType {
		ADD_OBJECT, REMOVE_OBJECT, UPDATE_OBJECT, UPDATE_SQUARE
	}
}