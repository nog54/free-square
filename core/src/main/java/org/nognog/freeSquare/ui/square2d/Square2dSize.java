package org.nognog.freeSquare.ui.square2d;

/**
 * @author goshi 2014/12/26
 */
@SuppressWarnings("javadoc")
public enum Square2dSize {

	SMALL(1024), MEDIUM(1536), LARGE(2048);

	private final float width;

	private Square2dSize(float width) {
		this.width = width;
	}

	/**
	 * @return width
	 */
	public float getWidth() {
		return this.width;
	}
}
