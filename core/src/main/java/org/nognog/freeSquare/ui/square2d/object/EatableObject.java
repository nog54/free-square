package org.nognog.freeSquare.ui.square2d.object;

import org.nognog.freeSquare.ui.square2d.Direction;
import org.nognog.freeSquare.ui.square2d.object.Square2dObjectType.EatableObjectType;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/01/30
 */
public class EatableObject extends Square2dObject implements LandObject {

	private final int baseAmount;
	private final int originTextureRegionWidth;
	private final int originTextureRegionHeight;
	private final int originTextureRegionArea;
	private int amount;

	/**
	 * @param type
	 */
	public EatableObject(EatableObjectType type) {
		super(type);
		this.baseAmount = type.getQuantity();
		this.amount = this.baseAmount;
		this.originTextureRegionWidth = ((TextureRegionDrawable) this.image.getDrawable()).getRegion().getRegionWidth();
		this.originTextureRegionHeight = ((TextureRegionDrawable) this.image.getDrawable()).getRegion().getRegionHeight();
		this.originTextureRegionArea = this.originTextureRegionWidth * this.originTextureRegionHeight;
	}

	/**
	 * @param eatAmount
	 * @param direction
	 * @return amount of actually eaten
	 */
	public int eaten(int eatAmount, Direction direction) {
		if (this.amount == 0) {
			return this.amount;
		}
		final int eatenAmount = (this.amount > eatAmount) ? eatAmount : this.amount;
		this.amount -= eatenAmount;
		if (this.amount == 0) {
			this.getSquare().removeSquareObject(this);
			return this.amount;
		}
		final float eatenRatio = eatenAmount / (float) this.baseAmount;

		final int eatenRegionArea = (int) (this.originTextureRegionArea * eatenRatio);
		TextureRegion imageTextureRegion = ((TextureRegionDrawable) this.image.getDrawable()).getRegion();
		final int currentTextureRegionWidth = imageTextureRegion.getRegionWidth();
		final int currentTextureRegionHeight = imageTextureRegion.getRegionHeight();

		if (direction == Direction.UP) {
			final int eatenRegionHeight = eatenRegionArea / currentTextureRegionWidth;
			final int afterEatenTextureRegionHeight = currentTextureRegionHeight - eatenRegionHeight;
			imageTextureRegion.setRegionY(imageTextureRegion.getRegionY() + eatenRegionHeight);
			imageTextureRegion.setRegionHeight(afterEatenTextureRegionHeight);
			this.image.setScaleY(afterEatenTextureRegionHeight / (float) this.originTextureRegionHeight);
		}

		if (direction == Direction.DOWN) {
			final int eatenRegionHeight = eatenRegionArea / currentTextureRegionWidth;
			final int afterEatenTextureRegionHeight = currentTextureRegionHeight - eatenRegionHeight;
			imageTextureRegion.setRegionHeight(afterEatenTextureRegionHeight);
			this.image.setScaleY(afterEatenTextureRegionHeight / (float) this.originTextureRegionHeight);
		}

		if (direction == Direction.LEFT) {
			final int eatenRegionWidth = eatenRegionArea / currentTextureRegionHeight;
			final int afterEatenTextureRegionWidth = currentTextureRegionWidth - eatenRegionWidth;
			imageTextureRegion.setRegionX(imageTextureRegion.getRegionX() + eatenRegionWidth);
			imageTextureRegion.setRegionWidth(afterEatenTextureRegionWidth);
			float newScaleX = afterEatenTextureRegionWidth / (float) this.originTextureRegionWidth;
			this.image.setScaleX(newScaleX);
			this.image.setX((1 - newScaleX) * this.image.getWidth());
		}

		if (direction == Direction.RIGHT) {
			final int eatenRegionWidth = eatenRegionArea / currentTextureRegionHeight;
			final int afterEatenTextureRegionWidth = currentTextureRegionWidth - eatenRegionWidth;
			imageTextureRegion.setRegionWidth(afterEatenTextureRegionWidth);
			this.image.setScaleX(afterEatenTextureRegionWidth / (float) this.originTextureRegionWidth);
		}
		return eatenAmount;
	}

	/**
	 * resurrection!
	 */
	public void resurrection() {
		this.amount = this.baseAmount;
		TextureRegion imageTextureRegion = ((TextureRegionDrawable) this.image.getDrawable()).getRegion();
		imageTextureRegion.setRegionX(0);
		imageTextureRegion.setRegionY(0);
		imageTextureRegion.setRegionWidth(this.originTextureRegionWidth);
		imageTextureRegion.setRegionHeight(this.originTextureRegionHeight);
		this.image.setPosition(0, 0);
		this.image.setScale(1);
	}
	
	/**
	 * @return true if this is being eaten.
	 */
	public boolean isBeingEaten(){
		return this.baseAmount != this.amount;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return this.amount;
	}

}
