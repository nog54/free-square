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

package org.nognog.freeSquare.model.life.status.influence;

import org.nognog.freeSquare.model.life.status.Status;

/**
 * @author goshi 2015/02/08
 * @param <T>
 *            extended class
 */
public abstract class SingleStatusInfluence<T extends SingleStatusInfluence<T>> implements StatusInfluence<T> {
	private final double amount;

	/**
	 * @param type
	 * @param amount
	 */
	public SingleStatusInfluence(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return this.amount;
	}

	@Override
	public void applyTo(Status target) {
		this.applyTo(target, 1);
	}

}
