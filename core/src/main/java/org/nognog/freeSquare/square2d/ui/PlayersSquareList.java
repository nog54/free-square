package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square2d.SimpleSquare2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/22
 */
public class PlayersSquareList extends PlayerLinkingScrollList<Square<?>> {

	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayersSquareList(Camera camera, Player player, BitmapFont font) {
		super(camera, player, font);
	}

	@Override
	protected Texture getDrawTextureOf(Square<?> item) {
		if (item instanceof SimpleSquare2d) {
			return ((SimpleSquare2d) item).getSquare2dType().getTexture();
		}
		return null;
	}

	@Override
	protected Color getDrawTextureColorOf(Square<?> item) {
		return Color.WHITE;
	}

	@Override
	protected Array<Square<?>> getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getSquares();
	}

}
