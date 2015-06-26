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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.nognog.freeSquare.model.SelfValidatable;

/**
 * @author goshi 2014/08/24
 */
public class Status implements SelfValidatable {

	private int age; // 年
	private double tiredness; // 疲労
	private double condition; // コンディション
	private double hunger; // 空腹度
	private double power; // 力
	private double agility; // 敏捷性
	private double flexibility; // 柔軟性
	private double fortitude; // 忍耐力
	private double knowledge; // 知識
	private double smart; // かしこさ
	private double curiosity; // 好奇心
	private double communication; // 交流力
	private double tolerance; // 包容力
	private double defiance; // 反抗心
	private double calm; // 落ち着き
	private double stress; // ストレス
	private double masculinity; // 男らしさ
	private double effeminacy; // 女らしさ
	private double gift; // 個体値

	/**
	 * 全てのステータス値を０で初期化
	 */
	public Status() {
		this.age = (int) StatusRange.AGE.getMin();
		this.agility = StatusRange.AGILITY.getMin();
		this.calm = StatusRange.CALM.getMin();
		this.communication = StatusRange.COMMUNICATION.getMin();
		this.condition = StatusRange.CONDITION.getMin();
		this.curiosity = StatusRange.CURIOSITY.getMin();
		this.defiance = StatusRange.DEFIANCE.getMin();
		this.effeminacy = StatusRange.EFFEMINACY.getMin();
		this.flexibility = StatusRange.FLEXIBILITY.getMin();
		this.fortitude = StatusRange.FORTITUDE.getMin();
		this.gift = StatusRange.GIFT.getMin();
		this.hunger = StatusRange.GIFT.getMin();
		this.knowledge = StatusRange.KNOWLEDGE.getMin();
		this.masculinity = StatusRange.MASCULINITY.getMin();
		this.power = StatusRange.POWER.getMin();
		this.smart = StatusRange.SMART.getMin();
		this.stress = StatusRange.STRESS.getMin();
		this.tiredness = StatusRange.TIREDNESS.getMin();
		this.tolerance = StatusRange.TOLERANCE.getMin();
	}

	/**
	 * 年を返す
	 * 
	 * @return age
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * 年を加える
	 */
	public void addAge() {
		if (this.age == StatusRange.AGE.getMax()) {
			return;
		}
		this.age++;
	}

	/**
	 * 年を加える
	 * 
	 * @param addend
	 */
	public void addAge(int addend) {
		if (addend < 0) {
			throw new IllegalArgumentException("You can't turn the clock back."); //$NON-NLS-1$
		}
		if (this.age + addend > StatusRange.AGE.getMax()) {
			this.age = (int) StatusRange.AGE.getMax();
			return;
		}
		this.age += addend;
	}

	/**
	 * コンディション値を返す
	 * 
	 * @return condition
	 */
	public double getCondition() {
		return this.condition;
	}

	/**
	 * コンディション値にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addCondition(double addend) {
		final double result = this.condition + addend;
		if (result > StatusRange.CONDITION.getMax()) {
			this.condition = StatusRange.CONDITION.getMax();
			return;
		}
		if (result < StatusRange.CONDITION.getMin()) {
			this.condition = StatusRange.CONDITION.getMin();
			return;
		}
		this.condition = result;
	}

	/**
	 * 空腹度を返す
	 * 
	 * @return hunger
	 */
	public double getHunger() {
		return this.hunger;
	}

	/**
	 * 空腹度にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addHunger(double addend) {
		final double result = this.hunger + addend;
		if (result > StatusRange.HUNGER.getMax()) {
			this.hunger = StatusRange.HUNGER.getMax();
			return;
		}
		if (result < StatusRange.HUNGER.getMin()) {
			this.hunger = StatusRange.HUNGER.getMin();
			return;
		}
		this.hunger = result;
	}

	/**
	 * 力を返す
	 * 
	 * @return power
	 */
	public double getPower() {
		return this.power;
	}

