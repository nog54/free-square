package org.nognog.freeSquare.square2d;

import org.nognog.freeSquare.square.SquareObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2014/12/03
 */
public abstract class SquareObject2D extends Group implements SquareObject<Square2D> {

	private final float logicalWidth;
	private final float logicalHeight;

	protected Square2D square;
	private final Image image;

	private float minIntevalToNextAction;
	private float actionStoppingTime;

	private boolean isDisposed = false;

	/**
	 * @param texture
	 * @param logicalWidth
	 * @param performIndependentAction
	 */
	public SquareObject2D(Texture texture, float logicalWidth, boolean performIndependentAction) {
		this.image = new Image(texture);
		this.logicalWidth = logicalWidth;
		this.logicalHeight = this.image.getHeight() * (this.getLogicalWidth() / texture.getWidth());
		this.setWidth(logicalWidth);
		this.setHeight(this.getLogicalHeight());
		this.setOriginX(logicalWidth / 2);
		this.image.setWidth(logicalWidth);
		this.image.setHeight(this.getLogicalHeight());
		this.addActor(this.image);
		this.image.setOriginX(logicalWidth / 2);
		this.addListener(new ActorGestureListener() {
			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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
	public void setSquare(Square2D square) {
		if (this.square != null) {
			throw new RuntimeException("square already setted."); //$NON-NLS-1$
		}
		this.square = square;
		Vector2 randomPoint = Square2DUtils.getRandomPointOn(this.square);
		this.setX(randomPoint.x);
		this.setY(randomPoint.y);
	}

	@Override
	public void setX(float x) {
		super.setX(x - this.getOriginX());
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x - this.getOriginX(), y);
	}

	@Override
	public void setPosition(float x, float y, int align) {
		super.setPosition(x - this.getOriginX(), y, align);
	}

	@Override
	public float getX() {
		return super.getX() + this.getOriginX();
	}

	@Override
	public float getX(int align) {
		return super.getX(align) + this.getOriginX();
	}

	@Override
	public Square2D getSquare() {
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
	 * dispose
	 */
	public void dispose() {
		if (this.isDisposed) {
			return;
		}
		((TextureRegionDrawable) (this.image.getDrawable())).getRegion().getTexture().dispose();
		this.isDisposed = true;
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
	 * @return true if this is disposed.
	 */
	public boolean isDisposed() {
		return this.isDisposed;
	}

	/**
	 * notify all observers
	 */
	public void notifyObservers() {
		this.square.notifyObservers();
	}

	@Override
	public void act(float delta) {
		this.actionStoppingTime += delta;
		if (this.actionStoppingTime >= this.minIntevalToNextAction) {
			this.minIntevalToNextAction = this.independentAction(this.actionStoppingTime, 0);
			this.actionStoppingTime = 0;
		}
		super.act(delta);
	}

	/**
	 * This method will be called by {@link #act(float)}.
	 * 
	 * @return min interval to next act [ms]
	 * 
	 */
	protected abstract float independentAction(float delta, float defaultNextMinInterval);
}
