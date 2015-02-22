package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.CameraObserver;
import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PlayerObserver;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;

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
		ImageIncludedItemList<Life> list = new ImageIncludedItemList<Life>(createListStyle(font)){

			@Override
			protected Texture getTextureOf(Life item) {
				final LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(item);
				return bindingLifeObjectType.getTexture();
			}

			@Override
			protected Color getColorOf(Life item) {
				final LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(item);
				return bindingLifeObjectType.getColor();
			}
			
		};
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
