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

package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.square2d.Direction;
import org.nognog.freeSquare.square2d.object.types.Square2dObjectType;
import org.nognog.freeSquare.square2d.object.types.eatable.EatableObjectType;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * @author goshi 2015/01/30
 */
public class EatableObject extends Square2dObject implements LandObject {
	private int baseAmount;
	private int originTextureRegionWidth;
	private int originTextureRegionHeight;
	private int originTextureRegionArea;
	private int amount;

	private EatableObject() {
		// used by json
		super();
	}

	/**
	 * @param type
	 */
	public EatableObject(EatableObjectType type) {
		this();
		this.setupType(type);
	}

	@Override
	protected void setupType(Square2dObjectType<?> type) {
		super.setupType(type);
		if (type instanceof EatableObjectType) {
			this.setupEatableType((EatableObjectType) type);
		}
	}

	private void setupEatableType(EatableObjectType type) {
		this.baseAmount = type.getQuantity();
		this.amount = this.baseAmount;
		this.originTextureRegionWidth = this.getIconMainImageTextureRegion().getRegionWidth();
		this.originTextureRegionHeight = this.getIconMainImageTextureRegion().getRegionHeight();
		this.originTextureRegionArea = this.originTextureRegionWidth * this.originTextureRegionHeight;
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
		if (eatAmount <= 0) {
			return 0;
		}
		if (this.amount == 0) {
			return this.amount;
		}
		final int eatenAmount = (this.amount > eatAmount) ? eatAmount : this.amount;
		this.amount -= eatenAmount;
		this.getEatableObjectType().applyStatusInfluenceTo(eater, eatenAmount);
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

	@SuppressWarnings("boxing")
	@Override
	public void write(Json json) {
		super.write(json);
		json.writeField(this, "amount"); //$NON-NLS-1$
		TextureRegion imageTextureRegion = this.getIconMainImageTextureRegion();
		json.writeValue("imageX", this.getIconMainImage().getX()); //$NON-NLS-1$
		json.writeValue("imageY", this.getIconMainImage().getY()); //$NON-NLS-1$
		json.writeValue("imageScaleX", this.getIconMainImage().getScaleX()); //$NON-NLS-1$
		json.writeValue("imageScaleY", this.getIconMainImage().getScaleY()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionX", imageTextureRegion.getRegionX()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionY", imageTextureRegion.getRegionY()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionWidth", imageTextureRegion.getRegionWidth()); //$NON-NLS-1$
		json.writeValue("imageTextureRegionRegionHeight", imageTextureRegion.getRegionHeight()); //$NON-NLS-1$
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);
		json.readField(this, "amount", jsonData); //$NON-NLS-1$
		TextureRegion imageTextureRegion = this.getIconMainImageTextureRegion();
		this.getIconMainImage().setX(json.readValue("imageX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		this.getIconMainImage().setY(json.readValue("imageY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		this.getIconMainImage().setScaleX(json.readValue("imageScaleX", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		this.getIconMainImage().setScaleY(json.readValue("imageScaleY", Float.class, jsonData).floatValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionX(json.readValue("imageTextureRegionRegionX", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionY(json.readValue("imageTextureRegionRegionY", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionWidth(json.readValue("imageTextureRegionRegionWidth", Integer.class, jsonData).intValue()); //$NON-NLS-1$
		imageTextureRegion.setRegionHeight(json.readValue("imageTextureRegionRegionHeight", Integer.class, jsonData).intValue()); //$NON-NLS-1$
	}
}
