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

package org.nognog.freeSquare.model.life.status;

/**
 * @author goshi 2015/02/08
 */
public class DefianceInfluence extends StatusInfluence {

	/**
	 * @param influencePerAmount
	 */
	public DefianceInfluence(double influencePerAmount) {
		super(influencePerAmount);
	}

	@Override
	public void applyTo(Status targetStatus, int amount) {
		targetStatus.addDefiance(amount * this.getInfluencePerAmount());
	}
}
