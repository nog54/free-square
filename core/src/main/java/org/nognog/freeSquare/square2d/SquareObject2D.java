package org.nognog.freeSquare.square2d;

import java.util.concurrent.Future;

import org.nognog.freeSquare.RepeatRunThread;
import org.nognog.freeSquare.square.SquareObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2014/12/03
 */
public abstract class SquareObject2D extends Image implements SquareObject<Square2D> {

	private Square2D square;

	private Future<?> future;
	
	private boolean isDisposed = false;

	/**
	 * @param texture
	 * @param independentAction
	 */
	public SquareObject2D(Texture texture) {
		super(texture);
	}


	@Override
	public void setSquare(Square2D square) {
		this.square = square;
	}


	@Override
	public Square2D getSquare() {
		return this.square;
	}

	/**
	 * start independent action in new thread
	 */
	public void startIndependentAction() {
		if (this.future == null) {
			RepeatRunThread repeatIndependentAction = new RepeatRunThread(new Runnable() {
				@Override
				public void run() {
					SquareObject2D.this.independentAction();
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
	public void dispose(){
		((TextureRegionDrawable) (this.getDrawable())).getRegion().getTexture().dispose();
		this.isDisposed = true;
	}
	
	/**
	 * @return whether this is disposed
	 */
	public boolean isDisposed(){
		return this.isDisposed;
	}

	/**
	 * 
	 */
	protected abstract void independentAction();
}
