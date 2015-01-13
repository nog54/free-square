package org.nognog.freeSquare.square2d.objects;

import org.nognog.freeSquare.Messages;
import org.nognog.freeSquare.Resources;

/**
 * @author goshi 2015/01/14
 */
@SuppressWarnings("javadoc")
public enum SquareObjectInfo {

	RIKI(Messages.getString("riki"), Resources.rikiPath, 100), //$NON-NLS-1$
	TOFU(Messages.getString("tofu"), Resources.tofuPath, 75); //$NON-NLS-1$

	private SquareObjectInfo(String name, String texturePath, float logicalWidth) {
		this.name = name;
		this.texturePath = texturePath;
		this.logicalWidth = logicalWidth;
	}

	private final String name;
	private final String texturePath;
	private final float logicalWidth;

	public String getName() {
		return this.name;
	}

	public String getTexturePath() {
		return this.texturePath;
	}

	public float getLogicalWidth() {
		return this.logicalWidth;
	}
}
