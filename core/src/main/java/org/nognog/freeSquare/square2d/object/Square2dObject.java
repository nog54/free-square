/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.model.SelfValidatable;
import org.nognog.freeSquare.model.square.SquareEvent;
import org.nognog.freeSquare.model.square.SquareEventListener;
import org.nognog.freeSquare.model.square.SquareObject;
import org.nognog.freeSquare.square2d.Square2d;
import org.nognog.freeSquare.square2d.Vertex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * @author goshi 2014/12/03
 */
public class Square2dObject extends Group implements SquareObject<Square2d>, SquareEventListener, SelfValidatable {

	private final Square2dObjectType<?> type;
	private final float logicalWidth;
	private final float logicalHeight;
	private Square2d square;

	private final Square2dObjectIcon icon;

	private boolean enableAction = true;

	private Action priorityAction;

	private boolean isBeingTouched = false;
	private boolean isLongPressedInLastTouch = false;

	/**
	 * @param type
	 */
	public Square2dObject(Square2dObjectType<?> type) {
		this.type = type;
		final Texture texture = type.getTexture();
		final Image mainIconImage = new Image(texture);
		this.logicalWidth = type.getLogicalWidth();
		this.logicalHeight = mainIconImage.getHeight() * (this.getLogicalWidth() / texture.getWidth());
		mainIconImage.setWidth(this.logicalWidth);
		mainIconImage.setHeight(this.logicalHeight);
		this.icon = new Square2dObjectIcon(mainIconImage);
		this.icon.setX(-this.icon.getWidth() / 2);
		this.icon.setY(-this.icon.getHeight() / 8);
		this.addActor(this.icon);
		this.setColor(type.getColor());
		this.setWidth(this.logicalWidth);
		this.setHeight(this.logicalHeight);
		this.setupListeners();
	}

	private void setupListeners() {
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
	public Actor hit(float x, float y, boolean touchable) {
		final Actor superResult = super.hit(x, y, touchable);
		if (superResult == this) {
			return null;
		}
		return superResult;
	}

	@Override
	public void setSquare(Square2d square) {
		if (this.square != null && square != null) {
			throw new RuntimeException("square already setted."); //$NON-NLS-1$
		}
		this.square = square;
	}

	/**
	 * @return type of this object
	 */
	public Square2dObjectType<?> getType() {
		return this.type;
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
	 * @return icon
	 */
	public Square2dObjectIcon getIcon() {
		return this.icon;
	}

	protected Image getIconMainImage() {
		return this.icon.getMainImage();
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
		return this.square.containsPosition(this.getX(), this.getY());
	}

	/**
	 * @return true if this is landing on vertex
	 */
	public boolean isLandingOnVertex() {
		return this.square.isVertexPosition(this.getX(), this.getY());
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
		if (this.containsAction(action)) {
			return;
		}
		super.addAction(action);
	}

	/**
	 * @param action
	 */
	public void setPriorityAction(Action action) {
		if (action != null) {
			action.setActor(this);
		}
		this.priorityAction = action;
	}

	/**
	 * clear priority action
	 */
	public void clearPriorityAction() {
		this.setPriorityAction(null);
	}

	/**
	 * @return the priority action
	 */
	public Action getPriorityAction() {
		return this.priorityAction;
	}

	/**
	 * @return true if acting priority action.
	 */
	public boolean isPerformingPriorityAction() {
		return this.priorityAction != null;
	}

	@Override
	public final void act(float delta) {
		if (this.isBeingTouched || !this.isEnabledAction()) {
			return;
		}
		if (this.priorityAction == null) {
			this.actActions(delta);
			return;
		}
		final Array<Action> normalActions = new Array<>(this.getActions());
		this.getActions().clear();
		this.getActions().add(this.priorityAction);
		this.actActions(delta);
		if (this.finishedPriorityAction()) {
			this.priorityAction = null;
		}
		this.getActions().clear();
		this.getActions().addAll(normalActions);
	}

	private void actActions(float delta) {
		try {
			this.actChildren(delta);
			this.actMyself(delta);
		} catch (InterruptOtherActionException e) {
			// TODO
		}
	}

	private void actMyself(float delta) {
		Array<Action> actions = this.getActions();
		if (actions.size > 0) {
			final Stage stage = this.getStage();
			if (stage != null && stage.getActionsRequestRendering())
				Gdx.graphics.requestRendering();
			for (int i = 0; i < actions.size; i++) {
				Action action = actions.get(i);
				if (action.act(delta) && i < actions.size) {
					Action current = actions.get(i);
					int actionIndex = current == action ? i : actions.indexOf(action, true);
					if (actionIndex != -1) {
						actions.removeIndex(actionIndex);
						action.setActor(null);
						i--;
					}
				}
			}
		}
	}

	private void actChildren(float delta) {
		final SnapshotArray<Actor> children = this.getChildren();
		Actor[] actors = children.begin();
		for (int i = 0, n = children.size; i < n; i++)
			actors[i].act(delta);
		children.end();
	}

	private boolean finishedPriorityAction() {
		return !this.getActions().contains(this.priorityAction, true);
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
		this.getIconMainImage().setColor(color);
	}

	@Override
	public void notify(SquareEvent event) {
		// overridden by sub class
	}

	@Override
	public boolean isValid() {
		return this.square != null;
	}

	/**
	 * @return nearest vertex
	 */
	public Vertex getNearestSquareVertex() {
		if (this.square == null) {
			return null;
		}
		Vertex[] vertices = this.square.getVertices();
		int minIndex = -1;
		float minR = Float.MAX_VALUE;
		for (int i = 0; i < vertices.length; i++) {
			final float r = vertices[i].calculateR(this.getX(), this.getY());
			if (r < minR) {
				minR = r;
				minIndex = i;
			}
		}
		return vertices[minIndex];
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
		return this.getActions().contains(action, true) || this.priorityAction == action;
	}

	@Override
	public String toString() {
		return this.type.getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Square2dObject) {
			return this.equals((Square2dObject) obj);
		}
		return super.equals(obj);
	}

	/**
	 * @param obj
	 * @return true if type and position is same.
	 */
	public boolean equals(Square2dObject obj) {
		return this.type == obj.type && this.getX() == obj.getX() && this.getY() == obj.getY();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
