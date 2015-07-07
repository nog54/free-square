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

package org.nognog.gdx.util.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author goshi 2015/01/19
 * @param <T>
 *            element type
 */
public abstract class ImageIncludedItemList<T> extends List<T> {
	private static final GlyphLayout glyphLayout = new GlyphLayout();

	/**
	 * @param style
	 */
	public ImageIncludedItemList(ListStyle style) {
		super(style);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.validate();

		BitmapFont font = this.getStyle().font;
		Drawable selectedDrawable = this.getStyle().selection;
		Color fontColorSelected = this.getStyle().fontColorSelected;
		Color fontColorUnselected = this.getStyle().fontColorUnselected;

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX(), y = getY(), width = getWidth(), height = getHeight();
		float itemY = height;
		final float textOffsetX = selectedDrawable.getLeftWidth();
		final float textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();

		Drawable background = this.getStyle().background;
		if (background != null) {
			background.draw(batch, x, y, width, height);
			float leftWidth = background.getLeftWidth();
			x += leftWidth;
			itemY -= background.getTopHeight();
			width -= leftWidth + background.getRightWidth();
		}

		final float itemHeight = this.getItemHeight();
		font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
		for (int i = 0; i < this.getItems().size; i++) {
			T listItem = this.getItems().get(i);
			boolean selected = this.getSelection().contains(listItem);
			if (selected) {
				selectedDrawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
				font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
			}
			final String drawString = listItem.toString();
			font.draw(batch, drawString, x + textOffsetX, y + itemY - textOffsetY);
			if (selected) {
				font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
			}
			this.drawItemImage(batch, font, x, y, itemY, textOffsetX, itemHeight, listItem, drawString);
			itemY -= itemHeight;
		}
	}

	private void drawItemImage(Batch batch, BitmapFont font, float x, float y, float itemY, final float textOffsetX, final float itemHeight, T listItem, final String drawString) {
		final float rightSpace = itemHeight / 8;
		final float drawImageInterval = itemHeight * 0.05f;
		final float imageDrawX = x + textOffsetX + this.getWidth() - itemHeight - rightSpace;
		final float imageDrawY = y + itemY - itemHeight + drawImageInterval / 2;
		glyphLayout.setText(font, drawString);
		if (glyphLayout.width < imageDrawX) {
			final Texture drawTexture = this.getTextureOf(listItem);
			if (drawTexture != null) {
				final float textureWidth = drawTexture.getWidth();
				final float textureHeight = drawTexture.getHeight();

				final float drawImageWidth, drawImageHeight;
				if (textureWidth > textureHeight) {
					drawImageWidth = itemHeight - drawImageInterval;
					drawImageHeight = drawImageWidth * (textureHeight / textureWidth);
				} else {
					drawImageHeight = itemHeight - drawImageInterval;
					drawImageWidth = drawImageHeight * (textureWidth / textureHeight);
				}

				Color oldColor = batch.getColor();
				Color itemColor = this.getColorOf(listItem);
				batch.setColor(itemColor.r, itemColor.g, itemColor.b, oldColor.a);

				batch.draw(drawTexture, imageDrawX, imageDrawY, drawImageWidth, drawImageHeight);
				batch.setColor(oldColor);
			}
		}
	}

	protected abstract Texture getTextureOf(T item);

	protected abstract Color getColorOf(T item);

}