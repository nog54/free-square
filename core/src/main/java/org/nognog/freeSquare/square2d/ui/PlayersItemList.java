package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.model.SimpleDrawable;
import org.nognog.freeSquare.model.item.Item;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.player.PossessedItem;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author goshi 2015/02/22
 */
public class PlayersItemList extends PlayerLinkingScrollList<PossessedItem<?>> {

	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayersItemList(Camera camera, Player player, BitmapFont font) {
		super(camera, player, font);
	}

	@Override
	protected Texture getDrawTextureOf(PossessedItem<?> possessedItem) {
		Item<?, ?> item = possessedItem.getItem();
		if (item instanceof SimpleDrawable) {
			return ((SimpleDrawable) item).getSimpleTexture();
		}
		return null;
	}

	@Override
	protected Color getDrawTextureColorOf(PossessedItem<?> possessedItem) {
		Item<?, ?> item = possessedItem.getItem();
		if (item instanceof SimpleDrawable) {
			return ((SimpleDrawable) item).getColor();
		}
		return null;
	}

	@Override
	protected PossessedItem<?>[] getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getItemBox().toItemArray();
	}

}
