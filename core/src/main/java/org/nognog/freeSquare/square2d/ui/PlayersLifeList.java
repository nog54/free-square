package org.nognog.freeSquare.square2d.ui;

import org.nognog.freeSquare.model.life.Life;
import org.nognog.freeSquare.model.player.Player;
import org.nognog.freeSquare.square2d.object.types.LifeObjectType;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;

/**
 * @author goshi 2015/02/22
 */
public class PlayersLifeList extends PlayerLinkingScrollList<Life>{
	
	/**
	 * @param camera
	 * @param player
	 * @param font
	 */
	public PlayersLifeList(Camera camera, Player player, BitmapFont font) {
		super(camera, player, font);
	}

	@Override
	protected Texture getDrawTextureOf(Life item) {
		final LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(item);
		return bindingLifeObjectType.getTexture();
	}

	@Override
	protected Color getDrawTextureColorOf(Life item) {
		final LifeObjectType bindingLifeObjectType = LifeObjectType.getBindingLifeObjectType(item);
		return bindingLifeObjectType.getColor();
	}

	@Override
	protected Array<Life> getShowListItemsFromPlayer(Player setupPlayer) {
		return setupPlayer.getLifes();
	}

}
