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

package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.Settings;
import org.nognog.freeSquare.model.SimpleDrawable;
import org.nognog.freeSquare.model.item.Item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author goshi 2015/01/17
 */
public class ItemList extends ScrollPane implements CameraObserver {
	private static Color clearBlack = new Color(0, 0, 0, 0.75f);

	/**
	 * @param camera
	 * @param items
	 * @param font
	 */
	public ItemList(Camera camera, Item<?, ?>[] items, BitmapFont font) {
		super(createList(items, font));
		this.setupOverscroll(0, 0, 0);
		this.setWidth(camera.viewportWidth / Settings.getGoldenRatio());
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
		ImageIncludedItemList<Item<?, ?>> list = new ImageIncludedItemList<Item<?, ?>>(createListStyle(font)) {

			@Override
			protected Texture getTextureOf(Item<?, ?> item) {
				if (item instanceof SimpleDrawable) {
					return ((SimpleDrawable) item).getSimpleTexture();
				}
				return null;
			}

			@Override
			protected Color getColorOf(Item<?, ?> item) {
				if (item instanceof SimpleDrawable) {
					return ((SimpleDrawable) item).getColor();
				}
				return null;
			}

		};
		list.setItems(items);
		list.setSelectedIndex(-1);
		return list;
	}

	private static ListStyle createListStyle(BitmapFont font) {
		final ListStyle style = new ListStyle(font, ColorUtils.emerald, ColorUtils.nephritis, UiUtils.getPlaneTextureRegionDrawable(256, 128, Color.WHITE));
		style.background = UiUtils.getPlaneTextureRegionDrawable(256, 128, clearBlack);
		return style;
	}

	@SuppressWarnings("unchecked")
	protected List<Item<?, ?>> getList() {
		return (List<Item<?, ?>>) this.getWidget();
	}

	@Override
	public void updateCamera(Camera camera) {
		final float currentCameraZoom = ((OrthographicCamera) camera).zoom;
		final float newX = camera.position.x + currentCameraZoom * (camera.viewportWidth / 2 - this.getWidth());
		final float newY = camera.position.y - currentCameraZoom * this.getHeight();
		this.setPosition(newX, newY);
		this.setScale(currentCameraZoom);
	}

	protected void selectedItemTapped(Item<?, ?> tappedItem) {
		// default is empty.
	}
}
