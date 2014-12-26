package org.nognog.freeSquare.square2d.objects;

import org.nognog.freeSquare.Resources;
import org.nognog.freeSquare.square2d.SquareObject2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @author goshi 2014/12/19
 */
public class Riki extends SquareObject2D {

	private static final float logicalWidth = 0.1f;
	/**
	 * create Riki
	 */
	public Riki() {
		super(new Texture(Gdx.files.internal(Resources.rikiPath)));
		final float degree = 5;
		final float cycleTime = 4;
		Action foreverRotate = ActionUtils.foreverRotate(degree, cycleTime, Interpolation.sine);
		final float upDownAmount = 5;
		Action foreverUpDown = ActionUtils.foreverUpdown(upDownAmount, cycleTime, Interpolation.pow5);
		this.addAction(Actions.parallel(foreverRotate, foreverUpDown));
	}
	
	@Override
	protected void independentAction(float delta) {
		this.act(delta);
		
		Gdx.graphics.requestRendering();
	}
	@Override
	protected float getLosicalWidth(){
		return logicalWidth;
	}
}
