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

/**
 * 種族を示す列挙
 * 
 * @author goshi 2014/08/25
 */
@SuppressWarnings("javadoc")
public enum Family {
	RIKI(getString("riki")), //$NON-NLS-1$
	SMALL_RIKI(getString("riki", "small")), //$NON-NLS-1$ //$NON-NLS-2$
	BIG_RIKI(getString("riki", "large")), //$NON-NLS-1$ //$NON-NLS-2$
	FLY_RIKI(getString("friable", "riki")), //$NON-NLS-1$ //$NON-NLS-2$
	SMALL_FLY_RIKI(getString("friable", "riki", "small")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	BIG_FLY_RIKI(getString("friable", "riki", "large")), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	;

	private Family(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	private final String name;

}
