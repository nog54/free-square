package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.Settings;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PlayerObserver;

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
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author goshi 2015/01/17
 * @param <T>
 */
public abstract class PlayerLinkingScrollList<T> extends ScrollPane implements PlayerObserver, CameraObserver {
	private static final Color clearBlack = new Color(0, 0, 0, 0.75f);

	private final Player player;
	private final BitmapFont font;

	private static final float tapCountInterval = 0.6f; // [s]

	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayerLinkingScrollList(Camera camera, Player player, BitmapFont font) {
		super(null);
		this.player = player;
		this.player.addObserver(this);
		this.setWidget(this.createList(font));
		this.font = font;
		this.setupOverscroll(0, 0, 0);
		this.setWidth(camera.viewportWidth / Settings.getGoldenRatio());
		this.setHeight(camera.viewportHeight / 2);

		final ActorGestureListener listener = new ActorGestureListener() {

			private List<T> list = PlayerLinkingScrollList.this.getList();
			private T lastTouchDownedItem;
			private T lastSelectedItem;
			private boolean isSameItemTouch;

			private long lastTapTime;
			private int sameItemTapCount = 0;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.isSameItemTouch = this.lastTouchDownedItem == this.list.getSelected();
				this.lastTouchDownedItem = this.list.getSelected();
				if (this.isSameItemTouch) {
					PlayerLinkingScrollList.this.setFlickScroll(false);
					return;
				}
				PlayerLinkingScrollList.this.setFlickScroll(true);
				if (this.lastSelectedItem == null) {
					this.list.setSelectedIndex(-1);
				} else {
					this.list.setSelected(this.lastSelectedItem);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if (this.isSameItemTouch) {
					PlayerLinkingScrollList.this.selectedItemLongPressed(this.lastTouchDownedItem, x, y);
					return true;
				}
				return false;
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (this.isSameItemTouch) {
					PlayerLinkingScrollList.this.selectedItemPanned(this.lastTouchDownedItem, x, y, deltaX, deltaY);
				}
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				final long currentTapTime = TimeUtils.millis();
				final boolean isSuccessiveTap = (currentTapTime - this.lastTapTime) / 1000f < tapCountInterval;
				this.lastTapTime = currentTapTime;
				if (this.isSameItemTouch) {
					if (!isSuccessiveTap) {
						this.sameItemTapCount = 0;
					}
					this.sameItemTapCount++;
					PlayerLinkingScrollList.this.selectedItemTapped(this.lastTouchDownedItem, this.sameItemTapCount);
					return;
				}
				this.sameItemTapCount = 0;
				this.list.setSelected(this.lastTouchDownedItem);
				this.lastSelectedItem = this.lastTouchDownedItem;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				PlayerLinkingScrollList.this.touchUp(x, y);
			}
		};
		listener.getGestureDetector().setTapCountInterval(tapCountInterval);
		this.getWidget().addListener(listener);
	}

	private List<T> createList(BitmapFont bitmapFont) {
		ImageIncludedItemList<T> list = new ImageIncludedItemList<T>(createListStyle(bitmapFont)) {

			@Override
			protected Texture getTextureOf(T item) {
				return PlayerLinkingScrollList.this.getDrawTextureOf(item);
			}

			@Override
			protected Color getColorOf(T item) {
				return PlayerLinkingScrollList.this.getDrawTextureColorOf(item);
			}

		};
		list.setItems(this.getShowListItemsFromPlayer(this.player));
		list.setSelectedIndex(-1);
		return list;
	}

	private static ListStyle createListStyle(BitmapFont font) {
		final ListStyle style = new ListStyle(font, ColorUtils.emerald, ColorUtils.nephritis, UiUtils.getPlaneTextureRegionDrawable(256, 128, Color.WHITE));
		style.background = UiUtils.getPlaneTextureRegionDrawable(256, 128, clearBlack);
		return style;
	}

	/**
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<T> getList() {
		return (List<T>) this.getWidget();
	}

	/**
	 * @return font
	 */
	public BitmapFont getFont() {
		return this.font;
	}

	/**
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}

	protected void selectedItemTapped(T tappedItem, int count) {
		// default is empty.
	}

	protected void selectedItemPanned(T pannedItem, float x, float y, float deltaX, float deltaY) {
		// default is empty.
	}

	protected void selectedItemLongPressed(T longPressedItem, float x, float y) {
		// default is empty.
	}

	protected void touchUp(float x, float y) {
		// default is empty.
	}

	@Override
	public void updatePlayer() {
		this.getList().setItems(this.getShowListItemsFromPlayer(this.player));
	}

	protected abstract Texture getDrawTextureOf(T item);

	protected abstract Color getDrawTextureColorOf(T item);

	protected abstract T[] getShowListItemsFromPlayer(Player setupPlayer);

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
		final float newX = camera.position.x + currentCameraZoom * (camera.viewportWidth / 2 - this.getWidth());
		final float newY = camera.position.y - currentCameraZoom * this.getHeight();
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

}
