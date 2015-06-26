/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

package org.nognog.freeSquare.square2d.object.types.eatable;

import org.nognog.freeSquare.model.life.status.influence.StatusInfluence;
import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.action.Square2dActionUtlls;
import org.nognog.freeSquare.square2d.object.LandObject;
import org.nognog.freeSquare.square2d.object.MovableSquare2dObject;
import org.nognog.freeSquare.square2d.object.types.life.LifeObject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/01/30
 */
public class EatableObject extends MovableSquare2dObject implements LandObject {
	private int baseAmount;
	private int originTextureRegionWidth;
	private int originTextureRegionHeight;
	private int originTextureRegionArea;
	private int amount;

	/**
	 * @param type
	 */
	public EatableObject(EatableObjectType type) {
		super(type);
		this.baseAmount = type.getQuantity();
		this.amount = this.baseAmount;
		this.originTextureRegionWidth = this.getIconMainImageTextureRegion().getRegionWidth();
		this.originTextureRegionHeight = this.getIconMainImageTextureRegion().getRegionHeight();
		this.originTextureRegionArea = this.originTextureRegionWidth * this.originTextureRegionHeight;
		this.addMainAction(Square2dActionUtlls.keepLandingOnSquare());
	}

	private TextureRegion getIconMainImageTextureRegion() {
		return ((TextureRegionDrawable) this.getIconMainImage().getDrawable()).getRegion();
	}

	/**
	 * @param eater
	 * @param eatAmount
	 * @param direction
	 * @return amount of actually eaten
	 */
	public int eatenBy(LifeObject eater, int eatAmount, Direction direction) {
		if (this.getSquare() == null || this.getSquare() != eater.getSquare()) {
			throw new IllegalStateException("EatableObject is not on square when be eaten"); //$NON-NLS-1$
		}
		if (eatAmount <= 0) {
			return 0;
		}
		if (this.amount == 0) {
			return this.amount;
		}
		final int eatenAmount = (this.amount > eatAmount) ? eatAmount : this.amount;
		this.amount -= eatenAmount;
		final StatusInfluence<?> statusInfluence = this.getEatableObjectType().getStatusInfluence().createScaledInfluence(eatenAmount);
		eater.applyStatusInfluence(statusInfluence);
		
		if (this.amount == 0) {
			this.getSquare().removeSquareObject(this);
			return this.amount;
		}
		final float eatenRatio = eatenAmount / (float) this.baseAmount;

		final int eatenRegionArea = (int) (this.originTextureRegionArea * eatenRatio);
		TextureRegion imageTextureRegion = this.getIconMainImageTextureRegion();
		final int currentTextureRegionWidth = imageTextureRegion.getRegionWidth();
		final int currentTextureRegionHeight = imageTextureRegion.getRegionHeight();

		if (direction == Direction.UP) {
			final int eatenRegionHeight = eatenRegionArea / currentTextureRegionWidth;
			final int afterEatenTextureRegionHeight = currentTextureRegionHeight - eatenRegionHeight;
			imageTextureRegion.setRegionY(imageTextureRegion.getRegionY() + eatenRegionHeight);
			imageTextureRegion.setRegionHeight(afterEatenTextureRegionHeight);
			this.getIconMainImage().setScaleY(afterEatenTextureRegionHeight / (float) this.originTextureRegionHeight);
		}

		if (direction == Direction.DOWN) {
			final int eatenRegionHeight = eatenRegionArea / currentTextureRegionWidth;
			final int afterEatenTextureRegionHeight = currentTextureRegionHeight - eatenRegionHeight;
			imageTextureRegion.setRegionHeight(afterEatenTextureRegionHeight);
			this.getIconMainImage().setScaleY(afterEatenTextureRegionHeight / (float) this.originTextureRegionHeight);
		}

		if (direction == Direction.LEFT) {
			final int eatenRegionWidth = eatenRegionArea / currentTextureRegionHeight;
			final int afterEatenTextureRegionWidth = currentTextureRegionWidth - eatenRegionWidth;
			imageTextureRegion.setRegionX(imageTextureRegion.getRegionX() + eatenRegionWidth);
			imageTextureRegion.setRegionWidth(afterEatenTextureRegionWidth);
			float newScaleX = afterEatenTextureRegionWidth / (float) this.originTextureRegionWidth;
			this.getIconMainImage().setScaleX(newScaleX);
			this.getIconMainImage().setX((1 - newScaleX) * this.getIconMainImage().getWidth());
		}

		if (direction == Direction.RIGHT) {
			final int eatenRegionWidth = eatenRegionArea / currentTextureRegionHeight;
			final int afterEatenTextureRegionWidth = currentTextureRegionWidth - eatenRegionWidth;
			imageTextureRegion.setRegionWidth(afterEatenTextureRegionWidth);
			this.getIconMainImage().setScaleX(afterEatenTextureRegionWidth / (float) this.originTextureRegionWidth);
		}
		return eatenAmount;
	}

	/**
	 * resurrection!
	 */
	public void resurrection() {
		this.amount = this.baseAmount;
		TextureRegion imageTextureRegion = this.getIconMainImageTextureRegion();
		imageTextureRegion.setRegionX(0);
		imageTextureRegion.setRegionY(0);
		imageTextureRegion.setRegionWidth(this.originTextureRegionWidth);
		imageTextureRegion.setRegionHeight(this.originTextureRegionHeight);
		this.getIconMainImage().setPosition(0, 0);
		this.getIconMainImage().setScale(1);
	}

	/**
	 * @return true if this is being eaten.
	 */
	public boolean isBeingEaten() {
		return this.baseAmount != this.amount;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return this.amount;
	}

	private EatableObjectType getEatableObjectType() {
		return (EatableObjectType) super.getType();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EatableObject) {
			return this.equals((EatableObject) obj);
		}
		return super.equals(obj);
	}

	/**
	 * @param obj
	 * @return true if type, amount and values of image is same.
	 */
	public boolean equals(EatableObject obj) {
		if (this.getType() != obj.getType()) {
			return false;
		}
		final Image thisIconMainImage = this.getIconMainImage();
		final Image objIconMainImage = obj.getIconMainImage();
		if (thisIconMainImage.getX() != objIconMainImage.getX() || thisIconMainImage.getY() != objIconMainImage.getY()) {
			return false;
		}
		if (thisIconMainImage.getScaleX() != objIconMainImage.getScaleX() || thisIconMainImage.getScaleY() != objIconMainImage.getScaleY()) {
			return false;
		}
		TextureRegion thisImageTextureRegion = this.getIconMainImageTextureRegion();
		TextureRegion objImageTextureRegion = obj.getIconMainImageTextureRegion();
		if (thisImageTextureRegion.getRegionX() != objImageTextureRegion.getRegionX() || thisImageTextureRegion.getRegionY() != objImageTextureRegion.getRegionY()) {
			return false;
		}
		if (thisImageTextureRegion.getRegionWidth() != objImageTextureRegion.getRegionWidth() || thisImageTextureRegion.getRegionHeight() != objImageTextureRegion.getRegionHeight()) {
			return false;
		}
		return true;
	}

	/**
	 * @param amount
	 */
	void setAmount(int amount) {
		this.amount = amount;
	}
}
