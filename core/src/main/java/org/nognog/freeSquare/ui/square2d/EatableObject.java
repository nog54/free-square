package org.nognog.freeSquare.ui.square2d;

import org.nognog.freeSquare.ui.square2d.objects.Square2dObjectType;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/01/30
 */
public class EatableObject extends Square2dObject {

	private final int baseQuantity;
	private final int originTextureRegionWidth;
	private final int originTextureRegionHeight;
	private final int originTextureRegionArea;
	private int quantity;

	/**
	 * @param type
	 */
	public EatableObject(Square2dObjectType type) {
		super(type);
		this.setDebug(true);
		this.baseQuantity = (int) type.getOtherValues()[0];
		this.quantity = this.baseQuantity;
		this.originTextureRegionWidth = ((TextureRegionDrawable) this.image.getDrawable()).getRegion().getRegionWidth();
		this.originTextureRegionHeight = ((TextureRegionDrawable) this.image.getDrawable()).getRegion().getRegionHeight();
		this.originTextureRegionArea = this.originTextureRegionWidth * this.originTextureRegionHeight;
	}

	/**
	 * @param amount
	 * @param direction
	 * @return amount of actually eaten
	 */
	public int eaten(int amount, Direction direction) {
		final int eatenAmount = (this.quantity > amount) ? amount : this.quantity;
		this.quantity -= eatenAmount;
		if(this.quantity == 0){
			this.image.setScale(0);
		}
		final float eatenRatio = eatenAmount / (float) this.baseQuantity;

		final int eatenArea = (int) (this.originTextureRegionArea * eatenRatio);
		TextureRegion imageTextureRegion = ((TextureRegionDrawable) this.image.getDrawable()).getRegion();
		final int currentTextureRegionWidth = imageTextureRegion.getRegionWidth();
		final int currentTextureRegionHeight = imageTextureRegion.getRegionHeight();

		if (direction == Direction.UP) {
			final int eatenHeight = eatenArea / currentTextureRegionWidth;
			final int afterEatenTextureRegionHeight = currentTextureRegionHeight - eatenHeight;
			imageTextureRegion.setRegionY(imageTextureRegion.getRegionY() + eatenHeight);
			imageTextureRegion.setRegionHeight(afterEatenTextureRegionHeight);
			this.image.setScaleY(afterEatenTextureRegionHeight / (float) this.originTextureRegionHeight);
		}

		if (direction == Direction.DOWN) {
			final int eatenHeight = eatenArea / currentTextureRegionWidth;
			final int afterEatenTextureRegionHeight = currentTextureRegionHeight - eatenHeight;
			imageTextureRegion.setRegionHeight(afterEatenTextureRegionHeight);
			this.image.moveBy(0, eatenHeight / 4);
			this.image.setScaleY(afterEatenTextureRegionHeight / (float) this.originTextureRegionHeight);
		}

		if (direction == Direction.LEFT) {
			final int eatenWidth = eatenArea / currentTextureRegionHeight;
			final int afterEatenTextureRegionWidth = currentTextureRegionWidth - eatenWidth;
			imageTextureRegion.setRegionX(imageTextureRegion.getRegionX() + eatenWidth);
			imageTextureRegion.setRegionWidth(afterEatenTextureRegionWidth);
			this.image.moveBy(eatenWidth / 4, 0);
			this.image.setScaleX(afterEatenTextureRegionWidth / (float) this.originTextureRegionWidth);
		}

		if (direction == Direction.RIGHT) {
			final int eatenWidth = eatenArea / currentTextureRegionHeight;
			final int afterEatenTextureRegionWidth = currentTextureRegionWidth - eatenWidth;
			imageTextureRegion.setRegionWidth(afterEatenTextureRegionWidth);
			this.image.setScaleX(afterEatenTextureRegionWidth / (float) this.originTextureRegionWidth);
		}
		return eatenAmount;
	}

	/**
	 * resurrection!
	 */
	public void resurrection() {
		this.quantity = this.baseQuantity;
		TextureRegion imageTextureRegion = ((TextureRegionDrawable) this.image.getDrawable()).getRegion();
		imageTextureRegion.setRegionX(0);
		imageTextureRegion.setRegionY(0);
		imageTextureRegion.setRegionWidth(this.originTextureRegionWidth);
		imageTextureRegion.setRegionHeight(this.originTextureRegionHeight);
		this.image.setPosition(0, 0);
		this.image.setScale(1);
	}

}