	/**
	 * 力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addPower(double addend) {
		final double result = this.power + addend;
		if (result > StatusRange.POWER.getMax()) {
			this.power = StatusRange.POWER.getMax();
			return;
		}
		if (result < StatusRange.POWER.getMin()) {
			this.power = StatusRange.POWER.getMin();
			return;
		}
		this.power = result;
	}

	/**
	 * 敏捷性を返す
	 * 
	 * @return agility
	 */
	public double getAgility() {
		return this.agility;
	}

	/**
	 * 敏捷性にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addAgility(double addend) {
		final double result = this.agility + addend;
		if (result > StatusRange.AGILITY.getMax()) {
			this.agility = StatusRange.AGILITY.getMax();
			return;
		}
		if (result < StatusRange.AGILITY.getMin()) {
			this.agility = StatusRange.AGILITY.getMin();
			return;
		}
		this.agility = result;
	}

	/**
	 * 柔軟性
	 * 
	 * @return flexibility
	 */
	public double getFlexibility() {
		return this.flexibility;
	}

	/**
	 * 柔軟性にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addFlexibility(double addend) {
		final double result = this.flexibility + addend;
		if (result > StatusRange.FLEXIBILITY.getMax()) {
			this.flexibility = StatusRange.FLEXIBILITY.getMax();
			return;
		}
		if (result < StatusRange.FLEXIBILITY.getMin()) {
			this.flexibility = StatusRange.FLEXIBILITY.getMin();
			return;
		}
		this.flexibility = result;
	}

	/**
	 * 忍耐力
	 * 
	 * @return fortitude
	 */
	public double getFortitude() {
		return this.fortitude;
	}

	/**
	 * 忍耐力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addFortitude(double addend) {
		final double result = this.fortitude + addend;
		if (result > StatusRange.FORTITUDE.getMax()) {
			this.fortitude = StatusRange.FORTITUDE.getMax();
			return;
		}
		if (result < StatusRange.FORTITUDE.getMin()) {
			this.fortitude = StatusRange.FORTITUDE.getMin();
			return;
		}
		this.fortitude = result;
	}

	/**
	 * 知識
	 * 
	 * @return knowledge
	 */
	public double getKnowledge() {
		return this.knowledge;
	}

	/**
	 * 知識にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addKnowledge(double addend) {
		final double result = this.knowledge + addend;
		if (result > StatusRange.KNOWLEDGE.getMax()) {
			this.knowledge = StatusRange.KNOWLEDGE.getMax();
			return;
		}
		if (result < StatusRange.KNOWLEDGE.getMin()) {
			this.knowledge = StatusRange.KNOWLEDGE.getMin();
			return;
		}
		this.knowledge = result;
	}

	/**
	 * 賢さ
	 * 
	 * @return smart
	 */
	public double getSmart() {
		return this.smart;
	}

	/**
	 * 賢さにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addSmart(double addend) {
		final double result = this.smart + addend;
		if (result > StatusRange.SMART.getMax()) {
			this.smart = StatusRange.SMART.getMax();
			return;
		}
		if (result < StatusRange.SMART.getMin()) {
			this.smart = StatusRange.SMART.getMin();
			return;
		}
		this.smart = result;
	}

	/**
	 * 好奇心
	 * 
	 * @return curiosity
	 */
	public double getCuriosity() {
		return this.curiosity;
	}

	/**
	 * 好奇心にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addCuriosity(double addend) {
		final double result = this.curiosity + addend;
		if (result > StatusRange.CURIOSITY.getMax()) {
			this.curiosity = StatusRange.CURIOSITY.getMax();
			return;
		}
		if (result < StatusRange.CURIOSITY.getMin()) {
			this.curiosity = StatusRange.CURIOSITY.getMin();
			return;
		}
		this.curiosity = result;
	}

	/**
	 * 交流力
	 * 
	 * @return communication
	 */
	public double getCommunication() {
		return this.communication;
	}

