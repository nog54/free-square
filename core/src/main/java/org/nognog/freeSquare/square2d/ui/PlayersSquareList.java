package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.model.square.Square;
import org.nognog.freeSquare.square2d.Square2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
		if (item instanceof Square2d) {
			return ((Square2d) item).getSimpleTexture();
		}
		return null;
	}

	@Override
	protected Color getDrawTextureColorOf(Square<?> item) {
		return Color.WHITE;
	}

	@Override
	protected Square<?>[] getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getSquares();
	}

}
