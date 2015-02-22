package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.model.item.DrawableItem;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/22
 */
public class PlayerItemList extends PlayerLinkingScrollList<PossessedItem<?>> {

	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayerItemList(Camera camera, Player player, BitmapFont font) {
		super(camera, player, font);
	}

	@Override
	protected Texture getDrawTextureOf(PossessedItem<?> possessedItem) {
		Item<?, ?> item = possessedItem.getItem();
		if (item instanceof DrawableItem) {
			return ((DrawableItem) item).getTexture();
		}
		return null;
	}

	@Override
	protected Color getDrawTextureColorOf(PossessedItem<?> possessedItem) {
		Item<?, ?> item = possessedItem.getItem();
		if (item instanceof DrawableItem) {
			return ((DrawableItem) item).getColor();
		}
		return null;
	}

	@Override
	protected Array<PossessedItem<?>> getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getItemBox().toItemArray();
	}

}
