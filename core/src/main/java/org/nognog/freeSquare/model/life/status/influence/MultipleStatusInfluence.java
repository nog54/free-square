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
 * @author goshi 2015/06/25
 */
public class MultipleStatusInfluence implements StatusInfluence<MultipleStatusInfluence> {
	private final SingleStatusInfluence<?>[] influences;

	/**
	 * @param influences
	 */
	public MultipleStatusInfluence(SingleStatusInfluence<?>... influences) {
		this.influences = influences;
	}

	/**
	 * @return influences
	 */
	public SingleStatusInfluence<?>[] getSingleInfluences() {
		return this.influences;
	}

	@Override
	public MultipleStatusInfluence createScaledInfluence(double scale) {
		final SingleStatusInfluence<?>[] scaledInfluences = new SingleStatusInfluence<?>[this.influences.length];
		for (int i = 0; i < scaledInfluences.length; i++) {
			scaledInfluences[i] = this.influences[i].createScaledInfluence(scale);
		}
		return new MultipleStatusInfluence(scaledInfluences);
	}

	@Override
	public void applyTo(Status status) {
		this.applyTo(status, 1);
	}

	@Override
	public void applyTo(Status status, int times) {
		for (SingleStatusInfluence<?> influence : this.influences) {
			influence.applyTo(status, times);
		}
	}

}
