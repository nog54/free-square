package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.model.item.DrawableItem;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PlayerObserver;
import org.nognog.freeSquare.model.player.PossessedItem;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/01/17
 */
public class PlayerItemList extends ScrollPane implements PlayerObserver, CameraObserver {
	private static Color emerald = Color.valueOf("2ecc71"); //$NON-NLS-1$
	private static Color nephritis = Color.valueOf("27ae60"); //$NON-NLS-1$
	private static Color clearBlack = new Color(0, 0, 0, 0.75f);

	private Player player;

	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayerItemList(Camera camera, Player player, BitmapFont font) {
		super(createList(player, font));
		this.player = player;
		this.player.addObserver(this);
		this.setupOverscroll(0, 0, 0);
		this.setWidth(camera.viewportWidth / 2);
		this.setHeight(camera.viewportHeight / 2);

		this.getWidget().addListener(new ActorGestureListener() {

			private List<PossessedItem<?>> list = PlayerItemList.this.getList();
			private PossessedItem<?> lastTouchDownedItem;
			private PossessedItem<?> lastSelectedItem;
			private boolean isSameItemTouch;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.isSameItemTouch = this.lastTouchDownedItem == this.list.getSelected();
				this.lastTouchDownedItem = this.list.getSelected();
				if (this.isSameItemTouch) {
					PlayerItemList.this.setFlickScroll(false);
					return;
				}
				PlayerItemList.this.setFlickScroll(true);
				if (this.lastSelectedItem == null) {
					this.list.setSelectedIndex(-1);
				} else {
					this.list.setSelected(this.lastSelectedItem);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if (this.isSameItemTouch) {
					PlayerItemList.this.selectedItemLongPressed(this.lastTouchDownedItem, x, y);
					return true;
				}
				return false;
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (this.isSameItemTouch) {
					PlayerItemList.this.selectedItemPanned(this.lastTouchDownedItem, x, y, deltaX, deltaY);
				}
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (this.isSameItemTouch) {
					PlayerItemList.this.selectedItemTapped(this.lastTouchDownedItem);
					return;
				}
				this.list.setSelected(this.lastTouchDownedItem);
				this.lastSelectedItem = this.lastTouchDownedItem;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				PlayerItemList.this.touchUp(x, y);
			}
		});
	}

	private static List<PossessedItem<?>> createList(Player player, BitmapFont font) {
		ImageIncludedItemList<PossessedItem<?>> list = new ImageIncludedItemList<PossessedItem<?>>(createListStyle(font)) {

			@Override
			protected Texture getTextureOf(PossessedItem<?> possessedItem) {
				Item<?, ?> item = possessedItem.getItem();
				if (item instanceof DrawableItem) {
					return ((DrawableItem) item).getTexture();
				}
				return null;
			}

			@Override
			protected Color getColorOf(PossessedItem<?> possessedItem) {
				Item<?, ?> item = possessedItem.getItem();
				if (item instanceof DrawableItem) {
					return ((DrawableItem) item).getColor();
				}
				return null;
			}

		};
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

	protected void selectedItemTapped(PossessedItem<?> tappedItem) {
		// default is empty.
	}

	protected void selectedItemPanned(PossessedItem<?> pannedItem, float x, float y, float deltaX, float deltaY) {
		// default is empty.
	}

	protected void selectedItemLongPressed(PossessedItem<?> pannedItem, float x, float y) {
		// default is empty.
	}

	protected void touchUp(float x, float y) {
		// default is empty.
	}

	@Override
	public void updatePlayer() {
		List<PossessedItem<?>> list = this.getList();
		list.setItems(this.player.getItemBox().toItemArray());
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
