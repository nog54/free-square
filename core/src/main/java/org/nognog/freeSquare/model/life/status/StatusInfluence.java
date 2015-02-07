package org.nognog.freeSquare.model.life.status;



/**
 * @author goshi 2015/02/08
 */
public abstract class StatusInfluence {
	private final double influencePerAmount;

	/**
	 * @param type
	 * @param amount
	 */
	public StatusInfluence(double amount) {
		this.influencePerAmount = amount;
	}

	/**
	 * @return the amount
	 */
	public double getInfluencePerAmount() {
		return this.influencePerAmount;
	}

	/**
	 * @param targetStatus
	 * @param eatAmount
	 */
	public abstract void applyTo(Status targetStatus, int eatAmount);

}
