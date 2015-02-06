package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PlayerObserver;
import org.nognog.freeSquare.square2d.object.Square2dObjectType.LifeObjectType;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author goshi 2015/01/17
 */
public class PlayersLifeList extends ScrollPane implements PlayerObserver, CameraObserver {
	private static Color emerald = Color.valueOf("2ecc71"); //$NON-NLS-1$
	private static Color nephritis = Color.valueOf("27ae60"); //$NON-NLS-1$
	private static Color clearBlack = new Color(0, 0, 0, 0.75f);

	private Player player;

	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayersLifeList(Camera camera, Player player, BitmapFont font) {
		super(createLifeList(player, font));
		this.player = player;
		this.player.addObserver(this);
		this.setupOverscroll(0, 0, 0);
		this.setWidth(camera.viewportWidth / 2);
		this.setHeight(camera.viewportHeight / 2);

		this.getWidget().addListener(new ActorGestureListener() {

			private List<Life> list = PlayersLifeList.this.getList();
			private Life lastTouchDownedItem;
			private Life lastSelectedItem;
			private boolean isSameItemTouch;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.isSameItemTouch = this.lastTouchDownedItem == this.list.getSelected();
				this.lastTouchDownedItem = this.list.getSelected();
				if (this.isSameItemTouch) {
					PlayersLifeList.this.setFlickScroll(false);
					return;
				}
				PlayersLifeList.this.setFlickScroll(true);
				if (this.lastSelectedItem == null) {
					this.list.setSelectedIndex(-1);
				} else {
					this.list.setSelected(this.lastSelectedItem);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if (this.isSameItemTouch) {
					PlayersLifeList.this.selectedItemLongPressed(this.lastTouchDownedItem, x, y);
					return true;
				}
				return false;
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (this.isSameItemTouch) {
					PlayersLifeList.this.selectedItemPanned(this.lastTouchDownedItem, x, y, deltaX, deltaY);
				}
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (this.isSameItemTouch) {
					PlayersLifeList.this.selectedItemTapped(this.lastTouchDownedItem);
					return;
				}
				this.list.setSelected(this.lastTouchDownedItem);
				this.lastSelectedItem = this.lastTouchDownedItem;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				PlayersLifeList.this.touchUp(x, y);
			}
		});
	}

	private static List<Life> createLifeList(Player player, BitmapFont font) {
		ImageIncludedItemList list = new ImageIncludedItemList(createListStyle(font));
		list.setItems(player.getLifes());
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
	public List<Life> getList() {
		return (List<Life>) this.getWidget();
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
	private static class ImageIncludedItemList extends List<Life> {
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
				Life listItem = this.getItems().get(i);
				boolean selected = this.getSelection().contains(listItem);
				if (selected) {
					selectedDrawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
					font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
				}
				font.draw(batch, listItem.toString(), x + textOffsetX, y + itemY - textOffsetY);
				if (selected) {
					font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
				}

				LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(listItem);
				final Texture drawTexture = bindingLifeObjectType.getTexture();
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
				Color itemColor = bindingLifeObjectType.getColor();
				batch.setColor(itemColor.r, itemColor.g, itemColor.b, oldColor.a);
				batch.draw(drawTexture, x + textOffsetX + this.getWidth() - itemHeight - rightSpace, y + itemY - itemHeight + drawImageInterval / 2, drawImageWidth, drawImageHeight);
				batch.setColor(oldColor);

				itemY -= itemHeight;
			}
		}

	}

	protected void selectedItemTapped(Life tappedItem) {
		// default is empty.
	}

	protected void selectedItemPanned(Life pannedItem, float x, float y, float deltaX, float deltaY) {
		// default is empty.
	}

	protected void selectedItemLongPressed(Life pannedItem, float x, float y) {
		// default is empty.
	}

	protected void touchUp(float x, float y) {
		// default is empty.
	}

	@Override
	public void updatePlayer() {
		List<Life> list = this.getList();
		list.setItems(this.getPlayer().getLifes());
	}

	/**
	 * dispose
	 */
	public void dispose() {
		if (this.player != null) {
			this.player.removeObserver(this);
		}
		this.setWidget(null);
	}

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x;
		final float newY = camera.position.y - currentCameraZoom * this.getHeight();
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

}
