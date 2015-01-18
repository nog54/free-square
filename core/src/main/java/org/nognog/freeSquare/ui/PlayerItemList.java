package org.nognog.freeSquare.ui;

import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

/**
 * @author goshi 2015/01/17
 */
public class PlayerItemList extends ScrollPane {
	private static Color emerald = Color.valueOf("2ecc71"); //$NON-NLS-1$
	private static Color nephritis = Color.valueOf("27ae60"); //$NON-NLS-1$
	private static Color clearBlack = new Color(0, 0, 0, 0.75f);

	private Player player;

	/**
	 * @param player
	 * @param font
	 */
	public PlayerItemList(Player player, BitmapFont font) {
		super(createList(player, font));
		this.player = player;
		this.setupOverscroll(0, 0, 0);
	}

	private static List<PossessedItem<?>> createList(Player player, BitmapFont font) {
		List<PossessedItem<?>> list = new List<>(createListStyle(font));
		list.setItems(player.getItemBox().getItemArray());
		return list;
	}

	private static ListStyle createListStyle(BitmapFont font) {
		final ListStyle style = new ListStyle(font, emerald, nephritis, UiUtils.createPlaneTextureRegionDrawable(256, 128, Color.WHITE));
		style.background = UiUtils.createPlaneTextureRegionDrawable(256, 128, clearBlack);
		return style;
	}

	/**
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}
}
