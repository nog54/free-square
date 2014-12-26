package org.nognog.freeSquare.square2d;

/**
 * @author goshi 2014/12/26
 */
@SuppressWarnings("javadoc")
public enum Square2DSize {

	SMALL(1024), MEDIUM(1536), LARGE(2048);

	private int width;

	private Square2DSize(int width) {
		this.width = width;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return this.width;
	}
}