	/**
	 * 交流力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addCommunication(double addend) {
		final double result = this.communication + addend;
		if (result > StatusRange.COMMUNICATION.getMax()) {
			this.communication = StatusRange.COMMUNICATION.getMax();
			return;
		}
		if (result < StatusRange.COMMUNICATION.getMin()) {
			this.communication = StatusRange.COMMUNICATION.getMin();
			return;
		}
		this.communication = result;
	}

	/**
	 * 包容力
	 * 
	 * @return tolerance
	 */
	public double getTolerance() {
		return this.tolerance;
	}

	/**
	 * 包容力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addTolerance(double addend) {
		final double result = this.tolerance + addend;
		if (result > StatusRange.TOLERANCE.getMax()) {
			this.tolerance = StatusRange.TOLERANCE.getMax();
			return;
		}
		if (result < StatusRange.TOLERANCE.getMin()) {
			this.tolerance = StatusRange.TOLERANCE.getMin();
			return;
		}
		this.tolerance = result;
	}

	/**
	 * 反抗心
	 * 
	 * @return defiance
	 */
	public double getDefiance() {
		return this.defiance;
	}

	/**
	 * 反抗心にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addDefiance(double addend) {
		final double result = this.defiance + addend;
		if (result > StatusRange.DEFIANCE.getMax()) {
			this.defiance = StatusRange.DEFIANCE.getMax();
			return;
		}
		if (result < StatusRange.DEFIANCE.getMin()) {
			this.defiance = StatusRange.DEFIANCE.getMin();
			return;
		}
		this.defiance = result;
	}

	/**
	 * 落ち着き
	 * 
	 * @return clam
	 */
	public double getCalm() {
		return this.calm;
	}

	/**
	 * 落ち着きにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addClam(double addend) {
		final double result = this.calm + addend;
		if (result > StatusRange.CALM.getMax()) {
			this.calm = StatusRange.CALM.getMax();
			return;
		}
		if (result < StatusRange.CALM.getMin()) {
			this.calm = StatusRange.CALM.getMin();
			return;
		}
		this.calm = result;
	}

	/**
	 * ストレス
	 * 
	 * @return stress
	 */
	public double getStress() {
		return this.stress;
	}

	/**
	 * ストレスにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addStress(double addend) {
		final double result = this.stress + addend;
		if (result > StatusRange.STRESS.getMax()) {
			this.stress = StatusRange.STRESS.getMax();
			return;
		}
		if (result < StatusRange.STRESS.getMin()) {
			this.stress = StatusRange.STRESS.getMin();
			return;
		}
		this.stress = result;
	}

	/**
	 * 男らしさ
	 * 
	 * @return masculinity
	 */
	public double getMasculinity() {
		return this.masculinity;
	}

	/**
	 * 男らしさにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addMasculinity(double addend) {
		final double result = this.masculinity + addend;
		if (result > StatusRange.MASCULINITY.getMax()) {
			this.masculinity = StatusRange.MASCULINITY.getMax();
			return;
		}
		if (result < StatusRange.MASCULINITY.getMin()) {
			this.masculinity = StatusRange.MASCULINITY.getMin();
			return;
		}
		this.masculinity = result;
	}

	/**
	 * 女らしさ
	 * 
	 * @return effeminacy
	 */
	public double getEffeminacy() {
		return this.effeminacy;
	}

	/**
	 * 女らしさにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addEffeminacy(double addend) {
		final double result = this.effeminacy + addend;
		if (result > StatusRange.EFFEMINACY.getMax()) {
			this.effeminacy = StatusRange.EFFEMINACY.getMax();
			return;
		}
		if (result < StatusRange.EFFEMINACY.getMin()) {
			this.effeminacy = StatusRange.EFFEMINACY.getMin();
			return;
		}
		this.effeminacy = result;
	}

	/**
	 * 才能
	 * 
	 * @return gift
	 */
	public double getGift() {
		return this.gift;
	}

