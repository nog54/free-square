package org.nognog.freeSquare.square2d.object;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author goshi 2015/03/13
 */
public class Square2dObjectIcon extends Group {
	private final Image mainImage;

	Square2dObjectIcon(Image mainIcon) {
		if (mainIcon == null) {
			throw new IllegalArgumentException();
		}
		this.mainImage = mainIcon;
		this.addActor(this.mainImage);
		this.setWidth(mainIcon.getWidth());
		this.setHeight(mainIcon.getHeight());
		this.setOriginX(this.getWidth() / 2);
	}

	/**
	 * @return the mainImage
	 */
	public Image getMainImage() {
		return this.mainImage;
	}
}
