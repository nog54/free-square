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

package org.nognog.freeSquare.ui;

import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PlayerObserver;
import org.nognog.gdx.util.ui.ColorUtils;
import org.nognog.gdx.util.ui.ImageIncludedItemList;
import org.nognog.gdx.util.ui.UiUtils;

import com.badlogic.gdx.graphics.Color;
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
public abstract class PlayerItemScrollList<T> extends ScrollPane implements PlayerObserver {

	private Player player;
	private final BitmapFont font;

	private static final float tapCountInterval = 0.4f; // [s]

	/**
	 * @param width
	 * @param height
	 * @param player
	 * @param font
	 */
	public PlayerItemScrollList(float width, float height, Player player, BitmapFont font) {
		super(null);
		this.setPlayer(player);
		this.setWidget(this.createList(font));
		this.font = font;
		this.setupOverscroll(0, 0, 0);
		this.setWidth(width);
		this.setHeight(height);

		final ActorGestureListener listener = new ActorGestureListener() {

			private List<T> list = PlayerItemScrollList.this.getList();
			private T lastTouchDownedItem;
			private T lastSelectedItem;
			private boolean isSameItemTouch;

			private long lastTapTime;
			private int sameItemSuccessiveTapCount = 0;

			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				this.isSameItemTouch = this.lastTouchDownedItem == this.list.getSelected();
				this.lastTouchDownedItem = this.list.getSelected();
				if (this.isSameItemTouch) {
					PlayerItemScrollList.this.setFlickScroll(false);
					return;
				}
				PlayerItemScrollList.this.setFlickScroll(true);
				if (this.lastSelectedItem == null) {
					this.list.setSelectedIndex(-1);
				} else {
					this.list.setSelected(this.lastSelectedItem);
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if (this.isSameItemTouch) {
					PlayerItemScrollList.this.selectedItemLongPressed(this.lastTouchDownedItem, x, y);
					return true;
				}
				return false;
			}

			@Override
			public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
				if (this.isSameItemTouch) {
					PlayerItemScrollList.this.selectedItemPanned(this.lastTouchDownedItem, x, y, deltaX, deltaY);
				}
			}

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				final long currentTapTime = TimeUtils.millis();
				final boolean isSuccessiveTap = (currentTapTime - this.lastTapTime) / 1000f < tapCountInterval;
				this.lastTapTime = currentTapTime;
				if (this.isSameItemTouch) {
					if (isSuccessiveTap) {
						this.sameItemSuccessiveTapCount = Math.max(2, this.sameItemSuccessiveTapCount + 1);
					} else {
						this.sameItemSuccessiveTapCount = 1;
					}
					PlayerItemScrollList.this.selectedItemTapped(this.lastTouchDownedItem, this.sameItemSuccessiveTapCount);
					return;
				}
				this.sameItemSuccessiveTapCount = 0;
				this.list.setSelected(this.lastTouchDownedItem);
				this.lastSelectedItem = this.lastTouchDownedItem;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				PlayerItemScrollList.this.touchUp(x, y);
			}
		};
		listener.getGestureDetector().setTapCountInterval(tapCountInterval);
		this.getWidget().addListener(listener);
	}

	private List<T> createList(BitmapFont bitmapFont) {
		ImageIncludedItemList<T> list = new ImageIncludedItemList<T>(createListStyle(bitmapFont)) {

			@Override
			protected Texture getTextureOf(T item) {
				return PlayerItemScrollList.this.getDrawTextureOf(item);
			}

			@Override
			protected Color getColorOf(T item) {
				return PlayerItemScrollList.this.getDrawTextureColorOf(item);
			}

		};
		list.setItems(this.getListItemsFromPlayer(this.getPlayer()));
		list.setSelectedIndex(-1);
		return list;
	}

	private static ListStyle createListStyle(BitmapFont font) {
		final ListStyle style = new ListStyle(font, ColorUtils.emerald, ColorUtils.nephritis, UiUtils.getPlaneTextureRegionDrawable(256, 128, Color.WHITE));
		style.background = UiUtils.getPlaneTextureRegionDrawable(256, 128, ColorUtils.clearBlack);
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

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		if (this.player != null) {
			this.player.removeObserver(this);
		}
		this.player = player;
		if (this.player != null) {
			this.player.addObserver(this);
		}
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
		this.getList().setItems(this.getListItemsFromPlayer(this.getPlayer()));
	}

	protected abstract Texture getDrawTextureOf(T item);

	protected abstract Color getDrawTextureColorOf(T item);

	protected abstract T[] getListItemsFromPlayer(Player setupPlayer);

	/**
	 * dispose
	 */
	public void dispose() {
		if (this.player != null) {
			this.player.removeObserver(this);
		}
		this.setWidget(null);
	}

}