	/**
	 * 才能にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addGift(double addend) {
		final double result = this.gift + addend;
		if (result > StatusRange.GIFT.getMax()) {
			this.gift = StatusRange.GIFT.getMax();
			return;
		}
		if (result < StatusRange.GIFT.getMin()) {
			this.gift = StatusRange.GIFT.getMin();
			return;
		}
		this.gift = result;
	}

	/**
	 * 敏捷性を返す
	 * 
	 * @return tiredness
	 */
	public double getTiredness() {
		return this.tiredness;
	}

	/**
	 * 疲労にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addTiredness(double addend) {
		final double result = this.tiredness + addend;
		if (result > StatusRange.TIREDNESS.getMax()) {
			this.tiredness = StatusRange.TIREDNESS.getMax();
			return;
		}
		if (result < StatusRange.TIREDNESS.getMin()) {
			this.tiredness = StatusRange.TIREDNESS.getMin();
			return;
		}
		this.tiredness = result;
	}

	@Override
	public boolean isValid() {
		return StatusRange.AGE.includes(this.age) && StatusRange.AGILITY.includes(this.agility) && StatusRange.CALM.includes(this.calm) && StatusRange.COMMUNICATION.includes(this.communication)
				&& StatusRange.CONDITION.includes(this.condition) && StatusRange.CURIOSITY.includes(this.curiosity) && StatusRange.DEFIANCE.includes(this.defiance)
				&& StatusRange.EFFEMINACY.includes(this.effeminacy) && StatusRange.FLEXIBILITY.includes(this.flexibility) && StatusRange.FORTITUDE.includes(this.fortitude)
				&& StatusRange.GIFT.includes(this.gift) && StatusRange.HUNGER.includes(this.hunger) && StatusRange.KNOWLEDGE.includes(this.knowledge)
				&& StatusRange.MASCULINITY.includes(this.masculinity) && StatusRange.POWER.includes(this.power) && StatusRange.SMART.includes(this.smart) && StatusRange.STRESS.includes(this.stress)
				&& StatusRange.TOLERANCE.includes(this.tolerance) && StatusRange.TIREDNESS.includes(this.tiredness);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Status) {
			return this.equals((Status) obj);
		}
		return super.equals(obj);
	}

	/**
	 * @param status
	 * @return true if same value.
	 */
	public boolean equals(Status status) {
		if (this.age != status.age || this.agility != status.agility || this.calm != status.calm || this.communication != status.communication || this.condition != status.condition
				|| this.curiosity != status.curiosity || this.defiance != status.defiance || this.effeminacy != status.effeminacy || this.flexibility != status.flexibility
				|| this.fortitude != status.fortitude || this.gift != status.gift || this.hunger != status.hunger || this.knowledge != status.knowledge || this.masculinity != status.masculinity
				|| this.power != status.power || this.smart != status.smart || this.stress != status.stress || this.tolerance != status.tolerance) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		Class<Status> class1 = Status.class;
		for (Field field : class1.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			try {
				sb.append(field.getName()).append(":").append(field.get(this)).append(newLine); //$NON-NLS-1$
			} catch (IllegalArgumentException | IllegalAccessException e) {
				//
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@SuppressWarnings("javadoc")
	public static enum StatusRange {
		AGE(0, 1000), TIREDNESS(0, 100), CONDITION(0, 100), HUNGER(0, 100), POWER(0, 1000),

		AGILITY(0, 1000), FLEXIBILITY(0, 1000), FORTITUDE(0, 1000), KNOWLEDGE(0, 1000), SMART(0, 1000),

		CURIOSITY(0, 100), COMMUNICATION(0, 100), TOLERANCE(0, 100), DEFIANCE(0, 100), CALM(0, 100),

		STRESS(0, 100), MASCULINITY(0, 100), EFFEMINACY(0, 100), GIFT(0, 100);

		private final double min;
		private final double max;

		private StatusRange(double min, double max) {
			this.min = min;
			this.max = max;
		}

		public double getMin() {
			return this.min;
		}

		public double getMax() {
			return this.max;
		}

		public boolean includes(double value) {
			return (value >= this.min) && (value <= this.max);
		}

	}
}
