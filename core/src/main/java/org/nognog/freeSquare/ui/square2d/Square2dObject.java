package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.model.SelfValidatable;
import org.nognog.freeSquare.ui.SquareEvent;
import org.nognog.freeSquare.ui.SquareObject;
import org.nognog.freeSquare.ui.SquareObserver;
import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2014/12/03
 */
public class Square2dObject extends Group implements SquareObject<Square2d>, SquareObserver, SelfValidatable {

	private Square2dObjectType type;
	private final float logicalWidth;
	private final float logicalHeight;

	protected Square2d square;
	private final Image image;

	private boolean enableAction = true;
	protected boolean isBeingTouched = false;
	
	/**
	 * @param type
	 */
	public Square2dObject(Square2dObjectType type) {
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
		this.image.setOriginX(this.logicalWidth / 2);
		this.addListener(new ActorGestureListener() {
			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Square2dObject.this.isBeingTouched = true;
				event.stop();
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Square2dObject.this.isBeingTouched = false;
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
	public Square2dObjectType getType() {
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

	@Override
	public void act(float delta) {
		final float yBeforeAct = this.getY();
		if (this.isEnabledAction()) {
			super.act(delta);
		}
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
	public String toString() {
		return this.type.name();
	}

	@Override
	public void notify(SquareEvent event) {
		// overridden by sub class
	}

	@Override
	public boolean isValid() {
		return this.square != null;
	}
}
