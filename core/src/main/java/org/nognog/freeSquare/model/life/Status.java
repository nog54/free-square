package org.nognog.freeSquare.model.life;

import org.nognog.freeSquare.model.Savable;

/**
 * @author goshi 2014/08/24
 */
public class Status implements Savable {

	private int age; // 年
	private int condition; // コンディション
	private int hunger; // 空腹度
	private int power; // 力
	private int agility; // 敏捷性
	private int flexibility; // 柔軟性
	private int fortitude; // 忍耐力
	private int knowledge; // 知識
	private int smart; // かしこさ
	private int curiosity; // 好奇心
	private int communication; // 交流力
	private int tolerance; // 包容力
	private int defiance; // 反抗心
	private int calm; // 落ち着き
	private int stress; // ストレス
	private int masculinity; // 男らしさ
	private int effeminacy; // 女らしさ
	private int gift; // 個体値

	private static final int MIN_AGE = 0;
	private static final int MAX_AGE = 1000;
	private static final int MIN_CONDITION = 0;
	private static final int MAX_CONDITION = 100;
	private static final int MIN_HUNGER = 0;
	private static final int MAX_HUNGER = 100;
	private static final int MIN_POWER = 0;
	private static final int MAX_POWER = 1000;
	private static final int MIN_AGILITY = 0;
	private static final int MAX_AGILITY = 1000;
	private static final int MIN_FLEXIBILITY = 0;
	private static final int MAX_FLEXIBILITY = 1000;
	private static final int MIN_FORTITUDE = 0;
	private static final int MAX_FORTITUDE = 1000;
	private static final int MIN_KNOWLEDGE = 0;
	private static final int MAX_KNOWLEDGE = 1000;
	private static final int MIN_SMART = 0;
	private static final int MAX_SMART = 1000;
	private static final int MIN_CURIOSITY = 0;
	private static final int MAX_CURIOSITY = 100;
	private static final int MIN_COMMUNICATION = 0;
	private static final int MAX_COMMUNICATION = 100;
	private static final int MIN_TOLERANCE = 0;
	private static final int MAX_TOLERANCE = 100;
	private static final int MIN_DEFIANCE = 0;
	private static final int MAX_DEFIANCE = 100;
	private static final int MIN_CALM = 0;
	private static final int MAX_CALM = 100;
	private static final int MIN_STRESS = 0;
	private static final int MAX_STRESS = 100;
	private static final int MIN_MASCULINITY = 0;
	private static final int MAX_MASCULINITY = 100;
	private static final int MIN_EFFEMINACY = 0;
	private static final int MAX_EFFEMINACY = 100;
	private static final int MIN_GIFT = 0;
	private static final int MAX_GIFT = 100;

