package org.nognog.freeSquare.square2d;

/**
 * @author goshi 2015/01/31
 */
@SuppressWarnings("javadoc")
public enum Direction {
	UP, DOWN, LEFT, RIGHT, ;

	public static Direction getCounterDirectionOf(Direction direction) {
		if (direction == UP) {
			return DOWN;
		}
		if (direction == DOWN) {
			return UP;
		}
		if (direction == LEFT) {
			return RIGHT;
		}
		if (direction == RIGHT) {
			return LEFT;
		}
		return null;
	}
}
