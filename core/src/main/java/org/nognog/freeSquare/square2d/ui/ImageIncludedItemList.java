package org.nognog.freeSquare.square2d.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author goshi 2015/01/19
 * @param <T>
 *            element type
 */
public abstract class ImageIncludedItemList<T> extends List<T> {
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
			font.draw(batch, listItem.toString(), x + textOffsetX, y + itemY - textOffsetY);
			if (selected) {
				font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
			}
			final Texture drawTexture = this.getTextureOf(listItem);
			if (drawTexture != null) {
				final float textureWidth = drawTexture.getWidth();
				final float textureHeight = drawTexture.getHeight();
				final float drawImageInterval = itemHeight * 0.05f;
				final float drawImageWidth, drawImageHeight;
				if (textureWidth > textureHeight) {
					drawImageWidth = itemHeight - drawImageInterval;
					drawImageHeight = drawImageWidth * (textureHeight / textureWidth);
				} else {
					drawImageHeight = itemHeight - drawImageInterval;
					drawImageWidth = drawImageHeight * (textureWidth / textureHeight);
				}

				final float rightSpace = itemHeight / 8;
				Color oldColor = batch.getColor();
				Color itemColor = this.getColorOf(listItem);
				batch.setColor(itemColor.r, itemColor.g, itemColor.b, oldColor.a);
				batch.draw(drawTexture, x + textOffsetX + this.getWidth() - itemHeight - rightSpace, y + itemY - itemHeight + drawImageInterval / 2, drawImageWidth, drawImageHeight);
				batch.setColor(oldColor);
			}
			itemY -= itemHeight;
		}
	}

	protected abstract Texture getTextureOf(T item);

	protected abstract Color getColorOf(T item);

}