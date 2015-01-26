package org.nognog.freeSquare.ui;

/**
 * @author goshi 2015/01/25
 */
public class SquareEvent {

	private final EventType eventType;
	private final SquareObject<?> relatedObject;
	private final SquareObserver targetObserver;

	private static final SquareEvent simpleSquareUpdateEvent = new SquareEvent();
	
	/**
	 * 
	 */
	private SquareEvent() {
		this(EventType.UPDATE_SQUARE);
	}

	/**
	 * @param eventType
	 */
	public SquareEvent(EventType eventType) {
		this(eventType, null);
	}

	/**
	 * @param eventType
	 * @param relatedObject
	 */
	public SquareEvent(EventType eventType, SquareObject<?> relatedObject) {
		this(eventType, relatedObject, null);
	}

	/**
	 * @param eventType
	 * @param relatedObject
	 * @param targetObject
	 */
	public SquareEvent(EventType eventType, SquareObject<?> relatedObject, SquareObserver targetObject) {
		this.eventType = eventType;
		this.relatedObject = relatedObject;
		this.targetObserver = targetObject;
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
	public SquareObject<?> getRelatedObject() {
		return this.relatedObject;
	}

	/**
	 * @return object that related this event
	 */
	public SquareObserver getTargetObserver() {
		return this.targetObserver;
	}

	/**
	 * @param event
	 * @return true if all field is same.
	 */
	public boolean equals(SquareEvent event) {
		return (this.eventType == event.eventType) && (this.relatedObject == event.relatedObject) && (this.targetObserver == event.targetObserver);
	}

	/**
	 * @return simple event
	 */
	public static SquareEvent getSimpleSquareUpdateEvent(){
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