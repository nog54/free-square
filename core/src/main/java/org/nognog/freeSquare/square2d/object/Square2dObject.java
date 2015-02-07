package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.model.SelfValidatable;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Square2dEvent;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;
import org.nognog.freeSquare.square2d.ui.SquareObject;
import org.nognog.freeSquare.square2d.ui.SquareObserver;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2014/12/03
 */
public class Square2dObject extends Group implements SquareObject<Square2d>, SquareObserver, SelfValidatable {

	private final Square2dObjectType<?> type;
	private final float logicalWidth;
	private final float logicalHeight;

	protected Square2d square;
	protected final Image image;

	private boolean enableAction = true;

	private boolean lockAddAction = false;
	private Array<Action> pausingActions;

	private boolean isBeingTouched = false;
	private boolean isLongPressedInLastTouch = false;

	/**
	 * @param type
	 */
	public Square2dObject(Square2dObjectType<?> type) {
		this.type = type;
		final Texture texture = type.getTexture();
		this.image = new Image(texture);
		this.logicalWidth = type.getLogicalWidth();
		this.logicalHeight = this.image.getHeight() * (this.getLogicalWidth() / texture.getWidth());
		this.setColor(type.getColor());
		this.setWidth(this.logicalWidth);
		this.setHeight(this.getLogicalHeight());
		this.setOriginX(this.logicalWidth / 2);
		this.setOriginY(this.logicalHeight / 4);
		this.image.setWidth(this.logicalWidth);
		this.image.setHeight(this.getLogicalHeight());
		this.addActor(this.image);
		this.pausingActions = new Array<>();
		this.addListener(new ActorGestureListener() {
			private boolean lastTouchCallLongPress;

			@SuppressWarnings("synthetic-access")
			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Square2dObject.this.isBeingTouched = true;
				this.lastTouchCallLongPress = false;
				event.stop();
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Square2dObject.this.isBeingTouched = false;
				Square2dObject.this.isLongPressedInLastTouch = this.lastTouchCallLongPress;
				event.stop();
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				event.stop();
			}

			@Override
			public void zoom(InputEvent event, float initialDistance, float distance) {
				event.stop();
			}

			@Override
			public void fling(InputEvent event, float velocityX, float velocityY, int button) {
				event.stop();
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				event.stop();
			}

			@Override
			public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				event.stop();
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				this.lastTouchCallLongPress = true;
				return false;
			}

		});
	}

	@Override
	public void setSquare(Square2d square) {
		if (this.square != null && square != null) {
			throw new RuntimeException("square already setted."); //$NON-NLS-1$
		}
		this.square = square;
	}

	@Override
	public void setX(float x) {
		this.setPosition(x, this.getY());
	}

	@Override
	public void setY(float y) {
		this.setPosition(this.getX(), y);
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x - this.getOriginX(), y - this.getOriginY());
	}

	/**
	 * @return type of this object
	 */
	public Square2dObjectType<?> getType() {
		return this.type;
	}

	@Override
	public float getX() {
		return super.getX() + this.getOriginX();
	}

	@Override
	public float getY() {
		return super.getY() + this.getOriginY();
	}

	/**
	 * Return NaN if this sauare is different to object's square.
	 * 
	 * @param object
	 * @return Distance to object.
	 */
	public float getDistanceTo(Square2dObject object) {
		if (this.getSquare() == null || this.getSquare() != object.getSquare()) {
			return Float.NaN;
		}
		return (float) Math.sqrt(Math.pow(this.getX() - object.getX(), 2) + Math.pow(this.getY() - object.getY(), 2));
	}

	@Override
	public Square2d getSquare() {
		return this.square;
	}

	/**
	 * @return logical width
	 */
	public float getLogicalWidth() {
		return this.logicalWidth;
	}

	/**
	 * @return logical height
	 */
	public float getLogicalHeight() {
		return this.logicalHeight;
	}

	/**
	 * @return true if this is landing on square
	 */
	public boolean isLandingOnSquare() {
		if (this.square == null) {
			return false;
		}
		return this.square.containsInSquareArea(this.getX(), this.getY());
	}

	/**
	 * @return true if this is touching.
	 */
	public boolean isBeingTouched() {
		return this.isBeingTouched;
	}

	/**
	 * @return true if last touch to this object is long press.
	 */
	public boolean isLongPressedInLastTouch() {
		return this.isLongPressedInLastTouch;
	}

	/**
	 * through if action is pausing-action.
	 * 
	 * @param action
	 */
	@Override
	public void addAction(Action action) {
		if (this.lockAddAction == true) {
			return;
		}
		if (this.containsAction(action)) {
			return;
		}
		super.addAction(action);
	}

	@Override
	public void act(float delta) {
		if (!this.isEnabledAction() || this.isBeingTouched) {
			return;
		}
		final float yBeforeAct = this.getY();
		super.act(delta);
		final float yAfterAct = this.getY();
		if (yBeforeAct != yAfterAct) {
			this.square.requestDrawOrderUpdate();
		}
	}

	/**
	 * @param enableIndependentAction
	 */
	public void setEnabledAction(boolean enableIndependentAction) {
		this.enableAction = enableIndependentAction;
	}

	/**
	 * @return true if independent-action is enabled.
	 */
	public boolean isEnabledAction() {
		return this.enableAction;
	}

	@Override
	public void setColor(Color color) {
		super.setColor(color);
		this.image.setColor(color);
	}

	@Override
	public void notify(Square2dEvent event) {
		// overridden by sub class
	}

	@Override
	public boolean isValid() {
		return this.square != null;
	}

	/**
	 * 
	 */
	public void clearAllPausingActions() {
		for (Action pausingAction : this.pausingActions) {
			pausingAction.setActor(null);
		}
		this.pausingActions.clear();
	}

	/**
	 * @param action
	 */
	public void pauseAction(Action action) {
		if (action == null) {
			return;
		}
		if (this.getActions().removeValue(action, true) == true) {
			this.pausingActions.add(action);
		}
	}

	/**
	 * @return paused actions.
	 */
	public Array<Action> pauseAllPerformingActions() {
		Array<Action> bePausedActions = new Array<>();
		bePausedActions.addAll(this.getActions());
		for (Action performingAction : bePausedActions) {
			this.pauseAction(performingAction);
		}
		return bePausedActions;
	}

	/**
	 * @param actions
	 * @return paused actions.
	 */
	public Array<Action> pausePerformingActionsExcept(Action... actions) {
		if (actions == null) {
			return this.pauseAllPerformingActions();
		}
		Array<Action> bePausedActions = new Array<>(this.getActions());
		Array<Action> exceptActions = new Array<>(actions);
		for (Action performingAction : bePausedActions) {
			if (exceptActions.contains(performingAction, true)) {
				bePausedActions.removeValue(performingAction, true);
				continue;
			}
			this.pauseAction(performingAction);
		}
		return bePausedActions;
	}

	/**
	 * @param action
	 * @return true if action is pausing-action.
	 */
	public boolean isPausingAction(Action action) {
		return this.pausingActions.contains(action, true);
	}

	/**
	 * @param action
	 * @return true if action is performing-action.
	 */
	public boolean isPerformingAction(Action action) {
		return this.getActions().contains(action, true);
	}

	/**
	 * @param action
	 * @return true if action is performing-action or pausing-action.
	 */
	public boolean containsAction(Action action) {
		return this.getActions().contains(action, true) || this.pausingActions.contains(action, true);
	}

	/**
	 * @param actions
	 */
	public void resumePausingAction(Array<Action> actions) {
		Action[] actionsArray = actions.toArray(Action.class);
		this.resumePausingAction(actionsArray);
	}

	/**
	 * @param actions
	 */
	public void resumePausingAction(Action... actions) {
		if (actions == null) {
			return;
		}
		if (this.isLockingAddAction()) {
			return;
		}
		for (int i = 0; i < actions.length; i++) {
			if (actions[i] == null) {
				continue;
			}
			if (this.pausingActions.removeValue(actions[i], true) == true) {
				this.getActions().add(actions[i]);
			}
		}
	}

	/**
	 * @return true if lock add-action.
	 */
	public boolean isLockingAddAction() {
		return this.lockAddAction;
	}

	/**
	 * 
	 */
	public void lockAddAction() {
		this.lockAddAction = true;
	}

	/**
	 * 
	 */
	public void unlockAddAction() {
		this.lockAddAction = false;
	}

	@Override
	public String toString() {
		return this.type.getName();
	}
}
