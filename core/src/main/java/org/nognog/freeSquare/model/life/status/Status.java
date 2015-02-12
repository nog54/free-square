package org.nognog.freeSquare.model.life.status;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.nognog.freeSquare.model.SelfValidatable;

/**
 * @author goshi 2014/08/24
 */
public class Status implements SelfValidatable {

	private int age; // 年
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

	private static final int MIN_AGE = 0;
	private static final int MAX_AGE = 1000;
	private static final double MIN_CONDITION = 0;
	private static final double MAX_CONDITION = 100;
	private static final double MIN_HUNGER = 0;
	private static final double MAX_HUNGER = 100;
	private static final double MIN_POWER = 0;
	private static final double MAX_POWER = 1000;
	private static final double MIN_AGILITY = 0;
	private static final double MAX_AGILITY = 1000;
	private static final double MIN_FLEXIBILITY = 0;
	private static final double MAX_FLEXIBILITY = 1000;
	private static final double MIN_FORTITUDE = 0;
	private static final double MAX_FORTITUDE = 1000;
	private static final double MIN_KNOWLEDGE = 0;
	private static final double MAX_KNOWLEDGE = 1000;
	private static final double MIN_SMART = 0;
	private static final double MAX_SMART = 1000;
	private static final double MIN_CURIOSITY = 0;
	private static final double MAX_CURIOSITY = 100;
	private static final double MIN_COMMUNICATION = 0;
	private static final double MAX_COMMUNICATION = 100;
	private static final double MIN_TOLERANCE = 0;
	private static final double MAX_TOLERANCE = 100;
	private static final double MIN_DEFIANCE = 0;
	private static final double MAX_DEFIANCE = 100;
	private static final double MIN_CALM = 0;
	private static final double MAX_CALM = 100;
	private static final double MIN_STRESS = 0;
	private static final double MAX_STRESS = 100;
	private static final double MIN_MASCULINITY = 0;
	private static final double MAX_MASCULINITY = 100;
	private static final double MIN_EFFEMINACY = 0;
	private static final double MAX_EFFEMINACY = 100;
	private static final double MIN_GIFT = 0;
	private static final double MAX_GIFT = 100;

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

	private static boolean isRange(double value, double min, double max) {
		return (value >= min) && (value <= max);
	}
	
	@Override
	public String toString() {
		String newLine = System.getProperty("line.separator"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		Class<Status> class1 = Status.class;
		for(Field field : class1.getDeclaredFields()){
			if(Modifier.isStatic(field.getModifiers())){
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
}
