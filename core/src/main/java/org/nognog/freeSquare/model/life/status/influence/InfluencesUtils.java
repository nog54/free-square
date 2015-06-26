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

/**
 * @author goshi 2015/02/08
 */
public class InfluencesUtils {
	/**
	 * @param amount
	 * @return new instance
	 */
	public static AgilityInfluence agility(double amount) {
		return new AgilityInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static ClamInfluence clam(double amount) {
		return new ClamInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static ConditionInfluence condition(double amount) {
		return new ConditionInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static CuriosityInfluence curiosity(double amount) {
		return new CuriosityInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static DefianceInfluence defiance(double amount) {
		return new DefianceInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static EffeminacyInfluence effeminacy(double amount) {
		return new EffeminacyInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static FlexibilityInfluence flexibility(double amount) {
		return new FlexibilityInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static FortitudeInfluence fortitude(double amount) {
		return new FortitudeInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static GiftInfluence gift(double amount) {
		return new GiftInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static KnowledgeInfluence knowledge(double amount) {
		return new KnowledgeInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static MasculinityInfluence masculinity(double amount) {
		return new MasculinityInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static PowerInfluence power(double amount) {
		return new PowerInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static SmartInfluence smart(double amount) {
		return new SmartInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static StressInfluence stress(double amount) {
		return new StressInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static ToleranceInfluence tolerance(double amount) {
		return new ToleranceInfluence(amount);
	}

	/**
	 * @param amount
	 * @return new instance
	 */
	public static TirednessInfluence tiredness(double amount) {
		return new TirednessInfluence(amount);
	}
}
