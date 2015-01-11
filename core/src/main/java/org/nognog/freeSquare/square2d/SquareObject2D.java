package org.nognog.freeSquare.square2d;

import java.util.concurrent.Future;

import org.nognog.freeSquare.square.SquareObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author goshi 2014/12/03
 */
public abstract class SquareObject2D extends Group implements SquareObject<Square2D> {

	private final float logicalWidth;
	private final float logicalHeight;

	protected Square2D square;
	private final Image image;

	private Future<?> future;

	boolean enablesIndependentAction;
	boolean isPerformingIndependentAction;

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
		this.enablesIndependentAction = performIndependentAction;
		this.isPerformingIndependentAction = false;
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
	 * start independent action in new thread
	 */
	public void startIndependentAction() {
		if (!this.enablesIndependentAction) {
			return;
		}
		if (this.future == null) {
			Runnable repeatIndependentAction = new Runnable() {
				private static final long defaultInterval = 10;
				private SquareObject2D actionTarget = SquareObject2D.this;
				private long previousActionTime = TimeUtils.millis();

				@Override
				public void run() {
					this.actionTarget.isPerformingIndependentAction = true;
					long previousInterval = 0;
					while (true) {
						final long currentTime = TimeUtils.millis();
						final float delta = (currentTime - this.previousActionTime) / 1000f;
						final long requestedInterval = this.actionTarget.independentAction(delta, previousInterval, defaultInterval);
						this.actionTarget.act(delta);
						this.previousActionTime = currentTime;
						if (Thread.currentThread().isInterrupted()) {
							break;
						}
						if (requestedInterval > 0) {
							try {
								Thread.sleep(requestedInterval);
								previousInterval = requestedInterval;
							} catch (InterruptedException e) {
								break;
							}
						}
					}
					this.actionTarget.isPerformingIndependentAction = false;
				}

			};

			this.future = this.square.getPool().submit(repeatIndependentAction);
		}
	}

	/**
	 * halt IndependentAction
	 */
	public void haltIndependentAction() {
		if (this.future == null) {
			return;
		}
		this.future.cancel(true);
		this.future = null;
	}

	/**
	 * dispose
	 */
	public void dispose() {
		if (this.isDisposed) {
			return;
		}
		((TextureRegionDrawable) (this.image.getDrawable())).getRegion().getTexture().dispose();
		this.haltIndependentAction();
		this.isDisposed = true;
	}

	/**
	 * @return true if this is performing independent action
	 */
	public boolean isPerformingIndependentAction() {
		return this.isPerformingIndependentAction;
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
	 * {@link #act(float)} will be called after this method.
	 * 
	 * @param delta
	 * @param previousInterval
	 * @return interval to next action [ms]
	 * 
	 */
	protected abstract long independentAction(float delta, long previousInterval, long defaultInterval);
}
