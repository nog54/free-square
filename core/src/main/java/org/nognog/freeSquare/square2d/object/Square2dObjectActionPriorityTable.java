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

package org.nognog.freeSquare.square2d.object;

import org.nognog.freeSquare.square2d.action.PrioritizableAction;
import org.nognog.freeSquare.square2d.action.object.ForeverTryToEatAction;
import org.nognog.freeSquare.square2d.action.object.FreeRunningAction;
import org.nognog.freeSquare.square2d.action.object.KeepLandingOnSquareAction;
import org.nognog.freeSquare.square2d.action.object.MomentumMoveAction;
import org.nognog.freeSquare.square2d.action.object.SleepAction;

/**
 * @author goshi 2015/06/23
 */
public class Square2dObjectActionPriorityTable implements ActionPriorityTable {
	private static final Square2dObjectActionPriorityTable instance = new Square2dObjectActionPriorityTable();

	private Square2dObjectActionPriorityTable() {
	}

	/**
	 * @return instance
	 */
	public static Square2dObjectActionPriorityTable getInstance() {
		return instance;
	}

	@Override
	public int getPriority(Class<? extends PrioritizableAction> actionClass) {
		return TableBody.getPriority(actionClass);
	}

	private enum TableBody {

		FREE_RUNNING(FreeRunningAction.class, 0),

		FOREVER_TRY_TO_EAT(ForeverTryToEatAction.class, 7),

		SLEEP(SleepAction.class, 8),

		KEEP_LANDING(KeepLandingOnSquareAction.class, 9),

		MOMENTUM_MOVE(MomentumMoveAction.class, 10), ;

		private Class<? extends PrioritizableAction> klass;
		private int priority;

		private TableBody(Class<? extends PrioritizableAction> klass, int priority) {
			this.klass = klass;
			this.priority = priority;
		}

		public static int getPriority(Class<? extends PrioritizableAction> actionClass) {
			for (TableBody tuple : TableBody.values()) {
				if (tuple.klass == actionClass) {
					return tuple.priority;
				}
			}
			throw new IllegalArgumentException("not exists in table"); //$NON-NLS-1$
		}
	}
}
