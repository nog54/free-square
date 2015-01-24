package org.nognog.freeSquare.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.model.item.DrawableItem;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author goshi 2015/01/17
 */
public class ItemList extends ScrollPane implements CameraObserver {
	private static Color emerald = Color.valueOf("2ecc71"); //$NON-NLS-1$
	private static Color nephritis = Color.valueOf("27ae60"); //$NON-NLS-1$
	private static Color clearBlack = new Color(0, 0, 0, 0.75f);

	/**
	 * @param camera
	 * @param items
	 * @param font
	 */
	public ItemList(Camera camera, Item<?, ?>[] items, BitmapFont font) {
		super(createList(items, font));
		this.setupOverscroll(0, 0, 0);
		this.setWidth(camera.viewportWidth / 2);
		this.setHeight(camera.viewportHeight / 2);
		this.getWidget().addListener(new ActorGestureListener() {

			private List<Item<?, ?>> list = ItemList.this.getList();
			private Item<?, ?> lastTouchDownedItem;
			private Item<?, ?> lastSelectedItem;
			private boolean isSameItemTouch;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.isSameItemTouch = this.lastTouchDownedItem == this.list.getSelected();
				this.lastTouchDownedItem = this.list.getSelected();
				if (this.isSameItemTouch) {
					return;
				}
				if (this.lastSelectedItem == null) {
					this.list.setSelectedIndex(-1);
				} else {
					this.list.setSelected(this.lastSelectedItem);
				}
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (this.isSameItemTouch) {
					ItemList.this.selectedItemTapped(this.lastTouchDownedItem);
					return;
				}
				this.list.setSelected(this.lastTouchDownedItem);
				this.lastSelectedItem = this.lastTouchDownedItem;
			}
		});
	}

	private static List<Item<?, ?>> createList(Item<?, ?>[] items, BitmapFont font) {
		ImageIncludedItemList list = new ImageIncludedItemList(createListStyle(font));
		list.setItems(items);
		list.setSelectedIndex(-1);
		return list;
	}

	private static ListStyle createListStyle(BitmapFont font) {
		final ListStyle style = new ListStyle(font, emerald, nephritis, UiUtils.createPlaneTextureRegionDrawable(256, 128, Color.WHITE));
		style.background = UiUtils.createPlaneTextureRegionDrawable(256, 128, clearBlack);
		return style;
	}

	@SuppressWarnings("unchecked")
	protected List<Item<?, ?>> getList() {
		return (List<Item<?, ?>>) this.getWidget();
	}

	/**
	 * @author goshi 2015/01/19
	 */
	private static class ImageIncludedItemList extends List<Item<?, ?>> {
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
				Item<?, ?> listItem = this.getItems().get(i);
				boolean selected = this.getSelection().contains(listItem);
				if (selected) {
					selectedDrawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
					font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
				}
				font.draw(batch, listItem.toString(), x + textOffsetX, y + itemY - textOffsetY);
				if (selected) {
					font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
				}

				if (listItem instanceof DrawableItem) {
					final Texture drawTexture = ((DrawableItem) listItem).getTexture();
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
					batch.setColor(((DrawableItem) listItem).getColor());
					batch.draw(drawTexture, x + textOffsetX + this.getWidth() - itemHeight - rightSpace, y + itemY - itemHeight + drawImageInterval / 2, drawImageWidth, drawImageHeight);
					batch.setColor(oldColor);
				}

				itemY -= itemHeight;
			}
		}

	}

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x - currentCameraZoom * camera.viewportWidth / 2;
		final float newY = camera.position.y - currentCameraZoom * this.getHeight();
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

	protected void selectedItemTapped(Item<?, ?> tappedItem) {
		// default is empty.
	}
}
