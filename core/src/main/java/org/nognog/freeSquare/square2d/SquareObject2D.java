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

	private Square2D square;

	private Image image;

	private Future<?> future;

	private boolean enablesIndependentAction;

	boolean isPerformingIndependentAction;

	private boolean isDisposed = false;

	/**
	 * @param texture
	 */
	public SquareObject2D(Texture texture) {
		this(texture, true);
	}

	/**
	 * @param texture
	 * @param performIndependentAction
	 */
	public SquareObject2D(Texture texture, boolean performIndependentAction) {
		this.image = new Image(texture);
		final float logicalWidth = this.getLogicalWidth();
		final float logicalHeight = this.image.getHeight() * (this.getLogicalWidth() / texture.getWidth());
		this.setWidth(logicalWidth);
		this.setHeight(logicalHeight);
		this.setOriginX(logicalWidth / 2);
		this.image.setWidth(logicalWidth);
		this.image.setHeight(logicalHeight);
		this.addActor(this.image);
		this.image.setOriginX(logicalWidth / 2);
		this.enablesIndependentAction = performIndependentAction;
		this.isPerformingIndependentAction = false;
		this.addListener(new ActorGestureListener(){
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
			public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
					Vector2 pointer2) {
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
		this.setX(randomPoint.x - this.getOriginX());
		this.setY(randomPoint.y);
	}

	@Override
	public void setX(float x) {
		super.setX(x - this.getWidth() / 2);
	}

	@Override
	public Square2D getSquare() {
		return this.square;
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
				private static final long minInterval = 60;
				private SquareObject2D actionTarget = SquareObject2D.this;
				private long previousActionTime = TimeUtils.millis();

				@Override
				public void run() {
					this.actionTarget.isPerformingIndependentAction = true;
					long previousInterval = 0;
					while (true) {
						final long currentTime = TimeUtils.millis();
						final float delta = (currentTime - this.previousActionTime) / 1000f;
						final long requestInterval = this.actionTarget.independentAction(delta, previousInterval,
								minInterval);
						this.previousActionTime = currentTime;
						final long interval = Math.max(minInterval, requestInterval);
						if (Thread.currentThread().isInterrupted()) {
							break;
						}
						try {
							Thread.sleep(interval);
							previousInterval = interval;
						} catch (InterruptedException e) {
							break;
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
	 * @return true if this is disposed.
	 */
	public boolean isDisposed() {
		return this.isDisposed;
	}

	/**
	 * @param delta
	 * @param previousInterval
	 * @return interval to next action [ms]
	 */
	protected abstract long independentAction(float delta, long previousInterval, long minInterval);

	protected abstract float getLogicalWidth();
}
