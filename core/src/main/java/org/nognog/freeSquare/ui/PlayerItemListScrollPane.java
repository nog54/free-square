package org.nognog.freeSquare.ui;

import org.nognog.freeSquare.model.item.DrawableItem;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PlayerObserver;
import org.nognog.freeSquare.model.player.PossessedItem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author goshi 2015/01/17
 */
public class PlayerItemListScrollPane extends ScrollPane implements PlayerObserver {
	private static Color emerald = Color.valueOf("2ecc71"); //$NON-NLS-1$
	private static Color nephritis = Color.valueOf("27ae60"); //$NON-NLS-1$
	private static Color clearBlack = new Color(0, 0, 0, 0.75f);

	private Player player;

	/**
	 * @param player
	 * @param font
	 */
	public PlayerItemListScrollPane(Player player, BitmapFont font) {
		super(createList(player, font));
		this.player = player;
		this.player.addObserver(this);
		this.setupOverscroll(0, 0, 0);
	}

	private static List<PossessedItem<?>> createList(Player player, BitmapFont font) {
		ImageIncludedItemList list = new ImageIncludedItemList(createListStyle(font));
		list.setItems(player.getItemBox().toItemArray());
		list.setSelectedIndex(-1);
		return list;
	}

	private static ListStyle createListStyle(BitmapFont font) {
		final ListStyle style = new ListStyle(font, emerald, nephritis, UiUtils.createPlaneTextureRegionDrawable(256, 128, Color.WHITE));
		style.background = UiUtils.createPlaneTextureRegionDrawable(256, 128, clearBlack);
		return style;
	}

	/**
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<PossessedItem<?>> getList() {
		return (List<PossessedItem<?>>) this.getWidget();
	}

	/**
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * @author goshi 2015/01/19
	 */
	private static class ImageIncludedItemList extends List<PossessedItem<?>> {
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
				PossessedItem<?> listItem = this.getItems().get(i);
				boolean selected = this.getSelection().contains(listItem);
				if (selected) {
					selectedDrawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
					font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
				}
				font.draw(batch, listItem.toString(), x + textOffsetX, y + itemY - textOffsetY);
				if (selected) {
					font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
				}

				Item<?, ?> item = listItem.getItem();
				if (item instanceof DrawableItem) {
					final Texture drawTexture = ((DrawableItem) item).getTexture();
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
					batch.setColor(((DrawableItem) item).getColor());
					batch.draw(drawTexture, x + textOffsetX + this.getWidth() - itemHeight - rightSpace, y + itemY - itemHeight + drawImageInterval / 2, drawImageWidth, drawImageHeight);
					batch.setColor(oldColor);
				}

				itemY -= itemHeight;
			}
		}

	}

	@Override
	public void update() {
		List<PossessedItem<?>> list = this.getList();
		list.setItems(this.player.getItemBox().toItemArray());
	}
	
	/**
	 * dispose
	 */
	public void dispose(){
		if(this.player != null){
			this.player.removeObserver(this);
		}
		this.setWidget(null);
	}

}