	/**
	 * 全てのステータス値を０で初期化
	 */
	public Status() {
		this.age = MIN_AGE;
		this.agility = MIN_AGILITY;
		this.calm = MIN_CALM;
		this.communication = MIN_COMMUNICATION;
		this.condition = MIN_CONDITION;
		this.curiosity = MIN_CURIOSITY;
		this.defiance = MIN_DEFIANCE;
		this.effeminacy = MIN_EFFEMINACY;
		this.flexibility = MIN_FLEXIBILITY;
		this.fortitude = MIN_FORTITUDE;
		this.gift = MIN_GIFT;
		this.hunger = MIN_GIFT;
		this.knowledge = MIN_KNOWLEDGE;
		this.masculinity = MIN_MASCULINITY;
		this.power = MIN_POWER;
		this.smart = MIN_SMART;
		this.stress = MIN_STRESS;
		this.tolerance = MIN_TOLERANCE;
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
		if (this.age == MAX_AGE) {
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
		if (this.age + addend > MAX_AGE) {
			this.age = MAX_AGE;
			return;
		}
		this.age += addend;
	}

	/**
	 * コンディション値を返す
	 * 
	 * @return condition
	 */
	public int getCondition() {
		return this.condition;
	}

	/**
	 * コンディション値にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addCondition(int addend) {
		final int result = this.condition + addend;
		if (result > MAX_CONDITION) {
			this.condition = MAX_CONDITION;
			return;
		}
		if (result < MIN_CONDITION) {
			this.condition = MIN_CONDITION;
			return;
		}
		this.condition = result;
	}

	/**
	 * 空腹度を返す
	 * 
	 * @return hunger
	 */
	public int getHunger() {
		return this.hunger;
	}

	/**
	 * 空腹度にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addHunger(int addend) {
		final int result = this.hunger + addend;
		if (result > MAX_HUNGER) {
			this.hunger = MAX_HUNGER;
			return;
		}
		if (result < MIN_HUNGER) {
			this.hunger = MIN_HUNGER;
			return;
		}
		this.hunger = result;
	}

	/**
	 * 力を返す
	 * 
	 * @return power
	 */
	public int getPower() {
		return this.power;
	}

	/**
	 * 力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addPower(int addend) {
		final int result = this.power + addend;
		if (result > MAX_POWER) {
			this.power = MAX_POWER;
			return;
		}
		if (result < MIN_POWER) {
			this.power = MIN_POWER;
			return;
		}
		this.power = result;
	}

	/**
	 * 敏捷性を返す
	 * 
	 * @return agility
	 */
	public int getAgility() {
		return this.agility;
	}

	/**
	 * 敏捷性にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addAgility(int addend) {
		final int result = this.agility + addend;
		if (result > MAX_AGILITY) {
			this.agility = MAX_AGILITY;
			return;
		}
		if (result < MIN_AGILITY) {
			this.agility = MIN_AGILITY;
			return;
		}
		this.agility = result;
	}

	/**
	 * 柔軟性
	 * 
	 * @return flexibility
	 */
	public int getFlexibility() {
		return this.flexibility;
	}

	/**
	 * 柔軟性にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addFlexibility(int addend) {
		final int result = this.flexibility + addend;
		if (result > MAX_FLEXIBILITY) {
			this.flexibility = MAX_FLEXIBILITY;
			return;
		}
		if (result < MIN_FLEXIBILITY) {
			this.flexibility = MIN_FLEXIBILITY;
			return;
		}
		this.flexibility = result;
	}

	/**
	 * 忍耐力
	 * 
	 * @return fortitude
	 */
	public int getFortitude() {
		return this.fortitude;
	}

	/**
	 * 忍耐力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addFortitude(int addend) {
		final int result = this.fortitude + addend;
		if (result > MAX_FORTITUDE) {
			this.fortitude = MAX_FORTITUDE;
			return;
		}
		if (result < MIN_FORTITUDE) {
			this.fortitude = MIN_FORTITUDE;
			return;
		}
		this.fortitude = result;
	}

	/**
	 * 知識
	 * 
	 * @return knowledge
	 */
	public int getKnowledge() {
		return this.knowledge;
	}

	/**
	 * 知識にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addKnowledge(int addend) {
		final int result = this.knowledge + addend;
		if (result > MAX_KNOWLEDGE) {
			this.knowledge = MAX_KNOWLEDGE;
			return;
		}
		if (result < MIN_KNOWLEDGE) {
			this.knowledge = MIN_KNOWLEDGE;
			return;
		}
		this.knowledge = result;
	}

	/**
	 * 賢さ
	 * 
	 * @return smart
	 */
	public int getSmart() {
		return this.smart;
	}

	/**
	 * 賢さにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addSmart(int addend) {
		final int result = this.smart + addend;
		if (result > MAX_SMART) {
			this.smart = MAX_SMART;
			return;
		}
		if (result < MIN_SMART) {
			this.smart = MIN_SMART;
			return;
		}
		this.smart = result;
	}

	/**
	 * 好奇心
	 * 
	 * @return curiosity
	 */
	public int getCuriosity() {
		return this.curiosity;
	}

	/**
	 * 好奇心にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addCuriosity(int addend) {
		final int result = this.curiosity + addend;
		if (result > MAX_CURIOSITY) {
			this.curiosity = MAX_CURIOSITY;
			return;
		}
		if (result < MIN_CURIOSITY) {
			this.curiosity = MIN_CURIOSITY;
			return;
		}
		this.curiosity = result;
	}

	/**
	 * 交流力
	 * 
	 * @return communication
	 */
	public int getCommunication() {
		return this.communication;
	}

	/**
	 * 交流力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addCommunication(int addend) {
		final int result = this.communication + addend;
		if (result > MAX_COMMUNICATION) {
			this.communication = MAX_COMMUNICATION;
			return;
		}
		if (result < MIN_COMMUNICATION) {
			this.communication = MIN_COMMUNICATION;
			return;
		}
		this.communication = result;
	}

	/**
	 * 包容力
	 * 
	 * @return tolerance
	 */
	public int getTolerance() {
		return this.tolerance;
	}

	/**
	 * 包容力にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addTolerance(int addend) {
		final int result = this.tolerance + addend;
		if (result > MAX_TOLERANCE) {
			this.tolerance = MAX_TOLERANCE;
			return;
		}
		if (result < MIN_TOLERANCE) {
			this.tolerance = MIN_TOLERANCE;
			return;
		}
		this.tolerance = result;
	}

	/**
	 * 反抗心
	 * 
	 * @return defiance
	 */
	public int getDefiance() {
		return this.defiance;
	}

	/**
	 * 反抗心にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addDefiance(int addend) {
		final int result = this.defiance + addend;
		if (result > MAX_DEFIANCE) {
			this.defiance = MAX_DEFIANCE;
			return;
		}
		if (result < MIN_DEFIANCE) {
			this.defiance = MIN_DEFIANCE;
			return;
		}
		this.defiance = result;
	}

	/**
	 * 落ち着き
	 * 
	 * @return clam
	 */
	public int getCalm() {
		return this.calm;
	}

	/**
	 * 落ち着きにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addClam(int addend) {
		final int result = this.calm + addend;
		if (result > MAX_CALM) {
			this.calm = MAX_CALM;
			return;
		}
		if (result < MIN_CALM) {
			this.calm = MIN_CALM;
			return;
		}
		this.calm = result;
	}

	/**
	 * ストレス
	 * 
	 * @return stress
	 */
	public int getStress() {
		return this.stress;
	}

	/**
	 * ストレスにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addStress(int addend) {
		final int result = this.stress + addend;
		if (result > MAX_STRESS) {
			this.stress = MAX_STRESS;
			return;
		}
		if (result < MIN_STRESS) {
			this.stress = MIN_STRESS;
			return;
		}
		this.stress = result;
	}

	/**
	 * 男らしさ
	 * 
	 * @return masculinity
	 */
	public int getMasculinity() {
		return this.masculinity;
	}

	/**
	 * 男らしさにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addMasculinity(int addend) {
		final int result = this.masculinity + addend;
		if (result > MAX_MASCULINITY) {
			this.masculinity = MAX_MASCULINITY;
			return;
		}
		if (result < MIN_MASCULINITY) {
			this.masculinity = MIN_MASCULINITY;
			return;
		}
		this.masculinity = result;
	}

	/**
	 * 女らしさ
	 * 
	 * @return effeminacy
	 */
	public int getEffeminacy() {
		return this.effeminacy;
	}

	/**
	 * 女らしさにaddendを加えます
	 * 
	 * @param addend
	 */
	public void addEffeminacy(int addend) {
		final int result = this.effeminacy + addend;
		if (result > MAX_EFFEMINACY) {
			this.effeminacy = MAX_EFFEMINACY;
			return;
		}
		if (result < MIN_EFFEMINACY) {
			this.effeminacy = MIN_EFFEMINACY;
			return;
		}
		this.effeminacy = result;
	}

	/**
	 * 才能
	 * 
	 * @return gift
	 */
	public int getGift() {
		return this.gift;
	}

	/**
	 * 才能にaddendを加えます
	 * 
	 * @param addend
	 */
	public void addGift(int addend) {
		final int result = this.gift + addend;
		if (result > MAX_GIFT) {
			this.gift = MAX_GIFT;
			return;
		}
		if (result < MIN_GIFT) {
			this.gift = MIN_GIFT;
			return;
		}
		this.gift = result;
	}

	@Override
	public boolean isValid() {
		if (!isRange(this.age, MIN_AGE, MAX_AGE)) {
			return false;
		}
		if (!isRange(this.agility, MIN_AGILITY, MAX_AGILITY)) {
			return false;
		}
		if (!isRange(this.calm, MIN_CALM, MAX_CALM)) {
			return false;
		}
		if (!isRange(this.communication, MIN_COMMUNICATION, MAX_COMMUNICATION)) {
			return false;
		}
		if (!isRange(this.condition, MIN_CONDITION, MAX_CONDITION)) {
			return false;
		}
		if (!isRange(this.curiosity, MIN_CURIOSITY, MAX_CURIOSITY)) {
			return false;
		}
		if (!isRange(this.defiance, MIN_DEFIANCE, MAX_DEFIANCE)) {
			return false;
		}
		if (!isRange(this.effeminacy, MIN_EFFEMINACY, MAX_EFFEMINACY)) {
			return false;
		}
		if (!isRange(this.flexibility, MIN_FLEXIBILITY, MAX_FLEXIBILITY)) {
			return false;
		}
		if (!isRange(this.fortitude, MIN_FORTITUDE, MAX_FORTITUDE)) {
			return false;
		}
		if (!isRange(this.gift, MIN_GIFT, MAX_GIFT)) {
			return false;
		}
		if (!isRange(this.hunger, MIN_HUNGER, MAX_HUNGER)) {
			return false;
		}
		if (!isRange(this.knowledge, MIN_KNOWLEDGE, MAX_KNOWLEDGE)) {
			return false;
		}
		if (!isRange(this.masculinity, MIN_MASCULINITY, MAX_MASCULINITY)) {
			return false;
		}
		if (!isRange(this.power, MIN_POWER, MAX_POWER)) {
			return false;
		}
		if (!isRange(this.smart, MIN_SMART, MAX_SMART)) {
			return false;
		}
		if (!isRange(this.stress, MIN_STRESS, MAX_STRESS)) {
			return false;
		}
		if (!isRange(this.tolerance, MIN_TOLERANCE, MAX_TOLERANCE)) {
			return false;
		}
		return true;
	}

	private static boolean isRange(int value, int min, int max) {
		return (value >= min) && (value <= max);
	}
}
