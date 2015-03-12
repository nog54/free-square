package org.nognog.freeSquare.square2d.action;

import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.Square2dObject;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author goshi 2015/01/29
 */
public class FireEventAction extends Action {

	private Square2dEvent event;
	private Square2dObject eventListener;

	/**
	 * 
	 */
	public FireEventAction() {

	}

	/**
	 * @param event
	 */
	public FireEventAction(Square2dEvent event) {
		this.event = event;
	}

	/**
	 * @param eventListener
	 */
	public void setEventListener(Square2dObject eventListener) {
		this.eventListener = eventListener;
	}

	/**
	 * @return eventListener
	 */
	public Square2dObject getEventListener() {
		return this.eventListener;
	}

	/**
	 * @param event
	 */
	public void setEvent(Square2dEvent event) {
		this.event = event;
	}

	/**
	 * @return be fired event
	 */
	public Square2dEvent getEvent() {
		return this.event;
	}

	@Override
	public boolean act(float delta) {
		this.eventListener.notify(this.event);
		return true;
	}

}
