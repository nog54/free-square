package org.nognog.freeSquare.square2d.objects;

import org.nognog.freeSquare.square2d.SquareObject2D;

import com.badlogic.gdx.graphics.Color;

/**
 * @author goshi
 * 2015/01/13
 */
public class Tofu extends SquareObject2D{

	/**
	 * @param texture
	 * @param logicalWidth
	 * @param performIndependentAction
	 */
	public Tofu() {
		super(SquareObjectInfo.TOFU);
		this.setColor(Color.RED);
	}

}
