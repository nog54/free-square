package org.nognog.freeSquare.square2d;

import java.util.concurrent.Future;

import org.nognog.freeSquare.RepeatRunThread;
import org.nognog.freeSquare.square.SquareObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author goshi 2014/12/03
 */
public abstract class SquareObject2D extends Image implements SquareObject<Square2D> {

	private Square2D square;
	long independentActionInterval; // if zero, independentAction is
									// diseable
	private static final long defaultInterval = 60;

	private Future<?> future;

	private boolean isDisposed = false;

	/**
	 * @param texture
	 */
	public SquareObject2D(Texture texture) {
		this(texture, defaultInterval);
	}

	/**
	 * @param texture
	 * @param independentActionInterval
	 */
	public SquareObject2D(Texture texture, long independentActionInterval) {
		super(texture);
		this.setOriginX(this.getWidth() / 2);
		final float scale = (1024 * this.getLosicalWidth()) / texture.getWidth();
		this.setScale(scale);
		this.independentActionInterval = independentActionInterval;
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
		if (this.independentActionInterval == 0) {
			return;
		}
		if (this.future == null) {
			RepeatRunThread repeatIndependentAction = new RepeatRunThread(new Runnable() {
				private final long interval = SquareObject2D.this.independentActionInterval;
				private long previousTime = TimeUtils.millis();

				@Override
				public void run() {
					final long currentTime = TimeUtils.millis();
					SquareObject2D.this.independentAction((currentTime - this.previousTime) / 1000f);
					this.previousTime = currentTime;
					try {
						Thread.sleep(this.interval);
					} catch (InterruptedException e) {
						return;
					}

				}
			});
			this.future = this.square.getPool().submit(repeatIndependentAction);
		}
	}

	/**
	 * halt IndependentAction
	 */
	public void haltIndependentAction() {
		this.future.cancel(true);
		this.future = null;
	}

	/**
	 * dispose
	 */
	public void dispose() {
		((TextureRegionDrawable) (this.getDrawable())).getRegion().getTexture().dispose();
		this.isDisposed = true;
	}

	/**
	 * @return whether this is disposed
	 */
	public boolean isDisposed() {
		return this.isDisposed;
	}

	protected abstract void independentAction(float delta);

	protected abstract float getLosicalWidth();
}
