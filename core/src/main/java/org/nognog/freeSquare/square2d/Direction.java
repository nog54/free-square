/** Copyright 2015 Goshi Noguchi (noggon54@gmail.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. */

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
