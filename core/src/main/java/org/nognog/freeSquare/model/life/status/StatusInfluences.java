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
public class StatusInfluences {
	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static AgilityInfluence agility(double influencePerAmount) {
		return new AgilityInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static ClamInfluence clam(double influencePerAmount) {
		return new ClamInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static ConditionInfluence condition(double influencePerAmount) {
		return new ConditionInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static CuriosityInfluence curiosity(double influencePerAmount) {
		return new CuriosityInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static DefianceInfluence defiance(double influencePerAmount) {
		return new DefianceInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static EffeminacyInfluence effeminacy(double influencePerAmount) {
		return new EffeminacyInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static FlexibilityInfluence flexibility(double influencePerAmount) {
		return new FlexibilityInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static FortitudeInfluence fortitude(double influencePerAmount) {
		return new FortitudeInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static GiftInfluence gift(double influencePerAmount) {
		return new GiftInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static KnowledgeInfluence knowledge(double influencePerAmount) {
		return new KnowledgeInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static MasculinityInfluence masculinity(double influencePerAmount) {
		return new MasculinityInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static PowerInfluence power(double influencePerAmount) {
		return new PowerInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static SmartInfluence smart(double influencePerAmount) {
		return new SmartInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static StressInfluence stress(double influencePerAmount) {
		return new StressInfluence(influencePerAmount);
	}

	/**
	 * @param influencePerAmount
	 * @return new instance
	 */
	public static ToleranceInfluence tolerance(double influencePerAmount) {
		return new ToleranceInfluence(influencePerAmount);
	}

}
