package org.nognog.freeSquare.square2d.objects;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.SquareObject2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends SquareObject2D {


	/**
	 * create Riki
	 */
	public Riki() {
		super(new Texture(Gdx.files.internal(Resources.RIKI_PATH)));
	}


	@Override
	protected void independentAction() {
		try {
			Thread.sleep(1000);
			this.moveBy(5f, 0);
		} catch (InterruptedException e) {
			// nothing to do
		}
		Gdx.graphics.requestRendering();
		
	}
}
