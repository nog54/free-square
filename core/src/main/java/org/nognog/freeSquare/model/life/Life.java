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

import org.nognog.freeSquare.model.Nameable;
import org.nognog.freeSquare.model.SelfValidatable;
import org.nognog.freeSquare.model.life.status.Status;

/**
 * @author goshi 2014/08/25
 */
public class Life implements SelfValidatable, Nameable {
	private String name;
	private Family family;
	private Status status;

	@SuppressWarnings("unused")
	private Life() {
		// used by json
	}

	/**
	 * @param family
	 */
	public Life(Family family) {
		this(family.getName(), family);
	}

	/**
	 * @param name
	 * @param family
	 */
	public Life(String name, Family family) {
		this(name, family, new Status());
	}

	/**
	 * @param name
	 * @param family
	 * @param status
	 */
	public Life(String name, Family family, Status status) {
		this.name = name;
		this.family = family;
		this.status = status;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 種族を返す
	 * 
	 * @return family
	 */
	public Family getFamily() {
		return this.family;
	}

	/**
	 * ステータス
	 * 
	 * @return status
	 */
	public Status getStatus() {
		return this.status;
	}

	@Override
	public boolean isValid() {
		if (this.family == null) {
			return false;
		}
		if (this.status == null) {
			return false;
		}
		if (!this.status.isValid()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * @return status string
	 */
	public String getStatusString() {
		StringBuilder sb = new StringBuilder();
		sb.append("name :").append(this.name).append(System.getProperty("line.separator")).append(this.status); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}
}
