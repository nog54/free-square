package org.nognog.freeSquare;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author goshi 2015/01/08
 */
public class MenuTable extends Table {

	private BitmapFont font;
	private OrthographicCamera camera = new OrthographicCamera();

	MenuTable(BitmapFont font, final float viewportWidth, final float viewportHeight) {
		this.font = font;
		this.camera.setToOrtho(false, viewportWidth, viewportHeight);
		TextButton button = this.createTextButton("Quick", Color.valueOf("2ecc71"), Color.valueOf("27ae60")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.add(button);
	}

	private TextButton createTextButton(String text, Color up, Color down) {
		TextureRegionDrawable upTexture = createPlaneTextureRegionDrawable(up);
		TextureRegionDrawable downTexture = createPlaneTextureRegionDrawable(down);
		TextButtonStyle buttonStyle = new TextButtonStyle(upTexture, downTexture, upTexture, this.font);
		return new TextButton(text, buttonStyle);
	}

	private static TextureRegionDrawable createPlaneTextureRegionDrawable(Color color) {
		Pixmap pixmap = new Pixmap(256, 128, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
	}

}
