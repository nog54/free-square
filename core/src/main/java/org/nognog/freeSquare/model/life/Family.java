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

package org.nognog.freeSquare.model.life;

import static org.nognog.freeSquare.Messages.getString;

import org.nognog.freeSquare.model.Nameable;

/**
 * 種族を示す列挙
 * 
 * @author goshi 2014/08/25
 */
@SuppressWarnings("javadoc")
public interface Family {

	/**
	 * @return the name
	 */
	public String getName();

	public static enum Prepared implements Family {
		RIKI(getString("riki")), //$NON-NLS-1$
		SMALL_RIKI(getString("riki", "small")), //$NON-NLS-1$ //$NON-NLS-2$
		BIG_RIKI(getString("riki", "large")), //$NON-NLS-1$ //$NON-NLS-2$
		FLY_RIKI(getString("friable", "riki")), //$NON-NLS-1$ //$NON-NLS-2$
		SMALL_FLY_RIKI(getString("friable", "riki", "small")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		BIG_FLY_RIKI(getString("friable", "riki", "large")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		UNKNOWN("unknown"), //$NON-NLS-1$

		;

		private final String name;

		private Prepared(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}
	}

	public static class OriginalFamily implements Family, Nameable {

		private String name;

		/**
		 * 
		 */
		public OriginalFamily() {
			this("noname"); //$NON-NLS-1$
		}

		/**
		 * 
		 */
		public OriginalFamily(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

	}
}
