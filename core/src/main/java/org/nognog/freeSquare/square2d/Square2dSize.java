package org.nognog.freeSquare.square2d;

/**
 * @author goshi 2014/12/26
 */
@SuppressWarnings("javadoc")
public enum Square2dSize {

	TINY(256), SMALL(1024), MEDIUM(1536), LARGE(2048);

	private final double width;

	private Square2dSize(float width) {
		this.width = width;
	}

	/**
	 * @return width
	 */
	public double getWidth() {
		return this.width;
	}
}
