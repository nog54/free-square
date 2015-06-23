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

package org.nognog.freeSquare.square2d.action;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author goshi 2015/06/19
 */
public interface PrioritizableAction {

	/**
	 * @param priority
	 */
	void setPriority(int priority);

	/**
	 * @return priority
	 */
	int getPriority();

	/**
	 * @param actor
	 */
	public void setActor(Actor actor);

	/**
	 * @return true if this action is performable state
	 */
	public boolean isPerformableState();

	/**
	 * @param delta
	 * @return true if action is finished
	 */
	public boolean act(float delta);

	/**
	 * @author goshi 2015/06/22
	 */
	public static class Comparator implements java.util.Comparator<PrioritizableAction> {

		private static final Comparator instance = new Comparator();

		private Comparator() {

		}

		/**
		 * @return instance
		 */
		public static Comparator getInstance() {
			return instance;
		}

		@Override
		public int compare(PrioritizableAction o1, PrioritizableAction o2) {
			final int priority1 = o1.getPriority();
			final int priority2 = o2.getPriority();
			if (priority1 < priority2) {
				return 1;
			}
			if (priority1 > priority2) {
				return -1;
			}
			return 0;
		}

	}
}
